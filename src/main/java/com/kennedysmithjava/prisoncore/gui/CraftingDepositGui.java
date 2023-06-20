package com.kennedysmithjava.prisoncore.gui;

import com.kennedysmithjava.prisoncore.crafting.PrisonObject;
import com.kennedysmithjava.prisoncore.crafting.Recipe;
import com.kennedysmithjava.prisoncore.engine.EngineCraftingMenu;
import com.kennedysmithjava.prisoncore.util.ClickHandler;
import com.kennedysmithjava.prisoncore.util.Color;
import com.kennedysmithjava.prisoncore.util.ItemBuilder;
import com.kennedysmithjava.prisoncore.util.RemovableItem;
import com.massivecraft.massivecore.chestgui.ChestGui;
import com.massivecraft.massivecore.util.MUtil;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public class CraftingDepositGui extends BaseGui{
    Map<Integer, ItemStack> givenIngredients;
    Recipe recipe;
    ItemStack slotted;
    int reqIngredientSlot;
    PrisonObject requiredIngredient;
    BaseGui homeMenu;
    BaseGui craftingMenu;

    public CraftingDepositGui(Player player,
                              Map<Integer, ItemStack> givenIngredients, Recipe recipe,
                              Integer reqIngredientSlot, ItemStack slotted, PrisonObject requiredIngredient, BaseGui craftingMenu, BaseGui homeMenu) {
        super(player, "Add Ingredient: &c" + ChatColor.stripColor(Color.get(requiredIngredient.getName())), 3, false, false, craftingMenu);
        this.givenIngredients = givenIngredients;
        this.recipe = recipe;
        this.reqIngredientSlot = reqIngredientSlot;
        this.slotted = slotted;
        this.requiredIngredient = requiredIngredient;
        this.homeMenu = homeMenu;
        this.craftingMenu = craftingMenu;
    }

    @Override
    public void onBuild(Player player, ChestGui gui, Inventory inventory) {
        gui.getMeta().put("ingredientmenu_" + player.getUniqueId(), 1);
        blockFill(Material.BLACK_STAINED_GLASS_PANE);
        if(slotted.getType() == Material.AIR) {
            inventory.setItem(13, new ItemStack(Material.AIR));
        } else {
            RemovableItem removableItem = new RemovableItem(slotted);
            inventory.setItem(13, removableItem);
            givenIngredients.remove(reqIngredientSlot); //Remove this item from the cache, so that it doesn't get duplicated if the menu closes.
            EngineCraftingMenu.addToCache(player, givenIngredients.values());
            setAction(13, ClickHandler.toChestAction(removableItem.getClickHandler()));
        }

        ItemStack confirmItem = new ItemBuilder(Material.LIME_CONCRETE).name("&a&lSelect").lore(" &r").build();
        ItemStack cancelItem = new ItemBuilder(Material.RED_CONCRETE).name("&c&lCancel").lore(" &r").build();

        inventory.setItem(23, confirmItem);
        inventory.setItem(21, cancelItem);

        setAction(23, inventoryClickEvent -> {
            ItemStack slotted1 = inventory.getItem(13);
            if(slotted1 == null) {
                inventory.setItem(23, new
                        ItemBuilder(confirmItem).clearLore().lore("&cPlease select an item first.").build());
                return false;
            }
            if(!requiredIngredient.is(slotted1)){
                inventory.setItem(23, new
                        ItemBuilder(confirmItem).clearLore().lore(
                                MUtil.list(
                                        "&cPlease select the ingredient matching: ",
                                        "&7- " + ChatColor.stripColor(Color.get(requiredIngredient.getName()))))
                        .build());
            }else {
                givenIngredients.put(reqIngredientSlot, slotted1);
                CraftingMenuGui craftingMenuGui = new CraftingMenuGui(player, craftingMenu.getName(), givenIngredients, recipe, homeMenu);
                close();
                craftingMenuGui.open();
                EngineCraftingMenu.removeFromCache(player);
                EngineCraftingMenu.addToCache(player, givenIngredients.values());
            }
            return false;
        });

        EngineCraftingMenu.removeFromCache(player);
        EngineCraftingMenu.addToCache(player, givenIngredients.values());
    }
}
