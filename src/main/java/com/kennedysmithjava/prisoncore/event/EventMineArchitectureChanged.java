package com.kennedysmithjava.prisoncore.event;

import com.kennedysmithjava.prisoncore.entity.mines.Mine;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class EventMineArchitectureChanged extends Event {

    private final Mine mine;

    public EventMineArchitectureChanged(Mine mine) {
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
