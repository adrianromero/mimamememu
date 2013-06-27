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
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import javax.swing.Icon;

/**
 *
 * @author adrian
 */
public class ScaledIcon implements Icon {
    
    private BufferedImage img;
    private int width;
    private int height;
    
//    private double scale;
    
    public ScaledIcon(BufferedImage img, int width, int height) {
        this.img = img;
        this.width = width;
        this.height = height;       
    }

    @Override
    public int getIconHeight() {
        return height;
    }

    @Override
    public int getIconWidth() {
        return width;
    }

    @Override
    public void paintIcon(Component c, Graphics g, int x, int y) {

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

        Color oldcolor = g2d.getColor();

        g2d.setColor(Color.BLACK);
        g2d.fillRect(x, y, width, height);
        
        int myheight = height;
        int mywidth = width;

        if (myheight > height) {
            mywidth = (int) (mywidth * height / myheight);
            myheight = height;
        }
        if (mywidth > width) {
            myheight = (int) (myheight * width / mywidth);
            mywidth = width;
        }

        double scalex = (double) mywidth / (double) img.getWidth();
        double scaley = (double) myheight / (double) img.getHeight();

        g2d.fillRect(x, y, mywidth, myheight);
        if (scalex < scaley) {
            g2d.drawImage(img, x, y + (int) ((myheight - img.getHeight() * scalex) / 2.0)
                    , mywidth, (int) (img.getHeight() * scalex), null);
        } else {
            g2d.drawImage(img, x + (int) ((mywidth - img.getWidth() * scaley) / 2.0), y
                    , (int) (img.getWidth() * scaley), myheight, null);
        }        
        g2d.setColor(oldcolor);
    }
}
