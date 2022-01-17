package com.kennedysmithjava.prisoncore.event;

import com.kennedysmithjava.prisoncore.entity.player.MPlayer;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public final class ReturningPlayerJoinEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private MPlayer player;

    public ReturningPlayerJoinEvent(MPlayer player) {
        this.player = player;
    }

    public MPlayer getPlayer() {
        return player;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}