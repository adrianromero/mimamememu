//    Mimamememu is launcher for M.A.M.E and other emulators.
//    Copyright (C) 2013-2014 Adrián Romero Corchado.
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

package com.adr.mimame;

import java.io.IOException;
import java.util.Map.Entry;
import java.util.Properties;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 *
 * @author adrian
 */
public class GamesItem implements Comparable<GamesItem> {
    
    private final String name;   
    private final Platform platform;
    private final String title;
    
    private String manufacturer;
    private String year;
    private String titles;
    private String snap;
    private String cabinets;
    private String marquees;
    
    private final Properties props;
    
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
        this.titles = null;
        this.snap = null;
        this.cabinets = null;
        this.marquees = null;
        
        this.props = new Properties();
        
        this.driveremulation = null;
        this.drivercolor = null;
        this.driversound = null;
        this.drivergraphic = null;
        this.driverstate = null;
    }
    
    public GamesItem(Element e) throws IOException {

        name = e.getAttribute("name");
        title = getElementText(e, "description", name);
        platform = PlatformList.INSTANCE.findPlatform(getElementText(e, "platform"));
        
        manufacturer = getElementText(e, "manufacturer"); 
        year = getElementText(e, "year");   
        driveremulation = getElementText(e, "emulation");
        drivercolor = getElementText(e, "color");
        driversound = getElementText(e, "sound");
        drivergraphic = getElementText(e, "graphic");
        driverstate = getElementText(e, "savestate");
        titles = getElementText(e, "titles");
        snap = getElementText(e, "snap");
        cabinets = getElementText(e, "cabinets");
        marquees = getElementText(e, "marquees");
                       
        props = new Properties();
        NodeList n = e.getElementsByTagName("properties");
        if (n.getLength() == 1) {
            NodeList eprops = ((Element) n.item(0)).getChildNodes();
            for (int i = 0; i < eprops.getLength(); i++) {
                if (eprops.item(i) instanceof Element) {
                    Element echild = (Element) eprops.item(i);
                    props.setProperty(echild.getTagName(), echild.getTextContent());
                }
            }
        }
    }

    public void toElement(Element e) throws IOException {
        e.setAttribute("name", name);
        setElementText(e, "description", title);
        setElementText(e, "platform", platform.getPlatformName());
        setElementText(e, "manufacturer", manufacturer);
        setElementText(e, "year", year);
        setElementText(e, "emulation", driveremulation);
        setElementText(e, "color", drivercolor);
        setElementText(e, "sound", driversound);
        setElementText(e, "graphic", drivergraphic);
        setElementText(e, "savestate", driverstate);
        setElementText(e, "titles", titles);
        setElementText(e, "snap", snap);
        setElementText(e, "cabinets", cabinets);
        setElementText(e, "marquees", marquees);
        
        if (!props.isEmpty()) {
            Element eprops = e.getOwnerDocument().createElement("properties");
            e.appendChild(eprops);  
            for (Entry en : props.entrySet()) {
                setElementText(eprops, (String) en.getKey(), (String) en.getValue());
            }
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
    
    public String getTitle1() {
        int i = title.indexOf('(');
        if (i >= 0) {
            return title.substring(0, i).trim();
        } else {
            return title;
        }
    }
    
    public String getTitle2() {
        int i = title.indexOf('(');
        int j = title.lastIndexOf(')');
        if (i >= 0 && i < j) {
            return title.substring(i + 1, j).trim();
        } else {
            return "";
        }        
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
    
    public String[] getCommand() {
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
        return getTitle1() + "\n" + getTitle2();
    }

    /**
     * @return the snap
     */
    public String getTitles() {
        return titles;
    }

    /**
     * @param snap the snap to set
     */
    public void setTitles(String snap) {
        this.titles = snap;
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

    /**
     * @return the snap
     */
    public String getSnap() {
        return snap;
    }

    /**
     * @param snap the snap to set
     */
    public void setSnap(String snap) {
        this.snap = snap;
    }

    /**
     * @return the cabinet
     */
    public String getCabinets() {
        return cabinets;
    }

    /**
     * @param cabinet the cabinet to set
     */
    public void setCabinets(String cabinet) {
        this.cabinets = cabinet;
    }

    /**
     * @return the marquee
     */
    public String getMarquees() {
        return marquees;
    }

    /**
     * @param marquee the marquee to set
     */
    public void setMarquees(String marquee) {
        this.marquees = marquee;
    }
    
    public String getProperty(String key) {
        return props.getProperty(key);
    }
    
    public String getProperty(String key, String defaultValue) {
        return props.getProperty(key, defaultValue);
    }
    
    public void setProperty(String key, String value) {
        props.setProperty(key, value);
    }
     
    private String getElementText(Element e, String tag, String def) {
        NodeList n = e.getElementsByTagName(tag);
        if (n != null && n.getLength() > 0) {
            return n.item(0).getTextContent();
        } else {
            return def;
        }                    
    }     
     
    private String getElementText(Element e, String tag) {
        return getElementText(e, tag, null);                  
    }
    
    private void setElementText(Element e, String tag, String value) {
        
        if (value != null && !value.equals("")) {
            Element child = e.getOwnerDocument().createElement(tag);
            child.appendChild(e.getOwnerDocument().createTextNode(value));
            e.appendChild(child);
        }
    }      
}
