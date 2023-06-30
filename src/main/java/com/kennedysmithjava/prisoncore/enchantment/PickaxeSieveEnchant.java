package com.kennedysmithjava.prisoncore.enchantment;

import com.kennedysmithjava.prisoncore.crafting.Recipe;
import com.kennedysmithjava.prisoncore.eco.Cost;
import com.kennedysmithjava.prisoncore.eco.CostCurrency;
import com.kennedysmithjava.prisoncore.eco.CostSkillLevel;
import com.kennedysmithjava.prisoncore.eco.CurrencyType;
import com.kennedysmithjava.prisoncore.entity.tools.EnchantConf;
import com.kennedysmithjava.prisoncore.event.EventMineBlockBreak;
import com.kennedysmithjava.prisoncore.skill.SkillType;
import com.massivecraft.massivecore.util.MUtil;
import org.bukkit.Material;

import java.text.DecimalFormat;
import java.util.Collection;
import java.util.List;
import java.util.Random;

public class PickaxeSieveEnchant extends BlockBreakEnchant<PickaxeSieveEnchant> {

    public static PickaxeSieveEnchant i = new PickaxeSieveEnchant();
    public static PickaxeSieveEnchant get() {
        return i;
    }
    public static Random random = new Random();

    @Override
    public void onBreak(EventMineBlockBreak event, int enchantLevel) {
        event.setOreChance(1 - Math.pow(1.05, enchantLevel));
    }

    @Override
    public int getMaxLevel() {
        return EnchantConf.get().PickaxeLuckEnchantMaxLevel;
    }

    @Override
    public int getMinLevel() {
        return EnchantConf.get().PickaxeLuckEnchantMinLevel;
    }

    @Override
    public String getID() {
        return "Luck";
    }

    @Override
    public String getDisplayName() {
        return EnchantConf.get().PickaxeSieveEnchantDisplayName;
    }

    @Override
    public Collection<String> getAllowedToolIDs() {
        return null;
    }

    @Override
    public int getEnchantGUISlot() {
        return EnchantConf.get().PickaxeSieveEnchantGUISlot;
    }

    @Override
    public List<String> getUnformattedGUILore() {
        return EnchantConf.get().PickaxeSieveEnchantGUILore;
    }

    @Override
    public String getMagnitudeString(int level) {
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        String percent = decimalFormat.format(1 - Math.pow(1.05, level));
        return "Chance: " + percent + "%";
    }

    @Override
    public String getName() {
        return EnchantConf.get().PickaxeSieveEnchantName;
    }

    @Override
    public String getLore() {
        return EnchantConf.get().PickaxeSieveEnchantLore;
    }

    @Override
    public Material getIcon() {
        return EnchantConf.get().PickaxeSieveEnchantIcon;
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
