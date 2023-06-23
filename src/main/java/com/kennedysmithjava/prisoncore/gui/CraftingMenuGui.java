package com.kennedysmithjava.prisoncore.gui;

import com.kennedysmithjava.prisoncore.crafting.CraftingRequest;
import com.kennedysmithjava.prisoncore.crafting.PrisonObject;
import com.kennedysmithjava.prisoncore.crafting.ProductItem;
import com.kennedysmithjava.prisoncore.crafting.Recipe;
import com.kennedysmithjava.prisoncore.eco.Cost;
import com.kennedysmithjava.prisoncore.engine.EngineCooldown;
import com.kennedysmithjava.prisoncore.engine.EngineCraftingMenu;
import com.kennedysmithjava.prisoncore.entity.player.MPlayer;
import com.kennedysmithjava.prisoncore.util.Color;
import com.kennedysmithjava.prisoncore.util.CooldownReason;
import com.kennedysmithjava.prisoncore.util.ItemBuilder;
import com.kennedysmithjava.prisoncore.util.MiscUtil;
import com.massivecraft.massivecore.chestgui.ChestGui;
import com.massivecraft.massivecore.util.InventoryUtil;
import com.massivecraft.massivecore.util.MUtil;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class CraftingMenuGui extends BaseGui{
    private final Map<Integer, ItemStack> givenIngredients;

    private final Map<Integer, PrisonObject> neededIngredients;
    private final ProductItem product;

    private final List<Cost> additionalCosts;

    public CraftingMenuGui(Player player, String name, Map<Integer, ItemStack> givenIngredients, Recipe recipe, BaseGui returningMenu) {
        super(player, name, 6, false, false, returningMenu);
        this.givenIngredients = givenIngredients;
        this.additionalCosts = recipe.getAdditionalCosts();
        this.product = recipe.getProduct();
        this.neededIngredients = recipe.getIngredients();
    }

    public CraftingMenuGui(Player player, String name, Map<Integer, ItemStack> givenIngredients, Map<Integer, PrisonObject> neededIngredients, ProductItem product, List<Cost> additionalCosts,  BaseGui returningMenu) {
        super(player, name, 6, false, false, returningMenu);
        this.givenIngredients = givenIngredients;
        this.additionalCosts = additionalCosts;
        this.product = product;
        this.neededIngredients = neededIngredients;
    }

    @Override
    public void onBuild(Player player, ChestGui gui, Inventory inventory) {
        gui.getMeta().put("craftingmenu", 1);
        blockFill(Material.BLACK_STAINED_GLASS_PANE);

        List<PrisonObject> ingredientsNotMet = new ArrayList<>();

        for (Integer s : neededIngredients.keySet()) {
            if (!givenIngredients.containsKey(s)) {
                ingredientsNotMet.add(neededIngredients.get(s));
            }
        }

        boolean allIngredientsEntered = (ingredientsNotMet.size() == 0);

        List<Cost> costsNotMet = new ArrayList<>();
        MPlayer mPlayer = MPlayer.get(player);
        for (Cost additionalCost : additionalCosts) {
            if(!additionalCost.hasCost(mPlayer)) {
                costsNotMet.add(additionalCost);
            }
        }

        if(allIngredientsEntered && costsNotMet.size() < 1){
            blockFillCenter(Material.LIME_STAINED_GLASS_PANE);
        }else {
            blockFillCenter(Material.RED_STAINED_GLASS_PANE);

        }
        // ** Logic for ConfirmItem ** //
        ItemStack confirmItem = new ItemBuilder(Material.LIME_CONCRETE).name("&a&lFinish Crafting").lore(" &r").build();

        if(allIngredientsEntered && costsNotMet.size() < 1){
            List<String> lore = MUtil.list("&7Ready to craft!");
            if(!additionalCosts.isEmpty()){
                lore.add(" &r");
                lore.add("&e&lCOST");
                for (Cost additionalCost : additionalCosts) {
                    lore.add(additionalCost.getPriceline());
                }
            }
            setItem(50, new ItemBuilder(confirmItem).clearLore().lore(lore).build());
            setAction(50, inventoryClickEvent -> {
                givenIngredients.forEach((slot, itemStack) -> {
                    inventory.setItem(slot, new ItemStack(Material.AIR));
                });
                EngineCraftingMenu.removeFromCache(player);
                player.closeInventory();
                additionalCosts.forEach(additionalCost -> additionalCost.transaction(mPlayer));
                if(canReturnToLastMenu()) returnToLastMenu();
                product.get(new CraftingRequest(player, givenIngredients, product, additionalCosts));
                return false;
            });
        }else {
            ItemStack unreadyItem = new ItemBuilder(Material.ORANGE_CONCRETE).name("&6&lNot ready to craft").build();
            setItem(50, unreadyItem);
            Set<String> loreSet = new HashSet<>();
            ingredientsNotMet.forEach(prisonObject ->
                    loreSet.add("&7- " + ChatColor.stripColor(Color.get(prisonObject.getName()))));
            List<String> lore = new ArrayList<>(loreSet);
            lore.add(0, "&7Ingredient requirement(s) not met:");
            if(!costsNotMet.isEmpty()){
                lore.add(" &r");
                lore.add("&7Cost requirement(s) not met:");
                for (Cost additionalCost : costsNotMet) {
                    lore.add(additionalCost.getInsufficientLine(mPlayer));
                }
            }

            setItem(50, new ItemBuilder(unreadyItem).clearLore().lore(lore).build());
        }

        // ** Logic for CancelItem ** //
        ItemStack cancelItem = new ItemBuilder(Material.RED_CONCRETE).name("&c&lCancel").lore("&7Return all items").build();
        inventory.setItem(48, cancelItem);
        setAction(48, inventoryClickEvent -> {
            if(canReturnToLastMenu()) returnToLastMenu();
            givenIngredients.forEach((integer, itemStack) -> MiscUtil.givePlayerItem(player, itemStack, itemStack.getAmount()));
            return false;
        });

        /* Used for auto-add button */
        Map<Integer, PrisonObject> unslottedIngredients = new HashMap<>();

        neededIngredients.forEach((slot, ingredient) -> {
            ItemStack slotted = new ItemStack(Material.AIR);
            if(!givenIngredients.containsKey(slot)){ //If this ingredient isn't already provided
                ItemStack ingredientItem = new ItemBuilder(Material.WHITE_STAINED_GLASS_PANE)
                        .lore(MUtil.list(" &r", "&7This slot accepts: ", "&7- &c" + ChatColor.stripColor(Color.get(ingredient.getName())), " &r", "&eLeft-Click &7to select an item", "&7to place in this slot."))
                        .name("&f&lEmpty Slot")
                        .build();
                inventory.setItem(slot, ingredientItem);
                unslottedIngredients.put(slot, ingredient);
            } else {
                inventory.setItem(slot, givenIngredients.get(slot));
                slotted = givenIngredients.get(slot);
            }

            ItemStack finalSlottedItem = slotted;
            setAction(slot, inventoryClickEvent -> {
                CraftingDepositGui depositGui = new CraftingDepositGui(player, slot, givenIngredients, neededIngredients, finalSlottedItem, ingredient, product, additionalCosts, this, this.getReturnMenu());
                depositGui.open();
                return false;
            });

        });

        // ** Logic for Autoadd item ** //
        ItemStack autoItem = new ItemBuilder(Material.HOPPER).name("&6&lAuto Add").lore("&7Add all recipe ingredients.").build();
        inventory.setItem(49, autoItem);
        setAction(49, inventoryClickEvent -> {
            if(!EngineCooldown.inCooldown(player, CooldownReason.GUI_ACTION)){
                EngineCooldown.add(player, 40, CooldownReason.GUI_ACTION);
                getAllHadIngredient(player);
                close();
                open();
            }
            return false;
        });
    }

    private void blockFillCenter(Material material){
        blockFill(material, 10, 16);
        blockFill(material, 19, 25);
        blockFill(material, 28, 34);
        blockFill(material, 37, 43);
    }

    public void getAllHadIngredient(Player player){

        ItemStack[] itemStacks = InventoryUtil.getContentsAll(player.getInventory());

        for (ItemStack item : itemStacks) {
            if(item == null) continue;
            if(!PrisonObject.isPrisonObj(item)) continue;
            for (Map.Entry<Integer, PrisonObject> ie : neededIngredients.entrySet()) {
                if(givenIngredients.containsKey(ie.getKey())) continue;
                if(!ie.getValue().is(item)) continue;
                int amt = item.getAmount();
                if(amt >= 2){
                    item.setAmount(amt - 1);
                } else {
                    player.getInventory().removeItem(item);
                }
                givenIngredients.put(ie.getKey(), new ItemBuilder(item.clone()).amount(1).build());
            }
        }
    }
}
