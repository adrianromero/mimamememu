/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adr.mmmmm;

import java.util.ArrayList;
import javax.swing.AbstractListModel;

/**
 *
 * @author adrian
 */
public class GamesModel extends AbstractListModel {
    
    private ArrayList<GamesItem> items = new ArrayList<GamesItem>();
    
    
    public void add(GamesItem item) {
        items.add(item);
    }
    
    @Override
    public int getSize() {
        return items.size();
    }
    @Override
    public Object getElementAt(int i) { 
        return items.get(i); 
    }
}
