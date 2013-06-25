/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adr.mmmmm;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author adrian
 */
public class GamesActionLauncher implements ActionListener {
    
    private Component parent;

    
    public GamesActionLauncher(Component parent) {
        this.parent = parent;
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getID() == ActionEvent.ACTION_PERFORMED) {
            executeGamesItem((GamesItem) e.getSource());
        }
    }    

    
    public void executeGamesItem(final GamesItem item) {
        
        (new Thread() {
            @Override
            public void run() {
                try {
                    
                    
                    Process p = Runtime.getRuntime().exec("mame -window " + item.getName());

                    java.awt.EventQueue.invokeLater(new Runnable() { @Override public void run() {                   
                        parent.setVisible(false);
                    }});

                    try {
                        p.waitFor();
                    } catch (InterruptedException ex) {
                        Logger.getLogger(FrmMain.class.getName()).log(Level.SEVERE, null, ex);
                    } finally {                
                        java.awt.EventQueue.invokeLater(new Runnable() { @Override public void run() {                   
                            parent.setVisible(true);
                        }});  
                    }

                } catch (IOException ex) {
                    Logger.getLogger(FrmMain.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }).start();
    }    
}
