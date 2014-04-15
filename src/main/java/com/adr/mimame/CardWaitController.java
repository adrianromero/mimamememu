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

import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.animation.FillTransition;
import javafx.animation.Interpolator;
import javafx.animation.ParallelTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.Transition;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;
import javafx.util.Duration;

/**
 *
 * @author adrian
 */
public class CardWaitController {
    
    private final StackPane cardwait;
    private final VBox cardwait_content;
    private final Text cardwait_progress;
    private final Circle cardwait_circle1;
    private final Circle cardwait_circle2;
    private final Circle cardwait_circle3;
    private final Circle cardwait_circle4;
    private final Circle cardwait_circle5;
    
    private final Animation circlesanimation;
    private final ShowAnimation cardwait_show;
    private final BooleanProperty displayed;    
    
    public CardWaitController(StackPane cardwait,
                    VBox cardwait_content,
                    Text cardwait_progress,
                    Circle cardwait_circle1,
                    Circle cardwait_circle2,
                    Circle cardwait_circle3,
                    Circle cardwait_circle4,
                    Circle cardwait_circle5) {
        this.cardwait = cardwait;
        this.cardwait_content = cardwait_content;
        this.cardwait_progress = cardwait_progress;
        this.cardwait_circle1 = cardwait_circle1;
        this.cardwait_circle2 = cardwait_circle2;
        this.cardwait_circle3 = cardwait_circle3;
        this.cardwait_circle4 = cardwait_circle4;
        this.cardwait_circle5 = cardwait_circle5;
        
        displayed = new SimpleBooleanProperty(cardwait.isVisible());
        displayed.addListener(this::changeDisplayed); 
        
        circlesanimation = createAllCirclesAnimation();
        cardwait_show = new ShowAnimation(cardwait, createCardWaitAnimation());
        cardwait_show.displayedProperty().bind(displayed);
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
    
    private void changeDisplayed(ObservableValue<? extends Boolean> ov, Boolean oldvalue, Boolean newvalue) {
        if (newvalue) {
            circlesanimation.play();
        } else {
            circlesanimation.pause();
        }
    }    
    
    private Animation createCardWaitAnimation() {
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
        return new ParallelTransition(t, s, s2);
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
