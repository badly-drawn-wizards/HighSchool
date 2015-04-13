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
import fancyshader.entity.ShaderDemo;
import fancyshader.service.FancyShaderInternalException;
import fancyshader.service.FancyShaderLoginException;
import fancyshader.service.FancyShaderRemoteService;
import fancyshader.service.FancyShaderService;
import fancyshader.service.FancyShaderSessionExpiredException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

/**
 * Implementation of {@link fancyshader.service.FancyShaderRemoteService} that delegates
 * to an instance of {@link fancyshader.service.FancyShaderService}
 * @author Reuben Steenekamp
 */
public class FancyShaderRemoteServiceImpl extends UnicastRemoteObject implements FancyShaderRemoteService {
    private FancyShaderService service;
    public FancyShaderRemoteServiceImpl() throws RemoteException {
        super(0);
        service = new FancyShaderServiceImpl();
    }
    public FancyShaderRemoteServiceImpl(FancyShaderService service) throws RemoteException {
        super(0);
        this.service = service;
    }

    @Override
    public String login(String username, String password) throws FancyShaderLoginException, FancyShaderInternalException, RemoteException {
        return service.login(username, password);
    }

    @Override
    public void logout(String sessionUUID) throws RemoteException {
        service.logout(sessionUUID);
    }

    @Override
    public void registerAccount(String username, String password) throws FancyShaderInternalException, RemoteException {
        service.registerAccount(username, password);
    }

    @Override
    public Account getAccount(String sessionUUID) throws FancyShaderSessionExpiredException, RemoteException {
        return service.getAccount(sessionUUID);
    }

    @Override
    public List<ShaderDemo> search(String sessionUUID, String q) throws FancyShaderSessionExpiredException, RemoteException {
        return service.search(sessionUUID, q);
    }

    @Override
    public ShaderDemo create(String sessionUUID, ShaderDemo source) throws FancyShaderSessionExpiredException, RemoteException {
        return service.create(sessionUUID, source);
    }

    @Override
    public void update(String sessionUUID, ShaderDemo demo) throws FancyShaderSessionExpiredException, RemoteException {
        service.update(sessionUUID, demo);
    }

    @Override
    public void delightful(String sessionUUID, ShaderDemo demo) throws FancyShaderSessionExpiredException, RemoteException {
        service.delightful(sessionUUID, demo);
    }

    @Override
    public void visit(String sessionUUID, ShaderDemo demo) throws FancyShaderSessionExpiredException, RemoteException {
        service.visit(sessionUUID, demo);
    }

    @Override
    public boolean isUsernameTaken(String username) throws FancyShaderInternalException, RemoteException {
        return service.isUsernameTaken(username);
    }

    @Override
    public void delete(String uuid, ShaderDemo shaderDemo) throws FancyShaderSessionExpiredException, RemoteException {
        service.delete(uuid, shaderDemo);
    }

    @Override
    public boolean isDemoTitleTaken(String title) throws FancyShaderInternalException, RemoteException {
        return service.isDemoTitleTaken(title);
    }

    @Override
    public boolean isDelighted(String sessionUUID, ShaderDemo value) throws FancyShaderSessionExpiredException, RemoteException {
        return service.isDelightful(sessionUUID, value);
    }
}
