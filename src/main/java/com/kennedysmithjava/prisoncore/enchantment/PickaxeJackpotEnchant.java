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

import java.util.Collection;
import java.util.List;
import java.util.Random;

public class PickaxeJackpotEnchant extends BlockBreakEnchant<PickaxeJackpotEnchant> {

    public static PickaxeJackpotEnchant i = new PickaxeJackpotEnchant();
    public static PickaxeJackpotEnchant get() {
        return i;
    }
    public static Random random = new Random();
    public static double SLOPE = (-1D/20D);

    @Override
    public void onBreak(EventMineBlockBreak event, int enchantLevel) {
        double r = random.nextDouble();
        double chance = (SLOPE * enchantLevel) + 1;
        boolean noRewards = r < chance;
        if(!noRewards){
            event.setBlockMultiplier(2D);
        }
    }

    @Override
    public int getMaxLevel() {
        return EnchantConf.get().PickaxeJackpotEnchantMaxLevel;
    }

    @Override
    public int getMinLevel() {
        return EnchantConf.get().PickaxeJackpotEnchantMinLevel;
    }

    @Override
    public String getID() {
        return "Jackpot";
    }

    @Override
    public String getDisplayName() {
        return EnchantConf.get().PickaxeJackpotEnchantDisplayName;
    }

    @Override
    public Collection<String> getAllowedToolIDs() {
        return null;
    }

    @Override
    public int getEnchantGUISlot() {
        return EnchantConf.get().PickaxeJackpotEnchantGUISlot;
    }

    @Override
    public List<String> getUnformattedGUILore() {
        return EnchantConf.get().PickaxeJackpotEnchantGUILore;
    }

    @Override
    public String getMagnitudeString(int level) {
        return String.valueOf(level);
    }

    @Override
    public String getName() {
        return EnchantConf.get().PickaxeJackpotEnchantName;
    }

    @Override
    public String getLore() {
        return EnchantConf.get().PickaxeJackpotEnchantLore;
    }

    @Override
    public Material getIcon() {
        return EnchantConf.get().PickaxeJackpotEnchantIcon;
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
