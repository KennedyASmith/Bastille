package com.kennedysmithjava.prisoncore.gui;

import com.kennedysmithjava.prisoncore.crafting.objects.PrisonEnchantBook;
import com.kennedysmithjava.prisoncore.tools.Pickaxe;
import com.kennedysmithjava.prisoncore.util.ItemBuilder;
import com.massivecraft.massivecore.chestgui.ChestGui;
import com.massivecraft.massivecore.util.MUtil;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EnchantBookSelectGui extends BaseGui {

    Map<Integer, PrisonEnchantBook> selectedEnchants;
    int pickaxeSlot;
    Pickaxe pickaxe;
    public EnchantBookSelectGui(Player player, Pickaxe pickaxe, BaseGui returnMenu) {
        super(player, "&8Select Enchant Book(s)", 6, false, false, returnMenu);
        this.pickaxe = pickaxe;
        this.selectedEnchants = new HashMap<>();
    }

    @Override
    public void onBuild(Player player, ChestGui gui, Inventory inventory) {
        blockFill(Material.WHITE_STAINED_GLASS_PANE, 45, 53);

        setItem(48, Material.RED_WOOL, "&cGo Back", new ArrayList<>(), false);
        setAction(48, inventoryClickEvent -> {
            returnToLastMenu();
            return false;
        });

        Map<Integer, ItemStack> originalBookItems = new HashMap<>();
        Inventory playerInventory = player.getInventory();
        for (ItemStack item : playerInventory) {
            if(item == null) continue;
            if (!PrisonEnchantBook.isEnchantBook(item)) continue;
            int slot = addItem(item);
            if(slot == -1) continue;
            originalBookItems.put(slot, item);
            PrisonEnchantBook enchantBook = PrisonEnchantBook.getFrom(item.clone());
            ItemMeta meta = item.getItemMeta();
            List<String> lore = meta.getLore();
            ItemBuilder clone = new ItemBuilder(Material.BOOK)
                            .lore(meta.getLore())
                            .name(meta.getDisplayName())
                            .clearEnchantments();
            if(selectedEnchants.containsKey(slot)){
                setAction(slot, inventoryClickEvent -> {
                    selectedEnchants.remove(slot);
                    close();
                    open();
                    return false;
                });
                clone.addGlow();
                lore.add(0, "&7[&aSELECTED&7]");
            }else {
                setAction(slot, inventoryClickEvent -> {
                    selectedEnchants.put(slot, enchantBook);
                    close();
                    open();
                    return false;
                });
                lore.add(0, "&7[UNSELECTED]");
            }
            clone.lore(lore);
            setItem(slot, clone.build());
        }

        List<String> confirmLore;
        Material confirmMaterial = Material.GREEN_WOOL;
        if(selectedEnchants.size() > 0){
            confirmLore = MUtil.list("&e" + selectedEnchants.size() + " &dEnchants &7selected.");
        }else {
            confirmLore = MUtil.list("&cPlease select at least &e1 &dEnchant Book&c to continue..");
            confirmMaterial = Material.ORANGE_WOOL;
        }
        setItem(50, confirmMaterial, "&aContinue", confirmLore, false);
        setAction(50, inventoryClickEvent -> {
            if(selectedEnchants.size() < 1) return false;
            EnchantApplyGui applyGui = new EnchantApplyGui(player, pickaxe, selectedEnchants, originalBookItems, this);
            close();
            applyGui.open();
            return false;
        });
    }
}
