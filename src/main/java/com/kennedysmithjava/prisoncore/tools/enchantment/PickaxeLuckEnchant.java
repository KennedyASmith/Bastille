package com.kennedysmithjava.prisoncore.tools.enchantment;

import com.kennedysmithjava.prisoncore.blockhandler.BlockWrapper;
import com.kennedysmithjava.prisoncore.entity.mines.Distribution;
import com.kennedysmithjava.prisoncore.entity.mines.objects.PrisonBlock;
import com.kennedysmithjava.prisoncore.event.EventMineBlockBreak;
import com.kennedysmithjava.prisoncore.entity.tools.EnchantConf;
import org.bukkit.Material;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class PickaxeLuckEnchant extends BlockBreakEnchant<PickaxeLuckEnchant> {

    public static PickaxeLuckEnchant i = new PickaxeLuckEnchant();
    public static PickaxeLuckEnchant get() {
        return i;
    }
    public static Random random = new Random();

    @Override
    public void onBreak(EventMineBlockBreak event, int enchantLevel) {
        Distribution distribution = event.getDistribution();
        double r = random.nextDouble();
        double chance = 0.02 + (enchantLevel * 0.02);
        if(r < chance){
            Map<BlockWrapper, Double> rates = distribution.getRates();
            Object[] values = rates.values().toArray();
            BlockWrapper material = (BlockWrapper) values[random.nextInt(values.length)];
            PrisonBlock pb = distribution.generatePrisonBlock(material.getMaterial(), material.getBlockData());
            event.addReward(pb);
        }
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
    protected List<String> getUnformattedGUILore() {
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

}
