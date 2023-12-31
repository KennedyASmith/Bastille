package com.kennedysmithjava.prisoncore.gui.buttons;

import com.kennedysmithjava.prisoncore.eco.Cost;
import com.kennedysmithjava.prisoncore.entity.mines.Mine;
import com.kennedysmithjava.prisoncore.entity.mines.upgrades.UpgradeName;
import com.kennedysmithjava.prisoncore.entity.player.MPlayer;
import com.kennedysmithjava.prisoncore.gui.BaseGui;
import com.massivecraft.massivecore.util.MUtil;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;

public class GuiOpenerButton extends GuiButton {

    BaseGui destinationGUI;
    Runnable onPurchase;

    public GuiOpenerButton(String displayName, String buttonTag, int slot,
                           List<String> lore, Material material, BaseGui destinationGUI, List<UpgradeName> requiredUpgradesToUnlock,
                           List<Cost> additionalCosts) {
        super(displayName, buttonTag, slot, lore, material, requiredUpgradesToUnlock, null, () -> {}, additionalCosts);

        this.destinationGUI = destinationGUI;
    }

    public GuiOpenerButton(String displayName, String buttonTag, int slot,
                           List<String> lore, Material material, BaseGui destinationGUI,
                           UpgradeName thisUpgrade, List<UpgradeName> requiredUpgradesToUnlock,
                           List<Cost> additionalCosts, Runnable onPurchase) {
        super(displayName, buttonTag, slot, lore, material, requiredUpgradesToUnlock, thisUpgrade,
                () -> {}, additionalCosts);
        this.destinationGUI = destinationGUI;
        this.onPurchase = onPurchase;
    }


    @Override
    public List<String> getBuyPrompt(boolean isUnlocked) {
        if(getThisUpgrade() == null){
            return MUtil.list(" &r", "&7&lREQUIREMENTS");
        }else {
            return super.getBuyPrompt(isUnlocked);
        }
    }

    @Override
    public Runnable getOnPurchase() {
        return onPurchase;
    }

    @Override
    public Runnable getOnClick() {
        BaseGui returnableGui = destinationGUI.getReturnMenu();
        return () -> {
            GuiOpenerButton.super.getOnClick().run();
            returnableGui.close();
            destinationGUI.open();
        };
    }

    @Override
    public boolean isActive(Mine mine) {
        if(getThisUpgrade() == null) {
            return false;
        }else {
            return super.isActive(mine);
        }
    }

    @Override
    public List<String> getUnlockRequirements() {
        List<String> list = new ArrayList<>();
        for (UpgradeName upgrade : getRequiredUnlockedUpgrades()) {
            list.add("&7- " + upgrade.getDisplayName() + " &7required.");
        }
        return list;
    }

    @Override
    public boolean isPurchased(Mine mine, MPlayer player) {
        if(getThisUpgrade() == null){
            return isUnlocked(mine, player);
        }else {
            return super.isPurchased(mine, player);
        }
    }

    @Override
    public boolean isUnlocked(Mine mine, MPlayer player) {
        if(getThisUpgrade() == null){
            boolean hasRequiredUpgrades = super.isUnlocked(mine, player);
            return isAffordable(player) && hasRequiredUpgrades;
        }else {
            return super.isUnlocked(mine, player);
        }
    }
}
