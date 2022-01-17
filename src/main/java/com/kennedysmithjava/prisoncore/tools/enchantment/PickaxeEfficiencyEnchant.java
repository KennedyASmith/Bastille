package com.kennedysmithjava.prisoncore.tools.enchantment;

import com.kennedysmithjava.prisoncore.entity.tools.EnchantConf;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;

import java.util.Collection;
import java.util.List;

public class PickaxeEfficiencyEnchant extends VanillaEnchant<PickaxeEfficiencyEnchant>{

    public static PickaxeEfficiencyEnchant i = new PickaxeEfficiencyEnchant();
    public static PickaxeEfficiencyEnchant get() {
        return i;
    }

    @Override
    public Enchantment getEnchantment() {
        return Enchantment.DIG_SPEED;
    }

    @Override
    public String getID() {
        return "Efficiency";
    }

    @Override
    public String getDisplayName() {
        return EnchantConf.get().PickaxeEfficiencyEnchantDisplayName;
    }

    @Override
    public int getMaxLevel() {
        return EnchantConf.get().PickaxeEfficiencyEnchantMaxLevel;
    }

    @Override
    public int getMinLevel() {
        return EnchantConf.get().PickaxeEfficiencyEnchantMinLevel;
    }

    @Override
    public Collection<String> getAllowedToolIDs() {
        return null;
    }

    @Override
    public int getEnchantGUISlot() {
        return EnchantConf.get().PickaxeEfficiencyEnchantGUISlot;
    }

    @Override
    protected List<String> getUnformattedGUILore() {
        return EnchantConf.get().PickaxeEfficiencyEnchantGUILore;
    }

    @Override
    public String getMagnitudeString(int level) {
        return String.valueOf(level);
    }

    @Override
    public String getName() {
        return EnchantConf.get().PickaxeEfficiencyEnchantName;
    }

    @Override
    public String getLore() {
        return EnchantConf.get().PickaxeEfficiencyEnchantLore;
    }

    @Override
    public Material getIcon() {
        return EnchantConf.get().PickaxeEfficiencyEnchantIcon;
    }
}
