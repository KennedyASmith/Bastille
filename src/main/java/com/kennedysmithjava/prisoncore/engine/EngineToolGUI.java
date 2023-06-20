package com.kennedysmithjava.prisoncore.engine;

import com.kennedysmithjava.prisoncore.tools.Pickaxe;
import com.kennedysmithjava.prisoncore.tools.enchantment.Enchant;
import com.kennedysmithjava.prisoncore.util.Color;
import com.kennedysmithjava.prisoncore.util.Glow;
import com.massivecraft.massivecore.Engine;
import com.massivecraft.massivecore.chestgui.ChestAction;
import com.massivecraft.massivecore.chestgui.ChestGui;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class EngineToolGUI extends Engine {

    private static EngineToolGUI i = new EngineToolGUI();

    public static EngineToolGUI get()
    {
        return i;
    }

    private static ChestAction upgradeChestAction(Enchant<?> enchant, int currentLevel, Pickaxe pickaxeLegacy, Player player){
        return event -> {
            pickaxeLegacy.addEnchant(enchant, enchant.incrementLevel(currentLevel));
            player.sendMessage(Color.get("&aYou have upgraded your " + enchant.getDisplayName().replaceAll("%name%", enchant.getName()) + " &aenchant!"));
            ItemStack item = pickaxeLegacy.getItem();
            ItemMeta meta = item.getItemMeta();
            meta.setLore(pickaxeLegacy.getType().getLore(pickaxeLegacy.getEnchants(), pickaxeLegacy.getLeveledAbility(), pickaxeLegacy.getDurability(), pickaxeLegacy.getMaxDurability()));
            item.setItemMeta(meta);
            player.setItemInHand(item);
            player.closeInventory();
            return false;
        };
    }

    private static ChestAction buyChestAction(Enchant<?> enchant, Pickaxe pickaxeLegacy, Player player, ChestGui gui){
        return inventoryClickEvent -> {
            pickaxeLegacy.addEnchant(enchant, 1);

            ItemStack item = pickaxeLegacy.getItem();
            ItemMeta meta = item.getItemMeta();
            meta.setLore(pickaxeLegacy.getType().getLore(pickaxeLegacy.getEnchants(), pickaxeLegacy.getLeveledAbility(), pickaxeLegacy.getDurability(), pickaxeLegacy.getMaxDurability()));
            item.setItemMeta(meta);

            player.setItemInHand(item);
            player.sendMessage(Color.get("&aYou purchased the " + enchant.getDisplayName().replaceAll("%name%", enchant.getName()) + " &aenchant!"));

            player.closeInventory();
            return false;
        };
    }

    private static ItemStack getItem(String name, Material material, List<String> lore, boolean glow){
        ItemStack item = new ItemStack(material, 1);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(Color.get((name)));
        List<String> newLore = new ArrayList<>();
        lore.forEach(s -> newLore.add(Color.get(s)));
        meta.setLore(newLore);
        if(glow){ meta.addEnchant(Glow.getGlow(), 1, true); }
        item.setItemMeta(meta);
        return item;
    }

    private String color(String color) {
        return ChatColor.translateAlternateColorCodes('&', color);
    }

}
