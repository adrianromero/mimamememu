//    Mimamememu is launcher for M.A.M.E and other emulators.
//    Copyright (C) 2013 Adrián Romero Corchado.
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
package com.adr.mmmmm;

import java.awt.Font;
import java.awt.FontFormatException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author adrian
 */
public class Main {
    
    
    public static Font FONT_NAME = Font.getFont("Dialog");
    public static Font FONT_TITLE = Font.getFont("Dialog");
    public static Font FONT_ARCADE = Font.getFont("Dialog");

    /**
     * @param args the command line arguments
     */
    public static void main(final String[] args) {
        
        System.setProperty("awt.useSystemAAFontSettings","on");
        System.setProperty("swing.aatext", "true"); 
              
        try {
            FONT_TITLE = Font.createFont(Font.TRUETYPE_FONT, Main.class.getResourceAsStream("/com/adr/mmmmm/res/RussoOne-Regular.ttf"))
                    .deriveFont(16.0f);    
            FONT_NAME = Font.createFont(Font.TRUETYPE_FONT, Main.class.getResourceAsStream("/com/adr/mmmmm/res/Audiowide-Regular.ttf"))
                    .deriveFont(32.0f);
            FONT_ARCADE = Font.createFont(Font.TRUETYPE_FONT, Main.class.getResourceAsStream("/com/adr/mmmmm/res/PressStart2P-Regular.ttf"))
                    .deriveFont(16.0f);      
        } catch (FontFormatException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }

        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new FrmMain().start(args);
            }
        });
    }
}
