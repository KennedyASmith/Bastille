package com.kennedysmithjava.prisoncore.gui;


import com.kennedysmithjava.prisoncore.eco.CostSkillLevel;
import com.kennedysmithjava.prisoncore.entity.mines.Mine;
import com.kennedysmithjava.prisoncore.entity.mines.upgrades.UpgradeName;
import com.kennedysmithjava.prisoncore.entity.player.MPlayer;
import com.kennedysmithjava.prisoncore.gui.buttons.GuiButton;
import com.kennedysmithjava.prisoncore.gui.buttons.GuiOpenerButton;
import com.kennedysmithjava.prisoncore.gui.buttons.GuiSkillUpgradeButton;
import com.kennedysmithjava.prisoncore.skill.SkillType;
import com.massivecraft.massivecore.chestgui.ChestGui;
import com.massivecraft.massivecore.util.MUtil;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.List;

public class MineMainGui extends BaseGui{


    public static final List<Integer> centerGlass = MUtil.list(12, 13, 14,21, 23, 30, 31, 32);
    public static final  List<Integer> fillerGlassLeft = MUtil.list(2,4,10,19,20,28,38);
    public static final  List<Integer> fillerGlassRight = MUtil.list(6,16,24,25,34,42,40);
    public static final  List<Integer> edgeGlassLeft = MUtil.list(0,1,9,18,27,36,37);
    public static final  List<Integer> edgeGlassRight = MUtil.list(7,8,17,26,35,44,43);
    public static final  List<Integer> lockableButtons = MUtil.list(3,5,15,33,41,39,29,11);
    public static final List<Integer> lightGreyGlass = MUtil.list(2,4,10,19,20,28,38,6,16,24,25,34,42,40);
    public static final List<Integer> darkGreyGlass = MUtil.list(0,1,9,18,27,36,37,7,8,17,26,35,44,43);
    public static final String CATEGORY_TAG = "&7[MENU]";

    private final MPlayer mPlayer;

    public MineMainGui(Player player, MPlayer mPlayer) {
        super(player, "&4&lUpgrades &7- Main Menu", 5, false, true);
        this.mPlayer = mPlayer;
    }

    @Override
    public void onBuild(Player player, ChestGui gui, Inventory inventory) {
        Mine mine = mPlayer.getMine();
        List<GuiButton> buttons = getButtons(player, mPlayer, this);
        lightGreyGlass.forEach(slot -> setItem(slot, Material.LIGHT_BLUE_STAINED_GLASS_PANE));
        darkGreyGlass.forEach(slot -> setItem(slot, Material.BLUE_STAINED_GLASS_PANE));
        centerGlass.forEach(slot -> setItem(slot, Material.WHITE_STAINED_GLASS_PANE));

        for (GuiButton button : buttons) {
            setItem(button.getSlot(), button.getItem(mPlayer, mine));
            setAction(button.getSlot(), inventoryClickEvent -> {
                button.clicked(mPlayer);
                return false;
            });
        }
    }

    public static List<GuiButton> getButtons(Player player, MPlayer mPlayer, BaseGui currentMenu){
        return MUtil.list(
                new GuiSkillUpgradeButton(mPlayer, SkillType.PLAYER, 22,
                        MUtil.list("&7Increase your player level to", "&7access more upgrades!"),
                        () -> {}),

                //////////////////////////////////////////////////////////////////////////////////////////////////////

                new GuiOpenerButton("&6Building", CATEGORY_TAG, 3,
                        MUtil.list("&7Unlock new buildings for your mine!"),
                        Material.CRAFTING_TABLE,
                        new MineMenuMobilityGui(player, "&4&lBuildings &7- Menu", 3, currentMenu),
                        MUtil.list(), MUtil.list(new CostSkillLevel(SkillType.PLAYER, 2))),

                //////////////////////////////////////////////////////////////////////////////////////////////////////

                new GuiOpenerButton("&6Blocks", CATEGORY_TAG, 15,
                        MUtil.list("&7Improve the block sets that appear in your mine!"),
                        Material.DIAMOND_ORE,
                        new MineMenuBlocksGui(player, currentMenu, 0),
                        MUtil.list(), MUtil.list(new CostSkillLevel(SkillType.PLAYER, 2))),

                //////////////////////////////////////////////////////////////////////////////////////////////////////

                new GuiOpenerButton("&6Architecture", CATEGORY_TAG, 5,
                        MUtil.list("&7Unlock new size and decor upgrades for your mine!"),
                        Material.OAK_FENCE,
                        new MineMenuArchitectureGui(player, "&4&lArchitecture &7- Menu", 3,  currentMenu),
                        MUtil.list(), MUtil.list(new CostSkillLevel(SkillType.PLAYER, 3))),

                //////////////////////////////////////////////////////////////////////////////////////////////////////

                new GuiOpenerButton("&6Environment", CATEGORY_TAG, 11,
                        MUtil.list("&7Unlock new environments for your mine!"),
                        Material.CLOCK,
                        new MineMenuArchitectureGui(player, "&4&lEnvironment &7- Menu", 3,  currentMenu),
                        MUtil.list(), MUtil.list(new CostSkillLevel(SkillType.PLAYER, 3))),

                //////////////////////////////////////////////////////////////////////////////////////////////////////

                new GuiOpenerButton("&6Storage", CATEGORY_TAG, 29,
                        MUtil.list("&7Unlock new ways to store items!"),
                        Material.CHEST,
                        new MineMenuStorageGui(player, "&4&lStorage &7- Menu",  currentMenu),
                        MUtil.list(), MUtil.list(new CostSkillLevel(SkillType.PLAYER, 3))),

                //////////////////////////////////////////////////////////////////////////////////////////////////////

                new GuiOpenerButton("&6Portal", CATEGORY_TAG, 33,
                        MUtil.list("&7Unlock new places to travel!"),
                        Material.FEATHER,
                        new MineMenuArchitectureGui(player, "&4&lTravel &7- Menu", 3,  currentMenu),
                        MUtil.list(UpgradeName.BUILDING_PORTAL), MUtil.list(new CostSkillLevel(SkillType.PLAYER, 3))),

                //////////////////////////////////////////////////////////////////////////////////////////////////////

                new GuiOpenerButton("&6Automation", CATEGORY_TAG, 39,
                        MUtil.list("&7Unlock new ways to automate your mine!"),
                        Material.REPEATER,
                        new MineMenuArchitectureGui(player, "&4&lAutomation &7- Menu", 3,  currentMenu),
                        MUtil.list(), MUtil.list(new CostSkillLevel(SkillType.PLAYER, 3))),

                //////////////////////////////////////////////////////////////////////////////////////////////////////

                new GuiOpenerButton("&6Mobility", CATEGORY_TAG, 41,
                        MUtil.list("&7Unlock new ways to move around your mine!"),
                        Material.LADDER,
                        new MineMenuMobilityGui(player, "&4&lMobility &7- Menu", 3,  currentMenu),
                        MUtil.list(), MUtil.list(new CostSkillLevel(SkillType.PLAYER, 3)))
        );
    }
}
