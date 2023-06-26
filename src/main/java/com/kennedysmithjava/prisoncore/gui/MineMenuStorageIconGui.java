package com.kennedysmithjava.prisoncore.gui;


import com.kennedysmithjava.prisoncore.entity.player.MPlayer;
import com.kennedysmithjava.prisoncore.entity.player.objects.StorageWrapper;
import com.kennedysmithjava.prisoncore.storage.StorageType;
import com.kennedysmithjava.prisoncore.util.Color;
import com.kennedysmithjava.prisoncore.util.ItemBuilder;
import com.massivecraft.massivecore.chestgui.ChestGui;
import com.massivecraft.massivecore.util.MUtil;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.List;

public class MineMenuStorageIconGui extends BaseGui {

    private static final List<Material> icons = MUtil.list(
        Material.IRON_SWORD, Material.SHIELD, Material.BOW, Material.ARROW, Material.IRON_HELMET, Material.IRON_CHESTPLATE,
            Material.IRON_LEGGINGS, Material.IRON_BOOTS, Material.TOTEM_OF_UNDYING, Material.IRON_PICKAXE,
            Material.IRON_AXE, Material.IRON_HOE, Material.FISHING_ROD, Material.STICK, Material.IRON_INGOT,
            Material.GOLD_ORE, Material.OAK_LOG, Material.TROPICAL_FISH, Material.WHITE_DYE, Material.BLAZE_POWDER,
            Material.BLUE_DYE, Material.BROWN_DYE, Material.TURTLE_SPAWN_EGG, Material.BONE, Material.SKELETON_SKULL,
            Material.HONEY_BOTTLE, Material.STRING, Material.APPLE, Material.LEATHER, Material.PORKCHOP, Material.BOOK,
            Material.WHEAT, Material.ENDER_EYE, Material.FIRE_CHARGE, Material.AMETHYST_CLUSTER, Material.NETHER_STAR
    );

    private final StorageWrapper wrapper;
    public MineMenuStorageIconGui(Player player, StorageType type, BaseGui returnMenu) {
        super(player, "", 5, false, true, returnMenu);
        this.wrapper = MPlayer.get(player).getStorageWrapper(type);
        setName("&4&lEdit Icon &8- " + Color.strip(wrapper.getName()));
    }

    @Override
    public void onBuild(Player player, ChestGui gui, Inventory inventory) {
        blockFill(Material.BLUE_STAINED_GLASS_PANE, 36, 44);
        for (Material icon : icons) {
            int slot = inventory.firstEmpty();
            ItemBuilder iconBuilder = new ItemBuilder(icon).name("&a&lSelect")
                    .lore(MUtil.list("&r", "&7Click to select this icon", "&7for your &f" + Color.strip(wrapper.getName()) + " &7storage."));
            setItem(slot, iconBuilder.build());
            setAction(slot, inventoryClickEvent -> {
                wrapper.setIcon(icon);
                returnToLastMenu();
                return false;
            });
        }
    }

}