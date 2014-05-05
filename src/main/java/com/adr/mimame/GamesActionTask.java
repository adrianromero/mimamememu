//    MIMAMEMEMU is a launcher for M.A.M.E and other emulators.
//    Copyright (C) 2013-2014 Adrián Romero Corchado.
//    https://github.com/adrianromero/mimamememu
//
//    This file is part of Mimamememu
//
//    MIMAMEMEMU is free software: you can redistribute it and/or modify
//    it under the terms of the GNU General Public License as published by
//    the Free Software Foundation, either version 3 of the License, or
//    (at your option) any later version.
//
//    MIMAMEMEMU is distributed in the hope that it will be useful,
//    but WITHOUT ANY WARRANTY; without even the implied warranty of
//    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//    GNU General Public License for more details.
//
//    You should have received a copy of the GNU General Public License
//    along with MIMAMEMEMU.  If not, see <http://www.gnu.org/licenses/>.

package com.adr.mimame;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.concurrent.Task;

/**
 *
 * @author adrian
 */
public class GamesActionTask extends Task<Void> {
    
    private final static Logger logger = Logger.getLogger(GamesActionTask.class.getName()); 

    private final GamesItem item;
    
    public GamesActionTask(GamesItem item) {
        this.item = item;
    }

    @Override
    protected Void call() throws GamesActionException {
            
        final String[] command = item.getCommand();
        if (command == null) {
            logger.log(Level.INFO, "Platform not supported: {0}", item.getPlatform().getPlatformName());
            throw new GamesActionException(
                    String.format(ResourceBundle.getBundle("properties/messages").getString("msg.platformnotsupported_title"), item.getTitle()),
                    String.format(ResourceBundle.getBundle("properties/messages").getString("msg.platformnotsupported_body"), item.getPlatform().getPlatformName()));
        }

        try {                       
            Process p = Runtime.getRuntime().exec(command);

            BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line;
            while ((line = br.readLine()) != null) {
                logger.info(line);
            }
                       
            int result = p.waitFor();
            if (result == 0) {
                return null; // success;
            } else {
                logger.log(Level.SEVERE, "Return error: {0}", result);
                throw new GamesActionException(
                    String.format(ResourceBundle.getBundle("properties/messages").getString("msg.platformerror_title"), item.getTitle()),
                    String.format(ResourceBundle.getBundle("properties/messages").getString("msg.platformerror_body"), result));
            }
        } catch (InterruptedException ex) {
            logger.log(Level.SEVERE, null, ex);
            throw new GamesActionException(
                    String.format(ResourceBundle.getBundle("properties/messages").getString("msg.platforminterrupted_title"), item.getTitle()),
                    String.format(ResourceBundle.getBundle("properties/messages").getString("msg.platforminterrupted_body"), item.getTitle()),
                    ex);
        } catch (IOException ex) {
            logger.log(Level.SEVERE, null, ex);
            throw new GamesActionException(
                    String.format(ResourceBundle.getBundle("properties/messages").getString("msg.platformcannotexecute_title"), item.getTitle()),
                    String.format(ResourceBundle.getBundle("properties/messages").getString("msg.platformcannotexecute_body"), item.getTitle()),
                    ex);
        } 
    }
}
