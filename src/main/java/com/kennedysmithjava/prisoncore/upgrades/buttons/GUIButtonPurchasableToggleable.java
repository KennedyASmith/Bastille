package com.kennedysmithjava.prisoncore.upgrades.buttons;

import com.kennedysmithjava.prisoncore.eco.Cost;
import com.kennedysmithjava.prisoncore.upgrades.actions.AbstractAction;
import com.massivecraft.massivecore.util.MUtil;
import org.bukkit.Material;

import java.util.List;

public class GUIButtonPurchasableToggleable extends GUIButtonPurchasable{

    String toggleableUpgrade;
    List<AbstractAction> onToggleOn;
    List<AbstractAction> onToggleOff;

    public GUIButtonPurchasableToggleable(String name, String toggleableUpgrade, int slot, List<String> lore, List<String> lockedLore, Material material, List<AbstractAction> onClick, List<String> requiredUnlockedUpgrades, List<Cost> costs, List<AbstractAction> onPurchase, List<AbstractAction> onToggleOn, List<AbstractAction> onToggleOff) {
        super(name, toggleableUpgrade, slot, lore, lockedLore, material, onClick, requiredUnlockedUpgrades, costs, onPurchase);
        this.onToggleOff = onToggleOff;
        this.onToggleOn = onToggleOn;
        this.buttonTypeList = MUtil.list(ButtonType.PURCHASABLE, ButtonType.TOGGLEABLE);
    }

    public List<AbstractAction> getOnToggleOff() {
        return onToggleOff;
    }

    public List<AbstractAction> getOnToggleOn() {
        return onToggleOn;
    }
}
