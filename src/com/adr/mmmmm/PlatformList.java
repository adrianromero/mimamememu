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

package com.adr.mmmmm;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.FileUtils;

/**
 *
 * @author adrian
 */
public class PlatformList {
    
    private final static Logger logger = Logger.getLogger(PlatformList.class.getName()); 
    
    public final static PlatformList INSTANCE = new PlatformList();
      
    private Platform[] platforms = {
        new com.adr.mmmmm.platform.MameCommand()
    };
    
    private PlatformList() {
    }

    public GamesItem createGame(String name, String title, String platform, String manufacturer, String year) {
        GamesItem item = new GamesItem(name, title, findPlatform(platform));
        item.setManufacturer(manufacturer);
        item.setYear(year);
        
        return item;
    }
    
    public List<GamesItem> getAllGames() {
        
        ArrayList<GamesItem> l = new ArrayList<GamesItem>();
        for (Platform p: platforms) {
            
            // try to load games from local folder
            List<GamesItem> platformgames = loadLocalGames(p.getPlatformName());
            if (platformgames == null) {
                // Load from platform and save for future use.
                platformgames = p.getGames();
                saveLocalGames(platformgames, p.getPlatformName());
            }
            // And finally add for display
            l.addAll(platformgames);           
        }
        
        Collections.sort(l);
        return l;
    }
    
    private void saveLocalGames(List<GamesItem> games, String platformname) {
        
        System.out.println("saving");
        
        DataOutputStreamExt out = null;
        File f = new File(new File(System.getProperty("user.home"), ".mimamememu"), platformname);
        try {    
            // Create cache folder
            f.mkdirs();
            
            out = new DataOutputStreamExt(new FileOutputStream(new File(f, "games.ser")));
            out.writeInt(games.size());
            for (GamesItem g : games) {
                g.save(out, f);
            }
            
            return;
            
        } catch (IOException ex) {
            logger.log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException ex) {
                logger.log(Level.SEVERE, null, ex);
            }
        }
        
        // Delete directory if not possible to save
        try {
            FileUtils.deleteDirectory(f);
        } catch (IOException ex) {
            logger.log(Level.SEVERE, null, ex);
        }

    }
    
    private List<GamesItem> loadLocalGames(String platformname) {
        
        System.out.println("loading");
        
        File f = new File(new File(System.getProperty("user.home"), ".mimamememu"), platformname);
        DataInputStreamExt in = null;
        if (f.exists()){
            try {
                
                in = new DataInputStreamExt(new FileInputStream(new File(f, "games.ser")));
                int size = in.readInt();
                ArrayList<GamesItem> games = new ArrayList<GamesItem>();
                for (int i = 0; i < size; i++) {
                    games.add(new GamesItem(in, f));
                }
                System.out.println("loading success");
                return games;
            } catch (FileNotFoundException ex) {
                Logger.getLogger(PlatformList.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(PlatformList.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    if (in != null) {
                        in.close();
                    }
                } catch (IOException ex) {
                    logger.log(Level.SEVERE, null, ex);
                }
            }
            
            // Delete directory if not possible to save
            try {
                FileUtils.deleteDirectory(f);
            } catch (IOException ex) {
                logger.log(Level.SEVERE, null, ex);
            }   
        }
        System.out.println("loading fail");
        return null;
    }
     
    public Platform findPlatform(String platformname) {
        
        for (Platform gc: platforms) {
            if (gc.getPlatformName().equals(platformname)) {
                return gc;
            }
        }
        return new PlatformUnsupported(platformname);
    }      
}
