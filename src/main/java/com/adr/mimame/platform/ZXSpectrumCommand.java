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
import com.adr.mimame.ProgressUpdate;
import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;
import javafx.scene.image.Image;

/**
 *
 * @author adrian
 */
public class ZXSpectrumCommand implements Platform {
    
    private static final Logger logger = Logger.getLogger(SNESCommand.class.getName());  
    
    private final String[] command;
    private final File roms; 
    
    private Image defimage = null;
    private Image defcabinet = null;
    
    public ZXSpectrumCommand(Properties options) {
             
        String emu = options.getProperty("zxspectrum.emu", "FUSE");
        if ("FUSE".equals(emu)) {
            command = new String[] {"fuse"};
        } else {
            command = new String[] {emu};
        }
        
        roms = new File(options.getProperty("zxspectrum.roms"));

        defimage = new Image(getClass().getResourceAsStream("/images/zxspectrum.png"));
        defcabinet = new Image(getClass().getResourceAsStream("/images/zxspectrum-cabinet.png"));
    }
    
    @Override
    public String getPlatformName() {
        return "ZXSPECTRUM";
    }

    @Override
    public String getPlatformTitle() {
        return "Sinclair ZX Spectrum";
    }

    @Override
    public String[] getCommand(GamesItem item) {
        
        String[] mycommand = new String[command.length + 1];
        System.arraycopy(command, 0, mycommand, 0, command.length);
        String extension = item.getProperty("extension", ".z80");
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
    public List<GamesItem> getGames(ProgressUpdate progress) {
        return Collections.EMPTY_LIST;
    }   
}
