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

package com.adr.mimame;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
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
     
    private Properties options;
    private File mimamememuhome;
    
    private Platform[] platforms;
    
    private List<Platform> errorplatforms = new ArrayList<Platform>();
    
    private PlatformList() {    
    }
    
    public void init() {
        mimamememuhome = new File(System.getProperty("user.home"), ".mimamememu");
        
        // Loading default properties
        options = loadDefaults();
      
        InputStream in = null;
        try {
            in = new FileInputStream(new File(mimamememuhome, "mimamememu.properties"));
            options.load(in);
        } catch (IOException ex) {
            logger.log(Level.INFO, ex.getLocalizedMessage());
            
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException ex) {
                    logger.log(Level.SEVERE, null, ex);
                }
            }
        }
        
        platforms = new Platform [] {
            new com.adr.mimame.platform.MameCommand(options),
            new com.adr.mimame.platform.SNESCommand(options),
            new com.adr.mimame.platform.ZXSpectrumCommand(options)
        };        
    }
    
    private Properties loadDefaults() {
        Properties opts = new Properties();   
        
        opts.setProperty("display.screenmode", "window"); // can be "window" or "fullscreen"
        opts.setProperty("display.listmode", "0"); // can be "0", "1" or "2".
        
        opts.setProperty("snes.roms", new File(getHome(), "ROMS").getPath());
        opts.setProperty("snes.emu", "SNES9X");      
        
        opts.setProperty("zxspectrum.roms", new File(getHome(), "ROMS").getPath());
        opts.setProperty("zxspectrum.emu", "FUSE");      
        return opts;
    }

    public GamesItem createGame(String name, String title, String platform, String manufacturer, String year) {
        GamesItem item = new GamesItem(name, title, findPlatform(platform));
        item.setManufacturer(manufacturer);
        item.setYear(year);
        
        return item;
    }
    
    public List<GamesItem> getAllGames(boolean refresh) {
        
        // Initialize errors
        errorplatforms.clear();
        
        // Initialize gameslist
        ArrayList<GamesItem> l = new ArrayList<GamesItem>();
        
        // Get games from list        
        List<GamesItem> lgeneral = loadList(new File(mimamememuhome, "GENERAL"));
        if (lgeneral != null) {
            l.addAll(lgeneral);
        }

        // Get games from platforms      
        for (Platform p: platforms) {
            
            if (refresh) {
                clearConfigFolder("_" + p.getPlatformName());
            }
            
            // try to load games from local folder
            List<GamesItem> platformgames = loadList(new File(mimamememuhome, "_" + p.getPlatformName()));
            if (platformgames == null) {
                try {
                    // Load from platform and save for future use.
                    platformgames = p.getGames();
                    clearConfigFolder("_" + p.getPlatformName());
                    if (!saveList(platformgames, new File(mimamememuhome, "_" + p.getPlatformName()))) {
                        clearConfigFolder("_" + p.getPlatformName());
                    }                    
                } catch (PlatformException ex) {
                    errorplatforms.add(p);
                }
            }
            // And finally add for display
            l.addAll(platformgames);           
        }
        
        // Sort games and return
        Collections.sort(l);
        return l;
    }
    
    public List<Platform> getLastErrors() {
        return errorplatforms;
    }
    
    private void clearConfigFolder(String configfolder) {
        File f = new File(mimamememuhome, configfolder);
        // Delete directory if not possible to save
        try {
            FileUtils.deleteDirectory(f);
        } catch (IOException ex) {
            logger.log(Level.SEVERE, null, ex);
        }        
    }
    
    private boolean saveList(List<GamesItem> games, File f) {
        
        try {           
            FileUtils.forceMkdir(f);
            
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.newDocument();
            Element root = doc.createElement("gameslist");
            doc.appendChild(root);        

            for (GamesItem g : games) {
                Element e = doc.createElement("game");
                root.appendChild(e);
                g.toElement(e, f);
            }

            // write the content into xml file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(f, "games.xml"));
            transformer.transform(source, result);  
            return true;
        } catch (IOException ex) {
            logger.log(Level.SEVERE, null, ex);            
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(PlatformList.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TransformerConfigurationException ex) {
            Logger.getLogger(PlatformList.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TransformerException ex) {
            Logger.getLogger(PlatformList.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;       
    }

    private List<GamesItem> loadList(File f) {
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(new File(f, "games.xml"));
            
            ArrayList<GamesItem> games = new ArrayList<GamesItem>();
            
            NodeList nodegames = doc.getElementsByTagName("game");
            for (int i = 0; i < nodegames.getLength(); i++) {
                GamesItem item = new GamesItem((Element) nodegames.item(i), f);    
                games.add(item);
            }
            
            return games;
        } catch (FileNotFoundException ex) {
            logger.log(Level.INFO, ex.getLocalizedMessage());
        } catch (IOException ex) {
            logger.log(Level.SEVERE, null, ex);
        } catch (ParserConfigurationException ex) {
            logger.log(Level.SEVERE, null, ex);
        } catch (SAXException ex) {
            logger.log(Level.SEVERE, null, ex);
        }         
        return null;
    }

    public File getHome() {
        return mimamememuhome;
    }
     
    public String getOption(String key) {
        return options.getProperty(key);
    }
    
    public String getOption(String key, String defaultValue) {
        return options.getProperty(key, defaultValue);
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
