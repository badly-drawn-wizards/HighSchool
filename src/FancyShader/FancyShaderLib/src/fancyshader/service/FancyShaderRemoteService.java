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
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/**
 * Remote interface paralleling {@link FancyShaderService}
 * @author Reuben Steenekamp
 */
public interface FancyShaderRemoteService extends Remote {
    public String login(String username, String password) throws FancyShaderLoginException, FancyShaderInternalException, RemoteException;
    public void logout(String sessionUUID) throws RemoteException;
    public void registerAccount(String username, String password) throws FancyShaderInternalException, RemoteException;
    
    
    public Account getAccount(String sessionUUID) throws FancyShaderSessionExpiredException, RemoteException;
    public List<ShaderDemo> search(String sessionUUID, String q) throws FancyShaderSessionExpiredException, RemoteException;
    
    public ShaderDemo create(String sessionUUID, ShaderDemo source) throws FancyShaderSessionExpiredException, RemoteException;
    
    public void update(String sessionUUID, ShaderDemo demo) throws FancyShaderSessionExpiredException, RemoteException;
    
    public void delightful(String sessionUUID, ShaderDemo demo) throws FancyShaderSessionExpiredException, RemoteException;
    public void visit(String sessionUUID, ShaderDemo demo) throws FancyShaderSessionExpiredException, RemoteException;

    public boolean isUsernameTaken(String username) throws FancyShaderInternalException, RemoteException;

    public void delete(String uuid, ShaderDemo shaderDemo) throws FancyShaderSessionExpiredException, RemoteException;

    public boolean isDemoTitleTaken(String title) throws FancyShaderInternalException, RemoteException;

    public boolean isDelighted(String sessionUUID, ShaderDemo value) throws FancyShaderSessionExpiredException, RemoteException;
    
}
