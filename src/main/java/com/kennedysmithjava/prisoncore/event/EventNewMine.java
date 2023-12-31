package com.kennedysmithjava.prisoncore.event;

import com.kennedysmithjava.prisoncore.entity.player.MPlayer;
import com.kennedysmithjava.prisoncore.entity.mines.Mine;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class EventNewMine extends Event {

    private static final HandlerList handlers = new HandlerList();
    private MPlayer player;
    private Mine mine;

    public EventNewMine(MPlayer player, Mine mine) {
        this.player = player;
        this.mine = mine;
    }

    public MPlayer getPlayer() {
        return player;
    }

    public Mine getMine() {
        return mine;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
