package com.kennedysmithjava.prisoncore.entity.mines;

import com.kennedysmithjava.prisoncore.cmd.type.TypeMobility;
import com.kennedysmithjava.prisoncore.util.BuildingType;
import com.kennedysmithjava.prisoncore.upgrades.*;
import com.kennedysmithjava.prisoncore.upgrades.actions.*;
import com.kennedysmithjava.prisoncore.upgrades.buttons.*;
import com.massivecraft.massivecore.command.editor.annotation.EditorName;
import com.massivecraft.massivecore.store.Entity;
import com.massivecraft.massivecore.util.MUtil;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@EditorName("config")
public class UpgradesConf extends Entity<UpgradesConf>
{
    // -------------------------------------------- //
    // META
    // -------------------------------------------- //

    protected static transient UpgradesConf i;

    public static UpgradesConf get() { return i; }

    public static String mainMenu = "MAIN";
    public static String initialUpgrade = "COBWEB_CLEAR";

    public static Map<Integer, MainUpgrade> mainUpgrades = MUtil.map(
            2, new MainUpgrade(
                    MUtil.list("", "%level%", "&e&lCOST", "&a⛃100.00", "", "&a+ &7Unlock &6Buildings &7category", "&a+ &7Unlock &6Architecture &7category."),
                    MUtil.list(new ActionMineLevelIncrement(), new ActionUnlockUpgrade(UpgradeName.CATEGORY_ARCHITECTURE.get(), true, true), new ActionUnlockUpgrade(UpgradeName.CATEGORY_BUILDINGS.get(), true, true)),
                    MUtil.list()),
            3, new MainUpgrade(
                    MUtil.list("", "%level%", "&e&lCOST", "&a⛃550.00", "", "&a+ &7Unlock &6Mine Regeneration &7category", "&a+ &7Unlock &6Regen Speed &7upgrade"),
                    MUtil.list(new ActionMineLevelIncrement(), new ActionUnlockUpgrade(UpgradeName.CATEGORY_REGENERATION.get(), true, true), new ActionUnlockUpgrade(UpgradeName.CATEGORY_REGENERATION.get())),
                    MUtil.list()),
                    4, new MainUpgrade(
                    MUtil.list("", "%level%", "&e&lCOST", "&a⛃2000.00", "", "&a+ &7Unlock &6Regen Speed &7upgrade"),
                    MUtil.list(new ActionMineLevelIncrement(), new ActionUnlockUpgrade(UpgradeName.CATEGORY_REGENERATION.get(), true, true)),
                    MUtil.list())
    );

    public static Map<String, UpgradeGUI> upgradeGUIs = MUtil.map(
      "MAIN", new UpgradeGUI("&4Mine Upgrades", 9 * 5, MUtil.list(
                new GUIButtonMainUpgrade("&6&lCell Upgrade",
                        22,
                        Material.MINECART,
                        MUtil.list()),
                    new GUIButton(
                            "&6Buildings",
                            3,
                            MUtil.list("&7[&eCATEGORY&7]", "", "&7Unlock new buildings for your mine!"),
                            MUtil.list("&7[&cLOCKED&7]", "", "&7&lREQUIREMENTS","&7- &eCell &7level &a2"),
                            Material.CRAFTING_TABLE,
                            MUtil.list(new ActionOpenGUI("BUILDINGS")), MUtil.list(UpgradeName.CATEGORY_BUILDINGS.get())),
                    new GUIButton(
                            "&6Mine Regeneration",
                            15, MUtil.list("&7[&eCATEGORY]", "", "&7Unlock upgrades to change how your mine regenerates!"),
                            MUtil.list("&7[&cLOCKED&7]", "", "&7&lREQUIREMENTS","&7- &eCell &7level &a3"),
                            Material.PISTON,
                            MUtil.list(new ActionOpenGUI("REGENERATION")), MUtil.list(UpgradeName.CATEGORY_REGENERATION.get())),
                    new GUIButton(
                            "&6Architecture",
                            5, MUtil.list("&7[&eCATEGORY]", "", "&7Unlock new size and layout upgrades for your mine!"),
                            MUtil.list("&7[&cLOCKED&7]", "", "&7&lREQUIREMENTS","&7- &eCell &7level &a2"),
                            Material.OAK_FENCE,
                            MUtil.list(new ActionOpenGUI("ARCHITECTURE")), MUtil.list(UpgradeName.CATEGORY_ARCHITECTURE.get())),
                    new GUIButton(
                            "&6Environment",
                            11, MUtil.list("&7[&eCATEGORY]", "", "&7Unlock new environments for your mine!"),
                            MUtil.list("&7[&cLOCKED&7]"),
                            Material.CLOCK,
                            MUtil.list(new ActionOpenGUI("ENVIRONMENT")), MUtil.list(UpgradeName.CATEGORY_ENVIRONMENT.get())),
                    new GUIButton(
                            "&6Block Collection",
                            29, MUtil.list("&7[&eCATEGORY]", "", "&7Unlock new upgrades to make collecting", "&7and selling blocks easier!"),
                            MUtil.list("&7[&cLOCKED&7]"),
                            Material.HOPPER,
                            MUtil.list(new ActionOpenGUI("COLLECTION")), MUtil.list(UpgradeName.CATEGORY_COLLECTION.get())),
                    new GUIButton(
                            "&6Storage",
                            33, MUtil.list("&7[&eCATEGORY]", "", "&7Unlock personal storage for your mine!"),
                            MUtil.list("&7[&cLOCKED&7]"),
                            Material.CHEST,
                            MUtil.list(new ActionOpenGUI("STORAGE")), MUtil.list(UpgradeName.CATEGORY_STORAGE.get())),
                    new GUIButton(
                            "&6Robotics",
                            39, MUtil.list("&7[&eCATEGORY]", "", "&7Unlock upgrades for your mine's robots!"),
                            MUtil.list("&7[&cLOCKED&7]"),
                            Material.DISPENSER,
                            MUtil.list(new ActionOpenGUI("ROBOTICS")), MUtil.list(UpgradeName.CATEGORY_ROBOTICS.get())),
                    new GUIButton(
                            "&6Mobility",
                            41, MUtil.list("&7[&eCATEGORY]", "", "&7Unlock new ways of moving around your mine!"),
                            MUtil.list("&7[&cLOCKED&7]"),
                            Material.LADDER,
                            MUtil.list(new ActionOpenGUI("MOBILITY")), MUtil.list(UpgradeName.CATEGORY_MOBILITY.get()))
            )),

            "ENVIRONMENT", new UpgradeGUI("&4Mine Blocks", 9 * 5, MUtil.list(
                    new GUIButton(
                            "&6BACK",
                            36, MUtil.list("", "&7Main Menu"),
                            MUtil.list(),
                            Material.ARROW,
                            MUtil.list(new ActionOpenGUI("MAIN")), MUtil.list())
            )),

            "ARCHITECTURE", new UpgradeGUI("&4Mine Blocks", 9 * 6, MUtil.list(
                    new GUIButtonPurchasable(
                            "&6Floor Customization",
                            UpgradeName.ARCHITECTURE_PATHS.get(),
                            20, MUtil.list("", "&7Unlock the ability to change your &eCell&7's floor layout!"),
                            MUtil.list("&7[&cLOCKED&7]", "", "&7Unlock the ability to change your &eCell&7's floor layout!"),
                            Material.STONE_SLAB,
                            MUtil.list(), MUtil.list(),
                            MUtil.list(), MUtil.list()),
                    new GUIButtonPurchasable(
                            "&6Wall Customization",
                            UpgradeName.ARCHITECTURE_WALLS.get(),
                            21, MUtil.list("", "&7Unlock the ability to change your &eCell&7's wall design!"),
                            MUtil.list("&7[&cLOCKED&7]", "", "&7Unlock the ability to change your &eCell&7's wall design!"),
                            Material.STONE_BRICK_WALL,
                            MUtil.list(), MUtil.list(UpgradeName.ARCHITECTURE_PATHS.get()),
                            MUtil.list(), MUtil.list()),
                    new GUIButtonPurchasable(
                            "&6Mine Height &7(Increase)",
                            UpgradeName.ARCHITECTURE_HEIGHT_INCREASE.get(),
                            23, MUtil.list("", "&7Unlock the ability to increase your &eMine&7's height!"),
                            MUtil.list("&7[&cLOCKED&7]", "", "&7Unlock the ability to decrease your &eMine&7's height!"),
                            Material.OAK_STAIRS,
                            MUtil.list(), MUtil.list(),
                            MUtil.list(), MUtil.list()),
                    new GUIButtonPurchasable(
                            "&6Mine Height &7(Decrease)",
                            UpgradeName.ARCHITECTURE_HEIGHT_DECREASE.get(),
                            24, MUtil.list("", "&7Unlock the ability to decrease your &eMine&7's height!"),
                            MUtil.list("&7[&cLOCKED&7]", "", "&7Unlock the ability to decrease your &eMine&7's height!"),
                            Material.NETHER_BRICK_STAIRS,
                            MUtil.list(), MUtil.list(UpgradeName.ARCHITECTURE_HEIGHT_INCREASE.get()),
                            MUtil.list(), MUtil.list()),
                    new GUIButtonPurchasable(
                            "&6Mine Width &7(Increase)",
                            UpgradeName.ARCHITECTURE_WIDTH_INCREASE.get(),
                            32, MUtil.list("", "&7Unlock the ability to increase your &eMine&7's width!"),
                            MUtil.list("&7[&cLOCKED&7]", "", "&7Unlock the ability to increase your &eMine&7's width!"),
                            Material.OAK_FENCE,
                            MUtil.list(), MUtil.list(),
                            MUtil.list(), MUtil.list()),
                    new GUIButtonPurchasable(
                            "&6Mine Width &7(Decrease)",
                            UpgradeName.ARCHITECTURE_WIDTH_DECREASE.get(),
                            33, MUtil.list("", "&7Unlock the ability to increase your &eMine&7's width!"),
                            MUtil.list("&7[&cLOCKED&7]", "", "&7Unlock the ability to increase your &eMine&7's width!"),
                            Material.NETHER_BRICK_FENCE,
                            MUtil.list(), MUtil.list(UpgradeName.ARCHITECTURE_WIDTH_INCREASE.get()),
                            MUtil.list(), MUtil.list()),
                    new GUIButton(
                            "&6BACK",
                            45, MUtil.list("", "&7Main Menu"),
                            MUtil.list(),
                            Material.ARROW,
                            MUtil.list(new ActionOpenGUI("MAIN")), MUtil.list()
                    )
                )
            ),
            "COLLECTION", new UpgradeGUI("&4Mine Blocks", 9 * 3, MUtil.list(
                    new GUIButton(
                            "&6BACK",
                            18, MUtil.list("", "&7Main Menu"),
                            MUtil.list("&7[&cLOCKED&7]"),
                            Material.ARROW,
                            MUtil.list(new ActionOpenGUI("MAIN")), MUtil.list()
                    ),
                    new GUIButtonPurchasableToggleable(
                            "&6Default",
                            UpgradeName.COLLECTION_DEFAULT.get(),
                            10, MUtil.list("", "&7Sell items to the Collector normally."),
                            MUtil.list("&7[&cLOCKED&7]"),
                            Material.STONE_BUTTON,
                            MUtil.list(), MUtil.list(), MUtil.list(),
                            MUtil.list(new ActionMineToggleUpgradeOn(UpgradeName.COLLECTION_DEFAULT.get())),
                            MUtil.list(new ActionCollectionChangeSetting(), new ActionMineToggleUpgradeOff(MUtil.list(UpgradeName.COLLECTION_SELL_ALL.get(), UpgradeName.COLLECTION_SELECT_ALL.get(), UpgradeName.COLLECTION_AUTO_1.get(), UpgradeName.COLLECTION_AUTO_2.get(), UpgradeName.COLLECTION_AUTO_3.get(), UpgradeName.COLLECTION_AUTO_4.get()))),
                            MUtil.list()),
                    new GUIButtonPurchasableToggleable(
                            "&6Select All Button",
                            UpgradeName.COLLECTION_SELECT_ALL.get(),
                            11, MUtil.list("", "&7Unlock a button that allows you to", "&7select all items from your inventory."),
                            MUtil.list("&7[&cLOCKED&7]"),
                            Material.STONE_PRESSURE_PLATE,
                            MUtil.list(), MUtil.list(), MUtil.list(),
                            MUtil.list(new ActionMineToggleUpgradeOn(UpgradeName.COLLECTION_SELECT_ALL.get())),
                            MUtil.list(new ActionCollectionChangeSetting(), new ActionMineToggleUpgradeOff(MUtil.list(UpgradeName.COLLECTION_SELL_ALL.get(), UpgradeName.COLLECTION_DEFAULT.get(), UpgradeName.COLLECTION_AUTO_1.get(), UpgradeName.COLLECTION_AUTO_2.get(), UpgradeName.COLLECTION_AUTO_3.get(), UpgradeName.COLLECTION_AUTO_4.get()))),
                            MUtil.list()),
                    new GUIButtonPurchasableToggleable(
                            "&6Sell All",
                            UpgradeName.COLLECTION_SELL_ALL.get(),
                            12, MUtil.list("", "&7Allows you to click one button to sell", "&7all items instantly."),
                            MUtil.list("&7[&cLOCKED&7]"),
                            Material.STONE,
                            MUtil.list(), MUtil.list(), MUtil.list(),
                            MUtil.list(new ActionMineToggleUpgradeOn(UpgradeName.COLLECTION_SELL_ALL.get())),
                            MUtil.list(new ActionCollectionChangeSetting(), new ActionMineToggleUpgradeOff(MUtil.list(UpgradeName.COLLECTION_DEFAULT.get(), UpgradeName.COLLECTION_SELECT_ALL.get(), UpgradeName.COLLECTION_AUTO_1.get(), UpgradeName.COLLECTION_AUTO_2.get(), UpgradeName.COLLECTION_AUTO_3.get(), UpgradeName.COLLECTION_AUTO_4.get()))),
                            MUtil.list()),
                    new GUIButtonPurchasableToggleable(
                            "&6Auto Sell I",
                            UpgradeName.COLLECTION_AUTO_1.get(),
                            14, MUtil.list("", "&7Automatically sell items every 60 seconds."),
                            MUtil.list("&7[&cLOCKED&7]"),
                            Material.HOPPER,
                            MUtil.list(), MUtil.list(), MUtil.list(),
                            MUtil.list(new ActionMineToggleUpgradeOn(UpgradeName.COLLECTION_AUTO_1.get())),
                            MUtil.list(new ActionCollectionChangeSetting(), new ActionMineToggleUpgradeOff(MUtil.list(UpgradeName.COLLECTION_SELL_ALL.get(), UpgradeName.COLLECTION_SELECT_ALL.get(), UpgradeName.COLLECTION_DEFAULT.get(), UpgradeName.COLLECTION_AUTO_2.get(), UpgradeName.COLLECTION_AUTO_3.get(), UpgradeName.COLLECTION_AUTO_4.get()))),
                            MUtil.list()),
                    new GUIButtonPurchasableToggleable(
                            "&6Auto Sell II",
                            UpgradeName.COLLECTION_AUTO_2.get(),
                            15, MUtil.list("", "&7Automatically sell items every 30 seconds."),
                            MUtil.list("&7[&cLOCKED&7]"),
                            Material.HOPPER,
                            MUtil.list(), MUtil.list(), MUtil.list(),
                            MUtil.list(new ActionMineToggleUpgradeOn(UpgradeName.COLLECTION_AUTO_2.get())),
                            MUtil.list(new ActionCollectionChangeSetting(), new ActionMineToggleUpgradeOff(MUtil.list(UpgradeName.COLLECTION_SELL_ALL.get(), UpgradeName.COLLECTION_SELECT_ALL.get(), UpgradeName.COLLECTION_DEFAULT.get(), UpgradeName.COLLECTION_AUTO_1.get(), UpgradeName.COLLECTION_AUTO_3.get(), UpgradeName.COLLECTION_AUTO_4.get()))),
                            MUtil.list()),
                    new GUIButtonPurchasableToggleable(
                            "&6Auto Sell III",
                            UpgradeName.COLLECTION_AUTO_3.get(),
                            16, MUtil.list("", "&7Automatically sell items every 15 seconds."),
                            MUtil.list("&7[&cLOCKED&7]"),
                            Material.HOPPER,
                            MUtil.list(), MUtil.list(), MUtil.list(),
                            MUtil.list(new ActionMineToggleUpgradeOn(UpgradeName.COLLECTION_AUTO_3.get())),
                            MUtil.list(new ActionCollectionChangeSetting(), new ActionMineToggleUpgradeOff(MUtil.list(UpgradeName.COLLECTION_SELL_ALL.get(), UpgradeName.COLLECTION_SELECT_ALL.get(), UpgradeName.COLLECTION_DEFAULT.get(), UpgradeName.COLLECTION_AUTO_1.get(), UpgradeName.COLLECTION_AUTO_2.get(), UpgradeName.COLLECTION_AUTO_4.get()))),
                            MUtil.list())
            )),
            "REGENERATION", new UpgradeGUI("&4Mine Blocks", 9 * 5, MUtil.list(
                    new GUIButton(
                            "&6BACK",
                            36, MUtil.list("", "&7Main Menu"),
                            MUtil.list(),
                            Material.ARROW,
                            MUtil.list(new ActionOpenGUI("MAIN")), MUtil.list())
            )),
            "ROBOTICS", new UpgradeGUI("&4Mine Blocks", 9 * 5, MUtil.list(
                    new GUIButton(
                            "&6BACK",
                            36, MUtil.list("", "&7Main Menu"),
                            MUtil.list(),
                            Material.ARROW,
                            MUtil.list(new ActionOpenGUI("MAIN")), MUtil.list())
            )),
            "MOBILITY", new UpgradeGUI("&4Mine Blocks", 9 * 3, MUtil.list(
                    new GUIButtonPurchasableToggleable(
                            "&6Ladders 1",
                            UpgradeName.MOBILITY_LADDER_1.get(),
                            11, MUtil.list("", "&7A single ladder on one side of the mine."),
                            MUtil.list("&7[&cLOCKED&7]"),
                            Material.LADDER,
                            MUtil.list(), MUtil.list(), MUtil.list(),
                            MUtil.list(new ActionMineToggleUpgradeOn(UpgradeName.MOBILITY_LADDER_1.get())),
                            MUtil.list(new ActionMineSetMobility(TypeMobility.LADDER_1), new ActionMineToggleUpgradeOff(MUtil.list(UpgradeName.MOBILITY_JUMP_PAD.get(), UpgradeName.MOBILITY_LADDER_2.get(), UpgradeName.MOBILITY_LADDER_3.get(), UpgradeName.MOBILITY_FLIGHT.get()))),
                            MUtil.list()),
                    new GUIButtonPurchasableToggleable(
                            "&6Ladders 2",
                            UpgradeName.MOBILITY_LADDER_2.get(),
                            12, MUtil.list("", "&7A single ladder on every side of the mine."),
                            MUtil.list("&7[&cLOCKED&7]"),
                            Material.LADDER,
                            MUtil.list(), MUtil.list(), MUtil.list(),
                            MUtil.list(new ActionMineToggleUpgradeOn(UpgradeName.MOBILITY_LADDER_2.get())),
                            MUtil.list(new ActionMineSetMobility(TypeMobility.LADDER_2), new ActionMineToggleUpgradeOff(MUtil.list(UpgradeName.MOBILITY_JUMP_PAD.get(), UpgradeName.MOBILITY_LADDER_1.get(), UpgradeName.MOBILITY_LADDER_3.get(), UpgradeName.MOBILITY_FLIGHT.get()))),
                            MUtil.list()),
                    new GUIButtonPurchasableToggleable(
                            "&6Ladders 3",
                            UpgradeName.MOBILITY_LADDER_3.get(),
                            13, MUtil.list("", "&7Ladders cover the length of every side of the mine."),
                            MUtil.list("&7[&cLOCKED&7]"),
                            Material.LADDER,
                            MUtil.list(), MUtil.list(), MUtil.list(),
                            MUtil.list(new ActionMineToggleUpgradeOn(UpgradeName.MOBILITY_LADDER_3.get())),
                            MUtil.list(new ActionMineSetMobility(TypeMobility.FULL_LADDER), new ActionMineToggleUpgradeOff(MUtil.list(UpgradeName.MOBILITY_JUMP_PAD.get(), UpgradeName.MOBILITY_LADDER_1.get(), UpgradeName.MOBILITY_LADDER_2.get(),  UpgradeName.MOBILITY_FLIGHT.get()))),
                            MUtil.list()),
                    new GUIButtonPurchasableToggleable(
                            "&6Jump Pad",
                            UpgradeName.MOBILITY_JUMP_PAD.get(),
                            14, MUtil.list("", "&7Replace your old ladders with high tech jump pads!"),
                            MUtil.list("&7[&cLOCKED&7]"),
                            Material.HEAVY_WEIGHTED_PRESSURE_PLATE,
                            MUtil.list(), MUtil.list(), MUtil.list(),
                            MUtil.list(new ActionMineToggleUpgradeOn(UpgradeName.MOBILITY_JUMP_PAD.get())),
                            MUtil.list(new ActionMineSetMobility(TypeMobility.JUMP_PAD), new ActionMineToggleUpgradeOff(MUtil.list(UpgradeName.MOBILITY_LADDER_1.get(), UpgradeName.MOBILITY_LADDER_2.get(), UpgradeName.MOBILITY_LADDER_3.get(), UpgradeName.MOBILITY_FLIGHT.get()))),
                            MUtil.list()),
                    new GUIButtonPurchasableToggleable(
                            "&6Flight",
                            UpgradeName.MOBILITY_FLIGHT.get(),
                            15, MUtil.list("", "&7Learn to fly!"),
                            MUtil.list("&7[&cLOCKED&7]"),
                            Material.LADDER,
                            MUtil.list(), MUtil.list(), MUtil.list(),
                            MUtil.list(new ActionMineToggleUpgradeOn(UpgradeName.MOBILITY_FLIGHT.get())),
                            MUtil.list(new ActionMineSetMobility(TypeMobility.LADDER_1), new ActionMineToggleUpgradeOff(MUtil.list(UpgradeName.MOBILITY_JUMP_PAD.get(), UpgradeName.MOBILITY_LADDER_1.get(), UpgradeName.MOBILITY_LADDER_2.get(), UpgradeName.MOBILITY_LADDER_3.get()))),
                            MUtil.list()),
                    new GUIButton(
                            "&6BACK",
                            18, MUtil.list("", "&7Main Menu"),
                            MUtil.list("&7[&cLOCKED&7]"),
                            Material.ARROW,
                            MUtil.list(new ActionOpenGUI("MAIN")), MUtil.list())
            )),

            "BUILDINGS", new UpgradeGUI("&4Mine Buildings", 9 * 3, MUtil.list(
                    new GUIButton(
                            "&6Portal",
                            11, MUtil.list("", "&e&lCOST", "&a⛃100.00", "&b✪10", "", "&7Build a portal to the &6Old Town&7."),
                            MUtil.list("&7[&cLOCKED&7]", "", "&7&lREQUIREMENTS","&7- &eCell &7level &a3"),
                            Material.END_PORTAL_FRAME,
                            MUtil.list(new ActionMessage("&7You have unlocked a portal!"), new ActionBuildingUnlock(BuildingType.PORTAL)),
                            MUtil.list(UpgradeName.CATEGORY_BUILDINGS.get())),

                    new GUIButton(
                            "&6Enchantment Table",
                            12, MUtil.list("","&e&lCOST", "&a⛃100.00", "&b✪50", "", "&7Build an enchantment table to enchant your tools."),
                            MUtil.list("&7[&cLOCKED&7]", "", "&7&lREQUIREMENTS","&7- &eCell &7level &a4"),
                            Material.ENCHANTING_TABLE,
                            MUtil.list(new ActionMessage("&7You have unlocked an enchantment table!"), new ActionBuildingUnlock(BuildingType.ENCHANT_TABLE)),
                            MUtil.list(UpgradeName.BUILDING_ENCHANT_TABLE.get())),
                    new GUIButton(
                            "&6Trash Can",
                            13, MUtil.list("", "&e&lCOST", "&a⛃100.00", "&b✪10", "", "&7Unlock a trash can for your mine!"),
                            MUtil.list("&7[&cLOCKED&7]", "", "&7&lREQUIREMENTS","&7- &eCell &7level &a1", "&7- Player Life &b2"),
                            Material.HOPPER,
                            MUtil.list(), MUtil.list(UpgradeName.BUILDING_TRASH_CAN.get())),
                    new GUIButton(
                            "&6Anvil",
                            14, MUtil.list("&e&lCOST", "&a⛃1.0M", "&b✪200", "&7Build an anvil to edit your tools."),
                            MUtil.list("&7[&cLOCKED&7]", "", "&7&lREQUIREMENTS","&7- &eCell &7level &a4", "&7- Player Life &b5"),
                            Material.ANVIL,
                            MUtil.list(new ActionMessage("&7You have unlocked an anvil!"), new ActionBuildingUnlock(BuildingType.CHEST)),
                            MUtil.list(UpgradeName.BUILDING_ANVIL.get())),
                    new GUIButton(
                            "&6Beacon",
                            15, MUtil.list("&e&lCOST", "&a⛃1.0B", "&b✪500", "&7Build a beacon to unlock potion boosts."),
                            MUtil.list("&7[&cLOCKED&7]", "&7&lREQUIREMENTS","&7- &eCell &7level &a10", "&7- Player Life &b10"),
                            Material.BEACON,
                            MUtil.list(new ActionMessage("&7You have unlocked an anvil!"), new ActionBuildingUnlock(BuildingType.BEACON)),
                            MUtil.list(UpgradeName.BUILDING_BEACON.get())),
                    new GUIButton(
                            "&6BACK",
                                    18, MUtil.list("", "&7Main Menu"),
                            MUtil.list("&7[&cLOCKED&7]"),
                            Material.ARROW,
                            MUtil.list(new ActionOpenGUI("MAIN")), MUtil.list())
            )),

            "STORAGE", new UpgradeGUI("&4Mine Storage", 9 * 3, MUtil.list(
                    new GUIButton(
                            "&6Chest",
                            13, MUtil.list("", "&7Unlock a storage chest"),
                            MUtil.list("&7[&cLOCKED&7]"),
                            Material.CHEST,
                            MUtil.list(), MUtil.list("BUILDINGS_UNLOCKED")),
                    new GUIButton(
                            "&6BACK",
                            18, MUtil.list("", "&7Buildings"),
                            MUtil.list("&7[&cLOCKED&7]"),
                            Material.ARROW,
                            MUtil.list(new ActionOpenGUI("BUILDINGS")), MUtil.list())
            ))
    );

    @Override
    public UpgradesConf load(UpgradesConf that)
    {
        super.load(that);
        return this;
    }

    private static List<AbstractAction> listU(AbstractAction... upgrades){
        return new ArrayList<>(Arrays.asList(upgrades));
    }

    private static List<String> listS(String... strings){
        return new ArrayList<>(Arrays.asList(strings));
    }

}

