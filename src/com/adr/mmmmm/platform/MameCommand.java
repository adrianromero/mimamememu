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

import com.adr.mmmmm.Platform;
import com.adr.mmmmm.GamesItem;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
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
    
    @Override
    public String getPlatformName() {
        return "MAME";
    }
    
    @Override
    public String getPlatformTitle() {
        return "M.A.M.E.";
    }

    @Override
    public String getCommand(GamesItem item) {
        return "mame " + item.getName();
    } 
    
    @Override
    public List<GamesItem> getGames() {
       
        ArrayList<GamesItem> games = new ArrayList<GamesItem>();
        
        try {
            
            // First read the names of available games
            ArrayList<String> names = new ArrayList<String>();            
            Process p = Runtime.getRuntime().exec("mame -verifyroms");
            BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line;
            while ((line = br.readLine()) != null) {
                String[] tokens = line.split(" ");
                if (tokens.length >= 4 && "romset".equals(tokens[0])) {
                    if ("is".equals(tokens[tokens.length - 2]) && "good".equals(tokens[tokens.length - 1])) {
                        names.add(tokens[1]);
                    } else if ("is".equals(tokens[tokens.length - 3]) && "best".equals(tokens[tokens.length - 2]) && "available".equals(tokens[tokens.length - 1])) {
                        names.add(tokens[1]);
                    }
                }
            }
            p.waitFor();            
            
            
            // Get detailed information for each game available
            for (String n: names) {
                p = Runtime.getRuntime().exec("mame -listxml " + n);

                DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                Document doc = dBuilder.parse(p.getInputStream());

                NodeList nodegames = doc.getElementsByTagName("game");
                for (int i = 0; i < nodegames.getLength(); i++) {
                    Element e = (Element) nodegames.item(i);
                    
                    System.out.println(e.getAttribute("name"));
                    
                    GamesItem item = new GamesItem(
                            e.getAttribute("name"),
                            getElementText(e, "description"),
                            this);
                    item.setManufacturer(getElementText(e, "manufacturer"));
                    item.setYear(getElementText(e, "year"));
                    try {
                        item.setSnap(ImageIO.read(new URL("http://www.mamedb.com/snap/" + item.getName() + ".png")));
                    } catch (Exception ex) {
                        item.setSnap(null);
                    }

                    games.add(item);
                }
                p.waitFor();
            }
            
        } catch (IOException ex) {
            Logger.getLogger(MameCommand.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(MameCommand.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(MameCommand.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SAXException ex) {
            Logger.getLogger(MameCommand.class.getName()).log(Level.SEVERE, null, ex);
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
}
