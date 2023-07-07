package com.kennedysmithjava.prisoncore.gui.buttons;

import com.kennedysmithjava.prisoncore.eco.Cost;
import com.kennedysmithjava.prisoncore.entity.mines.Distribution;
import com.kennedysmithjava.prisoncore.entity.mines.Mine;
import com.kennedysmithjava.prisoncore.entity.player.MPlayer;
import com.massivecraft.massivecore.util.MUtil;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;

public class GuiDistributionButton extends GuiButton {

    List<Integer> unlockedDistributions;
    List<Integer> purchasedDistributions;
    int distributionID;

    public GuiDistributionButton(MPlayer player, Distribution distribution, int distributionID, int slot, List<String> lore, Material material, List<Cost> additionalCosts) {
        super(distribution.getName(), "&7[BLOCK SET]", slot, lore, material,
                new ArrayList<>(), null, () -> {
                    player.msg("&7[&bServer]&7 You changed your mine's block set!");
                    Mine mine = player.getMine();
                    mine.addPurchasedDistribution(distributionID);
                    player.getMine().setBlockDistribution(distributionID);
                }, additionalCosts);

        this.distributionID = distributionID;
        Mine mine = player.getMine();
        this.unlockedDistributions = mine.getUnlockedDistributions();
        this.purchasedDistributions = mine.getPurchasedDistributions();
    }

    @Override
    public boolean isUnlocked(Mine mine, MPlayer player) {
        return unlockedDistributions.contains(distributionID);
    }

    @Override
    public boolean isPurchased(Mine mine, MPlayer player) {
        return purchasedDistributions.contains(distributionID);
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

    @Override
    public String getButtonTag(boolean isUnlocked, boolean isAffordable, boolean isPurchased, boolean isActive) {
        if(!isPurchased){
            if(isUnlocked && isAffordable){
                return "&7[&aPURCHASE&7]";
            } else if(!isAffordable && isUnlocked){
                return "&7[&cCANT AFFORD&7]";
            }  else {
                return "&7[&cLOCKED&7]";
            }
        }else{
            if(isActive){
                return "&7[&cACTIVE&7]";
            }else {
                return "&7[INACTIVE&7]";
            }
        }
    }
}
