package com.kennedysmithjava.prisoncore.gui.buttons;

import com.kennedysmithjava.prisoncore.eco.Cost;
import com.kennedysmithjava.prisoncore.entity.mines.Mine;
import com.kennedysmithjava.prisoncore.entity.mines.upgrades.UpgradeName;
import com.kennedysmithjava.prisoncore.entity.player.MPlayer;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;

public class GuiToggleButton extends GuiButton {
    private final List<UpgradeName> toggleOff;
    private final MPlayer player;

    public GuiToggleButton(MPlayer player, String name, UpgradeName thisUpgrade,
                           Material material, int slot,
                           List<String> lore, List<UpgradeName> requiredUpgrades,
                           List<UpgradeName> toggleOff, List<Cost> additionalCosts, Runnable onClick) {
        super(name, "", slot, lore, material, requiredUpgrades, thisUpgrade, onClick, additionalCosts);
        this.toggleOff = toggleOff;
        this.player = player;
    }

    public GuiToggleButton(MPlayer player, UpgradeName thisUpgrade, Material material, int slot,
                           List<String> lore, List<UpgradeName> requiredUpgrades, List<UpgradeName> toggleOff, Runnable onClick) {
        super(thisUpgrade.getDisplayName(), "", slot, lore, material, requiredUpgrades, thisUpgrade, onClick, thisUpgrade.getCosts());
        this.toggleOff = toggleOff;
        this.player = player;
    }

    @Override
    public String getButtonTag(boolean isUnlocked, boolean isAffordable, boolean isPurchased, boolean isActive) {
        if(!isPurchased){
            return super.getButtonTag(isUnlocked, isAffordable, isPurchased, isActive);
        }else {
            if(!isActive){
                return "&7[INACTIVE]";
            }else {
                return "&7[&aACTIVE&7]";
            }
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
    public Runnable getOnClick() {
        Runnable runnable = super.getOnClick();
        return () -> {
            Mine mine = player.getMine();
            for (UpgradeName upgradeName : toggleOff) {
                mine.deactivateUpgrade(upgradeName.get());
            }
            mine.activateUpgrade(getThisUpgrade().get());
            runnable.run();
            player.msg("&7[&bServer&7] &aYou have activated " + getThisUpgrade().getDisplayName() + "&a!");
        };
    }
}
