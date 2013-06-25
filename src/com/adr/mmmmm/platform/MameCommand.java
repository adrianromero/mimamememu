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
}
