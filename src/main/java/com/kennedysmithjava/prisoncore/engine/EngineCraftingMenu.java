package com.kennedysmithjava.prisoncore.engine;

import com.kennedysmithjava.prisoncore.crafting.PrisonObject;
import com.kennedysmithjava.prisoncore.util.ClickHandler;
import com.kennedysmithjava.prisoncore.util.MiscUtil;
import com.kennedysmithjava.prisoncore.util.RemovableItem;
import com.massivecraft.massivecore.Engine;
import com.massivecraft.massivecore.chestgui.ChestGui;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;


public class EngineCraftingMenu extends Engine {

    private static final Map<UUID, Collection<ItemStack>> craftingPlayerCache = new HashMap<>();
    private static final EngineCraftingMenu i = new EngineCraftingMenu();

    public static EngineCraftingMenu get() {
        return i;
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onInventoryClick(InventoryClickEvent e) {
        if (!(e.getWhoClicked() instanceof Player)) return;

        Player player = (Player) e.getWhoClicked();
        Inventory openInventory = player.getOpenInventory().getTopInventory();

        if (openInventory == null)
            return;

        if(e.getClickedInventory() == null)
            return;

        if (e.getClickedInventory().equals(openInventory))
            return; // Let the ChestGUI handle

        ChestGui chestGui = ChestGui.getInventoryToGui().get(openInventory);
        if (chestGui == null)
            return;

        if (((Integer) chestGui.getMeta().getOrDefault("ingredientmenu_" + player.getUniqueId(), -1)) == -1)
            return;

        e.setCancelled(true);

        ItemStack item = e.getCurrentItem(); // Item clicked
        if (item == null)
            return;

        if(!PrisonObject.isPrisonObj(item))
            return;

        Inventory inv = chestGui.getInventory();
        ItemStack clonedCopy = item.clone();
        clonedCopy.setAmount(1);
        RemovableItem removableItem = new RemovableItem(clonedCopy);
        int slot = inv.firstEmpty();
        if(slot == -1)
            return; // The inventory is full


        inv.addItem(removableItem);
        chestGui.setAction(slot, ClickHandler.toChestAction(removableItem.getClickHandler()));
        int amt = item.getAmount();
        if(amt >= 2){
            item.setAmount(amt - 1);
        } else {
            player.getInventory().removeItem(item);
        }
        player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1, 1);
    }


    @EventHandler(priority = EventPriority.HIGH)
    public void onInventoryClose(InventoryCloseEvent e) {

        Inventory closedInventory = e.getInventory();
        if(!(e.getPlayer() instanceof Player player))
            return;

        ChestGui chestGui = ChestGui.getInventoryToGui().get(closedInventory);
        if (chestGui == null)
            return;
        if (((Integer) chestGui.getMeta().getOrDefault("ingredientmenu_" + player.getUniqueId(), -1)) == -1) {
            if (((Integer) chestGui.getMeta().getOrDefault("craftingmenu", -1)) == -1) {
                return;
            }
        } else { //We're in the ingredient menu
            ItemStack slottedItem = closedInventory.getItem(13);
            if(slottedItem == null)
                return;

            if(slottedItem.getType().equals(Material.AIR))
                return;

            ItemStack item = slottedItem.clone();
            item.setAmount(1);

            MiscUtil.givePlayerItem(player, item, slottedItem.getAmount());
            chestGui.getInventory().setItem(13, new ItemStack(Material.AIR));
        }

        if(getCraftingPlayerCache().containsKey(player.getUniqueId())){
            getCraftingPlayerCache().get(player.getUniqueId()).forEach(itemStack -> {
                ItemStack copy = itemStack.clone();
                copy.setAmount(1);
                MiscUtil.givePlayerItem(player, copy, itemStack.getAmount());
            });
        }

        removeFromCache(player);
        chestGui.remove();
    }


    public static Map<UUID, Collection<ItemStack>> getCraftingPlayerCache() {
        return craftingPlayerCache;
    }

    public static void addToCache(Player player, Collection<ItemStack> items){
        craftingPlayerCache.put(player.getUniqueId(), items);
    }

    public static void removeFromCache(Player player){
        craftingPlayerCache.remove(player.getUniqueId());
    }
}
