package com.kennedysmithjava.prisoncore.gui;

import com.kennedysmithjava.prisoncore.tools.Pickaxe;
import com.kennedysmithjava.prisoncore.util.ItemBuilder;
import com.massivecraft.massivecore.chestgui.ChestGui;
import com.massivecraft.massivecore.util.MUtil;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EnchantToolSelectGui extends BaseGui {

    int selectedGUISlot;
    int selectedPickaxeSlot;
    ItemStack selectedItem;
    public EnchantToolSelectGui(Player player, BaseGui returnMenu) {
        super(player, "&8Choose Enchantable Item", 6, false, false, returnMenu);
        selectedGUISlot = -1;
        selectedPickaxeSlot = -1;
    }

    @Override
    public void onBuild(Player player, ChestGui gui, Inventory inventory) {
        blockFill(Material.WHITE_STAINED_GLASS_PANE, 45, 53);

        setItem(48, Material.RED_WOOL, "&cGo Back", new ArrayList<>(), false);
        setAction(48, inventoryClickEvent -> {
            returnToLastMenu();
            return false;
        });

        Inventory playerInventory = player.getInventory();
        Map<Integer, ItemStack> foundTools = new HashMap<>();
        Map<Integer, Integer> slotMapping = new HashMap<>();
        for (ItemStack item : playerInventory) {
            if (!Pickaxe.isPickaxe(item)) continue;
            int slot = inventory.firstEmpty();
            if(slot == -1) continue;
            ItemBuilder itemBuilder = new ItemBuilder(item.clone());
            List<String> lore = item.getItemMeta().getLore();
            int playerInvSlot = playerInventory.first(item);

            if(selectedGUISlot == slot){
                setAction(slot, inventoryClickEvent -> {
                    selectedItem = null;
                    selectedGUISlot = -1;
                    selectedPickaxeSlot = -1;
                    close();
                    open();
                    return false;
                });
                lore.add(0, "&7[&aSELECTED&7]");
                itemBuilder.addGlow();
            }else{
                setAction(slot, inventoryClickEvent -> {
                    selectedItem = foundTools.get(playerInvSlot);
                    selectedGUISlot = slot;
                    selectedPickaxeSlot = playerInvSlot;
                    close();
                    open();
                    return false;
                });
                lore.add(0, "&7[UNSELECTED]");
            }
            setItem(slot,itemBuilder.lore(lore).build());
            foundTools.put(playerInvSlot, item);
            slotMapping.put(slot, playerInvSlot);
        }

        /* Auto select the first available tool for convenience */
        if(foundTools.size() > 0 && selectedItem == null){
            ItemStack firstItem = getInventory().getItem(0);
            if(firstItem == null) return;
            List<String> lore = firstItem.getItemMeta().getLore();
            selectedGUISlot = 0;
            selectedPickaxeSlot = slotMapping.get(0);
            selectedItem = foundTools.get(selectedPickaxeSlot);
            lore.set(0, "&7[&aSELECTED&7]");
            setItem(selectedGUISlot, new ItemBuilder(firstItem).lore(lore).addGlow().build());
            setAction(selectedGUISlot, inventoryClickEvent -> {
                selectedItem = null;
                selectedGUISlot = -1;
                selectedPickaxeSlot = -1;
                close();
                open();
                return false;
            });
        }

        List<String> confirmLore;
        Material confirmMaterial = Material.GREEN_WOOL;
        if(selectedGUISlot != -1){
            confirmLore = MUtil.list("&e" + selectedItem.getItemMeta().getDisplayName() + " &7selected.");
        }else {
            confirmLore = MUtil.list("&cPlease select at least &e1 &6Pickaxe&c to continue.");
            confirmMaterial = Material.ORANGE_WOOL;
        }
        setItem(50, confirmMaterial, "&aConfirm Selection", confirmLore, false);
        setAction(50, inventoryClickEvent -> {
            if(selectedItem == null) return false;
            if(Pickaxe.isPickaxe(selectedItem)){
                EnchantBookSelectGui bookSelectGui = new EnchantBookSelectGui(player, Pickaxe.get(selectedItem), this);
                close();
                bookSelectGui.open();
            }
            return false;
        });
    }
}
