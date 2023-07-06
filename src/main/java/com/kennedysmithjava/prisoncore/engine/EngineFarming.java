package com.kennedysmithjava.prisoncore.engine;

import com.kennedysmithjava.prisoncore.entity.farming.FarmingConf;
import com.kennedysmithjava.prisoncore.entity.player.MPlayer;
import com.kennedysmithjava.prisoncore.skill.SkillType;
import com.kennedysmithjava.prisoncore.tools.Hoe;
import com.kennedysmithjava.prisoncore.util.MiscUtil;
import com.massivecraft.massivecore.Engine;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.concurrent.ThreadLocalRandom;

public class EngineFarming extends Engine {

    private static final EngineFarming i = new EngineFarming();

    public static EngineFarming get() {
        return i;
    }

    @EventHandler(ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent evt) {

        Material type = evt.getBlock().getType();

        // Must be material wheat
        if (type != Material.WHEAT_SEEDS) return;

        // Objects
        Block block = evt.getBlock();

        if(!(block.getBlockData() instanceof Ageable crop)) return;

        // If the crop is not ripe, don't do this
        if (crop.getAge() != crop.getMaximumAge()) return;

        // Clears the drops and removes the event
        evt.getBlock().getDrops().clear();
        evt.setCancelled(true);

        /* Handle hoe mechanics */
        Player player = evt.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();
        if(item.getType() == Material.AIR) return;
        if(!Hoe.isHoe(item)) return;
        Hoe hoe = Hoe.get(item);
        int newDurability = hoe.getDurability() - 1;
        if(newDurability <= 0) return;
        hoe.setDurability(newDurability);
        Hoe.addToLoreUpdateQueue(hoe, hoe.getItem());

        // 20% chance to get 2x wheat
        int wheatToGive = ThreadLocalRandom.current().nextInt(0, 5) == 1 ? 2 : 1;

        // Gives player the wheat
        MiscUtil.givePlayerItem(evt.getPlayer(), FarmingConf.get().getWheatItem().build(), wheatToGive);
        MPlayer.get(player).getSkillProfile().getSkill(SkillType.FARMING).addXP(wheatToGive);

        // Seeds the ground
        FarmingConf.get().addSeed(block);
    }

    /**
     * Stop player from Trampling crops
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
            if (block.getType() == Material.FARMLAND/*
                    && (block.getRelative(BlockFace.UP).getType() == Material.WHEAT
                    || block.getRelative(BlockFace.UP).getType() == Material.CARROT
                    && block.getRelative(BlockFace.UP).getType() == Material.POTATO)*/) {
                // Deny event and set the block
                event.setUseInteractedBlock(org.bukkit.event.Event.Result.DENY);
                event.setCancelled(true);
                block.setType(block.getType());
            }
        }
    }
}
