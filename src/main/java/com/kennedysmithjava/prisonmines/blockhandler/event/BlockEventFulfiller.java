package com.kennedysmithjava.prisonmines.blockhandler.event;

import com.kennedysmithjava.prisonmines.entity.PrisonBlock;
import com.kennedysmithjava.prisonmines.pouch.NoPouchFoundException;
import com.kennedysmithjava.prisonmines.pouch.Pouch;
import com.kennedysmithjava.prisonmines.pouch.PouchFullException;
import com.kennedysmithjava.prisonmines.pouch.PouchManager;
import com.kennedysmithjava.prisonmines.util.Pair;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

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

    private void rewardPlayer(Player player, List<PrisonBlock> r) {

        final List<PrisonBlock> rewards = r.stream().filter(Objects::nonNull).collect(Collectors.toList());

        getPouches(player).forEach(pair -> {
            Pouch pouch = pair.getRight();
            ItemStack item = pair.getLeft();

            rewards.removeIf(p -> {
                try {
                    pouch.pouch(p, item);
                    return true;
                } catch (PouchFullException e) {
                    return false;
                }
            });

        });

        rewards.forEach(e -> player.getInventory().addItem(e.getProductItem(1)));
        player.updateInventory();
    }
//
    private List<Pair<ItemStack, Pouch>> getPouches(Player player) {
        PouchManager pouchManager = PouchManager.get();
        return Arrays.stream(player.getInventory().getContents())
                .map(item -> new Pair<>(item, pouchManager.getPouch(item)))
                .filter(p -> p.getRight() != null).collect(Collectors.toList());
    }


}
