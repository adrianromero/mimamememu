//    MIMAMEMEMU is a launcher for M.A.M.E and other emulators.
//    Copyright (C) 2014 Adrián Romero Corchado.
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

package com.adr.mimame;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.concurrent.Worker.State;
import javafx.concurrent.WorkerStateEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;

public class MainController implements Initializable {

    private final LoadGamesService loadgames = new LoadGamesService();
    
    @FXML
    private ListView<GamesItem> listgames;
    @FXML
    private AnchorPane cardlist;
    @FXML
    private StackPane cardwait;
    @FXML
    private StackPane carddialog;     
    @FXML
    private Label dialogtitle;  
    @FXML
    private Label dialogbody;    
    @FXML
    private GameView gameview;    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        // The launcher
        cardlist.disableProperty().bind(carddialog.visibleProperty());
                
        // carddialog
        carddialog.setVisible(false);       
        
        // The games list
        listgames.itemsProperty().bind(loadgames.valueProperty());              
        listgames.itemsProperty().addListener((ObservableValue<? extends ObservableList<GamesItem>> observable, ObservableList<GamesItem> oldValue, ObservableList<GamesItem> newValue) -> {
            if (newValue != null && newValue.size() > 0) {
                listgames.getSelectionModel().select(0);
                listgames.requestFocus();
            } else {
                gameview.showGameItem(null);
            }
        });
        listgames.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends GamesItem> observable, GamesItem oldValue, GamesItem newValue) -> {
            gameview.showGameItem(newValue);           
        });
        
        cardwait.visibleProperty().bind(loadgames.runningProperty());
        
        loadGames(false);
    }  
    
    private void executeGame(GamesItem item) {
        GamesActionTask task = new GamesActionTask(item);
        
        // Disable or hide UI
        
        task.setOnFailed((WorkerStateEvent event) -> {
            // Show UI
            GamesActionException ex = (GamesActionException) task.getException();
            showDialog(ex.getTitle(), ex.getMessage());
        });
        
        task.setOnSucceeded((WorkerStateEvent event) -> {
            // Show UI
        });
         
        new Thread(task).start();
    }
    
    @FXML
    void onListKeyPressed(KeyEvent event) {
        GamesItem item = listgames.getSelectionModel().getSelectedItem();
        if (item != null && KeyCode.ENTER == event.getCode()) {
            executeGame(item);
            event.consume(); 
        } else if(KeyCode.F5 == event.getCode()) {
            loadGames(true);
        }    
    }

    @FXML
    void onListMouseClicked(MouseEvent event) {
        GamesItem item = listgames.getSelectionModel().getSelectedItem();
        if (item != null && event.getClickCount() == 2 ) {
            executeGame(item);
            event.consume();
        }        
    }    
    
    @FXML
    void onDialogKeyPressed(KeyEvent event) {
        // needs to be focus traversable to receive events...
        if (KeyCode.ENTER == event.getCode() || KeyCode.ESCAPE == event.getCode()) {
            carddialog.setVisible(false);
            event.consume();
        }
    } 
    
    @FXML
    void onDialogClicked(MouseEvent event) {
        carddialog.setVisible(false);
        event.consume();
    }
    
    private void loadGames(final boolean refresh) {
        
        loadgames.setRefresh(refresh);
        if (loadgames.getState() == State.READY) {
            loadgames.start();
        } else {
            loadgames.restart();
        }
    } 
    
    private void showDialog(String title, String body) {
        dialogtitle.setText(title);
        dialogbody.setText(body);
        carddialog.setVisible(true);
    } 
    
    private int parseInt(String value) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException ex) {
            return 0;
        }
    }    
}
