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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
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
        
        try {
            defimage = new Image(getClass().getResourceAsStream("/com/adr/mimame/platform/mame.png"));
        } catch (IllegalArgumentException ex) {
            logger.log(Level.WARNING, null, ex);
            defimage = null;
        }    
        try {
            defcabinet = new Image(getClass().getResourceAsStream("/com/adr/mimame/platform/cabinet.png"));
        } catch (IllegalArgumentException ex) {
            logger.log(Level.WARNING, null, ex);
            defcabinet = null;
        }        
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
            // TODO: translate update messages
            progress.updateMessage("Verifying MAME roms");
            logger.log(Level.INFO, "Verifying MAME roms.");
            ArrayList<String> names = new ArrayList<String>();    
            
            Process p = Runtime.getRuntime().exec(getMameCommand("-verifyroms"));
            BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line;
            while ((line = br.readLine()) != null) {
                logger.log(Level.FINE, ">> {0}", line);
                String[] tokens = line.split(" ");
                if (tokens.length >= 4 && "romset".equals(tokens[0])) {
                    if ("is".equals(tokens[tokens.length - 2]) && "good".equals(tokens[tokens.length - 1])) {
                        logger.log(Level.FINE, "Adding >> {0}", tokens[1]);
                        names.add(tokens[1]);
                    } else if ("is".equals(tokens[tokens.length - 3]) && "best".equals(tokens[tokens.length - 2]) && "available".equals(tokens[tokens.length - 1])) {
                        logger.log(Level.FINE, "Adding >> {0}", tokens[1]);
                        names.add(tokens[1]);
                    }                    
                }
            }
            p.waitFor();            
            
            // Get detailed information for each game available
            ExecutorService exec = Executors.newFixedThreadPool(15);
            
            for (String n: names) {
                logger.log(Level.INFO, "Processsing details for game: {0}", n);
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

                            // Load image
                            exec.submit(Executors.callable(() -> {
                                try {
                                    item.setTitles(new Image("http://www.mamedb.com/titles/" + item.getName() + ".png"));
                                } catch (IllegalArgumentException ex) {
                                    item.setTitles(null);
                                }
                            }));
                            exec.submit(Executors.callable(() -> {
                                try {
                                    item.setCabinets(new Image("http://www.mamedb.com/cabinets/" + item.getName() + ".png"));
                                } catch (IllegalArgumentException ex) {
                                    item.setTitles(null);
                                }                        
                            }));
        // For the moment these images are not used in any display mode, so no need to waste traffic to mamedb                    
        //                    exec.submit(Executors.callable(new Runnable() { @Override public void run() {
        //                        try {
        //                            item.setSnap(ImageIO.read(new URL("http://www.mamedb.com/snap/" + item.getName() + ".png")));
        //                        } catch (Exception ex) {
        //                            item.setSnap(null);
        //                        }                        
        //                    }}));
        //                    exec.submit(Executors.callable(new Runnable() { @Override public void run() {
        //                        try {
        //                            item.setMarquees(ImageIO.read(new URL("http://www.mamedb.com/marquees/" + item.getName() + ".png")));
        //                        } catch (Exception ex) {
        //                            item.setMarquees(null);
        //                        }                        
        //                    }}));       
                            progress.updateMessage("Adding game " + item.getName());
                            logger.log(Level.INFO, "Adding game item {0}", item.getName());
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
            progress.updateMessage("Loading images");
            logger.log(Level.INFO, "Waiting for images.");
            shutdownAndAwaitTermination(exec);
            logger.log(Level.INFO, "Games list built.");
            progress.updateMessage("");
                     
        } catch (IOException | InterruptedException | ParserConfigurationException ex) {
            logger.log(Level.SEVERE, null, ex);
            throw new PlatformException(ex);
        }        
        return games;
    }
    
    
    private void shutdownAndAwaitTermination(ExecutorService pool) {
      pool.shutdown(); // Disable new tasks from being submitted
      try {
        // Wait a while for existing tasks to terminate
        if (!pool.awaitTermination(2, TimeUnit.MINUTES)) {
          pool.shutdownNow(); // Cancel currently executing tasks
          // Wait a while for tasks to respond to being cancelled
          if (!pool.awaitTermination(2, TimeUnit.MINUTES)) {
              System.err.println("Pool did not terminate");
          }
        }

      } catch (InterruptedException ie) {
        // (Re-)Cancel if current thread also interrupted
        pool.shutdownNow();
        // Preserve interrupt status
        Thread.currentThread().interrupt();
      }
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
