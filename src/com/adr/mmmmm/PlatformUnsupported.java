/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adr.mmmmm;

/**
 *
 * @author adrian
 */
public class PlatformUnsupported implements Platform {
    
    private String name;
    
    public PlatformUnsupported(String name) {
        this.name = name;
    }

    @Override
    public String getPlatformName() {
        return name;
    }

    @Override
    public String getPlatformTitle() {
        return "Platform not supported (" + name + ")";
    }

    @Override
    public String getCommand(GamesItem item) {
        return null;
    }
    
}
