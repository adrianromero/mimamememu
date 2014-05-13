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

import com.adr.mimame.media.Clip;
import java.io.IOException;
import java.util.ResourceBundle;
import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.ParallelTransition;
import javafx.animation.ScaleTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Callback;
import javafx.util.Duration;

/**
 * FXML Controller class
 *
 * @author adrian
 */
public class DialogView extends StackPane {
    
    private final ShowAnimation carddialogshow;
    private final Clip carddialog_sound;
    
    @FXML private VBox carddialog_box;
    @FXML private Text carddialog_title;
    @FXML private Text carddialog_message;
    @FXML private HBox boxbuttons;
    @FXML private Button buttonok;
    @FXML private Button buttoncancel;
    
    private Callback<DialogView.Result, Void> callback;
    
    public static enum Result {
        OK, CANCEL;
    }

    public DialogView(Pane parent) {
        
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/dialog.fxml"), ResourceBundle.getBundle("properties/messages"));
        loader.setController(this);
        loader.setRoot(this);
        
        try {
            loader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
        
        this.setVisible(false);
        carddialogshow = new ShowAnimation(this, createCardDialogAnimation());        
        carddialog_sound = PlatformList.INSTANCE.createClip(this.getClass().getResource("/sounds/150221__killkhan__reload-2.mp3").toString());

        parent.getChildren().add(this);
    }
    
    public void showConfirm(String title, String message, Callback<DialogView.Result, Void> callback) {
        
        if (!carddialog_box.getChildren().contains(boxbuttons)) {
            carddialog_box.getChildren().add(boxbuttons);
        }        
        show(title, message, callback);
    }
    
    public void show(String title, String message) {
        
        if (carddialog_box.getChildren().contains(boxbuttons)) {
            carddialog_box.getChildren().remove(boxbuttons);
        }
        show(title, message, null);
    }
    
    public void show(String title, String message, Callback<DialogView.Result, Void> callback) {
        this.callback = callback;
        carddialog_title.setText(title);
        carddialog_message.setText(message);
        carddialog_sound.play();
        carddialogshow.setDisplayed(true);
    }
    
    @FXML
    void onDialogKeyPressed(KeyEvent event) {

        if (KeyCode.ENTER == event.getCode() || KeyCode.CONTROL == event.getCode()) {
            callbackResult(Result.OK);
            carddialogshow.setDisplayed(false);
        } else if (KeyCode.ESCAPE == event.getCode()) {
            callbackResult(Result.CANCEL);
            carddialogshow.setDisplayed(false);                      
        }
    } 
    
    @FXML
    void onActionOK(ActionEvent event) {
        callbackResult(Result.OK);
        carddialogshow.setDisplayed(false);
    }

    @FXML
    void onActionCancel(ActionEvent event) {
        callbackResult(Result.CANCEL);
        carddialogshow.setDisplayed(false);
    }
    
    private void callbackResult(Result result) {
        if (callback != null) {
            callback.call(result);
        }
    }
    
    private Animation createCardDialogAnimation() {
        // The cardwait show animation
        FadeTransition t = new FadeTransition(Duration.millis(100), this);
        t.setInterpolator(Interpolator.EASE_BOTH);
        t.setFromValue(0.0);
        t.setToValue(1.0);        
        ScaleTransition s = new ScaleTransition(Duration.millis(250), carddialog_box);
        s.setInterpolator(Interpolator.EASE_BOTH);
        s.setFromX(0.25);
        s.setFromY(0.25);
        s.setToX(1.0);
        s.setToY(1.0);
        FadeTransition s2 = new FadeTransition(Duration.millis(250), carddialog_box);
        s2.setInterpolator(Interpolator.EASE_BOTH);
        s2.setFromValue(0.0);
        s2.setToValue(1.0);
        return new ParallelTransition(t, s, s2);
    }       
}
