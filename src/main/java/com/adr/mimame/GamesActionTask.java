//    MIMAMEMEMU is a launcher for M.A.M.E and other emulators.
//    Copyright (C) 2013 Adrián Romero Corchado.
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
import java.text.MessageFormat;
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
                    MessageFormat.format(ResourceBundle.getBundle("com/adr/mimame/res/messages").getString("msg.platformnotsupported"), new Object[]{item.getTitle()}),
                    MessageFormat.format(ResourceBundle.getBundle("com/adr/mimame/res/messages").getString("body.platformnotsupported"), new Object[]{item.getPlatform().getPlatformName()}));
        }

        try {            
            // if (true) throw new IOException("test message");
            
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
                    MessageFormat.format(ResourceBundle.getBundle("com/adr/mimame/res/messages").getString("msg.platformerror"), new Object[]{item.getTitle()}),
                    MessageFormat.format(ResourceBundle.getBundle("com/adr/mimame/res/messages").getString("body.platformerror"), new Object[]{result}));
            }
        } catch (InterruptedException ex) {
            logger.log(Level.SEVERE, null, ex);
            throw new GamesActionException(
                    MessageFormat.format(ResourceBundle.getBundle("com/adr/mimame/res/messages").getString("msg.platforminterrupted"), new Object[]{item.getTitle()}),
                    ex);
        } catch (IOException ex) {
            logger.log(Level.SEVERE, null, ex);
            throw new GamesActionException(
                    MessageFormat.format(ResourceBundle.getBundle("com/adr/mimame/res/messages").getString("msg.platformcannotexecute"), new Object[]{item.getTitle()}),
                    ex);
        } 
    }
}
