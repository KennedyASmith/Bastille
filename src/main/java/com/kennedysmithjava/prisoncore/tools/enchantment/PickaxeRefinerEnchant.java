package com.kennedysmithjava.prisoncore.tools.enchantment;

import com.kennedysmithjava.prisoncore.event.MineBlockBreakEvent;
import com.kennedysmithjava.prisoncore.entity.tools.EnchantConf;
import org.bukkit.Material;

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
    public void onBreak(MineBlockBreakEvent event, int enchantLevel) {
        double r = random.nextDouble();
        double chance = (SLOPE * enchantLevel) + 1;
        boolean noRewards = r < chance;
        if(!noRewards){
            event.setAwardMultiplier(2D);
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
    protected List<String> getUnformattedGUILore() {
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

}
