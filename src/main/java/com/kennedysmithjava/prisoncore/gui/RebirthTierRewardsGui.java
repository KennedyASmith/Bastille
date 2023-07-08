package com.kennedysmithjava.prisoncore.gui;

import com.massivecraft.massivecore.chestgui.ChestGui;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class RebirthTierRewardsGui extends BaseGui {
    public RebirthTierRewardsGui(Player player, BaseGui gui) {
        super(player, "&d&lRebirth &r&7Main Menu", 5, false, true, gui);
    }

    @Override
    public void onBuild(Player player, ChestGui gui, Inventory inventory) {
        blockFill(Material.BLUE_STAINED_GLASS_PANE, 0, 8);
        blockFill(Material.LIGHT_BLUE_STAINED_GLASS_PANE, 9, 35);
        blockFill(Material.BLUE_STAINED_GLASS_PANE, 36, 44);



    }
}
