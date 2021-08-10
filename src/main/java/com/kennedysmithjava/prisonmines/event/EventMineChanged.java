package com.kennedysmithjava.prisonmines.event;

import com.kennedysmithjava.prisonmines.entity.Mine;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class EventMineChanged extends Event {

    private final Mine mine;

    public EventMineChanged(Mine mine) {
        this.mine = mine;
    }

    public Mine getMine() {
        return mine;
    }

    private static final HandlerList handlers = new HandlerList();

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
