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
import com.adr.mimame.media.MediaFactory;
import java.io.IOException;
import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.util.Callback;
import javafx.util.Duration;

/**
 * FXML Controller class
 *
 * @author adrian
 */
public class SearchView extends StackPane {
    
    private final ShowAnimation searchshow;
    
    @FXML private Text search_character;

    private final String[] letters = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "0-9"};
    private int currentindex = 0;
    private Callback<Integer, Void> callback;
    
    private final Animation rightanimation;
    private final Animation leftanimation;
    
    private final Clip game_sound;
    
    public SearchView(Pane parent) {
        
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/search.fxml"));
        loader.setController(this);
        loader.setRoot(this);
        
        try {
            loader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
        
        this.setVisible(false);
        searchshow = new ShowAnimation(this, createSearchAnimation());        
        
        rightanimation = rightAnimation();
        leftanimation = leftAnimation();
        
        game_sound = MediaFactory.createClip(this.getClass().getResource("/sounds/150216__killkhan__menu-move-1.mp3").toString());

        parent.getChildren().add(this);
    }
    
    public static int getInitial(GamesItem game) {
         char initial = Character.toUpperCase(game.getTitle().charAt(0));
        if (initial >= 'A' && initial <= 'Z') {
            return initial - 'A';
        } else {
            return 26;
        }      
    }
    
    public void show(GamesItem game, Callback<Integer, Void> callback) {

        currentindex = getInitial(game);
        search_character.setText(letters[currentindex]);
        game_sound.play();
                    
        this.callback = callback;
        searchshow.setDisplayed(true);
    }
    
    @FXML
    void onSearchKeyPressed(KeyEvent event) {
        
        if (KeyCode.RIGHT == event.getCode() || KeyCode.DOWN == event.getCode()) {
            currentindex  = (currentindex + 1) % letters.length; 
            game_sound.play();
            leftanimation.play();
        } else if (KeyCode.LEFT == event.getCode() || KeyCode.UP == event.getCode()) {
            currentindex  = (currentindex + letters.length - 1) % letters.length; 
            game_sound.play();
            rightanimation.play();
        } else if (KeyCode.ENTER == event.getCode() || KeyCode.CONTROL == event.getCode()) {
            callback.call(currentindex);
            searchshow.setDisplayed(false);
            event.consume();
        } else if (KeyCode.ESCAPE == event.getCode()) {
            searchshow.setDisplayed(false);
            event.consume();
        }
        
    }   
    
    private Animation createSearchAnimation() {
        // The cardwait show animation
        FadeTransition t = new FadeTransition(Duration.millis(100), this);
        t.setInterpolator(Interpolator.EASE_BOTH);
        t.setFromValue(0.0);
        t.setToValue(1.0);        
        return t;
    }      
    
    private Animation rightAnimation() {
        
        Animation exit = exitRightAnimation();
        Animation entrance = entranceRightAnimation();
        exit.setOnFinished((ActionEvent event) -> {
            search_character.setText(letters[currentindex]);
            entrance.play();
        });
        return exit;
    }
    
    private Animation exitRightAnimation() {
        FadeTransition t = new FadeTransition(Duration.millis(150), search_character);
        t.setInterpolator(Interpolator.EASE_OUT);
        t.setFromValue(1.0);
        t.setToValue(0.0);
        TranslateTransition tt = new TranslateTransition(Duration.millis(150), search_character);
        tt.setInterpolator(Interpolator.EASE_OUT);
        tt.setFromX(0.0);
        tt.setToX(300.0);        
        return new ParallelTransition(t, tt);
    }
    
    private Animation entranceRightAnimation() {
        FadeTransition t = new FadeTransition(Duration.millis(150), search_character);
        t.setInterpolator(Interpolator.EASE_IN);
        t.setFromValue(0.0);
        t.setToValue(1.0);
        TranslateTransition tt = new TranslateTransition(Duration.millis(150), search_character);
        tt.setInterpolator(Interpolator.EASE_IN);
        tt.setFromX(-300.0);
        tt.setToX(0.0);        
        return new ParallelTransition(t, tt);
    }    
    
    
    private Animation leftAnimation() {
        
        Animation exit = exitLeftAnimation();
        Animation entrance = entranceLeftAnimation();
        exit.setOnFinished((ActionEvent event) -> {
            search_character.setText(letters[currentindex]);
            entrance.play();
        });
        return exit;
    }
    
    private Animation exitLeftAnimation() {
        FadeTransition t = new FadeTransition(Duration.millis(150), search_character);
        t.setInterpolator(Interpolator.EASE_OUT);
        t.setFromValue(1.0);
        t.setToValue(0.0);
        TranslateTransition tt = new TranslateTransition(Duration.millis(150), search_character);
        tt.setInterpolator(Interpolator.EASE_OUT);
        tt.setFromX(0.0);
        tt.setToX(-300.0);        
        return new ParallelTransition(t, tt);
    }
    
    private Animation entranceLeftAnimation() {
        FadeTransition t = new FadeTransition(Duration.millis(150), search_character);
        t.setInterpolator(Interpolator.EASE_IN);
        t.setFromValue(0.0);
        t.setToValue(1.0);
        TranslateTransition tt = new TranslateTransition(Duration.millis(150), search_character);
        tt.setInterpolator(Interpolator.EASE_IN);
        tt.setFromX(300.0);
        tt.setToX(0.0);        
        return new ParallelTransition(t, tt);
    }        
}
