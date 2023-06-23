package com.kennedysmithjava.prisoncore.gui;


import com.kennedysmithjava.prisoncore.gui.buttons.GuiButton;
import com.massivecraft.massivecore.chestgui.ChestGui;
import com.massivecraft.massivecore.util.MUtil;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.List;

public class MineMenuRegenerationGui extends UpgradesGui {

    public MineMenuRegenerationGui(Player player, String name, int rows, BaseGui returnMenu) {
        super(player, name, rows, true, returnMenu);
    }

    @Override
    public void onBuild(Player player, ChestGui gui, Inventory inventory) {

    }

    @Override
    public List<GuiButton> getButtons() {
        return MUtil.list();
    }
}
