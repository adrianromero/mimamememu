//    Mimamememu is launcher for M.A.M.E and other emulators.
//    Copyright (C) 2013 Adrián Romero Corchado.
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

package com.adr.mmmmm.platform;

import com.adr.mmmmm.GamesItem;
import com.adr.mmmmm.Platform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 *
 * @author adrian
 */
public class ZXSpectrumCommand implements Platform {
    
    private String[] command;
    private File roms; 
    
    private BufferedImage defimage = null;
    private BufferedImage defcabinet = null;
    
    public ZXSpectrumCommand(Properties options) {
        
        String emu = options.getProperty("zxspectrum.emu", "FUSE");
        if ("FUSE".equals(emu)) {
            command = new String[] {"fuse"};
        } else {
            command = new String[] {emu};
        }
        
        roms = new File(options.getProperty("zxspectrum.roms"));

        try {
            defimage = ImageIO.read(getClass().getResourceAsStream("/com/adr/mmmmm/platform/zxspectrum.png"));
        } catch (IOException ex) {
            Logger.getLogger(MameCommand.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            defcabinet = ImageIO.read(getClass().getResourceAsStream("/com/adr/mmmmm/platform/zxspectrum-cabinet.png"));
        } catch (IOException ex) {
            Logger.getLogger(MameCommand.class.getName()).log(Level.SEVERE, null, ex);
        }
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
    public BufferedImage getDefaultImage() {
        return defimage;
    }

    @Override
    public BufferedImage getDefaultCabinet() {
        return defcabinet;
    }

    @Override
    public List<GamesItem> getGames() {
        return Collections.EMPTY_LIST;
    }   
}
