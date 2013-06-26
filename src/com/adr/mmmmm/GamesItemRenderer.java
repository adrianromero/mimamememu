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
    
    private static final Color COLOR_BG1 = new Color(0xffffff);   
    private static final Color COLOR_BG2 = new Color(0xf3f3f3);
    
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
            setBackground((index & 1) == 0 ? COLOR_BG1 : COLOR_BG2);
        }

        if (value == null) {
            jtitle.setText("");
            jmanufacturer.setText("");
            jyear.setText("");
            
            jtitle.setForeground(Color.GRAY);  
            jmanufacturer.setForeground(Color.GRAY);  
            jyear.setForeground(Color.GRAY);    
            jplatform.setForeground(Color.GRAY);
        } else {
            GamesItem item = (GamesItem) value;
            jtitle.setText(item.getTitle());
            jmanufacturer.setText(item.getManufacturer());
            jyear.setText(item.getYear());
            jplatform.setText(item.getPlatform().getPlatformTitle());

            jicon.setText(null);
            jicon.setIcon(item.getSnap() == null ? null : new ScaledIcon(new ImageIcon(item.getSnap()), jicon.getWidth(), jicon.getHeight()));
            
            if (item.getCommand() == null) {
                // Not working game
                jtitle.setForeground(Color.GRAY);  
                jmanufacturer.setForeground(Color.GRAY);  
                jyear.setForeground(Color.GRAY);  
                jplatform.setForeground(Color.GRAY);
            } else if (isSelected) {
                // Selected, ready to run
                jtitle.setForeground(list.getSelectionForeground());
                jmanufacturer.setForeground(Color.GRAY);
                jyear.setForeground(Color.GRAY);                
                jplatform.setForeground(Color.GRAY);
            } else {
                jtitle.setForeground(list.getSelectionForeground());
                jmanufacturer.setForeground(Color.GRAY);
                jyear.setForeground(Color.GRAY);               
                jplatform.setForeground(Color.GRAY);
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
        jmanufacturer = new javax.swing.JLabel();
        jplatform = new javax.swing.JLabel();
        jyear = new javax.swing.JLabel();
        jicon = new javax.swing.JLabel();

        jtitle.setFont(jtitle.getFont().deriveFont(jtitle.getFont().getStyle() | java.awt.Font.BOLD, jtitle.getFont().getSize()+6));
        jtitle.setText("jtitle");

        jmanufacturer.setText("jmanufacturer");

        jplatform.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jplatform.setText("jplatform");

        jyear.setText("jyear");

        jicon.setText("jicon");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jicon, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jmanufacturer, javax.swing.GroupLayout.PREFERRED_SIZE, 372, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jyear, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jplatform, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jtitle, javax.swing.GroupLayout.PREFERRED_SIZE, 717, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jtitle)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jmanufacturer)
                    .addComponent(jyear)
                    .addComponent(jplatform)))
            .addComponent(jicon, javax.swing.GroupLayout.DEFAULT_SIZE, 120, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jicon;
    private javax.swing.JLabel jmanufacturer;
    private javax.swing.JLabel jplatform;
    private javax.swing.JLabel jtitle;
    private javax.swing.JLabel jyear;
    // End of variables declaration//GEN-END:variables
}
