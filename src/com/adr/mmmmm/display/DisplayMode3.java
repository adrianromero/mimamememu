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

import com.adr.mmmmm.GamesItemInfo;
import javax.swing.ListCellRenderer;

/**
 *
 * @author adrian
 */
public class DisplayMode3 implements DisplayMode {

    @Override
    public ListCellRenderer getListRenderer() {
        return new RendererInfo();
    }

    @Override
    public int getListLayoutOrientation() {
        return javax.swing.JList.VERTICAL;
    }

    @Override
    public GamesItemInfo getGamesItemInfo() {
        return new InfoTitle();
    }
    
}
