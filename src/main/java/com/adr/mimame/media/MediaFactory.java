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

package com.adr.mimame.media;

import java.util.Properties;
import javafx.application.ConditionalFeature;
import javafx.application.Platform;

/**
 *
 * @author adrian
 */
public class MediaFactory {
    
    private final String[] mpgcommand;
    
    public MediaFactory(Properties options) {
        
        String mp3player = options.getProperty("mp3.player", "OMXPlayer");
        if ("OMXPlayer".equals(mp3player)) {
            mpgcommand = new String[] {"omxplayer"};
        } else if ("MPG321".equals(mp3player)) {
            mpgcommand = new String[] {"mpg321", "-q"};
        } else {
            mpgcommand = new String[] {mp3player};
        }        
    }
    
    public Clip createClip(String url) {
        if (Platform.isSupported(ConditionalFeature.MEDIA)) {
            return new StandardClip(url);
        } else {
            return new MpgClip(mpgcommand, url);
        }
    }
}
