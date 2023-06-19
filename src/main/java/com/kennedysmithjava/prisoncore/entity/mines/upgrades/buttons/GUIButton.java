package com.kennedysmithjava.prisoncore.entity.mines.upgrades.buttons;

import com.kennedysmithjava.prisoncore.entity.mines.upgrades.actions.AbstractAction;
import com.massivecraft.massivecore.util.MUtil;
import org.bukkit.Material;

import java.util.List;

public class GUIButton {
    String name;
    int slot;
    List<String> lore;
    List<String> lockedLore;
    Material material;
    List<AbstractAction> onClick;
    List<String> requiredUnlockedUpgrades;
    List<ButtonType> buttonTypeList;

    public GUIButton(String name, int slot, List<String> lore, List<String> lockedLore, Material material, List<AbstractAction> onClick, List<String> requiredUnlockedUpgrades) {
        this.name = name;
        this.slot = slot;
        this.lore = lore;
        this.lockedLore = lockedLore;
        this.material = material;
        this.onClick = onClick;
        this.requiredUnlockedUpgrades = requiredUnlockedUpgrades;
        this.buttonTypeList = MUtil.list();
    }

    public String getName() {
        return name;
    }

    public List<String> getRequiredUnlockedUpgrades() {
        return requiredUnlockedUpgrades;
    }

    public List<AbstractAction> getOnClick() {
        return onClick;
    }

    public List<String> getLore() {
        return lore;
    }

    public int getSlot() {
        return slot;
    }

    public Material getMaterial() {
        return material;
    }

    public List<String> getLockedLore() {
        return lockedLore;
    }

    public List<ButtonType> getButtonTypeList() {
        return buttonTypeList;
    }
}
