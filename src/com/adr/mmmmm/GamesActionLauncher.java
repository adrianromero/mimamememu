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
            dlg.setMsgTitle(java.util.ResourceBundle.getBundle("com/adr/mmmmm/res/messages").getString("msg.cannotrungame"));
            dlg.setMsgBody(java.text.MessageFormat.format(java.util.ResourceBundle.getBundle("com/adr/mmmmm/res/messages").getString("msg.platformnotsupported"), new Object[] {item.getPlatform().getPlatformName()}));
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

                    DlgMessages dlg = null;
                    try {
                        int result = p.waitFor();
                        
                        if (result != 0) {
                            Logger.getLogger(GamesActionLauncher.class.getName()).log(Level.SEVERE, "Return error: {0}", result);
                            
                            dlg = new DlgMessages(parent);
                            dlg.setMsgTitle(java.util.ResourceBundle.getBundle("com/adr/mmmmm/res/messages").getString("msg.cannotrungame"));
                            dlg.setMsgBody(java.text.MessageFormat.format(java.util.ResourceBundle.getBundle("com/adr/mmmmm/res/messages").getString("msg.platformerror"), new Object[] {item.getTitle()}));                          
                        }
                    } catch (InterruptedException ex) {
                        Logger.getLogger(FrmMain.class.getName()).log(Level.SEVERE, null, ex);
                        
                        dlg = new DlgMessages(parent);
                        dlg.setMsgTitle(java.util.ResourceBundle.getBundle("com/adr/mmmmm/res/messages").getString("msg.cannotrungame"));
                        dlg.setMsgBody(java.text.MessageFormat.format(java.util.ResourceBundle.getBundle("com/adr/mmmmm/res/messages").getString("msg.platforminterrupted"), new Object[] {item.getTitle()}));                                                       
                    } finally {                       
                        final DlgMessages finaldlg = dlg;
                        java.awt.EventQueue.invokeLater(new Runnable() { @Override public void run() {                   
                            parent.setVisible(true);
                            if (finaldlg != null) {
                                finaldlg.display();           
                            }
                        }});  
                    }

                } catch (IOException ex) {
                    Logger.getLogger(FrmMain.class.getName()).log(Level.SEVERE, null, ex);
                    DlgMessages dlg = new DlgMessages(parent);
                    dlg.setMsgTitle(java.util.ResourceBundle.getBundle("com/adr/mmmmm/res/messages").getString("msg.cannotrungame"));
                    dlg.setMsgBody(java.text.MessageFormat.format(java.util.ResourceBundle.getBundle("com/adr/mmmmm/res/messages").getString("msg.platformcannotexecute"), new Object[] {item.getTitle()}));
                    final DlgMessages finaldlg = dlg;
                    java.awt.EventQueue.invokeLater(new Runnable() { @Override public void run() {                   
                        if (finaldlg != null) {
                            finaldlg.display();           
                        }
                    }});                     
                }
            }
        }).start();
    }    
}
