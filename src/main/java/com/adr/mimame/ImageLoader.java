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

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

/**
 *
 * @author adrian
 */
public class ImageLoader extends StackPane {
    
    private final ImageView imagedefault;
    private final ImageView image;
    
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
        
        progressListener = (ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
            if (newValue.doubleValue() == 1.0 && !image.getImage().isError()) {
                imagedefault.setVisible(false);
                image.setVisible(true);
            }
        };
    }
    
    public void loadImage(Image img) {
        loadImage(img, null);
    }
    
    public void loadImage(Image img, Image imgdefault) {
        
        Image oldimage = image.getImage();
        if (oldimage != null) {
            oldimage.progressProperty().removeListener(progressListener);
        }
        
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
        } else {
            imagedefault.setImage(imgdefault);
            imagedefault.setVisible(true);
            image.setImage(img);
            image.setVisible(false);
            img.progressProperty().addListener(progressListener);
        }
    }   
}
