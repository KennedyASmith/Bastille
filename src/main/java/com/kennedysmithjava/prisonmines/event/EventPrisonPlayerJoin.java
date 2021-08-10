package com.kennedysmithjava.prisonmines.event;

import com.kennedysmithjava.prisonmines.entity.Mine;
import com.kennedysmithjava.prisonmines.entity.MineColl;
import com.mcrivals.prisoncore.entity.MPlayer;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class EventPrisonPlayerJoin extends Event {

    private static final HandlerList handlers = new HandlerList();
    private MPlayer player;
    private Mine mine;

    public EventPrisonPlayerJoin(MPlayer player) {
        this.player = player;
        this.mine = MineColl.get().get(player.getMineID());
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
