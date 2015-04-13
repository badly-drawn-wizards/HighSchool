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
package fancyshader.client;

import fancyshader.service.FancyShaderRemoteService;
import java.rmi.Remote;
import java.rmi.registry.LocateRegistry;

/**
 * A static library to obtain the remote service for the client
 * @author Reuben Steenekamp
 */
public class FancyShaderRemote {
    /**
     * obtains the {@link fancyshader.service.FancyShaderRemoteService}
     * from the RMI registry at the {@code host} and {@code port} provided.
     * @param host the host of the RMI registry
     * @param port the port of the RMI registry
     * @return the remote service
     * @throws Exception 
     */
    public static FancyShaderRemoteService create(String host, int port) throws Exception {
        Remote remote = LocateRegistry.getRegistry(host, port).lookup("FancyShaderRemoteService");
        return (FancyShaderRemoteService)remote;
    }
}
