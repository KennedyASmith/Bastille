package com.kennedysmithjava.prisoncore.gui;

import com.kennedysmithjava.prisoncore.entity.player.RebirthConf;
import com.kennedysmithjava.prisoncore.util.Color;
import com.kennedysmithjava.prisoncore.util.ItemBuilder;
import com.massivecraft.massivecore.chestgui.ChestGui;
import com.massivecraft.massivecore.util.MUtil;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.List;
import java.util.Map;

public class RebirthRewardsGui extends BaseGui {
    public RebirthRewardsGui(Player player, BaseGui returnMenu) {
        super(player, "&d&lRebirth &r&7Main Menu", 5, false, true, returnMenu);
    }

    @Override
    public void onBuild(Player player, ChestGui gui, Inventory inventory) {
        BaseGui baseGui = this;
        RebirthConf conf = RebirthConf.get();
        Map<Integer, List<String>> awardLoreMap = conf.getAwardLore();

        for (int i = 0; i < 45; i++) {
            int life = i + 2;
            List<String> lore = MUtil.list("&7", "&7Awards for life ", "&r");
            lore.add("&7- 1x Random &dReborn &7tier item");
            List<String> awardLore = awardLoreMap.get(life);
            if(awardLore == null) awardLore = awardLoreMap.get(-1);
            lore.addAll(awardLore);
            ItemBuilder reward = new ItemBuilder(Material.TOTEM_OF_UNDYING)
                    .name("&b&lLife &e" + life)
                    .lore(lore);
            setItem(i, reward.build());
        }

        for (int i = 46; i < 52; i++) {
            int life = i + 2;
            List<String> lore = MUtil.list("&7", "&7Awards for life ", "&r");
            lore.add("&7- 1x Random &dReborn &7tier item");
            List<String> awardLore = awardLoreMap.get(life);
            if(awardLore == null) awardLore = awardLoreMap.get(-1);
            lore.addAll(awardLore);
            ItemBuilder reward = new ItemBuilder(Material.TOTEM_OF_UNDYING)
                    .name("&b&lLife &e" + life)
                    .lore(lore);
            setItem(i, reward.build());
        }

        ItemBuilder lastLevel = new ItemBuilder(Material.TOTEM_OF_UNDYING)
                .name("&b&lLife 51+")
                .lore(MUtil.list("&r", "- 3x Random &dReborn &7tier items"));
        setItem(52, lastLevel.build());

        ItemBuilder randomRewards = new ItemBuilder(Material.CHEST)
                .name("&d&lReborn &7Tier Items")
                .lore(MUtil.list("&r", "&7Preview a list of reborn tier items!"));
        setItem(53, randomRewards.build());
        setAction(53, inventoryClickEvent -> {
            close();
            RebirthTierRewardsGui gui1 = new RebirthTierRewardsGui(player, baseGui);
            gui1.open();
            return false;
        });

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
