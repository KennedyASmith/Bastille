package com.kennedysmithjava.prisoncore.crafting;

import com.kennedysmithjava.prisoncore.crafting.objects.*;
import com.kennedysmithjava.prisoncore.crafting.objects.type.*;
import com.kennedysmithjava.prisoncore.engine.EngineBlacksmith;
import com.kennedysmithjava.prisoncore.engine.EngineCraftingMenu;
import com.kennedysmithjava.prisoncore.util.*;
import com.massivecraft.massivecore.chestgui.ChestGui;
import com.massivecraft.massivecore.util.MUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Supplier;

public enum Recipe {

    PICKAXE(MUtil.map(
            12, new PrisonMetal(MetalType.ANY),
            13, new PrisonMetal(MetalType.ANY),
            14, new PrisonMetal(MetalType.ANY),
            20, new PrisonMetal(MetalType.ANY),
            24, new PrisonMetal(MetalType.ANY),
            22, new PrisonStick(StickType.WOOD),
            31, new PrisonStick(StickType.WOOD),
            40, new PrisonStick(StickType.WOOD)), () -> new ProductItem(EngineBlacksmith::beginCrafting)),

    STICK(MUtil.map(22, new PrisonWoodenPlank()), () -> giveItem(new PrisonStick(StickType.WOOD), 1)),
    WOODEN_PLANK_4X(MUtil.map(22, new PrisonLog(LogType.ANY)), () -> giveItem(new PrisonWoodenPlank(), 4)),
    WOODEN_AXE(MUtil.map(
            12, new PrisonWoodenPlank(),
            13, new PrisonWoodenPlank(),
            21, new PrisonWoodenPlank(),
            22, new PrisonStick(StickType.WOOD),
            31, new PrisonStick(StickType.WOOD),
            40, new PrisonStick(StickType.WOOD)
    ), () -> new ProductItem(craftingRequest -> craftingRequest.getPlayer().getInventory().addItem(new PrisonWoodenAxe().give(1)))),
    WOODEN_SCYTHE(MUtil.map(
            12, new PrisonWoodenPlank(),
            13, new PrisonWoodenPlank(),
            21, new PrisonWoodenPlank(),
            22, new PrisonStick(StickType.WOOD),
            31, new PrisonStick(StickType.WOOD),
            40, new PrisonStick(StickType.WOOD)
    ), () -> new ProductItem(craftingRequest -> craftingRequest.getPlayer().getInventory().addItem(new PrisonWoodenScythe().give(1)))),
    FISHING_ROD(MUtil.map(
            30, new PrisonStick(StickType.WOOD),
            22, new PrisonStick(StickType.WOOD),
            14, new PrisonStick(StickType.WOOD),
            38, new PrisonWoodenPlank()
    ), () -> new ProductItem(craftingRequest -> craftingRequest.getPlayer().getInventory().addItem(new PrisonWoodenScythe().give(1)))),
    SAWDUST(MUtil.map(22, new PrisonLog(LogType.ANY)), () -> giveItem(new PrisonSawdust(), 3)),
    BOWL(MUtil.map(
            21, new PrisonWoodenPlank(),
            23, new PrisonWoodenPlank(),
            31, new PrisonWoodenPlank()
    ), () -> giveItem(new PrisonBowl(), 1)),
    ;

    //Key: GUI Slot ||| Value: Ingredient for that slot
    private Map<Integer, PrisonObject> ingredients;

    private Supplier<ProductItem> product;

    Recipe(Map<Integer, PrisonObject> ingredients, Supplier<ProductItem> product) {
        this.ingredients = ingredients;
        this.product = product;

    }

    private static ProductItem giveItem(PrisonObject obj, int amount){
        return new ProductItem(craftingRequest -> craftingRequest.getPlayer().getInventory().addItem(obj.give(amount)));
    }
    public Map<Integer, PrisonObject> getIngredients() {
        return ingredients;
    }

    public ProductItem getProduct() {
        return product.get();
    }
    public static ChestGui getCraftingMenu(ChestGui returningMenu, Recipe recipe, String menuName) {
        return getCraftingMenu(returningMenu, recipe, menuName, new HashMap<>());
    }

    public static ChestGui getCraftingMenu(ChestGui returningMenu, Recipe recipe, String menuName, Map<Integer, ItemStack> givenIngredients) {
        Inventory inventory = Bukkit.createInventory(null, 54, Color.get(menuName));
        Map<Integer, PrisonObject> ingredients = recipe.getIngredients();
        ChestGui chestGui = ChestGui.getCreative(inventory);
        chestGui.setAutoclosing(false);
        chestGui.setAutoremoving(false);
        chestGui.setSoundOpen(null);
        chestGui.setSoundClose(null);
        chestGui.getMeta().put("craftingmenu", 1);
        blockFill(inventory, Material.BLACK_STAINED_GLASS_PANE);

        List<PrisonObject> ingredientsNotMet = new ArrayList<>();

        for (Integer s : ingredients.keySet()) {
            if (!givenIngredients.containsKey(s)) {
                ingredientsNotMet.add(ingredients.get(s));
            }
        }

        boolean allIngredientsEntered = (ingredientsNotMet.size() == 0);

        if(allIngredientsEntered){
            blockFillCenter(inventory, Material.LIME_STAINED_GLASS_PANE);
        }else {
            blockFillCenter(inventory, Material.RED_STAINED_GLASS_PANE);

        }

        // ** Logic for ConfirmItem ** //
        ItemStack confirmItem = new ItemBuilder(Material.LIME_CONCRETE).name("&a&lFinish Crafting").lore(" &r").build();

        if(allIngredientsEntered){
            inventory.setItem(50, new ItemBuilder(confirmItem).clearLore().lore(MUtil.list("&7Ready to craft!")).build());
            chestGui.setAction(50, inventoryClickEvent -> {
                Player player = (Player) inventoryClickEvent.getWhoClicked();
                givenIngredients.forEach((slot, itemStack) -> {
                    inventory.setItem(slot, new ItemStack(Material.AIR));
                });
                EngineCraftingMenu.removeFromCache(player);
                player.closeInventory();
                if(returningMenu != null){
                    inventoryClickEvent.getWhoClicked().openInventory(returningMenu.getInventory());
                }
                chestGui.remove();
                recipe.getProduct().get(new CraftingRequest(player, givenIngredients));
                return false;
            });
        }else {
            ItemStack unreadyItem = new ItemBuilder(Material.ORANGE_CONCRETE).name("&6&lNot ready to craft").build();
            inventory.setItem(50, unreadyItem);
            Set<String> loreSet = new HashSet<>();
            ingredientsNotMet.forEach(prisonObject ->
                    loreSet.add("&7- " + ChatColor.stripColor(Color.get(prisonObject.getName()))));
            List<String> lore = new ArrayList<>(loreSet);
            lore.add(0, "&7Ingredient requirement(s) not met:");
            inventory.setItem(50, new ItemBuilder(unreadyItem).clearLore().lore(lore).build());
        }

        // ** Logic for CancelItem ** //
        ItemStack cancelItem = new ItemBuilder(Material.RED_CONCRETE).name("&c&lCancel").lore("&7Return all items").build();
        inventory.setItem(48, cancelItem);
        chestGui.setAction(48, inventoryClickEvent -> {
            inventoryClickEvent.getWhoClicked().closeInventory();
            if(returningMenu != null){
                inventoryClickEvent.getWhoClicked().openInventory(returningMenu.getInventory());
            }
            return false;
        });

        ingredients.forEach((slot, ingredient) -> {
            ItemStack slotted = new ItemStack(Material.AIR);
            if(!givenIngredients.containsKey(slot)){ //If this ingredient isn't already provided
                ItemStack ingredientItem = new ItemBuilder(Material.WHITE_STAINED_GLASS_PANE)
                        .lore(MUtil.list(" &r", "&7This slot accepts: ", "&7- &c" + ChatColor.stripColor(Color.get(ingredient.getName())), " &r", "&eLeft-Click &7to select an item", "&7to place in this slot."))
                        .name("&f&lEmpty Slot")
                        .build();
                inventory.setItem(slot, ingredientItem);
            } else {
                inventory.setItem(slot, givenIngredients.get(slot));
                slotted = givenIngredients.get(slot);
            }

            ItemStack finalSlotted = slotted;
            chestGui.setAction(slot, inventoryClickEvent -> {
                Player player = (Player) inventoryClickEvent.getWhoClicked();
                openIngredientDepositMenu(returningMenu, player, recipe, givenIngredients, ingredient, slot, menuName, finalSlotted);
                return false;
            });
        });

        return chestGui;
    }

    private static void openIngredientDepositMenu(ChestGui returningMenu, Player player,
                                                  Recipe recipe,
                                                  Map<Integer, ItemStack> givenIngredients,
                                                  PrisonObject requiredIngredient,
                                                  Integer reqIngredientSlot,
                                                  String menuName){
        openIngredientDepositMenu(returningMenu, player, recipe, givenIngredients,
                requiredIngredient, reqIngredientSlot, menuName, new ItemStack(Material.AIR));
    }

    private static void openIngredientDepositMenu(ChestGui returningMenu, Player player,
                                                  Recipe recipe,
                                                  Map<Integer, ItemStack> givenIngredients,
                                                  PrisonObject requiredIngredient,
                                                  Integer reqIngredientSlot,
                                                  String menuName,
                                                  ItemStack slotted){
        Inventory inventory = Bukkit.createInventory(null, 27, Color.get("Add Ingredient: &c" + ChatColor.stripColor(Color.get(requiredIngredient.getName()))));
        ChestGui chestGui = ChestGui.getCreative(inventory);
        chestGui.getMeta().put("ingredientmenu_" + player.getUniqueId(), 1);
        chestGui.setBottomInventoryAllow(true);
        chestGui.setAutoremoving(false);
        chestGui.setAutoclosing(false);
        chestGui.setSoundOpen(null);
        chestGui.setSoundClose(null);
        blockFill(inventory, Material.BLACK_STAINED_GLASS_PANE);
        if(slotted.getType() == Material.AIR) {
            inventory.setItem(13, new ItemStack(Material.AIR));
        } else {
            RemovableItem removableItem = new RemovableItem(slotted);
            inventory.setItem(13, removableItem);
            givenIngredients.remove(reqIngredientSlot); //Remove this item from the cache, so that it doesn't get duplicated if the menu closes.
            EngineCraftingMenu.addToCache(player, givenIngredients.values());
            chestGui.setAction(13, ClickHandler.toChestAction(removableItem.getClickHandler()));
        }

        ItemStack confirmItem = new ItemBuilder(Material.LIME_CONCRETE).name("&a&lSelect").lore(" &r").build();
        ItemStack cancelItem = new ItemBuilder(Material.RED_CONCRETE).name("&c&lCancel").lore(" &r").build();

        inventory.setItem(23, confirmItem);
        inventory.setItem(21, cancelItem);

        chestGui.setAction(23, inventoryClickEvent -> {
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
                ChestGui newInv = getCraftingMenu(returningMenu, recipe, menuName, givenIngredients);
                inventory.setItem(13, new ItemStack(Material.AIR));
                EngineCraftingMenu.removeFromCache(player);
                player.openInventory(newInv.getInventory());
                EngineCraftingMenu.addToCache(player, givenIngredients.values());
            }
            return false;
        });

        EngineCraftingMenu.removeFromCache(player);
        player.openInventory(chestGui.getInventory());
        EngineCraftingMenu.addToCache(player, givenIngredients.values());
    }


    private static void blockFillCenter(Inventory inv, Material material){
        blockFill(10, 17, inv, material);
        blockFill(19, 26, inv, material);
        blockFill(28, 35, inv, material);
        blockFill(37, 44, inv, material);
    }
    private static void blockFill(Inventory inv, Material material){
        blockFill(0, inv.getSize(), inv, material);
    }
    private static void blockFill(int start, int finish, Inventory inv, Material material){
        for (int b = start; b < finish; b++) {
            ItemStack p = new ItemStack(material, 1);
            ItemMeta itemMeta = p.getItemMeta();
            itemMeta.setDisplayName(" ");
            p.setItemMeta(itemMeta);
            inv.setItem(b, p);
        }
    }

}


class ProductItem {
    Consumer<CraftingRequest> productConsumer;
     ProductItem(Consumer<CraftingRequest> productConsumer){
        this.productConsumer = productConsumer;
    }
    public void get(CraftingRequest craftingRequest){
         productConsumer.accept(craftingRequest);
    }
}

