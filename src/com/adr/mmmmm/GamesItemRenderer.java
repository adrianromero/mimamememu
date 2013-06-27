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
import javax.swing.ImageIcon;
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
            
            jemulation.setText("");
            jcolor.setText("");
            jsound.setText("");
            
            jicon.setText(null);
            jicon.setIcon(null);   
            
            jtitle.setForeground(Color.GRAY);  
            jmanufacturer.setForeground(Color.GRAY);  
            jyear.setForeground(Color.GRAY);    
            jplatform.setForeground(Color.GRAY);
            
            jemulation.setForeground(Color.GRAY);
            jcolor.setForeground(Color.GRAY);
            jsound.setForeground(Color.GRAY);
            jLabel1.setForeground(Color.GRAY);
            jLabel3.setForeground(Color.GRAY);
            jLabel5.setForeground(Color.GRAY);
        } else {
            GamesItem item = (GamesItem) value;
            jtitle.setText(item.getTitle());
            jmanufacturer.setText(item.getManufacturer());
            jyear.setText(item.getYear());
            jplatform.setText(item.getPlatform().getPlatformTitle());
            
            jemulation.setText(item.getDriveremulation());
            jcolor.setText(item.getDrivercolor());
            jsound.setText(item.getDriversound());

            jicon.setText(null);
            jicon.setIcon(item.getSnap() == null ? null : new ScaledIcon(new ImageIcon(item.getSnap()), jicon.getWidth(), jicon.getHeight()));
            
            if (item.getCommand() == null) {
                // Not working game
                jtitle.setForeground(Color.GRAY);  
                jmanufacturer.setForeground(Color.GRAY);  
                jyear.setForeground(Color.GRAY);  
                jplatform.setForeground(Color.GRAY);
                jemulation.setForeground(Color.GRAY);
                jcolor.setForeground(Color.GRAY);
                jsound.setForeground(Color.GRAY);
                jLabel1.setForeground(Color.GRAY);
                jLabel3.setForeground(Color.GRAY);
                jLabel5.setForeground(Color.GRAY);
            } else if (isSelected) {
                // Selected, ready to run
                jtitle.setForeground(list.getSelectionForeground());
                jmanufacturer.setForeground(Color.GRAY);
                jyear.setForeground(Color.GRAY);                
                jplatform.setForeground(Color.GRAY);
                jemulation.setForeground(Color.GRAY);
                jcolor.setForeground(Color.GRAY);
                jsound.setForeground(Color.GRAY);
                jLabel1.setForeground(Color.GRAY);
                jLabel3.setForeground(Color.GRAY);
                jLabel5.setForeground(Color.GRAY);
            } else {
                jtitle.setForeground(list.getSelectionForeground());
                jmanufacturer.setForeground(Color.GRAY);
                jyear.setForeground(Color.GRAY);               
                jplatform.setForeground(Color.GRAY);
                jemulation.setForeground(Color.GRAY);
                jcolor.setForeground(Color.GRAY);
                jsound.setForeground(Color.GRAY);
                jLabel1.setForeground(Color.GRAY);
                jLabel3.setForeground(Color.GRAY);
                jLabel5.setForeground(Color.GRAY);
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

        jicon = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jtitle = new javax.swing.JLabel();
        jmanufacturer = new javax.swing.JLabel();
        jyear = new javax.swing.JLabel();
        jplatform = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jemulation = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jcolor = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jsound = new javax.swing.JLabel();

        jicon.setText("jicon");

        jPanel1.setOpaque(false);
        jPanel1.setLayout(new java.awt.GridBagLayout());

        jtitle.setFont(jtitle.getFont().deriveFont(jtitle.getFont().getStyle() | java.awt.Font.BOLD, jtitle.getFont().getSize()+6));
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

        jLabel1.setFont(jLabel1.getFont().deriveFont(jLabel1.getFont().getStyle() & ~java.awt.Font.BOLD));
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("com/adr/mmmmm/res/messages"); // NOI18N
        jLabel1.setText(bundle.getString("lbl.emulation")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.5;
        jPanel1.add(jLabel1, gridBagConstraints);

        jemulation.setFont(jemulation.getFont().deriveFont(jemulation.getFont().getStyle() & ~java.awt.Font.BOLD));
        jemulation.setText("jemulation");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.5;
        jPanel1.add(jemulation, gridBagConstraints);

        jLabel3.setFont(jLabel3.getFont().deriveFont(jLabel3.getFont().getStyle() & ~java.awt.Font.BOLD));
        jLabel3.setText(bundle.getString("lbl.color")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.5;
        jPanel1.add(jLabel3, gridBagConstraints);

        jcolor.setFont(jcolor.getFont().deriveFont(jcolor.getFont().getStyle() & ~java.awt.Font.BOLD));
        jcolor.setText("jcolor");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.5;
        jPanel1.add(jcolor, gridBagConstraints);

        jLabel5.setFont(jLabel5.getFont().deriveFont(jLabel5.getFont().getStyle() & ~java.awt.Font.BOLD));
        jLabel5.setText(bundle.getString("lbl.sound")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.5;
        jPanel1.add(jLabel5, gridBagConstraints);

        jsound.setFont(jsound.getFont().deriveFont(jsound.getFont().getStyle() & ~java.awt.Font.BOLD));
        jsound.setText("jsound");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.5;
        jPanel1.add(jsound, gridBagConstraints);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jicon, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 532, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jicon, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel jcolor;
    private javax.swing.JLabel jemulation;
    private javax.swing.JLabel jicon;
    private javax.swing.JLabel jmanufacturer;
    private javax.swing.JLabel jplatform;
    private javax.swing.JLabel jsound;
    private javax.swing.JLabel jtitle;
    private javax.swing.JLabel jyear;
    // End of variables declaration//GEN-END:variables
}
