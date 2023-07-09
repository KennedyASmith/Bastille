package com.kennedysmithjava.prisoncore.gui;

import com.kennedysmithjava.prisoncore.util.Color;
import com.kennedysmithjava.prisoncore.util.ItemBuilder;
import com.massivecraft.massivecore.chestgui.ChestGui;
import com.massivecraft.massivecore.util.MUtil;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class RebirthTierRewardsGui extends BaseGui {
    public RebirthTierRewardsGui(Player player, BaseGui gui) {
        super(player, "&d&lRebirth &r&7Tier Items", 6, false, true, gui);
    }

    @Override
    public void onBuild(Player player, ChestGui gui, Inventory inventory) {
        blockFill(Material.BLUE_STAINED_GLASS_PANE, 0, 8);
        blockFill(Material.LIGHT_BLUE_STAINED_GLASS_PANE, 9, 44);
        blockFill(Material.BLUE_STAINED_GLASS_PANE, 45, 53);

        /* Set item and ChestAction for the back button */
        int BACK_BUTTON_SLOT = 45;
        ItemBuilder backButtonBuilder = new ItemBuilder(Material.RED_WOOL, 1)
                .name("&c&lGo Back")
                .lore(MUtil.list(   "&7" + Color.strip(this.getReturnMenu().getName())));
        setItem(BACK_BUTTON_SLOT, backButtonBuilder.build());
        setAction(BACK_BUTTON_SLOT, inventoryClickEvent -> {
            returnToLastMenu();
            return false;
        });


    }
}
