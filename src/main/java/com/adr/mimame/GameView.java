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
//    along with Mimamememu.  If not, see <http://www.gnu.org/licenses/>.

package com.adr.mimame;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

/**
 *
 * @author adrian
 */
public class GameView extends AnchorPane {
    
    @FXML
    Text title;
    @FXML
    Text platform;
    @FXML
    ImageView titlesimage;

    public GameView() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/gameview.fxml"));
        loader.setController(this);
        loader.setRoot(this);
        
        try {
            loader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }        
    
    }
    
    public void showGameItem(GamesItem game) {
        if (game == null) {
            title.setText(null);
            platform.setText(null);     
            titlesimage.setImage(null);
        } else {
            title.setText(game.getTitle());
            platform.setText(game.getPlatform().getPlatformName());
            titlesimage.setImage(game.getTitles());
        }
    }
    
}
