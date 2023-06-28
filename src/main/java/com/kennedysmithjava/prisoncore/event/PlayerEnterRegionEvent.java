package com.kennedysmithjava.prisoncore.event;

import com.kennedysmithjava.prisoncore.regions.RegionType;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerEnterRegionEvent extends Event {
    private static final HandlerList handlers = new HandlerList();

    private Player player;
    private RegionType type;
    private String regionName;

    public PlayerEnterRegionEvent(Player player, RegionType type, String regionName) {
        this.player = player;
        this.type = type;
        this.regionName = regionName;
    }

    public Player getPlayer() {
        return player;
    }

    public RegionType getType() {
        return type;
    }

    public String getRegionName() {
        return regionName;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
