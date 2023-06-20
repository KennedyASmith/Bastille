package com.kennedysmithjava.prisoncore.gui;

import com.massivecraft.massivecore.chestgui.ChestGui;
import com.massivecraft.massivecore.util.MUtil;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.List;

public class EnchantMenuGui extends BaseGui {
    public EnchantMenuGui(Player player) {
        super(player, "&8Choose Option", 3, false, true);
    }


    @Override
    public void onBuild(Player player, ChestGui gui, Inventory inventory) {
        blockFill(Material.WHITE_STAINED_GLASS_PANE);
        String cName = "&d&lCreate Enchants";
        List<String> cLore = MUtil.list("&7Crafting Menu", "", "&7Produce new enchants!");
        setItem(12, Material.LECTERN, cName, cLore, false);
        setAction(12, inventoryClickEvent -> {
            EnchantCraftGui enchantCraftGui = new EnchantCraftGui(player, this);
            close();
            enchantCraftGui.open();
            return false;
        });

        String aName = "&d&lApply Enchants";
        List<String> aLore = MUtil.list("&7Tool Edit Menu", "", "&7Apply enchants to your tool!");
        setItem(14, Material.ENCHANTING_TABLE, aName, aLore, false);
        setAction(14, inventoryClickEvent -> {
            EnchantToolSelectGui enchantToolSelectGui = new EnchantToolSelectGui(player, this);
            close();
            enchantToolSelectGui.open();
            return false;
        });
    }
}
