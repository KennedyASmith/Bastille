package com.kennedysmithjava.prisoncore.gui.buttons;

import com.kennedysmithjava.prisoncore.eco.Cost;
import com.kennedysmithjava.prisoncore.entity.mines.Distribution;
import com.kennedysmithjava.prisoncore.entity.mines.Mine;
import com.kennedysmithjava.prisoncore.entity.player.MPlayer;
import com.kennedysmithjava.prisoncore.util.Color;
import com.massivecraft.massivecore.util.MUtil;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;

public class GuiDistributionButton extends GuiButton {

    List<Integer> unlockedDistributions;
    int distributionID;

    public GuiDistributionButton(MPlayer player, Distribution distribution, int distributionID, int slot, List<String> lore, Material material, List<Cost> additionalCosts) {
        super(distribution.getName(), "&7[BLOCK SET]", slot, lore, material,
                new ArrayList<>(), null, () -> {
                    player.sendMessage(Color.get("&7[&bServer]&7 You changed your mine's block set!"));
                    player.getMine().setBlockDistribution(distributionID);
                }, additionalCosts);

        this.distributionID = distributionID;
        this.unlockedDistributions = player.getMine().getUnlockedDistributions();
    }

    @Override
    public boolean isUnlocked(Mine mine, MPlayer player) {
        return unlockedDistributions.contains(distributionID);
    }

    @Override
    public boolean isActive(Mine mine) {
        return mine.getCurrentDistributionID() == distributionID;
    }

    public List<String> getBuyPrompt(boolean isUnlocked){
        if(isUnlocked){
            return MUtil.list(" &r", "&eClick to purchase!", " &r", " &7&lREQUIREMENTS");
        } else {
            return MUtil.list(" &r", "&cThis block set is locked!");
        }
    }
}
