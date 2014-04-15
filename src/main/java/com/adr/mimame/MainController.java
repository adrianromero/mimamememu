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
import java.util.ResourceBundle;
import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.animation.FillTransition;
import javafx.animation.Interpolator;
import javafx.animation.ParallelTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.Transition;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.concurrent.Worker.State;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

public class MainController implements Initializable {

    private final LoadGamesService loadgames = new LoadGamesService();
    
    @FXML
    private ListView<GamesItem> listgames;
    @FXML
    private AnchorPane cardlist;

    @FXML
    private StackPane carddialog;     
    @FXML
    private Label dialogtitle;  
    @FXML
    private Label dialogbody;    
    @FXML
    private GameView gameview;    
    
    @FXML private StackPane cardwait;
    @FXML private VBox cardwait_content;
    @FXML private Text cardwait_progress;
    @FXML private Circle cardwait_circle1;
    @FXML private Circle cardwait_circle2;
    @FXML private Circle cardwait_circle3;
    @FXML private Circle cardwait_circle4;
    @FXML private Circle cardwait_circle5;
    private Animation circlesanimation;
    
    
    @FXML private StackPane nogames;
    private Animation nogames_show;
    @FXML private Text nogames_title;
    @FXML private Text nogames_message;

    private ShowAnimation cardwait_show;
    
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
        
        
        // The cardwait show animation
        FadeTransition t = new FadeTransition(Duration.millis(100), cardwait);
        t.setInterpolator(Interpolator.EASE_BOTH);
        t.setFromValue(0.0);
        t.setToValue(1.0);        
        ScaleTransition s = new ScaleTransition(Duration.millis(250), cardwait_content);
        s.setInterpolator(Interpolator.EASE_BOTH);
        s.setFromX(0.25);
        s.setFromY(0.25);
        s.setToX(1.0);
        s.setToY(1.0);
        FadeTransition s2 = new FadeTransition(Duration.millis(250), cardwait_content);
        s2.setInterpolator(Interpolator.EASE_BOTH);
        s2.setFromValue(0.0);
        s2.setToValue(1.0);
        cardwait_show = new ShowAnimation(cardwait, new ParallelTransition(t, s, s2));
        cardwait_show.displayedProperty().bind(loadgames.runningProperty());
        cardwait_progress.textProperty().bind(loadgames.messageProperty());
        circlesanimation = createAllCirclesAnimation();
        circlesanimation.play();  // TODO: Play this animation only when loading visible
      
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
        } else if (KeyCode.F5 == event.getCode()) {
            loadGames(true);
        } else if (KeyCode.F11 == event.getCode()) {
            Stage s = ((Stage) listgames.getScene().getWindow());
            s.setFullScreen(!s.isFullScreen());
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
    
    private Animation createAllCirclesAnimation() {
        
        return new ParallelTransition(
                createCircleAnimation(cardwait_circle1, Duration.ZERO),
                createCircleAnimation(cardwait_circle2, Duration.millis(250)),
                createCircleAnimation(cardwait_circle3, Duration.millis(500)),
                createCircleAnimation(cardwait_circle4, Duration.millis(750)),
                createCircleAnimation(cardwait_circle5, Duration.millis(1000)));
    }
    
    private Animation createCircleAnimation(Shape s, Duration delay) {
        Transition a = new FillTransition(Duration.millis(1250), s, Color.GRAY, Color.BLUE);
        a.setAutoReverse(true);
        a.setCycleCount(Animation.INDEFINITE);
        a.setDelay(delay);
        return a;
    }
}
