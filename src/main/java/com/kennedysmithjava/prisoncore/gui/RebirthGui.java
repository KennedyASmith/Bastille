package com.kennedysmithjava.prisoncore.gui;

import com.kennedysmithjava.prisoncore.eco.CostSkillLevel;
import com.kennedysmithjava.prisoncore.eco.CurrencyType;
import com.kennedysmithjava.prisoncore.engine.EngineLoadingScreen;
import com.kennedysmithjava.prisoncore.entity.mines.Mine;
import com.kennedysmithjava.prisoncore.entity.mines.MinesConf;
import com.kennedysmithjava.prisoncore.entity.mines.UpgradesConf;
import com.kennedysmithjava.prisoncore.entity.mines.objects.UpgradeStatus;
import com.kennedysmithjava.prisoncore.entity.mines.upgrades.UpgradeName;
import com.kennedysmithjava.prisoncore.entity.player.MPlayer;
import com.kennedysmithjava.prisoncore.skill.SkillType;
import com.kennedysmithjava.prisoncore.storage.StorageType;
import com.kennedysmithjava.prisoncore.util.ItemBuilder;
import com.massivecraft.massivecore.chestgui.ChestGui;
import com.massivecraft.massivecore.util.MUtil;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class RebirthGui extends BaseGui {
    public RebirthGui(Player player) {
        super(player, "&d&lRebirth &r&7Main Menu", 3, false, true);
    }

    @Override
    public void onBuild(Player player, ChestGui gui, Inventory inventory) {
        blockFill(Material.BLUE_STAINED_GLASS_PANE, 0, 8);
        blockFill(Material.LIGHT_BLUE_STAINED_GLASS_PANE, 9, 17);
        blockFill(Material.BLUE_STAINED_GLASS_PANE, 18, 26);

        MPlayer mPlayer = MPlayer.get(player);
        int life = mPlayer.getLife();
        int nextLife = life + 1;

        ItemBuilder rewardsButton = new ItemBuilder(Material.ENDER_CHEST)
                .name("&dRebirth Rewards")
                .lore(MUtil.list("&r", "&7Preview rewards obtainable from rebirth!"));
        setItem(14, rewardsButton.build());
        setAction(14, inventoryClickEvent -> {
            close();
            RebirthRewardsGui rebirthRewardsGui = new RebirthRewardsGui(player, this);
            rebirthRewardsGui.open();
            return false;
        });

        ItemBuilder rebirthButton =
                new ItemBuilder(Material.END_CRYSTAL)
                        .name("&d&lBe Reborn")
                        .lore(MUtil.list(
                                "&r",
                                "&7Make a sacrifice for valuable rewards!",
                                "&r",
                                "&bLife &7" + life + " &7→ &e" + nextLife,
                                "&r",
                                "&e&lCOST",
                                "&7- &6Player &7level &e100",
                                "&r",
                                "&cYou will reset:",
                                "&7- Player Level",
                                "&7- Mine upgrades",
                                "&7- All Cash",
                                "&7- Items",
                                "&r",
                                "&aYou will keep:",
                                "&7- Skill levels",
                                "&7- All Gems",
                                "&7- All items in Rebirth Storage",
                                "&7- Collectible items",
                                "&7- Mine Decorations"
                        ));
        setItem(12, rebirthButton.build());
        CostSkillLevel costSkillLevel = new CostSkillLevel(SkillType.PLAYER, 100);
        if(costSkillLevel.hasCost(mPlayer)){
            setAction(12, inventoryClickEvent -> {
                close();
                player.getInventory().clear();
                Mine mine = mPlayer.getMine();
                EngineLoadingScreen.addLoadingScreen(player.getPlayer(), mine.getSpawnPoint());
                mine.setHeight(MinesConf.get().mineDefaultHeight);
                mine.setWidth(MinesConf.get().mineDefaultWidth, () -> {
                    mine.setUpgrades(MUtil.map(
                            UpgradeName.MOBILITY_LADDER_1.get(), new UpgradeStatus(true, true),
                            UpgradesConf.initialUpgrade, new UpgradeStatus(true, true)
                    ));
                    mine.setBlockDistribution(1);
                    for (StorageType type : StorageType.values()) {
                        if(type == StorageType.COLLECTIBLES || type == StorageType.PRESTIGE) continue;
                        mPlayer.removeStorage(type);
                    }
                    mPlayer.setBalance(CurrencyType.CASH, 0.0D);
                    mPlayer.getSkillProfile().getSkill(SkillType.PLAYER).setLevel(1, 0);
                    mPlayer.setLife(nextLife, true);
                    EngineLoadingScreen.removeLoadingScreen(player, 20*3);
                });
                return false;
            });
        }


    }
}
