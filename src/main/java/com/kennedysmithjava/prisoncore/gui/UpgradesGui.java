package com.kennedysmithjava.prisoncore.gui;

import com.kennedysmithjava.prisoncore.entity.mines.Mine;
import com.kennedysmithjava.prisoncore.entity.player.MPlayer;
import com.kennedysmithjava.prisoncore.gui.buttons.GuiButton;
import com.massivecraft.massivecore.chestgui.ChestGui;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.List;

public abstract class UpgradesGui extends BaseGui {


    public UpgradesGui(Player player, String name, int rows, boolean autoClosing, BaseGui returnMenu) {
        super(player, name, rows, false, autoClosing, returnMenu, true);
    }

    @Override
    public void onBuildInner(Player player, ChestGui gui, Inventory inventory) {
        MPlayer mPlayer = MPlayer.get(player);
        Mine mine = mPlayer.getMine();
        List<GuiButton> buttons = getButtons();
        for (GuiButton button : buttons) {
            setItem(button.getSlot(), button.getItem(mPlayer, mine));
            setAction(button.getSlot(), inventoryClickEvent -> {
                button.clicked(mPlayer);
                return false;
            });
        }
    }

    public abstract List<GuiButton> getButtons();
}
