package com.kennedysmithjava.prisoncore.gui;


import com.kennedysmithjava.prisoncore.gui.buttons.GuiButton;
import com.kennedysmithjava.prisoncore.util.Color;
import com.kennedysmithjava.prisoncore.util.ItemBuilder;
import com.massivecraft.massivecore.chestgui.ChestGui;
import com.massivecraft.massivecore.util.MUtil;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.List;

public class MineMenuStorageTabGui extends UpgradesGui {

    public MineMenuStorageTabGui(Player player, int tab, BaseGui returnMenu) {
        super(player, "&4&lStorage - &7Tab " + tab, 3, true, returnMenu);
    }

    @Override
    public void onBuild(Player player, ChestGui gui, Inventory inventory) {
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
    }

    @Override
    public List<GuiButton> getButtons() {
        return MUtil.list();
    }
}