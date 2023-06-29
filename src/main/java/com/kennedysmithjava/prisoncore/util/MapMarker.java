package com.kennedysmithjava.prisoncore.util;

import com.kennedysmithjava.prisoncore.entity.farming.objects.TwoDVector;
import org.bukkit.map.MapCursor;

public class MapMarker {
    public MapCursor.Type cursorType;
    public String cursorName;
    public TwoDVector location;

    public MapMarker(MapCursor.Type cursorType, String cursorName, TwoDVector location) {
        this.cursorType = cursorType;
        this.cursorName = cursorName;
        this.location = location;
    }

    public String name(){
        return cursorName;
    }

    public MapCursor.Type cursorType(){
        return cursorType;
    }

    public TwoDVector twoDVector(){
        return location;
    }
}
