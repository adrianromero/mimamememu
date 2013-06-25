/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adr.mmmmm;

/**
 *
 * @author adrian
 */
public class PlatformList {
     
    private Platform[] platforms = {
        new com.adr.mmmmm.platform.MameCommand()
    };
    
    
    public GamesItem createGame(String name, String title, String platform, String manufacturer, String year, String genre) {
        GamesItem item = new GamesItem(name, title, findPlatform(platform));
        item.setManufacturer(manufacturer);
        item.setYear(year);
        item.setGenre(genre);
        
        return item;
    }
     
    private Platform findPlatform(String platformname) {
        
        for (Platform gc: platforms) {
            if (gc.getPlatformName().equals(platformname)) {
                return gc;
            }
        }
        return new PlatformUnsupported(platformname);
    }      
}
