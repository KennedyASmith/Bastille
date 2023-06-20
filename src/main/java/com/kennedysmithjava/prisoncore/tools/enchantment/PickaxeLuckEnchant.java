package com.kennedysmithjava.prisoncore.tools.enchantment;

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

public class PickaxeLuckEnchant extends BlockBreakEnchant<PickaxeLuckEnchant> {

    public static PickaxeLuckEnchant i = new PickaxeLuckEnchant();
    public static PickaxeLuckEnchant get() {
        return i;
    }
    public static Random random = new Random();

    @Override
    public void onBreak(EventMineBlockBreak event, int enchantLevel) {
        /*Distribution distribution = event.getDistribution();
        double r = random.nextDouble();
        double chance = 0.02 + (enchantLevel * 0.02);
        if(r < chance){
            Map<BlockWrapper, Double> rates = distribution.getRates();
            Object[] values = rates.values().toArray();
            BlockWrapper material = (BlockWrapper) values[random.nextInt(values.length)];
            PrisonBlock pb = distribution.generatePrisonBlock(material.getMaterial(), material.getBlockData());
            event.addReward(pb);
        }*/
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
        return EnchantConf.get().PickaxeLuckEnchantDisplayName;
    }

    @Override
    public Collection<String> getAllowedToolIDs() {
        return null;
    }

    @Override
    public int getEnchantGUISlot() {
        return EnchantConf.get().PickaxeLuckEnchantGUISlot;
    }

    @Override
    public List<String> getUnformattedGUILore() {
        return EnchantConf.get().PickaxeLuckEnchantGUILore;
    }

    @Override
    public String getMagnitudeString(int level) {
        return "Chance: " + (0.02 + (level * 0.02)) + "%";
    }

    @Override
    public String getName() {
        return EnchantConf.get().PickaxeLuckEnchantName;
    }

    @Override
    public String getLore() {
        return EnchantConf.get().PickaxeLuckEnchantLore;
    }

    @Override
    public Material getIcon() {
        return EnchantConf.get().PickaxeLuckEnchantIcon;
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
