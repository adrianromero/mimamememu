//    Mimamememu is launcher for M.A.M.E and other emulators.
//    Copyright (C) 2013-2014 Adrián Romero Corchado.
//    https://github.com/adrianromero/mimamememu
//
//    This file is part of Mimamememu
//
//    Mimamememu is free software: you can redistribute it and/or modify
//    it under the terms of the GNU General Public License as published by
//    the Free Software Foundation, either version 3 of the License, or
//    (at your option) any later version.
//
//    Mimamememu is distributed in the hope that it will be useful,
//    but WITHOUT ANY WARRANTY; without even the implied warranty of
//    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//    GNU General Public License for more details.
//
//    You should have received a copy of the GNU General Public License
//    along with Mimamememu.  If not, see <http://www.gnu.org/licenses/>.

package com.adr.mimame.platform;

import com.adr.mimame.GamesItem;
import com.adr.mimame.Platform;
import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.image.Image;

/**
 *
 * @author adrian
 */
public class SNESCommand implements Platform {
    
    private static final Logger logger = Logger.getLogger(SNESCommand.class.getName());    
    
    private final String[] command;
    private final File roms; 
    
    private Image defimage = null;
    private Image defcabinet = null;
    
    public SNESCommand(Properties options) {
        
        String emu = options.getProperty("snes.emu", "SNES9X");
        if ("SNES9X".equals(emu)) {
            command = new String[] {"snes9x"};
        } else if ("SNES9X-GTK".equals(emu)) {
            command = new String[] {"snes9x-gtk"};
        } else if ("ZSNES".equals(emu)) {
            command = new String[]{"zsnes", "-m"};
        } else {
            command = new String[] {emu};
        }
        
        roms = new File(options.getProperty("snes.roms"));

        try {
            defimage = new Image(getClass().getResourceAsStream("/com/adr/mimame/platform/snes.png"));
        } catch (IllegalArgumentException ex) {
            logger.log(Level.WARNING, null, ex);
            defimage = null;
        } 
        try {
            defcabinet = new Image(getClass().getResourceAsStream("/com/adr/mimame/platform/snes-cabinet.png"));
        } catch (IllegalArgumentException ex) {
            logger.log(Level.WARNING, null, ex);
            defcabinet = null;
        } 
    }
    
    @Override
    public String getPlatformName() {
        return "SNES";
    }

    @Override
    public String getPlatformTitle() {
        return "Super Nintendo Entertainment System";
    }

    @Override
    public String[] getCommand(GamesItem item) {
        
        String[] mycommand = new String[command.length + 1];
        System.arraycopy(command, 0, mycommand, 0, command.length);
        String extension = item.getProperty("extension", ".smc");
        mycommand[command.length] = new File(roms, item.getName() + extension).getPath();
        
        return mycommand;
    }

    @Override
    public Image getDefaultImage() {
        return defimage;
    }

    @Override
    public Image getDefaultCabinet() {
        return defcabinet;
    }

    @Override
    public List<GamesItem> getGames() {
        return Collections.EMPTY_LIST;
    }   
}
