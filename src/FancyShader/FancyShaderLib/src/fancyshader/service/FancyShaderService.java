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
package fancyshader.service;

import fancyshader.entity.Account;
import fancyshader.entity.ShaderDemo;
import java.util.List;

/**
 * Interface providing methods for Client-Server communication in FancyShader.
 * @author Reuben Steenekamp
 */
public interface FancyShaderService {
    /**
     * Establish a session with the respective {@link fancyshader.entity.Account} as the owner
     * @param username the account username
     * @param password the account password
     * @return the session UUID of the established session
     * @throws FancyShaderLoginException
     * @throws FancyShaderInternalException
     */
    public String login(String username, String password) throws FancyShaderLoginException, FancyShaderInternalException;
    
    /**
     * Invalidates a session
     * @param sessionUUID session UUID of the session to invalidate
     */
    public void logout(String sessionUUID);
    
    /**
     * Get the owner of the session identified by the {@code sessionUUID}
     * @param sessionUUID the session UUID of the session from which the owner is retrieved
     * @return the account with which the session was created
     * @throws FancyShaderSessionExpiredException
     */
    public Account getAccount(String sessionUUID) throws FancyShaderSessionExpiredException;
    
    /**
     * Create a new account with the {@code username} and {@code password} provided.
     * @param username the username to use
     * @param password the password to use
     * @throws FancyShaderInternalException 
     */
    public void registerAccount(String username, String password) throws FancyShaderInternalException;
    
    /**
     * Search all the {@link fancyshader.entity.ShaderDemo} demos in the context
     * of the session identified by {@code sessionUUID} with {@code query} as the search parameter.
     * @param sessionUUID the sessionUUID of session providing context for the search
     * @param query the search parameter
     * @return the result list of {@link fancyshader.entity.ShaderDemo} demos
     * @throws FancyShaderSessionExpiredException 
     */
    public List<ShaderDemo> search(String sessionUUID, String query) throws FancyShaderSessionExpiredException;
        
    /**
     * Create a new {@link fancyshader.entity.ShaderDemo} demo, copying relevant fields from {@code source}.
     * Demos with {@code accessability} of greater than {@code fancyshader.entity.ShaderDemo.PRIVATE} will only 
     * appear in the search if their creator is the owner of the session.
     * @param sessionUUID the session UUID of the session providing the context for the creation of the {@link fancyshader.entity.ShaderDemo}
     * @param source the {@link fancyshader.entity.ShaderDemo} from which the fields are copied
     * @return the {@link fancyshader.entity.ShaderDemo} created
     * @throws FancyShaderSessionExpiredException
     */
    public ShaderDemo create(String sessionUUID, ShaderDemo source) throws FancyShaderSessionExpiredException;
    
    /**
     * Update an existing {@link fancyshader.entity.ShaderDemo} demo, copying relevant fields from {@code source}. 
     * Will do nothing if the owner of the session is not the creator of the demo.
     * @param sessionUUID the session UUID of the session providing the context for the updating of the {@link fancyshader.entity.ShaderDemo}
     * @param demo the {@link fancyshader.entity.ShaderDemo} from which the fields are copied and the {@code id} of the demo to update is obtained.
     * @throws FancyShaderSessionExpiredException 
     */
    public void update(String sessionUUID, ShaderDemo demo) throws FancyShaderSessionExpiredException;
    
    /**
     * Delete an existing {@link fancyshader.entity.ShaderDemo} demo.
     * Will do nothing if the owner of the session is not the creator of the demo.
     * @param uuid the session UUID of the session providing the context for the deletion of the {@link fancyshader.entity.ShaderDemo}
     * @param demo the {@link fancyshader.entity.ShaderDemo} to be deleted
     * @throws FancyShaderSessionExpiredException 
     */
    public void delete(String uuid, ShaderDemo demo) throws FancyShaderSessionExpiredException;
    
    /**
     * Toggle {@code demo} as delightful for the owner of the session identified by {@code sessionUUID}. 
     * If {@code state} is true then {@code demo} will be marked as delightful, else {@code demo} will be unmarked as delightful.
     * @param sessionUUID the session UUID of the session providing the context for the operation
     * @param demo the demo to mark or unmark
     * @throws FancyShaderSessionExpiredException 
     */
    public void delightful(String sessionUUID, ShaderDemo demo) throws FancyShaderSessionExpiredException;
    
    /**
     * Get whether a given demo is marked as delightful for the owner of the session identified by {@code sessionUUID}
     * @param sessionUUID the session UUID of the session providing the context for the operation
     * @param demo the demo that is checked
     * @return whether {@code demo} is marked as delightful
     * @throws FancyShaderSessionExpiredException 
     */
    public boolean isDelightful(String sessionUUID, ShaderDemo demo) throws FancyShaderSessionExpiredException;
    
    /**
     * Marks {@code demo} as visited by the owner of the session identified by {@code sessionUUID}. The operation is idempotent.
     * @param sessionUUID the session UUID of the session providing the context for the operation
     * @param demo the demo to mark
     * @throws FancyShaderSessionExpiredException 
     */
    public void visit(String sessionUUID, ShaderDemo demo) throws FancyShaderSessionExpiredException;

    /**
     * Test whether an {@link fancyshader.entity.Account} with a username {@code username} exists.
     * @param username the username to test
     * @return whether such an {@link fancyshader.entity.Account} exists
     * @throws FancyShaderInternalException 
     */
    public boolean isUsernameTaken(String username) throws FancyShaderInternalException;
    
    /**
     * Test whether a {@link fancyshader.entity.ShaderDemo} with a title {@code title} exists.
     * @param title the title to test
     * @return whether such a {@link fancyshader.entity.ShaderDemo} exists.
     * @throws FancyShaderInternalException 
     */
    public boolean isDemoTitleTaken(String title) throws FancyShaderInternalException;


}
