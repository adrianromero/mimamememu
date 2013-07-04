//    MIMAMEMEMU is a launcher for M.A.M.E and other emulators.
//    Copyright (C) 2013 Adrián Romero Corchado.
//    https://github.com/adrianromero/mimamememu
//
//    This file is part of Mimamememu
//
//    MIMAMEMEMU is free software: you can redistribute it and/or modify
//    it under the terms of the GNU General Public License as published by
//    the Free Software Foundation, either version 3 of the License, or
//    (at your option) any later version.
//
//    MIMAMEMEMU is distributed in the hope that it will be useful,
//    but WITHOUT ANY WARRANTY; without even the implied warranty of
//    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//    GNU General Public License for more details.
//
//    You should have received a copy of the GNU General Public License
//    along with MIMAMEMEMU.  If not, see <http://www.gnu.org/licenses/>.

package com.adr.mmmmm.display;

import java.util.logging.Logger;

/**
 *
 * @author adrian
 */
public class DisplayModeList {
    
    private final static Logger logger = Logger.getLogger(DisplayModeList.class.getName()); 
    
    public final static DisplayModeList INSTANCE = new DisplayModeList();
      
    private DisplayMode[] displaymodes = {
        new com.adr.mmmmm.display.DisplayMode0(),
        new com.adr.mmmmm.display.DisplayMode1(),
        new com.adr.mmmmm.display.DisplayMode2(),
    };    
    
    private DisplayModeList () {
    }
    
    public DisplayMode getDisplayMode(int i) {
        if (i < 0 || i >= displaymodes.length) {
            return displaymodes[0]; // fallback display mode.
        } else {
            return displaymodes[i];
        }
    }
}
