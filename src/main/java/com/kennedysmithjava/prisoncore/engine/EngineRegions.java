package com.kennedysmithjava.prisoncore.engine;

import com.kennedysmithjava.prisoncore.quest.region.QuestRegion;
import com.massivecraft.massivecore.Engine;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.*;

public class EngineRegions extends Engine {

    // -------------------------------------------- //
    // INSTANCE & CONSTRUCT
    // -------------------------------------------- //
    private static final EngineRegions i = new EngineRegions();
    public static EngineRegions get() {
        return i;
    }

    Map<UUID, QuestRegion> regionMap = new HashMap<>();
    Set<UUID> inTheirRegion = new HashSet<>();

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerMove(PlayerMoveEvent event){
        Location getTo = event.getTo();
        if(getTo  == null) return;
        Location getFrom = event.getFrom();
        if (getFrom.getBlockX() == getTo.getBlockX() && getFrom.getBlockZ() == getTo.getBlockZ())
            return; //The player hasn't moved an entire block, end here
        Player player = event.getPlayer();
        QuestRegion region = regionMap.get(player.getUniqueId());
        if(region == null) return;
        if(region.has(getTo)) {
            if(inTheirRegion.contains(player.getUniqueId())) return;
            Bukkit.broadcastMessage("You've entered your quest location!");
        }else {
            inTheirRegion.remove(player.getUniqueId());
            Bukkit.broadcastMessage("You've left your quest location!");
        }
    }

    public void addToRegionTracker(UUID player, QuestRegion region){
        regionMap.put(player, region);
    }
}
