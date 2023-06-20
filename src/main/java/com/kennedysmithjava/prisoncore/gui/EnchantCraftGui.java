package com.kennedysmithjava.prisoncore.gui;

import com.kennedysmithjava.prisoncore.crafting.Recipe;
import com.kennedysmithjava.prisoncore.eco.Cost;
import com.kennedysmithjava.prisoncore.tools.enchantment.Enchant;
import com.kennedysmithjava.prisoncore.util.ItemBuilder;
import com.massivecraft.massivecore.chestgui.ChestGui;
import com.massivecraft.massivecore.util.MUtil;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EnchantCraftGui extends BaseGui{
    public EnchantCraftGui(Player player, BaseGui returnMenu) {
        super(player, "&8Conjure Enchants", 6, false, true, returnMenu);

    }

    @Override
    public void onBuild(Player player, ChestGui gui, Inventory inventory) {
        blockFill(Material.WHITE_STAINED_GLASS_PANE);
        setItem(13, Material.WRITABLE_BOOK, "&d&lHelp",
                MUtil.list("&r",
                        "&7Here you can conjure new enchants for your tools!",
                        "&r",
                        "&7Most enchants cost &fCrafting Materials",
                        "&7and some variety of &dElemental Essence.",
                        "&r",
                        "&dEssence &7can be obtained from:",
                        "&7- Farming & Woodcutting (&aEarth Essence&7)",
                        "&7- Fishing (&bWater Essence&7)",
                        "&7- Forging (&cFire Essence&7)",
                        "&7- Questing (&fAir Essence&7)",
                        "&7- Killing Mobs (&dAll Types&7)"
                ), true);

        for (Enchant<?> enchant : Enchant.getActiveEnchants().values()) {
            ItemBuilder itemBuilder = new ItemBuilder(enchant.getIcon()).name("&d&l" + enchant.getName());
            List<String> lore = new ArrayList<>(enchant.getUnformattedGUILore());
            Recipe recipe = enchant.getEnchantBookRecipe();
            HashMap<String, Integer> ingredientNames = new HashMap<>();
            recipe.getIngredients().values().forEach(obj ->{
                int currentVal = ingredientNames.getOrDefault(obj.getName(), 0);
                ingredientNames.put(obj.getName(), currentVal + 1);
            });
            lore.add(" &r");
            lore.add("&7&lREQUIREMENTS");
            for (Map.Entry<String, Integer> entry : ingredientNames.entrySet()) {
                lore.add("&7- " + entry.getValue() + "&7x " + entry.getKey());
            }
            for (Cost cost : recipe.getAdditionalCosts()) {
                lore.add("&7- " + cost.getPriceline());
            }

            lore.add(" &r");
            lore.add("&7Enchant Max Level: " + enchant.getMaxLevel());

            itemBuilder.lore(lore);

            setAction(enchant.getEnchantGUISlot(), inventoryClickEvent -> {
                CraftingMenuGui craftingMenuGui = new CraftingMenuGui(player, "&d&lConjuring Enchantment", new HashMap<>(), recipe, this);
                close();
                craftingMenuGui.open();
                return false;
            });
            setItem(enchant.getEnchantGUISlot(), itemBuilder.build());
        }
    }
}
