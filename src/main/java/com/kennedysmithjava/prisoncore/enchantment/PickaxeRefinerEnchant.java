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
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.List;
import java.util.Random;

public class PickaxeRefinerEnchant extends BlockBreakEnchant<PickaxeRefinerEnchant> {

    public static PickaxeRefinerEnchant i = new PickaxeRefinerEnchant();
    public static PickaxeRefinerEnchant get() {
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
            event.setAwardMultiplier(2D);
            Player player = event.getPlayer();
            if(player != null)
                player.sendMessage("&7[&b&l⛏&7] The value of the block(s) you just mined were multiplied by your &bRefiner &7enchant!!");
        }
    }

    @Override
    public int getMaxLevel() {
        return EnchantConf.get().PickaxeRefinerEnchantMaxLevel;
    }

    @Override
    public int getMinLevel() {
        return EnchantConf.get().PickaxeRefinerEnchantMinLevel;
    }

    @Override
    public String getID() {
        return "Refiner";
    }

    @Override
    public String getDisplayName() {
        return EnchantConf.get().PickaxeRefinerEnchantDisplayName;
    }

    @Override
    public Collection<String> getAllowedToolIDs() {
        return null;
    }

    @Override
    public int getEnchantGUISlot() {
        return EnchantConf.get().PickaxeRefinerEnchantGUISlot;
    }

    @Override
    public List<String> getUnformattedGUILore() {
        return EnchantConf.get().PickaxeRefinerEnchantGUILore;
    }

    @Override
    public String getMagnitudeString(int level) {
        return String.valueOf(level);
    }

    @Override
    public String getName() {
        return EnchantConf.get().PickaxeRefinerEnchantName;
    }

    @Override
    public String getLore() {
        return EnchantConf.get().PickaxeRefinerEnchantLore;
    }

    @Override
    public Material getIcon() {
        return EnchantConf.get().PickaxeRefinerEnchantIcon;
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
