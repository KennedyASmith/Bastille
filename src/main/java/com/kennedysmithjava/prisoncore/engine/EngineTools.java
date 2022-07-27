package com.kennedysmithjava.prisoncore.engine;

import com.kennedysmithjava.prisoncore.blockhandler.MineRegionCache;
import com.kennedysmithjava.prisoncore.entity.mines.Distribution;
import com.kennedysmithjava.prisoncore.event.AbilityUseEvent;
import com.kennedysmithjava.prisoncore.event.BlockEventFulfiller;
import com.kennedysmithjava.prisoncore.event.MineBlockBreakEvent;
import com.kennedysmithjava.prisoncore.tools.Pickaxe;
import com.kennedysmithjava.prisoncore.tools.enchantment.BlockBreakEnchant;
import com.kennedysmithjava.prisoncore.tools.enchantment.HandEquipEnchant;
import com.kennedysmithjava.prisoncore.util.regions.LazyRegion;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class EngineTools implements Listener {
    private static final BlockEventFulfiller fulfiller = BlockEventFulfiller.get();
    private static final MineRegionCache cache = MineRegionCache.get();
    private static final Set<UUID> usingAbilityCache = new HashSet<>();

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerUse(PlayerInteractEvent event) {
        if (!event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) return;
        Player player = event.getPlayer();
        if (!player.isSneaking()) return;
        ItemStack i = player.getInventory().getItemInMainHand();
        Block block = event.getClickedBlock();
        if(block.getType() == null) return;
        //Ensure that the player isn't already using an ability.
        if(usingAbilityCache.contains(player.getUniqueId())){
            player.sendMessage("You're already using an ability.");
            return;
        }

        //Ensure that the clicked location is in a mine region
        LazyRegion region = cache.getRegion(block);

        // We may need to cache this region if it doesn't exist!
        if (region == null) {
            // Try and find it in the cache, if it's found restart from the top of this method.
            if (cache.tryCache(block)) {
                this.onPlayerUse(event);
            }
            return;
        }

        if (!region.contains(block)) {
            // No mine region exists there, we'll let something else handle the block breaking.
            return;
        }

        //Ensure that the item used to break a block is a Tool
        if (!Pickaxe.isPickaxe(i)) return;
        Pickaxe pick = Pickaxe.get(i);
        //If the item is a pickaxe
            if (pick.hasLeveledAbility()){
                Distribution distribution = cache.getDistribution(block);
                AbilityUseEvent abilityUseEvent = new AbilityUseEvent(event, region, distribution);
                pick.runAbility(abilityUseEvent);
                Bukkit.getServer().getPluginManager().callEvent(abilityUseEvent);
                usingAbilityCache.add(player.getUniqueId());
            }else{
                player.sendMessage("Your pickaxe doesn't have any abilities.");
            }
            if (pick.getOriginalUser() == null)
                pick.setOriginalUser(player.getUniqueId().toString());
    }

    public static void fulfillAbility(AbilityUseEvent event){
        if(event.getPlayer() != null){
            usingAbilityCache.remove(event.getPlayer().getUniqueId());
            fulfiller.handleEventReturn(event);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerToolChange(PlayerItemHeldEvent event) {
        Player p = event.getPlayer();
        int newSlot = event.getNewSlot();
        int oldSlot = event.getPreviousSlot();
        handleEquipEnchants(p, newSlot, true);
        handleEquipEnchants(p, oldSlot, false);
    }

    public void handleEquipEnchants(Player player, int slot, boolean thisIsNewItem){
        ItemStack item = player.getInventory().getItem(slot);
        if(item == null) return;
        if (!Pickaxe.isPickaxe(item)) return;
        Pickaxe p = Pickaxe.get(item);
        p.getEnchants().forEach((enchant, level) -> {
            if(enchant instanceof HandEquipEnchant<?> handEquipEnchant){
                Bukkit.broadcastMessage("Enchant: " + enchant.getName() + " Level: " + level);
                if(thisIsNewItem){
                    handEquipEnchant.onEquip(player, level);
                }else{
                    handEquipEnchant.onDequip(player, level);
                }
            }
        });
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockBreak(MineBlockBreakEvent event) {
        ItemStack i = event.getPlayer().getInventory().getItemInMainHand();
        if (!Pickaxe.isPickaxe(i)) return;
        Pickaxe p = Pickaxe.get(i);
        //If the item is a pickaxe
        if (p.hasBlockBreakEnchantments()) {
            Map<BlockBreakEnchant<?>, Integer> enchants = p.getBlockBreakEnchantments();
            enchants.forEach((enchant, level) -> enchant.onBreak(event, level));
        }
        if (p.getOriginalUser() == null) p.setOriginalUser(event.getPlayer().getUniqueId().toString());
        p.setDurability(p.getDurability()-1);
        Pickaxe.addToLoreUpdateQueue(p, p.getItem());
        fulfiller.handleEventReturn(event);
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onDisconnect(PlayerQuitEvent event) {
        Pickaxe.removeFromCache(event.getPlayer().getUniqueId().toString());
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onKick(PlayerQuitEvent event) {
        Pickaxe.removeFromCache(event.getPlayer().getUniqueId().toString());
    }

}
