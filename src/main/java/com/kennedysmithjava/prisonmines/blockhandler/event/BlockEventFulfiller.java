package com.kennedysmithjava.prisonmines.blockhandler.event;

import com.kennedysmithjava.prisonmines.entity.PrisonBlock;
import com.kennedysmithjava.prisonmines.pouch.NoPouchFoundException;
import com.kennedysmithjava.prisonmines.pouch.PlayerPouchHandler;
import com.kennedysmithjava.prisonmines.pouch.PouchFullException;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

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
        PlayerPouchHandler pouchHandler = PlayerPouchHandler.get();
        UUID uuid = player.getUniqueId();
        rewards.forEach(e -> {
            if (e != null) {
                try {
                    pouchHandler.tryFulfill(uuid, e, 1);
                } catch (PouchFullException | NoPouchFoundException pouchFullException) {
                    // TODO: Maybe give a message if the pouch is full
                    player.getInventory().addItem(e.getProductItem(1));
                }
            }
        });
    }


}
