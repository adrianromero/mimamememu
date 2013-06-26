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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author adrian
 */
public class PlatformList {
    
    
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
            l.addAll(p.getGames());
        }
        
        Collections.sort(l);
        return l;
    }
     
    private Platform findPlatform(String platformname) {
        
        for (Platform gc: platforms) {
            if (gc.getPlatformName().equals(platformname)) {
                return gc;
            }
        }
        return new PlatformUnsupported(platformname);
    }      
}
