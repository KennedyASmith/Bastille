package com.kennedysmithjava.prisoncore.gui.buttons;

import com.kennedysmithjava.prisoncore.eco.Cost;
import com.kennedysmithjava.prisoncore.entity.mines.Mine;
import com.kennedysmithjava.prisoncore.entity.mines.upgrades.UpgradeName;
import com.kennedysmithjava.prisoncore.entity.player.MPlayer;
import com.kennedysmithjava.prisoncore.gui.BaseGui;
import com.massivecraft.massivecore.util.MUtil;
import org.bukkit.Material;

import java.util.List;

public class GuiOpenerButton extends GuiButton {
    public GuiOpenerButton(String displayName, String buttonTag, int slot,
                           List<String> lore, Material material, BaseGui destinationGUI,
                           BaseGui returningGUI, List<UpgradeName> requiredUpgradesToUnlock,
                           List<Cost> additionalCosts) {
        super(displayName, buttonTag, slot, lore, material, requiredUpgradesToUnlock, null,
                () -> {
            returningGUI.close();
            destinationGUI.open();
        }, additionalCosts);
    }

    @Override
    public List<String> getBuyPrompt() {
        return MUtil.list(" &r", "&7&lREQUIREMENTS");
    }

    @Override
    public boolean isActive(Mine mine) {
        return false;
    }

    @Override
    public boolean isUnlocked(Mine mine, MPlayer player) {
        boolean hasRequiredUpgrades = super.isUnlocked(mine, player);
        return isAffordable(player) && hasRequiredUpgrades;
    }
}
