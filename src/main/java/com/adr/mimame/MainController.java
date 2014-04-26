//    Mimamememu is launcher for M.A.M.E and other emulators.
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
import java.util.List;
import java.util.ResourceBundle;
import javafx.animation.Animation;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.concurrent.Worker.State;
import javafx.concurrent.WorkerStateEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class MainController implements Initializable {

    private final LoadGamesService loadgames = new LoadGamesService();
    
    @FXML private StackPane stack;
    @FXML private ListView<GamesItem> listgames;
    @FXML private AnchorPane cardlist;
  
    @FXML private GameView gameview;    
    
    private DialogView dialogview;
    
    private LoadingView loadingview;

    
    @FXML private StackPane nogames;
    @FXML private Text nogames_title;
    @FXML private Text nogames_message;
    private Animation nogames_show;   
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
 
        dialogview = new DialogView(stack);
        cardlist.disableProperty().bind(dialogview.visibleProperty());        
        
        // The games list
        listgames.setCellFactory((ListView<GamesItem> list) -> new ListCellGamesItem());       
        listgames.itemsProperty().bind(loadgames.valueProperty());              
        listgames.itemsProperty().addListener((ObservableValue<? extends ObservableList<GamesItem>> observable, ObservableList<GamesItem> oldValue, ObservableList<GamesItem> newValue) -> {
            if (newValue != null && newValue.size() > 0) {
                nogames.setVisible(false);
                listgames.getSelectionModel().select(0);
                listgames.requestFocus();
            } else {
                nogames.setVisible(true);
                gameview.showGameItem(null);
            }
        });
        listgames.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends GamesItem> observable, GamesItem oldValue, GamesItem newValue) -> {
            gameview.showGameItem(newValue);           
        });
        
        // The nogames
       
        // The Loading controller
        loadingview = new LoadingView(stack);
        loadingview.displayedProperty().bind(loadgames.runningProperty());
        loadingview.messageProperty().bind(loadgames.messageProperty());
        
        loadgames.errorsProperty().addListener((ObservableValue<? extends List<PlatformException>> observable, List<PlatformException> oldValue, List<PlatformException> newValue) -> {
            if (newValue != null &&  newValue.size() > 0) {
                dialogview.show(
                        ResourceBundle.getBundle("properties/messages").getString("msg.loadingerror_title"), 
                        ResourceBundle.getBundle("properties/messages").getString("msg.loadingerror_body"));                
            }
        });
      
        loadGames(false);
    }  
    
    private void executeGame(GamesItem item) {
        GamesActionTask task = new GamesActionTask(item);
        
        // Disable or hide UI
        
        task.setOnFailed((WorkerStateEvent event) -> {
            // Show UI
            GamesActionException ex = (GamesActionException) task.getException();
            dialogview.show(ex.getTitle(), ex.getMessage());
        });
        
        task.setOnSucceeded((WorkerStateEvent event) -> {
            // Show UI
        });
         
        new Thread(task).start();
    }
    
    @FXML
    void onListKeyPressed(KeyEvent event) {
        GamesItem item = listgames.getSelectionModel().getSelectedItem();
        if (item != null && (KeyCode.ENTER == event.getCode() || KeyCode.CONTROL == event.getCode())) {
            executeGame(item);
            event.consume(); 
        } else if (KeyCode.F5 == event.getCode()) {
            loadGames(true);
            event.consume();
        } else if (KeyCode.F11 == event.getCode()) {
            Stage s = ((Stage) listgames.getScene().getWindow());
            s.setFullScreen(!s.isFullScreen());
            event.consume();
        } else if (KeyCode.ALT == event.getCode()) {
            dialogview.show("Save as favorties", "favorite");
            event.consume();
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
    
    private void loadGames(final boolean refresh) {
        
        loadgames.setRefresh(refresh);
        if (loadgames.getState() == State.READY) {
            loadgames.start();
        } else {
            loadgames.restart();
        }
    } 
}
