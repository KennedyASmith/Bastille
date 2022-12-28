package com.kennedysmithjava.prisoncore.engine;

import com.kennedysmithjava.prisoncore.entity.farming.FarmingConf;
import com.massivecraft.massivecore.Engine;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.Material;

public class EngineFarming extends Engine {

    private static final EngineFarming i = new EngineFarming();

    public static EngineFarming get() {
        return i;
    }

    @EventHandler(ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent evt) {
        FarmingConf.get().onWheatBreak(evt);
    }

    /**
     * Player Trampling Wheat
     * @param event
     */
    @EventHandler(ignoreCancelled = true)
    public void onPlayerInteract(PlayerInteractEvent event) {
        // Physical means jump on it
        if (event.getAction() == Action.PHYSICAL) {
            Block block = event.getClickedBlock();
            if (block == null) {
                return;
            }
            // If the block is farmland (soil) & matches any of the no trampling blocks
            if (block.getType() == Material.FARMLAND
                    && (block.getRelative(BlockFace.UP).getType() == Material.WHEAT
                    || block.getRelative(BlockFace.UP).getType() == Material.CARROT
                    && block.getRelative(BlockFace.UP).getType() == Material.POTATO)) {
                // Deny event and set the block
                event.setUseInteractedBlock(org.bukkit.event.Event.Result.DENY);
                event.setCancelled(true);
                block.setType(block.getType());
            }
        }
    }
}
