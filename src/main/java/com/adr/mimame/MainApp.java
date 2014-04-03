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
