package com.kennedysmithjava.prisoncore.gui;


import com.kennedysmithjava.prisoncore.cmd.type.TypeMobility;
import com.kennedysmithjava.prisoncore.entity.mines.upgrades.UpgradeName;
import com.kennedysmithjava.prisoncore.entity.mines.upgrades.actions.ActionMineSetMobility;
import com.kennedysmithjava.prisoncore.entity.player.MPlayer;
import com.kennedysmithjava.prisoncore.gui.buttons.GuiButton;
import com.kennedysmithjava.prisoncore.gui.buttons.GuiToggleButton;
import com.kennedysmithjava.prisoncore.util.Color;
import com.kennedysmithjava.prisoncore.util.ItemBuilder;
import com.massivecraft.massivecore.chestgui.ChestGui;
import com.massivecraft.massivecore.util.MUtil;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.List;

public class MineMenuMobilityGui extends UpgradesGui {

    public MineMenuMobilityGui(Player player, String name, int rows, BaseGui returnMenu) {
        super(player, name, rows, true, returnMenu);
    }

    @Override
    public void onBuild(Player player, ChestGui gui, Inventory inventory) {
        /* Fill inventory with glass panes */
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

    }

    @Override
    public List<GuiButton> getButtons() {

        /* Get the player that will be used in the button actions */
        MPlayer player = MPlayer.get(this.getPlayer());

        //////////////////////////////////////////////////////////////////////////////////////////////////////
        GuiToggleButton ladders_1 = new GuiToggleButton(
                player,
                UpgradeName.MOBILITY_LADDER_1,
                Material.LADDER,
                11,
                MUtil.list("&7A single ladder on one side of the mine."),
                MUtil.list(), /* No other upgrades required */
                MUtil.list(UpgradeName.MOBILITY_JUMP_PAD, UpgradeName.MOBILITY_FLIGHT, UpgradeName.MOBILITY_LADDER_2, UpgradeName.MOBILITY_LADDER_3),
                () -> new ActionMineSetMobility(TypeMobility.LADDER_1).apply(player)
        );
        //////////////////////////////////////////////////////////////////////////////////////////////////////
        GuiToggleButton ladders_2 = new GuiToggleButton(
                player,
                UpgradeName.MOBILITY_LADDER_2,
                Material.LADDER,
                12,
                MUtil.list("&7A single ladder on every side of the mine."),
                MUtil.list(UpgradeName.MOBILITY_LADDER_1),
                MUtil.list(UpgradeName.MOBILITY_JUMP_PAD, UpgradeName.MOBILITY_LADDER_1, UpgradeName.MOBILITY_FLIGHT, UpgradeName.MOBILITY_LADDER_3),
                () -> new ActionMineSetMobility(TypeMobility.LADDER_2).apply(player)
        );
        //////////////////////////////////////////////////////////////////////////////////////////////////////
        GuiToggleButton ladders_3 = new GuiToggleButton(
                player,
                UpgradeName.MOBILITY_LADDER_3,
                Material.LADDER,
                13,
                MUtil.list("&7Ladders cover the length of every side of the mine."),
                MUtil.list(UpgradeName.MOBILITY_LADDER_2),
                MUtil.list(UpgradeName.MOBILITY_JUMP_PAD, UpgradeName.MOBILITY_LADDER_1, UpgradeName.MOBILITY_LADDER_2, UpgradeName.MOBILITY_FLIGHT),
                () -> new ActionMineSetMobility(TypeMobility.FULL_LADDER).apply(player)
        );
        //////////////////////////////////////////////////////////////////////////////////////////////////////
        GuiToggleButton jump_pad = new GuiToggleButton(
                player,
                UpgradeName.MOBILITY_JUMP_PAD,
                Material.HEAVY_WEIGHTED_PRESSURE_PLATE,
                14,
                MUtil.list("&7Replace your old ladders with high tech jump pads!"),
                MUtil.list(UpgradeName.MOBILITY_LADDER_3),
                MUtil.list(UpgradeName.MOBILITY_FLIGHT, UpgradeName.MOBILITY_LADDER_1, UpgradeName.MOBILITY_LADDER_2, UpgradeName.MOBILITY_LADDER_3),
                () -> new ActionMineSetMobility(TypeMobility.JUMP_PAD).apply(player)
        );
        //////////////////////////////////////////////////////////////////////////////////////////////////////
        GuiToggleButton flight = new GuiToggleButton(
                player,
                UpgradeName.MOBILITY_FLIGHT,
                Material.FEATHER,
                15,
                MUtil.list("&7Learn the power of flight!"),
                MUtil.list(UpgradeName.MOBILITY_JUMP_PAD),
                MUtil.list(UpgradeName.MOBILITY_JUMP_PAD, UpgradeName.MOBILITY_LADDER_1, UpgradeName.MOBILITY_LADDER_2, UpgradeName.MOBILITY_LADDER_3),
                () -> new ActionMineSetMobility(TypeMobility.JUMP_PAD).apply(player)
        );
        //////////////////////////////////////////////////////////////////////////////////////////////////////

        return MUtil.list(ladders_1, ladders_2, ladders_3, jump_pad, flight);
    }
}
