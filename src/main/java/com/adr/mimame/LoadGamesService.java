/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.adr.mimame;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

/**
 *
 * @author adrian
 */
public class LoadGamesService extends Service<ObservableList<GamesItem>>{
    
    private boolean refresh = false;
    
    public void setRefresh(boolean refresh) {
        this.refresh = refresh;
    }

    @Override
    protected Task<ObservableList<GamesItem>> createTask() {
        return new Task<ObservableList<GamesItem>>() {
            @Override
            protected ObservableList<GamesItem> call() throws Exception {
                return FXCollections.observableArrayList(PlatformList.INSTANCE.getAllGames(refresh));
            }
        };
    }
}
