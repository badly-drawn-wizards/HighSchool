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

import fancyshader.service.FancyShaderSessionExpiredException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Singleton flyweight of user sessions.
 * @author Reuben Steenekamp
 */
public class FancyShaderSessionSingleton {
    public static final FancyShaderSessionSingleton instance = new FancyShaderSessionSingleton();
    
    private Map<String, FancyShaderSession> sessions;
    
    private FancyShaderSessionSingleton() {
        sessions = new HashMap<>();
    }
    
    /**
     * @param uuid the UUID of the session to get from the flyweight
     * @return the corresponding session
     * @throws FancyShaderSessionExpiredException when the session has expired or never existed
     */
    public FancyShaderSession getSession(String uuid) throws FancyShaderSessionExpiredException {
        reviseSessions();
        if(sessions.containsKey(uuid))
            return sessions.get(uuid);
        else
            throw new FancyShaderSessionExpiredException();
    }
    
    /**
     * Add a session to the flyweight
     * @param session the session to add to the flyweight
     */
    public void putSession(FancyShaderSession session) {
        sessions.put(session.getUUID(), session);
    }
    
    private void reviseSessions() {
        Iterator<String> it = sessions.keySet().iterator();
        while(it.hasNext()) {
            String uuid = it.next();
            if(sessions.get(uuid).hasExpired()) invalidateSession(uuid);
        }
    }

    /**
     * Remove the session corresponding to to given UUID from the flyweight, rendering it expired.
     * @param sessionUUID 
     */
    public void invalidateSession(String sessionUUID) {
        sessions.remove(sessionUUID);
    }
    
}
