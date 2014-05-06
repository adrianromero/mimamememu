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
import javafx.animation.Transition;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.util.Duration;

/**
 *
 * @author adrian
 */
public class GameView extends AnchorPane {
    
    @FXML Text title;
    @FXML Text title1;
    @FXML Text manufacturer;
    @FXML Text year;
    @FXML Text platform;
    @FXML ImageLoader titlesimage;
    
    private final Animation titleanimation;
    private final Animation title1animation;
    private final Animation manufactureranimation;
    private final Animation yearanimation;
    private final Animation platformanimation;
    
    private final Clip game_sound;
    private final Clip empty_sound; 
    
    public GameView() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/gameview.fxml"));
        loader.setController(this);
        loader.setRoot(this);
        
        try {
            loader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }      

        titleanimation = getEnterTransition(title);  
        title1animation = getEnterTransition(title1);
        manufactureranimation = getEnterTransition(manufacturer);    
        manufactureranimation.setDelay(Duration.millis(200));
        yearanimation = getEnterTransition(year);        
        yearanimation.setDelay(Duration.millis(400));
        platformanimation = getEnterTransition(platform);      
        platformanimation.setDelay(Duration.millis(600));        
        
        game_sound = MediaFactory.createClip(this.getClass().getResource("/sounds/150216__killkhan__menu-move-1.mp3").toString());
        empty_sound = MediaFactory.createClip(this.getClass().getResource("/sounds/150215__killkhan__reload-5.mp3").toString());
        
        // Initially blank
        showGameItem(null);
    }
    
    private Transition getEnterTransition(Node node) {
        
        Interpolator i = Interpolator.SPLINE(0.25, 0.1, 0.25, 1);
        FadeTransition f = new FadeTransition(Duration.millis(1000), node);
        f.setFromValue(0.0);
        f.setToValue(1.0);
        f.setInterpolator(i);
        
        TranslateTransition t = new TranslateTransition(Duration.millis(1000), node);
        t.setFromX(500.0);
        t.setToX(0.0);
        t.setInterpolator(i);
        
        return new ParallelTransition(f, t);
    }
    
    public final void showGameItem(GamesItem game) {
        if (game == null) {
            title.setText(null);
            title1.setText(null);
            manufacturer.setText(null);
            year.setText(null);
            platform.setText(null);     
            titlesimage.loadImage(null, null);
            
            empty_sound.play();
        } else {
            title.setText(game.getTitle1());
            title1.setText(game.getTitle2());
            manufacturer.setText(game.getManufacturer());
            year.setText(game.getYear());
            platform.setText(game.getPlatform().getPlatformName());
            titlesimage.loadImage(PlatformList.INSTANCE.getCachedImage(game.getTitles()), game.getPlatform().getDefaultImage());
            
            titleanimation.playFromStart();
            title1animation.playFromStart();
            manufactureranimation.playFromStart();
            yearanimation.playFromStart();
            platformanimation.playFromStart();
                       
            game_sound.play();
        }
    }    
}
