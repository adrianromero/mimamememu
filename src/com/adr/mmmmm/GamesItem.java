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

import java.awt.image.BufferedImage;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 *
 * @author adrian
 */
public class GamesItem implements Comparable<GamesItem> {
    
    private String name;
    
    private Platform platform;
    private String title;
    private String manufacturer;
    private String year;
    private BufferedImage snap;
    
    private String driveremulation;
    private String drivercolor;
    private String driversound;
    private String drivergraphic;
    private String driverstate;
    
    public GamesItem(String name, String title, Platform platform) {
        this.name = name;
        this.title = title;
        this.platform = platform;
        this.manufacturer = null;
        this.year = null;   
        this.snap = null;
        this.driveremulation = null;
        this.drivercolor = null;
        this.driversound = null;
        this.drivergraphic = null;
        this.driverstate = null;
    }
    
    public GamesItem(DataInputStreamExt in, File folder) throws IOException {
        name = in.readString();
        title = in.readString();
        platform = PlatformList.INSTANCE.findPlatform(in.readString());
        manufacturer = in.readString(); 
        year = in.readString();   
        driveremulation = in.readString();
        drivercolor = in.readString();
        driversound = in.readString();
        drivergraphic = in.readString();
        driverstate = in.readString();
        File f = new File(folder, name + ".png");
        if (f.exists()) {
            snap = ImageIO.read(new File(folder, name + ".png"));
        }
    }
    
    public void save(DataOutputStreamExt out, File folder) throws IOException {
        out.writeString(name);
        out.writeString(title);
        out.writeString(platform.getPlatformName());
        out.writeString(manufacturer);
        out.writeString(year);
        out.writeString(driveremulation);
        out.writeString(drivercolor);
        out.writeString(driversound);
        out.writeString(drivergraphic);
        out.writeString(driverstate);
        if (snap != null) {
            ImageIO.write(snap, "png", new File(folder, name + ".png"));
        }
    }
    
    public String getName() {
        return name;
    }

    /**
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @return the manufacturer
     */
    public String getManufacturer() {
        return manufacturer;
    }

    /**
     * @return the year
     */
    public String getYear() {
        return year;
    }

    /**
     * @return the platform
     */
    public Platform getPlatform() {
        return platform;
    }
    
    public String getCommand() {
        return platform.getCommand(this);
    }

    /**
     * @param manufacturer the manufacturer to set
     */
    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    /**
     * @param year the year to set
     */
    public void setYear(String year) {
        this.year = year;
    }

    @Override
    public int compareTo(GamesItem o) {
        return title.compareTo(o.title);
    }
    
    @Override
    public String toString() {
        return title;
    }

    /**
     * @return the snap
     */
    public BufferedImage getSnap() {
        return snap == null ? platform.getDefaultImage() : snap;
    }

    /**
     * @param snap the snap to set
     */
    public void setSnap(BufferedImage snap) {
        this.snap = snap;
    }

    /**
     * @return the driveremulation
     */
    public String getDriveremulation() {
        return driveremulation;
    }

    /**
     * @param driveremulation the driveremulation to set
     */
    public void setDriveremulation(String driveremulation) {
        this.driveremulation = driveremulation;
    }

    /**
     * @return the drivercolor
     */
    public String getDrivercolor() {
        return drivercolor;
    }

    /**
     * @param drivercolor the drivercolor to set
     */
    public void setDrivercolor(String drivercolor) {
        this.drivercolor = drivercolor;
    }

    /**
     * @return the driversound
     */
    public String getDriversound() {
        return driversound;
    }

    /**
     * @param driversound the driversound to set
     */
    public void setDriversound(String driversound) {
        this.driversound = driversound;
    }

    /**
     * @return the drivergraphic
     */
    public String getDrivergraphic() {
        return drivergraphic;
    }

    /**
     * @param drivergraphic the drivergraphic to set
     */
    public void setDrivergraphic(String drivergraphic) {
        this.drivergraphic = drivergraphic;
    }

    /**
     * @return the driverstate
     */
    public String getDriverstate() {
        return driverstate;
    }

    /**
     * @param driverstate the driverstate to set
     */
    public void setDriverstate(String driverstate) {
        this.driverstate = driverstate;
    }
    
}
