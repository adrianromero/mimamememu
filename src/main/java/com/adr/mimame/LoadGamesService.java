//    MIMAMEMEMU is a launcher for M.A.M.E and other emulators.
//    Copyright (C) 2014 Adrián Romero Corchado.
//    https://github.com/adrianromero/mimamememu
//
//    This file is part of Mimamememu
//
//    MIMAMEMEMU is free software: you can redistribute it and/or modify
//    it under the terms of the GNU General Public License as published by
//    the Free Software Foundation, either version 3 of the License, or
//    (at your option) any later version.
//
//    MIMAMEMEMU is distributed in the hope that it will be useful,
//    but WITHOUT ANY WARRANTY; without even the implied warranty of
//    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//    GNU General Public License for more details.
//
//    You should have received a copy of the GNU General Public License
//    along with MIMAMEMEMU.  If not, see <http://www.gnu.org/licenses/>.

package com.adr.mimame;

import java.util.List;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
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
    
    private final ReadOnlyObjectWrapper<List<PlatformException>> errors = new ReadOnlyObjectWrapper<List<PlatformException>>(null);
    
    public void setRefresh(boolean refresh) {
        this.refresh = refresh;
    }
    
    public ReadOnlyObjectProperty<List<PlatformException>> errorsProperty() {
        return errors.getReadOnlyProperty();
    }
    
    public List<PlatformException> getErrors() {
        return errors.get();
    }

    @Override
    protected Task<ObservableList<GamesItem>> createTask() {
        return new Task<ObservableList<GamesItem>>() {
            @Override
            protected ObservableList<GamesItem> call() throws Exception {
                if (refresh) {
                    PlatformList.INSTANCE.clearAllGames();
                }
                return FXCollections.observableArrayList(PlatformList.INSTANCE.getAllGames(this::updateMessage));
            }
        };
    }
    
    @Override
    protected void scheduled() {
        errors.set(null);
    }  
    
    @Override
    protected void succeeded() {
        errors.set(PlatformList.INSTANCE.getLastErrors());
    }
    
    @Override
    protected void failed() {
        errors.set(PlatformList.INSTANCE.getLastErrors());    
    }    
    
}
