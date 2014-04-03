/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.adr.mimame;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
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
        } else {
            title.setText(game.getTitle());
            platform.setText(game.getPlatform().getPlatformName());
        }
    }
    
}
