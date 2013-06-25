/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adr.mmmmm;

/**
 *
 * @author adrian
 */
public class GamesItem {
    
    private String name;
    
    private Platform platform;
    private String title;
    private String manufacturer;
    private String year;
    private String genre;
    
    public GamesItem(String name, String title, Platform platform) {
        this.name = name;
        this.title = title;
        this.platform = platform;
        this.manufacturer = null;
        this.year = null;
        this.genre = null;               
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
     * @return the genre
     */
    public String getGenre() {
        return genre;
    }

    /**
     * @return the platform
     */
    public Platform getPlatform() {
        return platform;
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

    /**
     * @param genre the genre to set
     */
    public void setGenre(String genre) {
        this.genre = genre;
    }
    
}
