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
    
    private final ImageView imageview;           
    private final FadeTransition imageanimation;   
    
    private Image image = null;
    private Image imageerror = null;
    
    private final ChangeListener<? super Number> progressListener;
    
    public ImageLoader() {
        
        imageview = new ImageView();    
        imageview.setPreserveRatio(true);        
        imageview.fitWidthProperty().bind(this.widthProperty()); 
        imageview.fitHeightProperty().bind(this.heightProperty()); 
        
        getChildren().add(imageview);
        
        imageanimation = new FadeTransition(Duration.millis(500), imageview);
        imageanimation.setFromValue(0.25);
        imageanimation.setToValue(1.0);   

        progressListener = (ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
            if (newValue.doubleValue() == 1.0) {
                imageview.setImage(image.isError() ? imageerror : image);
                imageanimation.playFromStart();
            }
        };
    }
    
    public void loadImage(Image img, Image imgerror) {
        loadImage(img, imgerror, null);
    }
    
    public void loadImage(Image img, Image imgerror, Image imgdefault) {
        
        if (image != null) {
            image.progressProperty().removeListener(progressListener);
        }
        imageanimation.stop();
        
        image = null;
        imageerror = null;
        
        // start
        if (img == null) {
            imageview.setImage(imgdefault);
        } else if (img.isError()) {
            imageview.setImage(imgerror);
            imageanimation.playFromStart();
        } else if (!img.isBackgroundLoading() || img.getProgress() == 1.0) {        
            imageview.setImage(img);
            imageanimation.playFromStart();
        } else {
            // Loading process...
            image = img;
            imageerror = imgerror;
            imageview.setImage(imgdefault);
            image.progressProperty().addListener(progressListener);
        }
    }   
}
