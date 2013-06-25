/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adr.mmmmm;

/**
 *
 * @author adrian
 */
public interface Platform {
    
    public String getPlatformName();
    public String getPlatformTitle();
    public String getCommand(GamesItem item);    
}
