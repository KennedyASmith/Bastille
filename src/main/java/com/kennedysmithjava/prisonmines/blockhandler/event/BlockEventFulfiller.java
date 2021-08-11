package com.kennedysmithjava.prisonmines.blockhandler.event;

import com.kennedysmithjava.prisonmines.entity.PrisonBlock;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.List;

public class BlockEventFulfiller {

    // -------------------------------------------- //
    // INSTANCE & CONSTRUCT
    // -------------------------------------------- //

    private static final BlockEventFulfiller i = new BlockEventFulfiller();

    private BlockEventFulfiller() {
    }

    public static BlockEventFulfiller get() {
        return i;
    }

    // -------------------------------------------- //
    // IMPL
    // -------------------------------------------- //

    /**
     * Handles the things that are defined in the block break event to be executed:
     * - Animations
     * - Deleting Blocks
     * - Item Rewards
     */
    public void handleEventReturn(MineBlockBreakEvent finishedEvent) {
        if (finishedEvent.isCancelled()) {
            return;
        }

        Block block = finishedEvent.getBlock();

        finishedEvent.getBreakAnimations().forEach(a -> a.play(block));

        if (finishedEvent.shouldDeleteBlock()) {
            block.setType(Material.AIR);
        }

        this.rewardPlayer(finishedEvent.getPlayer(), finishedEvent.getRewards());

    }

    private void rewardPlayer(Player player, List<PrisonBlock> rewards) {
        /*
         TODO: Decide whether to simply call the giveItem or and then modify that if they have a pouch or
               make a method that handles pouches and inventories proactively.
         */

        rewards.forEach(e -> {
            if (e != null) {
                player.getInventory().addItem(e.getProductItem(1));
            }
        });
    }


}
