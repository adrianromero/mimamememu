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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

/**
 *
 * @author adrian
 */
public class GamesItemRenderer3 extends javax.swing.JPanel implements ListCellRenderer {
    
    protected static Border noFocusBorder;
    
    private JPanelIcon jicon ;
    /**
     * Creates new form GamesItemRenderer2
     */
    public GamesItemRenderer3() {
        initComponents();
        
        jicon = new JPanelIcon(96, 128);
        jPanel1.add(jicon, BorderLayout.CENTER);
        
        if (noFocusBorder == null) {
            noFocusBorder = new EmptyBorder(1, 1, 1, 1);
        }

        setOpaque(true);
        setBorder(noFocusBorder);        
    }
    
    
    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        
        applyComponentOrientation(list.getComponentOrientation());
        
        if (isSelected) {          
            setBackground(list.getSelectionBackground());            
        } else {       
            setBackground(list.getBackground());
        }

        if (value == null) {
            jtitle.setText("");

            jicon.setImage(null);   
            
            jtitle.setForeground(Color.GRAY);
        } else {
            GamesItem item = (GamesItem) value;
            jtitle.setText(item.getTitle());

            jicon.setImage(item.getTitles());
            
            if (item.getCommand() == null) {
                // Not working game
                jtitle.setForeground(Color.GRAY);  
            } else if (isSelected) {
                // Selected, ready to run
                jtitle.setForeground(list.getSelectionForeground());
            } else {
                jtitle.setForeground(list.getSelectionForeground());
            }
        }

        setEnabled(list.isEnabled());
        setFont(list.getFont());
        setBorder((cellHasFocus) ? UIManager.getBorder("List.focusCellHighlightBorder") : noFocusBorder);

        return this;
    }
    
   /**
    * Overridden for performance reasons.
    */
    @Override protected void firePropertyChange(String propertyName, Object oldValue, Object newValue) {}
    @Override public void firePropertyChange(String propertyName, byte oldValue, byte newValue) {}
    @Override public void firePropertyChange(String propertyName, char oldValue, char newValue) {}
    @Override public void firePropertyChange(String propertyName, short oldValue, short newValue) {}
    @Override public void firePropertyChange(String propertyName, int oldValue, int newValue) {}
    @Override public void firePropertyChange(String propertyName, long oldValue, long newValue) {}
    @Override public void firePropertyChange(String propertyName, float oldValue, float newValue) {}
    @Override public void firePropertyChange(String propertyName, double oldValue, double newValue) {}
    @Override public void firePropertyChange(String propertyName, boolean oldValue, boolean newValue) {}
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jtitle = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();

        setLayout(new java.awt.BorderLayout());

        jtitle.setText("jtitle");
        add(jtitle, java.awt.BorderLayout.PAGE_END);

        jPanel1.setLayout(new java.awt.BorderLayout());
        add(jPanel1, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel jtitle;
    // End of variables declaration//GEN-END:variables
}
