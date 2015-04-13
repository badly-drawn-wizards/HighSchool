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
import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

/**
 * Immutable object class encapsulating session information.
 * @author Reuben Steenekamp
 */
public class FancyShaderSession implements Serializable {
    public static final long EXPIRE = 2*7*24*60*60; // 2 weeks in seconds;
    
    public static FancyShaderSession create(Account account) {
        FancyShaderSession session = new FancyShaderSession(account);
        FancyShaderSessionSingleton.instance.putSession(session);
        return session;
    }
    
    private Account account;
    private String uuid;
    private Date creationDate;

    private FancyShaderSession(Account account){
        this.uuid = UUID.randomUUID().toString();
        this.creationDate = new Date();
        this.account = account;
    }

    
    /**
     * @return whether the session has expired
     */
    public boolean hasExpired() {
        return (new Date()).toInstant().getEpochSecond()-getCreationDate().toInstant().getEpochSecond() > EXPIRE;
    }

    /**
     * @return the account
     */
    public Account getAccount() {
        return account;
    }

    /**
     * @return the UUID
     */
    public String getUUID() {
        return uuid;
    }

    /**
     * @return the creation date
     */
    public Date getCreationDate() {
        return creationDate;
    }
}
