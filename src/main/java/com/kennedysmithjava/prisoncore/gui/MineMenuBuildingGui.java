package com.kennedysmithjava.prisoncore.gui;


import com.kennedysmithjava.prisoncore.eco.CostCurrency;
import com.kennedysmithjava.prisoncore.eco.CostSkillLevel;
import com.kennedysmithjava.prisoncore.eco.CurrencyType;
import com.kennedysmithjava.prisoncore.entity.mines.upgrades.UpgradeName;
import com.kennedysmithjava.prisoncore.entity.mines.upgrades.actions.ActionBuildingUnlock;
import com.kennedysmithjava.prisoncore.entity.player.MPlayer;
import com.kennedysmithjava.prisoncore.gui.buttons.GuiButton;
import com.kennedysmithjava.prisoncore.skill.SkillType;
import com.kennedysmithjava.prisoncore.util.BuildingType;
import com.kennedysmithjava.prisoncore.util.Color;
import com.kennedysmithjava.prisoncore.util.ItemBuilder;
import com.massivecraft.massivecore.chestgui.ChestGui;
import com.massivecraft.massivecore.util.MUtil;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.List;

public class MineMenuBuildingGui extends UpgradesGui {

    public MineMenuBuildingGui(Player player, String name, int rows, BaseGui returnMenu) {
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
        GuiButton portal = new GuiButton(
                "&d&lPortal",
                "&7[BUILDING]",
                11,
                MUtil.list("&7Build a portal to visit new destinations!"),
                Material.NETHER_PORTAL,
                MUtil.list(),
                UpgradeName.BUILDING_PORTAL,
                () -> new ActionBuildingUnlock(BuildingType.PORTAL).apply(player),
                MUtil.list()
                );
        GuiButton enchant_table = new GuiButton(
                "&d&lEnchantment Table",
                "&7[BUILDING]",
                12,
                MUtil.list("&7Build a table to enchant your tools!"),
                Material.ENCHANTING_TABLE,
                MUtil.list(),
                UpgradeName.BUILDING_ENCHANT_TABLE,
                () -> new ActionBuildingUnlock(BuildingType.ENCHANT_TABLE).apply(player),
                MUtil.list(new CostSkillLevel(SkillType.PLAYER, 5), new CostCurrency(CurrencyType.CASH, 100))
        );
        GuiButton anvil = new GuiButton(
                "&d&lAnvil",
                "&7[BUILDING]",
                13,
                MUtil.list("Build an anvil to forge new tools from home!"),
                Material.ANVIL,
                MUtil.list(),
                UpgradeName.BUILDING_ANVIL,
                () -> new ActionBuildingUnlock(BuildingType.ANVIL).apply(player),
                MUtil.list(new CostSkillLevel(SkillType.PLAYER, 25), new CostCurrency(CurrencyType.CASH, 1000))
        );
        GuiButton furnace = new GuiButton(
                "&d&lFurnace",
                "&7[BUILDING]",
                14,
                MUtil.list("&7Build a furnace to smelt ores from home!"),
                Material.NETHER_PORTAL,
                MUtil.list(),
                UpgradeName.BUILDING_FURNACE,
                () -> new ActionBuildingUnlock(BuildingType.FURNACE).apply(player),
                MUtil.list(new CostSkillLevel(SkillType.PLAYER, 40), new CostCurrency(CurrencyType.CASH, 100000))
        );
        GuiButton beacon = new GuiButton(
                "&d&lBeacon",
                "&7[BUILDING]",
                15,
                MUtil.list("&7Build a beacon to gain persistent effects!"),
                Material.BEACON,
                MUtil.list(),
                UpgradeName.BUILDING_BEACON,
                () -> new ActionBuildingUnlock(BuildingType.BEACON).apply(player),
                MUtil.list(new CostSkillLevel(SkillType.PLAYER, 50), new CostCurrency(CurrencyType.CASH, 1000000))
        );


        return MUtil.list(portal, enchant_table, anvil, furnace, beacon);
    }
}
