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
import java.util.ResourceBundle;
import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.animation.FillTransition;
import javafx.animation.Interpolator;
import javafx.animation.ParallelTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.Transition;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
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
public class LoadingView extends StackPane {

    @FXML private VBox cardwait_content;
    @FXML private Text cardwait_progress;
    @FXML private Circle cardwait_circle1;
    @FXML private Circle cardwait_circle2;
    @FXML private Circle cardwait_circle3;
    @FXML private Circle cardwait_circle4;
    @FXML private Circle cardwait_circle5;
    
    private final Animation circlesanimation;
    private final ShowAnimation cardwait_show;
    private final BooleanProperty displayed;     
    
    public LoadingView(Pane parent) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/loading.fxml"), ResourceBundle.getBundle("properties/messages"));
        loader.setController(this);
        loader.setRoot(this);
        
        try {
            loader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
        
        this.setVisible(false);
        displayed = new SimpleBooleanProperty(this.isVisible());
        displayed.addListener(this::changeDisplayed); 
        
        circlesanimation = createAllCirclesAnimation();
        cardwait_show = new ShowAnimation(this, createCardWaitAnimation());
        cardwait_show.displayedProperty().bind(displayed);

        parent.getChildren().add(this);
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
    
    public StringProperty messageProperty() {
        return cardwait_progress.textProperty();
    }
    
    public String getMessage() {
        return cardwait_progress.getText();
    }
    
    public void setMessage(String message) {
        cardwait_progress.setText(message);
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
        FadeTransition t = new FadeTransition(Duration.millis(100), this);
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
