/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.adr.mimame;

/**
 *
 * @author adrian
 */
public class GamesActionException extends Exception {
    
    private final String title;
    
    public GamesActionException(String title, String body) {
        super(body);
        this.title = title;        
    }    
    
    public GamesActionException(String title, Throwable cause) {
        super(cause.getMessage(), cause);
        this.title = title;        
    }
    
    public String getTitle() {
        return title;
    }   
}
