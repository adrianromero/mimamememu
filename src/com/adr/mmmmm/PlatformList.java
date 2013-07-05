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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.commons.io.FileUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author adrian
 */
public class PlatformList {
    
    private final static Logger logger = Logger.getLogger(PlatformList.class.getName()); 
    
    public final static PlatformList INSTANCE = new PlatformList();
     
    private File mimamememuhome;
    
    private Platform[] platforms = {
        new com.adr.mmmmm.platform.MameCommand(),
        new com.adr.mmmmm.platform.SNESCommand()
    };
    
    private PlatformList() {
        mimamememuhome = new File(System.getProperty("user.home"), ".mimamememu");
    }

    public GamesItem createGame(String name, String title, String platform, String manufacturer, String year) {
        GamesItem item = new GamesItem(name, title, findPlatform(platform));
        item.setManufacturer(manufacturer);
        item.setYear(year);
        
        return item;
    }
    
    public List<GamesItem> getAllGames(boolean refresh) {
        
        ArrayList<GamesItem> l = new ArrayList<GamesItem>();
        
        // Get games from list
        
        File generalfolder = new File(mimamememuhome, "_GENERAL");
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(new FileInputStream(new File(generalfolder, "games.xml")));
            
            NodeList nodegames = doc.getElementsByTagName("game");
            for (int i = 0; i < nodegames.getLength(); i++) {
                GamesItem item = new GamesItem((Element) nodegames.item(i), generalfolder);    
                l.add(item);
            }
            
        } catch (IOException ex) {
            Logger.getLogger(PlatformList.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(PlatformList.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SAXException ex) {
            Logger.getLogger(PlatformList.class.getName()).log(Level.SEVERE, null, ex);
        }

        // Get games from platforms      
        for (Platform p: platforms) {
            
            if (refresh) {
                clearLocalGames(p.getPlatformName());
            }
            
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
        
        // Sort games and return
        Collections.sort(l);
        return l;
    }
    
    
    private void clearLocalGames(String platformname) {
        File f = new File(mimamememuhome, platformname);
        // Delete directory if not possible to save
        try {
            FileUtils.deleteDirectory(f);
        } catch (IOException ex) {
            logger.log(Level.SEVERE, null, ex);
        }        
    }
    
    private void saveLocalGames(List<GamesItem> games, String platformname) {
        
        DataOutputStreamExt out = null;
        File f = new File(mimamememuhome, platformname);
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
        
        File f = new File(mimamememuhome, platformname);
        DataInputStreamExt in = null;
        if (f.exists()){
            try {
                
                in = new DataInputStreamExt(new FileInputStream(new File(f, "games.ser")));
                int size = in.readInt();
                ArrayList<GamesItem> games = new ArrayList<GamesItem>();
                for (int i = 0; i < size; i++) {
                    games.add(new GamesItem(in, f));
                }
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
        return null;
    }
    
    public File getHome() {
        return mimamememuhome;
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
