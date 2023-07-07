package com.kennedysmithjava.prisoncore.gui.buttons;

import com.kennedysmithjava.prisoncore.eco.Cost;
import com.kennedysmithjava.prisoncore.entity.mines.Mine;
import com.kennedysmithjava.prisoncore.entity.mines.upgrades.actions.ActionMineSize;
import com.kennedysmithjava.prisoncore.entity.player.MPlayer;
import com.massivecraft.massivecore.util.MUtil;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;

public class GuiMineSizeButton extends GuiButton {

    List<Integer> unlockedDistributions;
    int distributionID;

    public GuiMineSizeButton(MPlayer player, String name, int width, int height, int slot, List<String> lore, Material material, List<Cost> additionalCosts) {
        super(name, "&7[UPGRADE]", slot, lore, material,
                new ArrayList<>(), null, () -> {
                    player.msg("&7[&bServer]&7 You changed your mine's size!");
                    new ActionMineSize(width, height).apply(player);
                }, additionalCosts);

    }

    @Override
    public boolean isUnlocked(Mine mine, MPlayer player) {
        return true;
    }

    @Override
    public boolean isPurchased(Mine mine, MPlayer player) {
        return false;
    }

    @Override
    public boolean isActive(Mine mine) {
        return false;
    }

    public List<String> getBuyPrompt(boolean isUnlocked){
        return MUtil.list(" &r", "&eClick to purchase!", " &r", " &7&lREQUIREMENTS");
    }


}
