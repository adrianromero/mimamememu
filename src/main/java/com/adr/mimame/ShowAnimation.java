//    MIMAMEMEMU is a launcher for M.A.M.E and other emulators.
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

import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.util.Duration;

/**
 *
 * @author adrian
 */
public class ShowAnimation {
 
    private final Node node;
    private final BooleanProperty displayed;
    private final Animation animation;
    
    public ShowAnimation(Node node, Animation animation) {
        this.node = node;
        this.animation = animation;
        this.animation.setOnFinished(this::animationFinished);
        
        this.displayed = new SimpleBooleanProperty(node.isVisible());
        this.displayed.addListener(this::changeDisplayed);                
    }
      
    private void animationFinished(ActionEvent event) {
        if (animation.getCurrentTime().equals(Duration.ZERO)) {
            node.setVisible(false);
        }
    }
    
    private void changeDisplayed(ObservableValue<? extends Boolean> ov, Boolean oldvalue, Boolean newvalue) {
        if (newvalue) {
            animation.setRate(1.0);
            if (animation.getStatus() != Animation.Status.RUNNING) {
                animation.setRate(1.0);
                node.setVisible(true);
                animation.playFromStart();
            }
        } else {
            animation.setRate(-1.0);
            if (animation.getStatus() != Animation.Status.RUNNING) {
                node.setVisible(true);
                animation.playFrom(animation.getCycleDuration());
            }            
        }      
    }
    
    public BooleanProperty displayedProperty() {
        return displayed;
    }
    
    public boolean isDisplayed() {
        return displayed.get();
    }
    
    public void setDisplayed(boolean value) {
        displayed.set(value);
    }
}
