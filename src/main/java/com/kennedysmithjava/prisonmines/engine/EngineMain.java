package com.kennedysmithjava.prisonmines.engine;

import com.kennedysmithjava.prisonmines.blockhandler.MineRegionCache;
import com.kennedysmithjava.prisonmines.entity.Mine;
import com.kennedysmithjava.prisonmines.entity.MineColl;
import com.massivecraft.massivecore.Engine;
import com.mcrivals.prisoncore.engine.Cooldown;
import com.mcrivals.prisoncore.engine.CooldownReason;
import com.mcrivals.prisoncore.entity.MPlayer;
import com.mcrivals.prisoncore.entity.MPlayerColl;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class EngineMain extends Engine {

    private static final EngineMain i = new EngineMain();

    public static EngineMain get() {
        return i;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerDisconnect(PlayerQuitEvent event) {

    }


    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerKick(PlayerKickEvent event) {

    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerJoin(PlayerJoinEvent event) {

    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerLeverClick(PlayerInteractEvent event) {
        if(!event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) return;
        Block block = event.getClickedBlock();
        if(!block.getType().equals(Material.LEVER)) return;
        Mine mine = MineColl.get().getByLocation(block);
        if(mine != null) {
            if(Cooldown.inCooldown(event.getPlayer(), CooldownReason.REGEN)) return;
            if(mine.tryManualRegen()) Cooldown.add(event.getPlayer(), 20*10, CooldownReason.REGEN);
        }
    }
}
