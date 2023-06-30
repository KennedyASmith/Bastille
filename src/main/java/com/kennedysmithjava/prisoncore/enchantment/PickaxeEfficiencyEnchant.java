package com.kennedysmithjava.prisoncore.enchantment;

import com.kennedysmithjava.prisoncore.crafting.Recipe;
import com.kennedysmithjava.prisoncore.eco.Cost;
import com.kennedysmithjava.prisoncore.eco.CostCurrency;
import com.kennedysmithjava.prisoncore.eco.CostSkillLevel;
import com.kennedysmithjava.prisoncore.eco.CurrencyType;
import com.kennedysmithjava.prisoncore.entity.tools.EnchantConf;
import com.kennedysmithjava.prisoncore.skill.SkillType;
import com.massivecraft.massivecore.util.MUtil;
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
    public List<String> getUnformattedGUILore() {
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

    @Override
    public Recipe getEnchantBookRecipe() {
        return Recipe.ENCHANT_BOOK_EFFICIENCY;
    }

    @Override
    public List<Cost> getCraftCosts() {
        return MUtil.list(
                new CostCurrency(CurrencyType.CASH, 1000),
                new CostSkillLevel(SkillType.ENCHANTING, 2),
                new CostSkillLevel(SkillType.CRAFTING, 2)
        );
    }

    @Override
    public List<Cost> getApplyCosts() {
        return MUtil.list(
                new CostCurrency(CurrencyType.CASH, 500),
                new CostSkillLevel(SkillType.ENCHANTING, 2)
        );
    }
}
