//    MIMAMEMEMU is a launcher for M.A.M.E and other emulators.
//    Copyright (C) 2013 Adrián Romero Corchado.
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


package com.adr.mmmmm.display;

import com.adr.mmmmm.GamesItem;
import com.adr.mmmmm.Main;
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
public class RendererInfo extends javax.swing.JPanel implements ListCellRenderer {
    
    protected static Border noFocusBorder;
    /**
     * Creates new form GamesItemRenderer2
     */
    public RendererInfo() {
        initComponents();
        
        jtitle.setFont(Main.FONT_NAME.deriveFont(24.0f));
        
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
            
            jemulation.setText("");
            jcolor.setText("");
            jsound.setText("");
            
            jtitle.setForeground(Color.GRAY);  
            jmanufacturer.setForeground(Color.GRAY);  
            jyear.setForeground(Color.GRAY);    
            jplatform.setForeground(Color.GRAY);
            
            jemulation.setForeground(Color.GRAY);
            jcolor.setForeground(Color.GRAY);
            jsound.setForeground(Color.GRAY);
        } else {
            GamesItem item = (GamesItem) value;
            jtitle.setText(item.getTitle());
            jmanufacturer.setText(item.getManufacturer());
            jyear.setText(item.getYear());
            jplatform.setText(item.getPlatform().getPlatformTitle());
            
            jemulation.setText(        
                    java.util.ResourceBundle.getBundle("com/adr/mmmmm/res/messages").getString("lbl.emulation") + ": " +
                    item.getDriveremulation());
            jcolor.setText(
                    java.util.ResourceBundle.getBundle("com/adr/mmmmm/res/messages").getString("lbl.color") + ": " +
                    item.getDrivercolor());
            jsound.setText(
                    java.util.ResourceBundle.getBundle("com/adr/mmmmm/res/messages").getString("lbl.sound") + ": " +
                    item.getDriversound());
            
            if (item.getCommand() == null) {
                // Not working game
                jtitle.setForeground(Color.GRAY);  
                jmanufacturer.setForeground(Color.GRAY);  
                jyear.setForeground(Color.GRAY);  
                jplatform.setForeground(Color.GRAY);
                jemulation.setForeground(Color.GRAY);
                jcolor.setForeground(Color.GRAY);
                jsound.setForeground(Color.GRAY);
            } else if (isSelected) {
                // Selected, ready to run
                jtitle.setForeground(list.getSelectionForeground());
                jmanufacturer.setForeground(Color.GRAY);
                jyear.setForeground(Color.GRAY);                
                jplatform.setForeground(Color.GRAY);
                jemulation.setForeground(Color.GRAY);
                jcolor.setForeground(Color.GRAY);
                jsound.setForeground(Color.GRAY);
            } else {
                jtitle.setForeground(list.getForeground());
                jmanufacturer.setForeground(Color.GRAY);
                jyear.setForeground(Color.GRAY);               
                jplatform.setForeground(Color.GRAY);
                jemulation.setForeground(Color.GRAY);
                jcolor.setForeground(Color.GRAY);
                jsound.setForeground(Color.GRAY);
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
        java.awt.GridBagConstraints gridBagConstraints;

        jPanel1 = new javax.swing.JPanel();
        jtitle = new javax.swing.JLabel();
        jmanufacturer = new javax.swing.JLabel();
        jyear = new javax.swing.JLabel();
        jplatform = new javax.swing.JLabel();
        jemulation = new javax.swing.JLabel();
        jcolor = new javax.swing.JLabel();
        jsound = new javax.swing.JLabel();

        jPanel1.setOpaque(false);
        jPanel1.setLayout(new java.awt.GridBagLayout());

        jtitle.setText("jtitle");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.5;
        jPanel1.add(jtitle, gridBagConstraints);

        jmanufacturer.setFont(jmanufacturer.getFont().deriveFont(jmanufacturer.getFont().getStyle() | java.awt.Font.BOLD));
        jmanufacturer.setText("jmanufacturer");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.5;
        jPanel1.add(jmanufacturer, gridBagConstraints);

        jyear.setFont(jyear.getFont().deriveFont(jyear.getFont().getStyle() | java.awt.Font.BOLD));
        jyear.setText("jyear");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.5;
        jPanel1.add(jyear, gridBagConstraints);

        jplatform.setFont(jplatform.getFont().deriveFont(jplatform.getFont().getStyle() | java.awt.Font.BOLD));
        jplatform.setText("jplatform");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.5;
        jPanel1.add(jplatform, gridBagConstraints);

        jemulation.setFont(jemulation.getFont().deriveFont(jemulation.getFont().getStyle() & ~java.awt.Font.BOLD));
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("com/adr/mmmmm/res/messages"); // NOI18N
        jemulation.setText(bundle.getString("lbl.emulation")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.5;
        jPanel1.add(jemulation, gridBagConstraints);

        jcolor.setFont(jcolor.getFont().deriveFont(jcolor.getFont().getStyle() & ~java.awt.Font.BOLD));
        jcolor.setText("jcolor");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.5;
        jPanel1.add(jcolor, gridBagConstraints);

        jsound.setFont(jsound.getFont().deriveFont(jsound.getFont().getStyle() & ~java.awt.Font.BOLD));
        jsound.setText("jsound");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.5;
        jPanel1.add(jsound, gridBagConstraints);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 738, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel jcolor;
    private javax.swing.JLabel jemulation;
    private javax.swing.JLabel jmanufacturer;
    private javax.swing.JLabel jplatform;
    private javax.swing.JLabel jsound;
    private javax.swing.JLabel jtitle;
    private javax.swing.JLabel jyear;
    // End of variables declaration//GEN-END:variables
}
