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

import com.adr.mimame.Platform;
import com.adr.mimame.GamesItem;
import com.adr.mimame.PlatformException;
import com.adr.mimame.ProgressUpdate;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.image.Image;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author adrian
 */

public class MameCommand implements Platform {
    
    private static final Logger logger = Logger.getLogger(MameCommand.class.getName());

    private String[] command;   
    private Image defimage = null;
    private Image defcabinet = null;
    
    public MameCommand(Properties options) {   
        
        String emu = options.getProperty("mame.emu", "MAME");
        if ("MAME".equals(emu)) {
            command = new String[] {"mame"};
        } else if ("MAME64".equals(emu)) {
            command = new String[] {"mame64"};
        } else {
            command = new String[] {emu};
        }
        
        // Add rompath
        String roms = options.getProperty("mame.roms");        
        if (roms != null && !roms.equals("")) {
            String[] newcommand = new String[command.length + 2];
            System.arraycopy(command, 0, newcommand, 0, command.length);
            newcommand[command.length] = "-rompath";
            newcommand[command.length + 1] = roms;
            command = newcommand;
        }
        
        defimage = new Image(getClass().getResourceAsStream("/images/mame.png"));
        defcabinet = new Image(getClass().getResourceAsStream("/images/mame-cabinet.png"));      
    }
    
    @Override
    public String getPlatformName() {
        return "MAME";
    }
    
    @Override
    public String getPlatformTitle() {
        return "M.A.M.E.";
    }

    @Override
    public String[] getCommand(GamesItem item) {
        return getMameCommand(item.getName());
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
    public List<GamesItem> getGames(ProgressUpdate progress) throws PlatformException {
       
        ArrayList<GamesItem> games = new ArrayList<GamesItem>();
        
        try {            
            // First read the names of available games
            progress.updateMessage(ResourceBundle.getBundle("properties/messagesmame").getString("msg.verifying"));
            logger.log(Level.INFO, "Verifying MAME ROMS installed.");
            ArrayList<String> names = new ArrayList<String>();    
            
            Process p;
            
            p = Runtime.getRuntime().exec(getMameCommand("-verifyroms"));
            BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line;
            while ((line = br.readLine()) != null) {
                logger.log(Level.FINE, ">> {0}", line);
                String[] tokens = line.split(" ");
                if (tokens.length >= 4 && "romset".equals(tokens[0])) {
                    if ("is".equals(tokens[tokens.length - 2]) && "good".equals(tokens[tokens.length - 1])) {
                        progress.updateMessage(String.format(ResourceBundle.getBundle("properties/messagesmame").getString("msg.found"), tokens[1]));
                        logger.log(Level.INFO, "Found roms {0}", tokens[1]);
                        names.add(tokens[1]);
                    } else if ("is".equals(tokens[tokens.length - 3]) && "best".equals(tokens[tokens.length - 2]) && "available".equals(tokens[tokens.length - 1])) {
                        progress.updateMessage(String.format(ResourceBundle.getBundle("properties/messagesmame").getString("msg.found"), tokens[1]));
                        logger.log(Level.INFO, "Found roms {0}", tokens[1]);
                        names.add(tokens[1]);
                    }                    
                }
            }
            p.waitFor();            
            
            for (String n: names) {
                progress.updateMessage(String.format(ResourceBundle.getBundle("properties/messagesmame").getString("msg.details"), n));
                logger.log(Level.INFO, "Processsing details for roms {0}", n);
                p = Runtime.getRuntime().exec(getMameCommand("-listxml", n));

                try {
                    DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                    DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                    Document doc = dBuilder.parse(p.getInputStream());
                    NodeList nodegames = doc.getElementsByTagName("game");
                    for (int i = 0; i < nodegames.getLength(); i++) {
                        
                        Element e = (Element) nodegames.item(i);
                        if (!"no".equals(e.getAttribute("runnable"))) { // Is a runnable game
                            final GamesItem item = new GamesItem(
                                    e.getAttribute("name"),
                                    getElementText(e, "description"),
                                    this);
                            item.setManufacturer(getElementText(e, "manufacturer"));
                            item.setYear(getElementText(e, "year"));

                            // driver attributes
                            NodeList nc = e.getElementsByTagName("driver");
                            if (nc != null && nc.getLength() > 0) {
                                Element ec =(Element) nc.item(0);
                                item.setDriveremulation(ec.getAttribute("emulation"));
                                item.setDrivercolor(ec.getAttribute("color"));
                                item.setDriversound(ec.getAttribute("sound"));
                                item.setDrivergraphic(ec.getAttribute("graphic"));
                                item.setDriverstate(ec.getAttribute("savestate"));
                            }

                            item.setTitles("http://www.mamedb.com/titles/" + item.getName() + ".png");
                            item.setCabinets("http://www.mamedb.com/cabinets/" + item.getName() + ".png");
                            item.setSnap("http://www.mamedb.com/snap/" + item.getName() + ".png");
                            item.setMarquees("http://www.mamedb.com/marquees/" + item.getName() + ".png");
                     
 
                            progress.updateMessage(String.format(ResourceBundle.getBundle("properties/messagesmame").getString("msg.addgame"), item.getName()));
                            logger.log(Level.INFO, "Adding game {0}", item.getName());
                            games.add(item);
                        }
                    }                    
                } catch (SAXException ex) {
                    // skip game
                    logger.log(Level.WARNING, "No details for game: {0}", n);
                }

                p.waitFor();
            }
            
            // Wait for pending images
            progress.updateMessage(ResourceBundle.getBundle("properties/messagesmame").getString("msg.loadingimages"));
            logger.log(Level.INFO, "Loading images.");
            progress.updateMessage("");
                     
        } catch (IOException | InterruptedException | ParserConfigurationException ex) {
            logger.log(Level.SEVERE, null, ex);
            throw new PlatformException(ex);
        }        
        return games;
    }
    
    private String getElementText(Element e, String tag) {
        NodeList n = e.getElementsByTagName(tag);
        if (n != null && n.getLength() > 0) {
            return n.item(0).getTextContent();
        } else {
            return null;
        }                    
    }
    
    private String[] getMameCommand(String... params) {
        String[] mycommand = new String[command.length + params.length];
        System.arraycopy(command, 0, mycommand, 0, command.length);   
        System.arraycopy(params, 0, mycommand, command.length, params.length);
        return mycommand;        
    }    
}
