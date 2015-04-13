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

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.RemoteServer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.PersistenceException;

/**
 * The entry point for the server
 * @author Reuben Steenekamp
 */

public class FancyShaderServer {
    public static void main(String[] args) throws Exception{
        try {
            int PORT = 1099;
            
            Registry registry = LocateRegistry.createRegistry(PORT);
            RemoteServer server = new FancyShaderRemoteServiceImpl();
            registry.rebind("FancyShaderRemoteService", server);
        } catch (PersistenceException | RemoteException e) {
            Logger.getLogger(FancyShaderServer.class.getName()).log(Level.SEVERE, null, e);
            javax.swing.JOptionPane.showMessageDialog(null, e.toString());
            System.exit(1);
        }
    }
}
