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
           
        final String command = item.getCommand();
        if (command == null) {       
            DlgMessages dlg = new DlgMessages(parent);
            dlg.setMsgTitle("Cannot run game");
            dlg.setMsgBody("The game platform (" + item.getPlatform().getPlatformName() + ") is not supported.");
            dlg.display();
            return;
        }
        
        (new Thread() {
            @Override
            public void run() {
                try {
                    Process p = Runtime.getRuntime().exec(command);

                    java.awt.EventQueue.invokeLater(new Runnable() { @Override public void run() {                   
                        parent.setVisible(false);
                    }});

                    try {
                        int result = p.waitFor();
                        
                        System.out.println("Result -- "  + result);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(FrmMain.class.getName()).log(Level.SEVERE, null, ex);
                        DlgMessages dlg = new DlgMessages(parent);
                        dlg.setMsgTitle("Cannot run game");
                        dlg.setMsgBody("The command for the game platform (" + item.getPlatform().getPlatformTitle() + ") has been interrupted.");
                        dlg.display();                         
                    } finally {                
                        java.awt.EventQueue.invokeLater(new Runnable() { @Override public void run() {                   
                            parent.setVisible(true);
                        }});  
                    }

                } catch (IOException ex) {
                    Logger.getLogger(FrmMain.class.getName()).log(Level.SEVERE, null, ex);
                    DlgMessages dlg = new DlgMessages(parent);
                    dlg.setMsgTitle("Cannot run game");
                    dlg.setMsgBody("The command for the game platform (" + item.getPlatform().getPlatformTitle() + ") cannot be executed.");
                    dlg.display();                    
                }
            }
        }).start();
    }    
}
