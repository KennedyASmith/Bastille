package com.kennedysmithjava.prisoncore.tools.enchantment;

import com.kennedysmithjava.prisoncore.entity.mines.Distribution;
import com.kennedysmithjava.prisoncore.entity.mines.objects.PrisonBlock;
import com.kennedysmithjava.prisoncore.entity.tools.EnchantConf;
import com.kennedysmithjava.prisoncore.event.EventMineBlockBreak;
import com.kennedysmithjava.prisoncore.util.regions.LazyRegion;
import org.bukkit.Material;
import org.bukkit.block.Block;

import java.util.Collection;
import java.util.List;
import java.util.Random;

public class PickaxeJackhammerEnchant extends BlockBreakEnchant<PickaxeJackhammerEnchant> {

    public static PickaxeJackhammerEnchant i = new PickaxeJackhammerEnchant();
    public static PickaxeJackhammerEnchant get() {
        return i;
    }
    public static Random random = new Random();
    public static double SLOPE = (-1D/65D); //38.5% chance at level 25

    @Override
    public void onBreak(EventMineBlockBreak event, int enchantLevel) {
        double r = random.nextDouble();
        double chance = (SLOPE * enchantLevel) + 1;
        boolean noRewards = r < chance;
        if(!noRewards){
            Block origin = event.getBlock();
            int y = origin.getY();
            Distribution distribution = event.getDistribution();
            LazyRegion region = event.getMineRegion();
            for (int x = region.getMinX(); x < region.getMaxX(); x++) {
                for (int z = region.getMinZ(); z < region.getMaxZ(); z++) {
                    Block block = origin.getWorld().getBlockAt(x,y,z);
                    PrisonBlock pb = distribution.generatePrisonBlock(block.getType(), block.getBlockData());
                    event.addReward(pb);
                    block.setType(Material.AIR);
                }
            }
        }
    }

    @Override
    public int getMaxLevel() {
        return EnchantConf.get().PickaxeJackhammerEnchantMaxLevel;
    }

    @Override
    public int getMinLevel() {
        return EnchantConf.get().PickaxeJackhammerEnchantMinLevel;
    }

    @Override
    public String getID() {
        return "Jackhammer";
    }

    @Override
    public String getDisplayName() {
        return EnchantConf.get().PickaxeJackhammerEnchantDisplayName;
    }

    @Override
    public Collection<String> getAllowedToolIDs() {
        return null;
    }

    @Override
    public int getEnchantGUISlot() {
        return EnchantConf.get().PickaxeJackhammerEnchantGUISlot;
    }

    @Override
    protected List<String> getUnformattedGUILore() {
        return EnchantConf.get().PickaxeJackhammerEnchantGUILore;
    }

    @Override
    public String getMagnitudeString(int level) {
        return String.valueOf(level + 2);
    }

    @Override
    public String getName() {
        return EnchantConf.get().PickaxeJackhammerEnchantName;
    }

    @Override
    public String getLore() {
        return EnchantConf.get().PickaxeJackhammerEnchantLore;
    }

    @Override
    public Material getIcon() {
        return EnchantConf.get().PickaxeJackhammerEnchantIcon;
    }

}
