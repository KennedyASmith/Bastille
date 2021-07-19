package com.kennedysmithjava.prisonmines.engine;

import com.kennedysmithjava.prisonmines.cmd.CmdOffset;
import com.massivecraft.massivecore.Engine;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class EngineOffsetWand extends Engine {

    private static final EngineOffsetWand i = new EngineOffsetWand();

    public static EngineOffsetWand get() {
        return i;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onBlockClick(PlayerInteractEvent event) {

        Player player = event.getPlayer();
        if (player.getItemInHand().getType() != Material.STONE_AXE) return;
        if (!player.hasPermission("offsetWand")) return;

        if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Location loc1 = CmdOffset.posOneCache.get(player.getUniqueId().toString());
            if (loc1 == null) {
                player.sendMessage("You must select a first position with /offset pos1");
                return;
            }

            CmdOffset.sendOffsetInfo(player, loc1, event.getClickedBlock().getLocation());

        }
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        if (player.getItemInHand().getType() != Material.STONE_AXE) return;
        if (!player.hasPermission("offsetWand")) return;

        event.setCancelled(true);

        CmdOffset.addToPosOneCache(player.getUniqueId().toString(), event.getBlock().getLocation());
        player.sendMessage("Position one saved.");

    }

}
