/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adr.mmmmm;

import java.awt.Window;

/**
 *
 * @author adrian
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                Window myWindow = new FrmMain();
                myWindow.setVisible(true);
                           
            }
        });
    }
}
