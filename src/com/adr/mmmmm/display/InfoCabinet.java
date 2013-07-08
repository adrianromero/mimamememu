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

import com.adr.mmmmm.GamesItem;
import com.adr.mmmmm.GamesItemInfo;
import com.adr.mmmmm.JPanelIcon;
import java.awt.Component;

/**
 *
 * @author adrian
 */
public class InfoCabinet implements GamesItemInfo {
    
    private JPanelIcon jicon = new JPanelIcon(480, 640);

    @Override
    public Component getComponent() {
        return jicon;
    }

    @Override
    public void renderGamesItem(GamesItem item) {
        if (item == null) {
            jicon.setImage(null);
        } else {
            jicon.setImage(item.getCabinets());
        }
    }   
}