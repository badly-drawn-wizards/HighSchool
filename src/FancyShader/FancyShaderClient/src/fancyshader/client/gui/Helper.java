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
package fancyshader.client.gui;

import fancyshader.service.FancyShaderInternalException;
import fancyshader.service.FancyShaderLoginException;
import fancyshader.service.FancyShaderSessionExpiredException;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Helper functions for controllers
 *
 * @author Reuben Steenekamp
 */
public class Helper {

    /**
     * Takes in a list of semicolon delimited tags and returns a set containing 
     * the whitespace trimmed tags
     * @param tags the tags to be parsed
     * @return the set of tags
     */
    public static Set<String> fromDelimitedTags(String tags) {
        List<String> tagList = Arrays.asList(tags.split(";"));
        tagList.replaceAll(String::trim);
        return new HashSet<>(tagList);
    }

    /**
     * Takes in a set of tags and returns a semicolon delimited String of the tags
     * @param tagSet the set of tags
     * @return the semicolon delimited tags
     */
    public static String toDelimitedTags(Set<String> tagSet) {
        Set<String> tags = tagSet != null ? tagSet : Collections.EMPTY_SET;
        String joinedTags = "";
        for (String tag : tags) {
            joinedTags += tag + "; ";
        }
        return joinedTags;
    }

    /**
     * @return the path to the system property "user.dir"
     */
    public static URL getUserPath() {
        try {
            return new URL("file:///" + System.getProperty("user.dir"));
        } catch (MalformedURLException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    /**
     * Takes in an exception and returns a user friendly message to display
     * @param e the exception
     * @return the user friendly message
     */
    public static String getErrorText(Exception e) {
        String text = "An error occurred";
        try {
            throw e;
        } catch (RemoteException ex) {
            text = "Error communicating with server";
        } catch (FancyShaderInternalException ex) {
            text = "An internal server error has occurred";
        } catch (FancyShaderLoginException ex) {
            text = "Login details incorrect";
        } catch (FancyShaderSessionExpiredException ex) {
            text = "Your session has expired";
        } catch (Exception ex) {}
        return text;
    }
}
