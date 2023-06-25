package com.kennedysmithjava.prisoncore.gui;


import com.kennedysmithjava.prisoncore.entity.mines.upgrades.UpgradeName;
import com.kennedysmithjava.prisoncore.entity.player.MPlayer;
import com.kennedysmithjava.prisoncore.gui.buttons.GuiButton;
import com.kennedysmithjava.prisoncore.gui.buttons.GuiOpenerButton;
import com.kennedysmithjava.prisoncore.util.Color;
import com.kennedysmithjava.prisoncore.util.ItemBuilder;
import com.massivecraft.massivecore.chestgui.ChestGui;
import com.massivecraft.massivecore.util.MUtil;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.List;

public class MineMenuStorageGui extends UpgradesGui {

    public MineMenuStorageGui(Player player, String name, BaseGui returnMenu) {
        super(player, name, 5, true, returnMenu);
    }

    @Override
    public void onBuild(Player player, ChestGui gui, Inventory inventory) {
        /* Fill inventory with glass panes */
        blockFill(Material.BLUE_STAINED_GLASS_PANE, 0, 8);
        blockFill(Material.LIGHT_BLUE_STAINED_GLASS_PANE, 9, 35);
        blockFill(Material.BLUE_STAINED_GLASS_PANE, 36, 44);

        /* Set item and ChestAction for the back button */
        int BACK_BUTTON_SLOT = 36;
        ItemBuilder backButtonBuilder = new ItemBuilder(Material.RED_WOOL, 1)
                .name("&c&lGo Back")
                .lore(MUtil.list(   "&7" + Color.strip(this.getReturnMenu().getName())));
        setItem(BACK_BUTTON_SLOT, backButtonBuilder.build());
        setAction(BACK_BUTTON_SLOT, inventoryClickEvent -> {
            returnToLastMenu();
            return false;
        });

    }

    @Override
    public List<GuiButton> getButtons() {

        /* Get the player that will be used in the button actions */
        MPlayer player = MPlayer.get(this.getPlayer());

        //////////////////////////////////////////////////////////////////////////////////////////////////////
        GuiOpenerButton tab_1 = new GuiOpenerButton(
                "&d&lStorage &7Tab 1",
                "&7[MENU]",
                10,
                MUtil.list("&7Unlock 36 new slots of magic storage!"),
                Material.CHEST,
                new MineMenuStorageTabGui(getPlayer(), 1, this),
                UpgradeName.STORAGE_2,
                MUtil.list(),
                MUtil.list(),
                () -> player.msg("&7[&bServer&7] You've unlocked new magic storage!")
        );
        GuiOpenerButton tab_2 = new GuiOpenerButton(
                "&d&lStorage &7Tab 2",
                "&7[MENU]",
                11,
                MUtil.list("&7Unlock 36 new slots of magic storage!"),
                Material.CHEST,
                new MineMenuStorageTabGui(getPlayer(), 2, this),
                UpgradeName.STORAGE_2,
                MUtil.list(),
                MUtil.list(),
                () -> player.msg("&7[&bServer&7] You've unlocked new magic storage!")
        );
        GuiOpenerButton tab_3 = new GuiOpenerButton(
                "&d&lStorage &7Tab 3",
                "&7[MENU]",
                12,
                MUtil.list("&7Unlock 36 new slots of magic storage!"),
                Material.CHEST,
                new MineMenuStorageTabGui(getPlayer(), 3, this),
                UpgradeName.STORAGE_3,
                MUtil.list(),
                MUtil.list(),
                () -> player.msg("&7[&bServer&7] You've unlocked new magic storage!")
        );
        GuiOpenerButton tab_4 = new GuiOpenerButton(
                "&d&lStorage &7Tab 4",
                "&7[MENU]",
                13,
                MUtil.list("&7Unlock 36 new slots of magic storage!"),
                Material.CHEST,
                new MineMenuStorageTabGui(getPlayer(), 4, this),
                UpgradeName.STORAGE_4,
                MUtil.list(),
                MUtil.list(),
                () -> player.msg("&7[&bServer&7] You've unlocked new magic storage!")
        );
        GuiOpenerButton tab_5 = new GuiOpenerButton(
                "&d&lStorage &7Tab 5",
                "&7[MENU]",
                14,
                MUtil.list("&7Unlock 36 new slots of magic storage!"),
                Material.CHEST,
                new MineMenuStorageTabGui(getPlayer(), 5, this),
                UpgradeName.STORAGE_5,
                MUtil.list(),
                MUtil.list(),
                () -> player.msg("&7[&bServer&7] You've unlocked new magic storage!")
        );
        GuiOpenerButton tab_6 = new GuiOpenerButton(
                "&d&lStorage &7Tab 6",
                "&7[MENU]",
                15,
                MUtil.list("&7Unlock 36 new slots of magic storage!"),
                Material.CHEST,
                new MineMenuStorageTabGui(getPlayer(), 6, this),
                UpgradeName.STORAGE_6,
                MUtil.list(),
                MUtil.list(),
                () -> player.msg("&7[&bServer&7] You've unlocked new magic storage!")
        );
        GuiOpenerButton tab_7 = new GuiOpenerButton(
                "&d&lStorage &7Tab 7",
                "&7[MENU]",
                16,
                MUtil.list("&7Unlock 36 new slots of magic storage!"),
                Material.CHEST,
                new MineMenuStorageTabGui(getPlayer(), 7, this),
                UpgradeName.STORAGE_7,
                MUtil.list(),
                MUtil.list(),
                () -> player.msg("&7[&bServer&7] You've unlocked new magic storage!")
        );
        //////////////////////////////////////////////////////////////////////////////////////////////////////
        GuiOpenerButton prestige = new GuiOpenerButton(
                "&d&lPrestige Storage",
                "&7[MENU]",
                32,
                MUtil.list("&7Unlock special storage to save items", "&7through rebirth!"),
                Material.CHEST,
                new MineMenuStoragePrestigeGui(getPlayer(),  this),
                UpgradeName.STORAGE_PRESTIGE,
                MUtil.list(),
                MUtil.list(),
                () -> player.msg("&7[&bServer&7] You've unlocked prestige storage!")
        );


        return MUtil.list(tab_1, tab_2, tab_3, tab_4, tab_5, tab_6, tab_7, prestige);
    }
}




