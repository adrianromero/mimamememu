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

import java.awt.Color;
import java.awt.Component;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

/**
 *
 * @author adrian
 */
public class GamesItemRenderer extends javax.swing.JPanel implements ListCellRenderer {
    protected static Border noFocusBorder;
    /**
     * Creates new form GamesItemRenderer2
     */
    public GamesItemRenderer() {
        initComponents();
        
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
            jmanufacturer.setText("");
            jyear.setText("");
            jgenre.setText("");
            
            jtitle.setForeground(Color.GRAY);  
            jmanufacturer.setForeground(Color.GRAY);  
            jyear.setForeground(Color.GRAY);  
            jgenre.setForeground(Color.GRAY);     
            jplatform.setForeground(Color.GRAY);
        } else {
            GamesItem item = (GamesItem) value;
            jtitle.setText(item.getTitle());
            jmanufacturer.setText(item.getManufacturer());
            jyear.setText(item.getYear());
            jgenre.setText(item.getGenre());
            jplatform.setText(item.getPlatform().getPlatformTitle());
            
            if (item.getCommand() == null) {
                jtitle.setForeground(Color.GRAY);  
                jmanufacturer.setForeground(Color.GRAY);  
                jyear.setForeground(Color.GRAY);  
                jgenre.setForeground(Color.GRAY);  
                jplatform.setForeground(Color.GRAY);
            } else if (isSelected) {
                jtitle.setForeground(list.getSelectionForeground());
                jmanufacturer.setForeground(list.getSelectionForeground());
                jyear.setForeground(list.getSelectionForeground());
                jgenre.setForeground(list.getSelectionForeground());                
                jplatform.setForeground(list.getSelectionForeground());
            } else {
                jtitle.setForeground(Color.BLUE);
                jmanufacturer.setForeground(list.getForeground());
                jyear.setForeground(list.getForeground());
                jgenre.setForeground(list.getForeground());                
                jplatform.setForeground(list.getForeground());
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
        jdetails = new javax.swing.JPanel();
        jmanufacturer = new javax.swing.JLabel();
        jyear = new javax.swing.JLabel();
        jgenre = new javax.swing.JLabel();
        jplatform = new javax.swing.JLabel();

        setLayout(new java.awt.BorderLayout());

        jtitle.setFont(jtitle.getFont().deriveFont(jtitle.getFont().getStyle() | java.awt.Font.BOLD, jtitle.getFont().getSize()+6));
        jtitle.setText("jtitle");
        add(jtitle, java.awt.BorderLayout.PAGE_START);

        jdetails.setOpaque(false);
        jdetails.setLayout(new java.awt.GridLayout(1, 0));

        jmanufacturer.setText("jmanufacturer");
        jdetails.add(jmanufacturer);

        jyear.setText("jyear");
        jdetails.add(jyear);

        jgenre.setText("jgenre");
        jdetails.add(jgenre);

        jplatform.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jplatform.setText("jplatform");
        jdetails.add(jplatform);

        add(jdetails, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jdetails;
    private javax.swing.JLabel jgenre;
    private javax.swing.JLabel jmanufacturer;
    private javax.swing.JLabel jplatform;
    private javax.swing.JLabel jtitle;
    private javax.swing.JLabel jyear;
    // End of variables declaration//GEN-END:variables
}
