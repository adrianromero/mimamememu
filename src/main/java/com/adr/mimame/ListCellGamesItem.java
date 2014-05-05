//    Mimamememu is launcher for M.A.M.E and other emulators.
//    Copyright (C) 2014 Adrián Romero Corchado.
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
//    along with Mimamememu.  If not, see <http://www.gnu.org/licenses

package com.adr.mimame;

import javafx.scene.control.ListCell;

/**
 *
 * @author adrian
 */
public class ListCellGamesItem extends ListCell<GamesItem> {
    
    @Override
    public void updateItem(GamesItem item, boolean empty) {
        super.updateItem(item, empty);
        if (item == null) {
            setText(null);
        } else {
            setText(item.toString());
        } 
    }
}
