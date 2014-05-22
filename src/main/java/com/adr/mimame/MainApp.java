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

import java.util.ResourceBundle;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCombination;
import javafx.scene.text.Font;
import javafx.stage.Screen;
import javafx.stage.Stage;


public class MainApp extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
        
        // Initialize fonts
        Font.loadFont(getClass().getResourceAsStream("/fonts/RussoOne-Regular.ttf"), 12.0);
        Font.loadFont(getClass().getResourceAsStream("/fonts/Audiowide-Regular.ttf"), 12.0);
        Font.loadFont(getClass().getResourceAsStream("/fonts/PressStart2P-Regular.ttf"), 12.0);

        // Initialize PlatformList
        PlatformList.INSTANCE.init();
        
        // Load Root.
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/main.fxml"), ResourceBundle.getBundle("properties/messages"));
        loader.load();
        Parent root = loader.getRoot();
        ((MainController) loader.getController()).installStage(stage);
        
        Scene scene = new Scene(root);
        scene.getStylesheets().add("/styles/main.css");
        
        if (isARMDevice()) {
            // Prepare stage to run in frambuffer
            Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();

            //set Stage boundaries to visible bounds of the main screen
            stage.setX(primaryScreenBounds.getMinX());
            stage.setY(primaryScreenBounds.getMinY());
            stage.setWidth(primaryScreenBounds.getWidth());
            stage.setHeight(primaryScreenBounds.getHeight());
        } else {
            // Prepare stage to run in window system
            stage.setTitle("MIMAMEMEMU");
            stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
            stage.setFullScreen(PlatformList.INSTANCE.isFullScreen());
            
            PlatformList.INSTANCE.fullScreenProperty().bind(stage.fullScreenProperty());
        }    

        stage.setScene(scene);
        stage.getIcons().add(new Image("/images/mimamememu.png"));
        stage.show();
    }
    
    @Override
    public void stop() throws java.lang.Exception {
        PlatformList.INSTANCE.shutdown();   
    }

    public static boolean isARMDevice() {
        return System.getProperty("os.arch").toUpperCase().contains("ARM");
    }
    
    /**
     * The main() method is ignored in correctly deployed JavaFX application.
     * main() serves only as fallback in case the application can not be
     * launched through deployment artifacts, e.g., in IDEs with limited FX
     * support. NetBeans ignores main().
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
