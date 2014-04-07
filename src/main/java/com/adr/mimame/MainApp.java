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

import java.awt.Font;
import java.awt.FontFormatException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.ResourceBundle;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;


public class MainApp extends Application {
    
    public static Font FONT_NAME = Font.getFont("Dialog");
    public static Font FONT_TITLE = Font.getFont("Dialog");
    public static Font FONT_ARCADE = Font.getFont("Dialog");
    
    @Override
    public void start(Stage stage) throws Exception {

        try {
            FONT_TITLE = Font.createFont(Font.TRUETYPE_FONT, getClass().getResourceAsStream("/com/adr/mimame/res/RussoOne-Regular.ttf"))
                    .deriveFont(16.0f);    
            FONT_NAME = Font.createFont(Font.TRUETYPE_FONT, getClass().getResourceAsStream("/com/adr/mimame/res/Audiowide-Regular.ttf"))
                    .deriveFont(32.0f);
            FONT_ARCADE = Font.createFont(Font.TRUETYPE_FONT, getClass().getResourceAsStream("/com/adr/mimame/res/PressStart2P-Regular.ttf"))
                    .deriveFont(16.0f);      
        } catch (FontFormatException | IOException ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        }  
        
        // Initialize PlatformList
        PlatformList.INSTANCE.init();
        
        // new FrmMain().start(args);
        
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/main.fxml"), ResourceBundle.getBundle("com/adr/mimame/res/messages"));
        
        Scene scene = new Scene(root);
        scene.getStylesheets().add("/styles/main.css");
        
        stage.setTitle("MIMAMEMEMU");
        stage.setFullScreen("fullscreen".equals(PlatformList.INSTANCE.getOption("display.screenmode")));
        stage.setScene(scene);
        stage.getIcons().add(new Image("/com/adr/mimame/res/mimamememu.png"));
        stage.show();
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
