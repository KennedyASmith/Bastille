package com.kennedysmithjava.prisoncore.upgrades.buttons;

import com.kennedysmithjava.prisoncore.eco.Cost;
import com.kennedysmithjava.prisoncore.upgrades.actions.AbstractAction;
import com.massivecraft.massivecore.util.MUtil;
import org.bukkit.Material;

import java.util.List;

public class GUIButtonPurchasable extends GUIButton {

    List<Cost> costs;
    List<AbstractAction> onPurchase;
    String associatedUpgrade;

    public GUIButtonPurchasable(String name, String associatedUpgrade, int slot, List<String> lore, List<String> lockedLore, Material material, List<AbstractAction> onClick, List<String> requiredUnlockedUpgrades, List<Cost> costs, List<AbstractAction> onPurchase) {
        super(name, slot, lore, lockedLore, material, onClick, requiredUnlockedUpgrades);
        this.associatedUpgrade = associatedUpgrade;
        this.costs = costs;
        this.onPurchase = onPurchase;
        this.buttonTypeList = MUtil.list(ButtonType.PURCHASABLE);
    }

    public List<Cost> getCosts() {
        return costs;
    }

    public List<AbstractAction> getOnPurchase() {
        return onPurchase;
    }

    public String getAssociatedUpgrade() {
        return associatedUpgrade;
    }
}
