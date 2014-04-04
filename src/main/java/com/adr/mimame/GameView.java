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
import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.ParallelTransition;
import javafx.animation.Transition;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.AudioClip;
import javafx.scene.text.Text;
import javafx.util.Duration;

/**
 *
 * @author adrian
 */
public class GameView extends AnchorPane {
    
    @FXML
    Text title;
    @FXML
    Text title1;
    @FXML
    Text manufacturer;
    @FXML
    Text year;
    @FXML
    Text platform;
    @FXML
    ImageView titlesimage;
    
    private final Animation titleanimation;
    private final Animation title1animation;
    private final Animation manufactureranimation;
    private final Animation yearanimation;
    private final Animation platformanimation;
    private final FadeTransition titlesimageanimation;
    
    private final AudioClip menu;

    public GameView() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/gameview.fxml"));
        loader.setController(this);
        loader.setRoot(this);
        
        try {
            loader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }      
        
        titlesimage.fitWidthProperty().bind(this.widthProperty()); 
        titlesimage.fitHeightProperty().bind(this.heightProperty()); 

        titleanimation = getEnterTransition(title);  
        title1animation = getEnterTransition(title1);
        manufactureranimation = getEnterTransition(manufacturer);    
        manufactureranimation.setDelay(Duration.millis(200));
        yearanimation = getEnterTransition(year);        
        yearanimation.setDelay(Duration.millis(400));
        platformanimation = getEnterTransition(platform);      
        platformanimation.setDelay(Duration.millis(600));        
        
        titlesimageanimation = new FadeTransition(Duration.millis(500), titlesimage);
        titlesimageanimation.setFromValue(0.4);
        titlesimageanimation.setToValue(1.0);   
        
        menu = new AudioClip(this.getClass().getResource("/sounds/150216__killkhan__menu-move-1.mp3").toString());   
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
    
    public void showGameItem(GamesItem game) {
        if (game == null) {
            title.setText(null);
            title1.setText(null);
            manufacturer.setText(null);
            year.setText(null);
            platform.setText(null);     
            titlesimage.setImage(null);
        } else {
            title.setText(game.getTitle1());
            title1.setText(game.getTitle2());
            manufacturer.setText(game.getManufacturer());
            year.setText(game.getYear());
            platform.setText(game.getPlatform().getPlatformName());
            titlesimage.setImage(game.getTitles());
            
            titleanimation.playFromStart();
            title1animation.playFromStart();
            manufactureranimation.playFromStart();
            yearanimation.playFromStart();
            platformanimation.playFromStart();
            titlesimageanimation.playFromStart();  
                       
            menu.play();
        }
    }    
}
