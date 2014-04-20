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

import javafx.animation.FadeTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

/**
 *
 * @author adrian
 */
public class ImageLoader extends StackPane {
    
    private final ImageView imagedefault;
    private final ImageView image;           
    private final FadeTransition imageanimation;   
    
    private final ChangeListener<? super Number> progressListener;
    
    public ImageLoader() {
        imagedefault = new ImageView();
        imagedefault.setVisible(true);
        imagedefault.setPreserveRatio(true);
        imagedefault.fitWidthProperty().bind(this.widthProperty()); 
        imagedefault.fitHeightProperty().bind(this.heightProperty()); 
        
        image = new ImageView();
        image.setVisible(false);        
        image.setPreserveRatio(true);        
        image.fitWidthProperty().bind(this.widthProperty()); 
        image.fitHeightProperty().bind(this.heightProperty()); 
        
        getChildren().addAll(imagedefault, image);
        
        imageanimation = new FadeTransition(Duration.millis(500), image);
        imageanimation.setFromValue(0.4);
        imageanimation.setToValue(1.0);   
//        imageanimation.setOnFinished((ActionEvent event) -> {
//            imagedefault.setImage(null);
//            imagedefault.setVisible(false);
//        });

        progressListener = (ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
            if (newValue.doubleValue() == 1.0 && !image.getImage().isError()) {
                imagedefault.setImage(null);
                imagedefault.setVisible(false);                
                image.setVisible(true);
                imageanimation.playFromStart();
            }
        };
    }
    
    public void loadImage(Image img) {
        loadImage(img, null, null);
    }
    
    public void loadImage(Image img, Image imgdefault, Image imgerror) {
        
        Image oldimage = image.getImage();
        if (oldimage != null) {
            oldimage.progressProperty().removeListener(progressListener);
        }
        imageanimation.stop();
        
        // start
        if (img == null || img.isError()) {
            imagedefault.setImage(imgdefault);
            imagedefault.setVisible(true);
            image.setImage(null);
            image.setVisible(false);
        } else if (!img.isBackgroundLoading() || img.getProgress() == 1.0) {
            imagedefault.setImage(null);
            imagedefault.setVisible(false);            
            image.setImage(img);
            image.setVisible(true);
            imageanimation.playFromStart();
        } else {
            imagedefault.setImage(imgdefault);
            imagedefault.setVisible(true);
            image.setImage(img);
            image.setVisible(false);
            img.progressProperty().addListener(progressListener);
        }
    }   
}
