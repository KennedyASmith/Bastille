package com.kennedysmithjava.prisoncore.npc;

import com.kennedysmithjava.prisoncore.crafting.Recipe;
import com.kennedysmithjava.prisoncore.entity.player.MPlayer;
import com.kennedysmithjava.prisoncore.entity.player.MPlayerColl;
import com.kennedysmithjava.prisoncore.gui.CraftingMenuGui;
import com.kennedysmithjava.prisoncore.util.Color;
import com.kennedysmithjava.prisoncore.util.ItemBuilder;
import com.massivecraft.massivecore.chestgui.ChestGui;
import com.massivecraft.massivecore.util.MUtil;
import net.citizensnpcs.api.event.NPCLeftClickEvent;
import net.citizensnpcs.api.event.NPCRightClickEvent;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.trait.Trait;
import net.citizensnpcs.api.trait.TraitName;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;


@TraitName("lumberjacktrait")
public class NPCLumberjackTrait extends Trait {

    public NPCLumberjackTrait() {
        super("lumberjacktrait");
    }

    @EventHandler
    public void click(NPCRightClickEvent event) {
        //Handle a click on a NPC.
        //Be sure to check event.getNPC() == this.getNPC() so you only handle clicks on this NPC!
        if(event.getNPC() != this.getNPC()) return;
        if(!event.getNPC().hasTrait(NPCLumberjackTrait.class)) return;
        NPC npc = event.getNPC();
        Player player = event.getClicker();
        if (player == null) return;
        //Ensure that the clicker isn't already in the Limbo NPC's dialogue.

        MPlayer mPlayer = MPlayerColl.get().getByPlayer(player);
        if (mPlayer == null) return;

        openDefaultGUI(mPlayer);
    }

    public void openDefaultGUI(MPlayer player) {
        ChestGui gui = getChestGUI(player.getPlayer());
        player.getPlayer().openInventory(gui.getInventory());
    }


    private ChestGui getChestGUI(Player player) {
        Inventory inventory = Bukkit.createInventory(null, 27, Color.get("&4&lLumberjack"));

        ChestGui chestGui = ChestGui.getCreative(inventory);
        chestGui.setAutoclosing(false);
        chestGui.setAutoremoving(false);
        chestGui.setSoundOpen(null);
        chestGui.setSoundClose(null);
        blockFill(chestGui.getInventory(), Material.WHITE_STAINED_GLASS_PANE);

        //ItemStack sellChest = buildItem(Material.CHEST, "&6Sell Logs", MUtil.list(" &r", "&7Sell your &elogs&7 for &a⛃ Cash&7!"));
        ItemStack craftCutter = buildItem(Material.STONECUTTER, "&6Craft Items", MUtil.list(" &r", "&7Craft your &elogs &7into &esticks", "&7and other items!"));
        ItemStack questCompass = buildItem(Material.COMPASS, "&6Quests", MUtil.list(" &r", "&7Access &eWoodcutting &7quests here!"));

        //inventory.setItem(11, sellChest);
        inventory.setItem(12, craftCutter);
        inventory.setItem(14, questCompass);

        //chestGui.setAction(11, inventoryClickEvent -> false);
        chestGui.setAction(12, inventoryClickEvent -> {
            openCraftMenu(player);
            return false;
        });
        chestGui.setAction(14, inventoryClickEvent -> false);

        return chestGui;
    }


    private void openSellMenu(Player player){
        Inventory inventory = Bukkit.createInventory(null, 27, Color.get("&4&lSell Logs"));
        ChestGui chestGui = ChestGui.getCreative(inventory);
        chestGui.setAutoclosing(false);
        chestGui.setAutoremoving(false);
        chestGui.setSoundOpen(null);
        chestGui.setSoundClose(null);

        player.openInventory(chestGui.getInventory());
    }

    private void openCraftMenu(Player player){
        Inventory inventory = Bukkit.createInventory(null, 36, Color.get("&4&lChoose a recipe"));
        ChestGui chestGui = ChestGui.getCreative(inventory);
        chestGui.setAutoclosing(false);
        chestGui.setAutoremoving(false);
        chestGui.setSoundOpen(null);
        chestGui.setSoundClose(null);
        blockFill(inventory, Material.WHITE_STAINED_GLASS_PANE);

        ItemStack planksItem = new ItemBuilder(Material.OAK_PLANKS).name("&74x &6Wooden Planks").lore(MUtil.list(" &r", "&7Used for crafting!", " &r", "&cRequired Ingredients: ", "&7- 1x Any log")).build();
        ItemStack stickItem = new ItemBuilder(Material.STICK).name("&71x &6Stick").lore(MUtil.list(" &r", "&7Used for crafting &ePickaxes&7!", "&7Durability: 100 to 500", " &r", "&cRequired Ingredients: ", "&7- 1x Wooden Planks")).build();
        ItemStack axeItem = new ItemBuilder(Material.WOODEN_AXE).name("&6Wooden Axe").lore(MUtil.list(" &r", "&7Not the best tool for", "&7woodcutting, but gets the job done.", " &r", "&cRequired Ingredients: ", "&7- 3x Wooden Planks", "&7- 3x Sticks")).build();
        //ItemStack fishingRod = new ItemBuilder(Material.FISHING_ROD).name("&6Fishing Rod").lore(MUtil.list(" &r", "&7 Not the best tool for", "&7fishing, but gets the job done.", " &r", "&cRequired Ingredients: ", "&7- 3x Sticks", "&7- 1x Any metal ingot")).build();
        ItemStack hoeItem = new ItemBuilder(Material.WOODEN_HOE).name("&6Wooden Scythe").lore(MUtil.list(" &r", "&7Not the best tool for", "&7farming, but gets the job done.", " &r", "&cRequired Ingredients: ", "&7- 2x Wooden Planks", "&7- 3x Sticks")).build();
        ItemStack bowlItem = new ItemBuilder(Material.BOWL).name("&6Wooden Bowl").lore(MUtil.list(" &r", "&7Great for holding soup!", " &r", "&cRequired Ingredients: ", "&7- 3x Wooden Planks")).build();
        ItemStack woodenHelmet = new ItemBuilder(Material.LEATHER_HELMET).name("&6Wooden Helmet").lore(MUtil.list(" &r", "&7Not the best armor,", "&7but gets the job done.", " &r", "&cRequired Ingredients: ", "&7- 12x Wooden Planks")).build();
        ItemStack woodenChestplate = new ItemBuilder(Material.LEATHER_CHESTPLATE).name("&6Wooden Chestplate").lore(MUtil.list(" &r", "&7Not the best armor,", "&7but gets the job done.", " &r", "&cRequired Ingredients: ", "&7- 15x Wooden Planks")).build();
        ItemStack woodenLeggings = new ItemBuilder(Material.LEATHER_LEGGINGS).name("&6Wooden Leggings").lore(MUtil.list(" &r", "&7Not the best armor,", "&7but gets the job done.", " &r", "&cRequired Ingredients: ", "&7- 17x Wooden Planks")).build();
        ItemStack woodenBoots = new ItemBuilder(Material.LEATHER_BOOTS).name("&6Wooden Boots").lore(MUtil.list(" &r", "&7Not the best armor,", "&7but gets the job done.", " &r", "&cRequired Ingredients: ", "&7- 14x Wooden Planks")).build();
        ItemStack sawDustItem = new ItemBuilder(Material.BEETROOT_SEEDS).name("&73x &6Sawdust").lore(MUtil.list(" &r", "&7Used for crafting!", " &r", "&cRequired Ingredients: ", "&7- 1x Any log")).build();

        setCraftable(chestGui, inventory, Recipe.WOODEN_PLANK_4X, 11, planksItem);
        setCraftable(chestGui, inventory, Recipe.STICK, 12, stickItem);
        setCraftable(chestGui, inventory, Recipe.WOODEN_AXE, 13, axeItem);
        setCraftable(chestGui, inventory, Recipe.WOODEN_SCYTHE, 14, hoeItem);
        setCraftable(chestGui, inventory, Recipe.SAWDUST, 15, sawDustItem);
        inventory.setItem(20, woodenHelmet);
        inventory.setItem(21, woodenChestplate);
        inventory.setItem(22, woodenLeggings);
        inventory.setItem(23, woodenBoots);
        setCraftable(chestGui, inventory,  Recipe.BOWL, 24, bowlItem);
        //setCraftable(chestGui, inventory, Recipe.WOODEN_HELMET, 20, woodenHelmet);
        //setCraftable(chestGui, inventory, Recipe.WOODEN_CEHSTPLATE, 21, woodenChestplate);
        //setCraftable(chestGui, inventory, Recipe.WOODEN_LEGGINGS, 22, woodenLeggings);
        //setCraftable(chestGui, inventory, Recipe.WOODEN_BOOTS, 23, woodenBoots);

        player.closeInventory();
        player.openInventory(chestGui.getInventory());

    }

    private static void setCraftable(ChestGui gui, Inventory inventory, Recipe recipe, Integer slot, ItemStack item){
        inventory.setItem(slot, item);
        gui.setAction(slot, inventoryClickEvent -> {
            Player player = (Player) inventoryClickEvent.getWhoClicked();
            CraftingMenuGui menuGui = new CraftingMenuGui(player, "Crafting: &4" + Color.strip(item.getItemMeta().getDisplayName()), new HashMap<>(), recipe, null);
            menuGui.open();
            return false;
        });
    }

    private void openInv(Player player, ChestGui gui){

    }
    private void openQuestMenu(Player player){
        Inventory inventory = Bukkit.createInventory(null, 27, Color.get("&4&lSell Logs"));
        ChestGui chestGui = ChestGui.getCreative(inventory);
        chestGui.setAutoclosing(false);
        chestGui.setAutoremoving(false);
        chestGui.setSoundOpen(null);
        chestGui.setSoundClose(null);

        player.openInventory(chestGui.getInventory());
    }

    @EventHandler
    public void click(NPCLeftClickEvent event) {

    }


    // Called every tick
    @Override
    public void run() {
    }

    //Run code when your trait is attached to a NPC.
    @Override
    public void onAttach() {

    }

    // Run code when the NPC is despawned. This is called before the entity actually despawns so npc.getEntity() is still valid.
    @Override
    public void onDespawn() {

    }

    //Run code when the NPC is spawned. Note that npc.getEntity() will be null until this method is called.
    //This is called AFTER onAttach and AFTER Load when the server is started.
    @Override
    public void onSpawn() {

    }

    //run code when the NPC is removed. Use this to tear down any repeating tasks.
    @Override
    public void onRemove() {

    }
    public ItemStack buildItem(Material material, String unformattedName, List<String> unformattedLore) {
        ItemStack itemStack = new ItemStack(material, 1);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(Color.get(unformattedName));
        itemMeta.setLore(unformattedLore.stream().map(Color::get).collect(Collectors.toList()));
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public void blockFill(Inventory inv, Material material){
        for (int b = 0; b < inv.getSize(); b++) {
            ItemStack p = new ItemStack(material, 1);
            ItemMeta itemMeta = p.getItemMeta();
            itemMeta.setDisplayName(" ");
            p.setItemMeta(itemMeta);
            inv.setItem(b, p);
        }
    }
}
