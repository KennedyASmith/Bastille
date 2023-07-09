package com.kennedysmithjava.prisoncore.gui;

import com.kennedysmithjava.prisoncore.crafting.Recipe;
import com.kennedysmithjava.prisoncore.util.ItemBuilder;
import com.massivecraft.massivecore.chestgui.ChestGui;
import com.massivecraft.massivecore.util.MUtil;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class ForgeToolCraftGui extends BaseGui{
    public ForgeToolCraftGui(Player player) {
        super(player, "&4&lSelect item to forge:", 6, false, true);
    }

    @Override
    public void onBuild(Player player, ChestGui gui, Inventory inventory) {
        blockFill(Material.RED_STAINED_GLASS_PANE, 0, 17);
        blockFill(Material.ORANGE_STAINED_GLASS_PANE, 18, 35);
        blockFill(Material.YELLOW_STAINED_GLASS_PANE, 36, 53);
        blockFill(Material.ORANGE_STAINED_GLASS_PANE, 38, 42);
        blockFill(Material.ORANGE_STAINED_GLASS_PANE, 48, 50);
        blockFill(Material.WHITE_STAINED_GLASS_PANE, 45, 46);
        blockFill(Material.WHITE_STAINED_GLASS_PANE, 52, 53);

        BaseGui baseGui = this;

        ItemBuilder pickItem = new ItemBuilder(Material.IRON_PICKAXE)
                .name("&cForge &6&lPickaxe")
                .lore(MUtil.list(" &r", "Use materials to forge a new pickaxe for mining.", "&r", "&eClick to forge!"));
        setItem(22, pickItem.build());
        setAction(22, inventoryClickEvent -> {
            close();
            CraftingMenuGui craftingMenuGui = new CraftingMenuGui(player, "&c&lForging Pickaxe", Recipe.PICKAXE, baseGui);
            craftingMenuGui.open();
            return false;
        });

        ItemBuilder axeItem = new ItemBuilder(Material.IRON_HOE)
                .name("&cForge &6&lAxe")
                .lore(MUtil.list(" &r", "Use materials to forge a new axe for woodcutting.", "&r", "&eClick to forge!"));
        setItem(29, axeItem.build());
        setAction(29, inventoryClickEvent -> {
            close();
            CraftingMenuGui craftingMenuGui = new CraftingMenuGui(player, "&c&lForging Axe", Recipe.AXE, baseGui);
            craftingMenuGui.open();
            return false;
        });

        ItemBuilder hoeItem = new ItemBuilder(Material.IRON_HOE)
                .name("&cForge &6&lHoe")
                .lore(MUtil.list(" &r", "Use materials to forge a new hoe for farming.", "&r", "&eClick to forge!"));
        setItem(33, hoeItem.build());
        setAction(33, inventoryClickEvent -> {
            close();
            CraftingMenuGui craftingMenuGui = new CraftingMenuGui(player, "&c&lForging Hoe", Recipe.HOE, baseGui);
            craftingMenuGui.open();
            return false;
        });

        }
    }
