/* 
 * Copyright (C) 2014 Reuben Steenekamp
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package fancyshader.server;

import fancyshader.entity.Account;
import fancyshader.entity.AuthDetail;
import fancyshader.entity.ShaderDemo;
import fancyshader.service.FancyShaderInternalException;
import fancyshader.service.FancyShaderLoginException;
import fancyshader.service.FancyShaderService;
import fancyshader.service.FancyShaderSessionExpiredException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

/**
 * Implementation of {@link fancyshader.service.FancyShaderService} using JPA to connect to FancyShaderDB
 *
 * @author Reuben Steenekamp
 */
public class FancyShaderServiceImpl implements FancyShaderService {

    EntityManager entityManager = Persistence.createEntityManagerFactory("FancyShaderPU").createEntityManager();

    @Override
    public String login(String username, String password) throws FancyShaderLoginException, FancyShaderInternalException {
        try {
            //Obtain the corresponding Account with the input username
            TypedQuery<Account> accountQuery = entityManager.createNamedQuery("Account.findByUsername", Account.class)
                    .setParameter("username", username);
            Account account = accountQuery.getSingleResult();

            // The AuthDetail in Account is not fetched automatically by JPA to avoid scrubbing Account of the AuthDetail later on when 
            // exposing Account through the FancyShaderService. We do not want users to have free access to password hashes of other users,
            // in the event that they chose easy to guess passwords.
            AuthDetail detail = entityManager.find(AuthDetail.class, account.getAuthDetail());

            String hash = getHash(password + detail.getSalt()); //Get the hash of the salted input password
            if (hash.equals(detail.getHashcode())) { //Check if the hash of the salted input password is the same as the hash stored in the AuthDetail
                return FancyShaderSession.create(account).getUUID(); //Create a session and return the UUID
            } else {
                throw new FancyShaderLoginException(); //The passwords do not match
            }
        } catch (NoResultException e) {
            throw new FancyShaderLoginException(); //There is no Account with the input username
        }
    }

    @Override
    public void logout(String sessionUUID) {
        FancyShaderSessionSingleton.instance.invalidateSession(sessionUUID);
    }

    @Override
    public void registerAccount(String username, String password) throws FancyShaderInternalException {
        String salt = getSalt();
        String hash = getHash(password + salt); //Get the hash of the salted password

        //Initialize the Account, except its AuthDetail id which is yet to be established
        Account account = new Account();
        account.setUsername(username);
        account.setDateCreated(new Date());
        account.setDelights(new HashSet());
        account.setVisits(new HashSet());

        //Initialize the AuthDetail
        AuthDetail detail = new AuthDetail();
        detail.setHashcode(hash);
        detail.setSalt(salt);

        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        try {
            entityManager.persist(detail); //Add the AuthDetail to the entity graph
            entityManager.flush(); //Ensure that all operations run so that detail is on the entity graph before proceeding
            account.setAuthDetail(detail.getId()); //Initialize the AuthDetail id of the Account
            entityManager.persist(account); //Add the Account to the entity graph
            if (transaction.isActive()) {
                transaction.commit();
            }
        } catch (Exception e) {
            Logger.getLogger(this.getClass().getName()).log(Level.WARNING, null, e);
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw new FancyShaderInternalException();
        }

    }

    /**
     * Obtains a cryptographically random salt
     *
     * @return the salt
     */
    private static String getSalt() {
        SecureRandom random = new SecureRandom();
        byte[] saltBytes = new byte[10];
        random.nextBytes(saltBytes);
        return new String(saltBytes);
    }

    /**
     * Obtains the SHA-1 hash of the {@code value}
     *
     * @param value the item to be hashed
     * @return the hash
     * @throws FancyShaderInternalException
     */
    private static String getHash(String value) throws FancyShaderInternalException {
        MessageDigest hasher;
        try {
            hasher = MessageDigest.getInstance("SHA-1");
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(FancyShaderRemoteServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            throw new FancyShaderInternalException();
        }
        return new String(hasher.digest(value.getBytes()));
    }

    @Override
    public List<ShaderDemo> search(String sessionUUID, String q) throws FancyShaderSessionExpiredException {
        Account account = getAccount(sessionUUID);

        //Get the lowercase words of the search query
        List<String> qWs = Arrays.asList(q.toLowerCase().split(" "));
        qWs.replaceAll(String::trim);

        //Prepare the query
        TypedQuery<ShaderDemo> searchQuery = entityManager.createNamedQuery("ShaderDemo.search", ShaderDemo.class)
                .setParameter("q", q)
                .setParameter("qWs", qWs)
                .setParameter("account", account);

        return searchQuery.getResultList();
    }

    @Override
    public ShaderDemo create(String sessionUUID, ShaderDemo source) throws FancyShaderSessionExpiredException {
        Account account = getAccount(sessionUUID);

        //Initialize the ShaderDemo
        ShaderDemo shaderDemo = new ShaderDemo();
        shaderDemo.setTitle(source.getTitle());
        shaderDemo.setCreator(account);
        shaderDemo.setDateCreated(new Date());
        shaderDemo.setCode(source.getCode() == null ? "" : source.getCode());
        shaderDemo.setTags(canonicalizeTags(source.getTags()));
        shaderDemo.setAvailability(source.getAvailability());

        //Add the ShaderDemo to the entity graph
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        entityManager.persist(shaderDemo);
        if (transaction.isActive()) {
            transaction.commit();
        }

        return shaderDemo;
    }

    @Override
    public void update(String sessionUUID, ShaderDemo demo) throws FancyShaderSessionExpiredException {
        Account account = getAccount(sessionUUID);

        //Obtain the corresponding managed entity with the id of the inputted ShaderDemo
        ShaderDemo managed = entityManager.find(ShaderDemo.class, demo.getId());
        if (!managed.getCreator().equals(account)) {
            return; //The owner of the session is not the creator of the ShaderDemo and therefore cannot update it
        }

        //Update the managed entity's fields
        managed.setTitle(demo.getTitle());
        managed.setCode(demo.getCode());
        managed.setTags(canonicalizeTags(demo.getTags()));
        managed.setAvailability(demo.getAvailability());

        //Commit the changes
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        entityManager.persist(managed);
        if (transaction.isActive()) {
            transaction.commit();
        }
    }

    /**
     * Change the given tags into a canonical form
     *
     * @param tags the tags to canonicalize
     * @return the canonical tags
     */
    private Set<String> canonicalizeTags(Set<String> tags) {
        if (tags == null) {
            tags = Collections.EMPTY_SET;
        }
        Set<String> canonicalTags = new HashSet<>();
        for (String tag : tags) {
            canonicalTags.add(tag.toLowerCase());
        }
        return canonicalTags;
    }

    @Override
    public void delightful(String sessionUUID, ShaderDemo demo) throws FancyShaderSessionExpiredException {
        Account account = getAccount(sessionUUID);

        //Obtain the corresponding managed entity with the id of the inputted ShaderDemo
        ShaderDemo managed = entityManager.find(ShaderDemo.class, demo.getId());

        //Toggle the demo's delightful status
        if (account.getDelights().contains(managed)) {
            account.getDelights().remove(managed); //The demo is marked and must be unmarked
        } else {
            account.getDelights().add(managed); //The demo is unmarked and must be marked
        }
        //Commit the changes
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        entityManager.persist(account);
        if (transaction.isActive()) {
            transaction.commit();
        }
    }

    @Override
    public void visit(String sessionUUID, ShaderDemo demo) throws FancyShaderSessionExpiredException {
        Account account = getAccount(sessionUUID);

        //Obtain the corresponding managed entity with the id of the inputted ShaderDemo
        ShaderDemo managed = entityManager.find(ShaderDemo.class, demo.getId());

        //Add visit to account
        if (!account.getVisits().contains(managed)) {
            account.getVisits().add(managed);
        }

        //Commit the changes
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        entityManager.persist(account);
        if (transaction.isActive()) {
            transaction.commit();
        }
    }

    @Override
    public Account getAccount(String sessionUUID) throws FancyShaderSessionExpiredException {
        //Get the Account stored in the session
        Account sessionAccount = FancyShaderSessionSingleton.instance.getSession(sessionUUID).getAccount();

        //Obtain the corresponding managed entity with the id of the session Account
        return entityManager.find(Account.class, sessionAccount.getId());
    }

    @Override
    public void delete(String sessionUUID, ShaderDemo shaderDemo) throws FancyShaderSessionExpiredException {
        //Obtain the corresponding managed entity with the id of the inputted ShaderDemo
        ShaderDemo managed = entityManager.find(ShaderDemo.class, shaderDemo.getId());

        if (!managed.getCreator().equals(getAccount(sessionUUID))) {
            return; //The owner of the session is not the creator of the demo and therefore cannot delete it
        }
        //Remove the demo
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        entityManager.remove(managed);
        if (transaction.isActive()) {
            transaction.commit();
        }
    }

    @Override
    public boolean isUsernameTaken(String username) throws FancyShaderInternalException {
        try {
            //Attempt to obtain the corresponding Account with the inputted username
            entityManager.createNamedQuery("Account.findByUsername").setParameter("username", username).getSingleResult();

            //No error occurred. The Account exists and the username is taken.
            return true;
        } catch (NoResultException e) {
            //No result obtained. The Account does not exist and the username is available.
            return false;
        } catch (Exception e) {
            //Some other error occured
            throw new FancyShaderInternalException();
        }
    }
    
    @Override
    public boolean isDemoTitleTaken(String title) throws FancyShaderInternalException {
        try {
            //Attempt to obtain the corresponding ShaderDemo with the inputted title
            entityManager.createNamedQuery("ShaderDemo.findByTitle").setParameter("title", title).getSingleResult();
            
            //No error occurred. The ShaderDemo exists and the title is taken.
            return true;
        } catch (NoResultException e) {
            //No result obtained. The ShaderDemo does not exist and the title is available.
            return false;
        } catch (Exception e) {
            //Some other error occured
            throw new FancyShaderInternalException();
        }
    }

    @Override
    public boolean isDelightful(String sessionUUID, ShaderDemo demo) throws FancyShaderSessionExpiredException {
        if (demo == null)
            return false;
        Account account = getAccount(sessionUUID);
        
        //Obtain the corresponding managed entity with the id of the inputted ShaderDemo
        ShaderDemo managed = entityManager.find(ShaderDemo.class, demo.getId());
        
        //Return whether the demo is contained in the owner of the sessions's delights
        return account.getDelights().contains(managed);
    }
}
