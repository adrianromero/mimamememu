/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adr.mmmmm.platform;

import com.adr.mmmmm.Platform;
import com.adr.mmmmm.GamesItem;

/**
 *
 * @author adrian
 */
public class MameCommand implements Platform {
    
    @Override
    public String getPlatformName() {
        return "MAME";
    }
    
    @Override
    public String getPlatformTitle() {
        return "M.A.M.E.";
    }

    @Override
    public String getCommand(GamesItem item) {
        return "mame " + item.getName();
    } 
}
