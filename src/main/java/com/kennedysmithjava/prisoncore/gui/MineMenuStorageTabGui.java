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

public class MineMenuStorageTabGui extends BaseGui {

    StorageWrapper storageWrapper;
    StorageType type;

    public MineMenuStorageTabGui(Player player, StorageType type, BaseGui returnMenu) {
        super(player, "", 3, false, true, returnMenu);
        this.storageWrapper = MPlayer.get(player).getStorageWrapper(type);
        this.type = type;
        setName("&4&lEdit Storage &8- " + Color.strip(storageWrapper.getName()));
    }

    @Override
    public void onBuild(Player player, ChestGui gui, Inventory inventory) {

        blockFill(Material.BLUE_STAINED_GLASS_PANE, 0, 8);
        blockFill(Material.LIGHT_BLUE_STAINED_GLASS_PANE, 9, 17);
        blockFill(Material.BLUE_STAINED_GLASS_PANE, 18, 26);

        /* Set item and ChestAction for the back button */
        int BACK_BUTTON_SLOT = 18;
        ItemBuilder backButtonBuilder = new ItemBuilder(Material.RED_WOOL, 1)
                .name("&c&lGo Back")
                .lore(MUtil.list(   "&7" + Color.strip(this.getReturnMenu().getName())));
        setItem(BACK_BUTTON_SLOT, backButtonBuilder.build());
        setAction(BACK_BUTTON_SLOT, inventoryClickEvent -> {
            returnToLastMenu();
            return false;
        });

        /* Set item and ChestAction for the back button */
        int OPEN_BUTTON_SLOT = 26;
        ItemBuilder openButtonBuilder = new ItemBuilder(Material.CHEST, 1)
                .name("&a&lOpen Storage")
                .lore(MUtil.list(   "&7" + Color.strip(storageWrapper.getName())));
        setItem(OPEN_BUTTON_SLOT, openButtonBuilder.build());
        setAction(OPEN_BUTTON_SLOT, inventoryClickEvent -> {
            MPlayer mPlayer = MPlayer.get(player);
            close();
            mPlayer.openStorage(type, player);
            return false;
        });


        /* Set item and ChestAction for the back button */
        int ICON_BUTTON_SLOT = 12;
        Material icon = storageWrapper.getIcon();
        if(icon == Material.CHEST) icon = Material.PAINTING;
        ItemBuilder iconButtonBuilder = new ItemBuilder(icon, 1)
                .name("&a&lEdit Icon")
                .lore(MUtil.list(   "&r", "&7Edit the icon that appears for this storage!"));
        setItem(ICON_BUTTON_SLOT, iconButtonBuilder.build());
        setAction(ICON_BUTTON_SLOT, inventoryClickEvent -> {
            close();
            MineMenuStorageIconGui iconGui = new MineMenuStorageIconGui(player, type, this);
            iconGui.open();
            return false;
        });

        /* Set item and ChestAction for the back button */
        int NAME_BUTTON_SLOT = 12;
        ItemBuilder nameButtonBuilder = new ItemBuilder(Material.PAINTING, 1)
                .name("&a&lEdit Name")
                .lore(MUtil.list(   "&r", "&7Edit the name that appears for this storage!"));
        setItem(NAME_BUTTON_SLOT, nameButtonBuilder.build());
        setAction(NAME_BUTTON_SLOT, inventoryClickEvent -> {
            close();
            player.sendMessage(
                    "&7[&bStorage&7] &7Use the command &e/storage rename " + type.getId()
                            + " &7to rename your &f" + Color.strip(storageWrapper.getName()) + " &7storage vault!");
            return false;
        });
    }

}