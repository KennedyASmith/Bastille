package com.kennedysmithjava.prisoncore.event;

import com.kennedysmithjava.prisoncore.blockhandler.Reward;
import com.kennedysmithjava.prisoncore.engine.EngineResearchPoint;
import com.kennedysmithjava.prisoncore.entity.mines.objects.PrisonBlock;
import com.kennedysmithjava.prisoncore.tools.pouch.Pouch;
import com.kennedysmithjava.prisoncore.tools.pouch.PouchFullException;
import com.kennedysmithjava.prisoncore.tools.pouch.PouchManager;
import com.kennedysmithjava.prisoncore.tools.pouch.Pouchable;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.*;
import java.util.stream.Collectors;

public class BlockEventFulfiller {

    // -------------------------------------------- //
    // INSTANCE & CONSTRUCT
    // -------------------------------------------- //

    private static final BlockEventFulfiller i = new BlockEventFulfiller();
    private static final EngineResearchPoint rpEngine = EngineResearchPoint.get();

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
    public void handleEventReturn(EventMineBlockBreak finishedEvent) {
        if (finishedEvent.isCancelled()) {
            return;
        }

        Block block = finishedEvent.getBlock();

        finishedEvent.getBreakAnimations().forEach(a -> a.play(block));

        if (finishedEvent.shouldDeleteBlock()) {
            block.setType(Material.AIR);
        }

        this.rewardPlayer(finishedEvent.getPlayer(), finishedEvent.getRewards(), finishedEvent.getBlockMultiplier(), finishedEvent.getAwardMultiplier());

        rpEngine.addBlockCount(finishedEvent.getPlayer());
    }

    public void handleEventReturn(EventAbilityUse finishedEvent) {
        if (finishedEvent.isCancelled()) {
            return;
        }

        Block block = finishedEvent.getBlock();
        this.rewardPlayer(finishedEvent.getPlayer(), finishedEvent.getRewards(), finishedEvent.getBlockMultiplier(), finishedEvent.getAwardMultiplier());
    }

    private void rewardPlayer(Player player, List<Reward> r, double blockMultiplier, double awardMultiplier) {

        final List<Reward> rewards = r.stream().filter(Objects::nonNull).collect(Collectors.toList());


        Map<Integer, Pouch> pouches = this.getPouches(player.getInventory());
        pouches.forEach((index, pouch) -> {
            ItemStack item = player.getInventory().getItem(index);

            rewards.removeIf(p -> {
                if (!(p instanceof Pouchable)) {
                    return false;
                }
                try {
                    pouch.pouch((Pouchable) p, item);
                    return true;
                } catch (PouchFullException e) {
                    return false;
                }
            });

        });

        rewards.forEach(e ->{
            if(e instanceof PrisonBlock){
                PrisonBlock pb = (PrisonBlock) e;
                pb.setValue(pb.getValue() * awardMultiplier);
            }
            player.getInventory().addItem(e.getProductItem((int) (blockMultiplier)));
        });
        player.updateInventory();
    }

    private Map<Integer, Pouch> getPouches(PlayerInventory inventory) {
        final PouchManager pouchManager = PouchManager.get();
        final Map<Integer,Pouch> result = new HashMap<>(5);

        for (int i = 0; i < inventory.getSize(); i++) {
            ItemStack item = inventory.getItem(i);
            if (!Pouch.isPouch(item)) {
                continue;
            }

            Pouch pouch = pouchManager.getPouch(item);
            if (pouch == null) {
                continue;
            }

            result.put(i, pouch);
        }

        return result;
    }


}
