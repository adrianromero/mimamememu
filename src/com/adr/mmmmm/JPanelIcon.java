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
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import javax.swing.JComponent;

/**
 *
 * @author adrian
 */
public class JPanelIcon extends JComponent {
    
    private Image image;
    private int compwidth;
    private int compheight;
    
    public JPanelIcon(int width, int height) {
        this.image = null;
        this.compwidth = width;
        this.compheight = height;
        setLayout(null);
    }
        
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;

        int myheight = this.getHeight();
        int mywidth = this.getWidth();
        
        Color oldcolor = g2d.getColor();
        g2d.setColor(Color.BLACK);
        g2d.fillRect(0, 0, mywidth, myheight);
        g2d.setColor(oldcolor);

        if (image != null) {
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

            double scalex = (double) mywidth / (double) getImage().getWidth(null);
            double scaley = (double) myheight / (double) getImage().getHeight(null);

            if (scalex < scaley) {
                g2d.drawImage(getImage(), 0, (int) ((myheight - getImage().getHeight(null) * scalex) / 2.0), mywidth, (int) (getImage().getHeight(null) * scalex), null);
            } else {
                g2d.drawImage(getImage(), (int) ((mywidth - getImage().getWidth(null) * scaley) / 2.0), 0, (int) (getImage().getWidth(null) * scaley), myheight, null);
            }
        }
    }
        
    @Override
    public Dimension getPreferredSize() {
            return new Dimension(compwidth, compheight);
    }
    @Override
    public Dimension getMinimumSize() {
            return getPreferredSize();
    }
    @Override
        public Dimension getMaximumSize() {
        return getPreferredSize();
    }

    /**
     * @return the image
     */
    public Image getImage() {
        return image;
    }

    /**
     * @param image the image to set
     */
    public void setImage(Image image) {
        this.image = image;
        repaint();
    }
}
