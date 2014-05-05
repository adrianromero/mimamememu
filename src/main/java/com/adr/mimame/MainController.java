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
import javafx.stage.Stage;

public class MainController implements Initializable {

    private final LoadGamesService loadgames = new LoadGamesService();
    
    @FXML private StackPane stack;
    @FXML private ListView<GamesItem> listgames;
    @FXML private AnchorPane cardlist;
  
    @FXML private GameView gameview; 
    private NoGamesView nogamesview;
    private DialogView dialogview;   
    private LoadingView loadingview;
    private SearchView searchview;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        nogamesview = new NoGamesView(stack);
        searchview = new SearchView(stack);
        dialogview = new DialogView(stack);
        loadingview = new LoadingView(stack);
        
        cardlist.disableProperty().bind(
                dialogview.visibleProperty()
                        .or(loadingview.visibleProperty())
                        .or(searchview.visibleProperty()));        
        
        // The games list
        listgames.setCellFactory((ListView<GamesItem> list) -> new ListCellGamesItem());       
        listgames.itemsProperty().bind(loadgames.valueProperty());              
        listgames.itemsProperty().addListener((ObservableValue<? extends ObservableList<GamesItem>> observable, ObservableList<GamesItem> oldValue, ObservableList<GamesItem> newValue) -> {
            if (newValue != null && newValue.size() > 0) {
                nogamesview.setVisible(false);
                listgames.getSelectionModel().select(0);
                listgames.requestFocus();
            } else {
                nogamesview.setVisible(true);
                gameview.showGameItem(null);
            }
        });
        listgames.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends GamesItem> observable, GamesItem oldValue, GamesItem newValue) -> {
            gameview.showGameItem(newValue);           
        });
        
        // The Loading controller       
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
    void onStackKeyPressed(KeyEvent event) {
        if (KeyCode.F11 == event.getCode()) {
            Stage s = ((Stage) listgames.getScene().getWindow());
            s.setFullScreen(!s.isFullScreen());
            event.consume();
        }  
    }
    
    @FXML
    void onListKeyPressed(KeyEvent event) {
        GamesItem item = listgames.getSelectionModel().getSelectedItem();
        if (item != null && (KeyCode.ENTER == event.getCode() || KeyCode.CONTROL == event.getCode())) {
            executeGame(item);
            event.consume(); 
        } else if (item != null && KeyCode.SPACE == event.getCode()) {
            searchview.show(item, (Integer selected) -> {
                int currentindex = listgames.getSelectionModel().getSelectedIndex();
                int i = currentindex;
                do {
                    i++;
                    if (i >= listgames.getItems().size()) {
                        i = 0;
                    }
                    if (selected == SearchView.getInitial(listgames.getItems().get(i))) {
                        listgames.getSelectionModel().select(i);
                        listgames.scrollTo(i);
                        return null;
                    }
                } while (currentindex != i);
                return null;
            });
            event.consume();  
        } else if (item != null && KeyCode.ALT == event.getCode()) {
            dialogview.show("Save as favorties", "favorite");
            event.consume();            
        } else if (KeyCode.F5 == event.getCode()) {
            loadGames(true);
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
