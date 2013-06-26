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
import java.awt.geom.AffineTransform;
import javax.swing.Icon;

/**
 *
 * @author adrian
 */
public class ScaledIcon implements Icon {
    
    private Icon icon;
    private int width;
    private int height;
    
    private double scale;
    
    public ScaledIcon(Icon icon, int width, int height) {
        this.icon = icon;
        this.width = width;
        this.height = height;       
        this.scale = Math.min(((double) height) / icon.getIconHeight(), ((double) width) / icon.getIconWidth());
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
            AffineTransform oldt = g2d.getTransform();
            
            g2d.setColor(Color.BLACK);
            g2d.fillRect(x, y, width, height);
            g2d.transform(AffineTransform.getScaleInstance(scale, scale));
            icon.paintIcon(c, g2d, (int)((x + (width - icon.getIconWidth() * scale) / 2) / scale), (int)((y + (height -icon.getIconHeight() * scale) / 2) / scale));
            g2d.setTransform(oldt);
    }
}
