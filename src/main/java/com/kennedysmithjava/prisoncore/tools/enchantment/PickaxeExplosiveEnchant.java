package com.kennedysmithjava.prisoncore.tools.enchantment;

import com.kennedysmithjava.prisoncore.crafting.Recipe;
import com.kennedysmithjava.prisoncore.eco.Cost;
import com.kennedysmithjava.prisoncore.eco.CostCurrency;
import com.kennedysmithjava.prisoncore.eco.CostSkillLevel;
import com.kennedysmithjava.prisoncore.eco.CurrencyType;
import com.kennedysmithjava.prisoncore.entity.mines.Distribution;
import com.kennedysmithjava.prisoncore.entity.mines.objects.PrisonBlock;
import com.kennedysmithjava.prisoncore.entity.tools.EnchantConf;
import com.kennedysmithjava.prisoncore.event.EventMineBlockBreak;
import com.kennedysmithjava.prisoncore.skill.SkillType;
import com.kennedysmithjava.prisoncore.util.regions.LazyRegion;
import com.massivecraft.massivecore.util.MUtil;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

import java.util.Collection;
import java.util.List;

public class PickaxeExplosiveEnchant extends BlockBreakEnchant<PickaxeExplosiveEnchant> {

    public static PickaxeExplosiveEnchant i = new PickaxeExplosiveEnchant();
    public static PickaxeExplosiveEnchant get() {
        return i;
    }

    @Override
    public void onBreak(EventMineBlockBreak event, int enchantLevel) {
        Location sL = event.getBlock().getLocation();
        LazyRegion region = event.getMineRegion();
        String worldName = sL.getWorld().getName();
        Distribution distribution = event.getDistribution();
        int radius = 3;
        int bx = sL.getBlockX();
        int by = sL.getBlockY();
        int bz = sL.getBlockZ();
        for (int x = bx - radius; x <= bx + radius; x++) {
            for (int y = by - radius; y <= by + radius; y++) {
                for (int z = bz - radius; z <= bz + radius; z++) {
                    double distance = ((bx - x) * (bx - x) + ((bz - z) * (bz - z)) + ((by - y) * (by - y)));
                    if (distance < radius * radius) {
                        if(region.contains(x, y, z, worldName)){
                            Block block = sL.getWorld().getBlockAt(x,y,z);
                            PrisonBlock pb = distribution.generatePrisonBlock(block.getType(), block.getBlockData());
                            event.addReward(pb);
                            block.setType(Material.AIR);
                        }
                    }
                }
            }
        }
    }

    @Override
    public int getMaxLevel() {
        return EnchantConf.get().PickaxeExplosiveEnchantMaxLevel;
    }

    @Override
    public int getMinLevel() {
        return EnchantConf.get().PickaxeExplosiveEnchantMinLevel;
    }

    @Override
    public String getID() {
        return "Explosive";
    }

    @Override
    public String getDisplayName() {
        return EnchantConf.get().PickaxeExplosiveEnchantDisplayName;
    }

    @Override
    public Collection<String> getAllowedToolIDs() {
        return null;
    }

    @Override
    public int getEnchantGUISlot() {
        return EnchantConf.get().PickaxeExplosiveEnchantGUISlot;
    }

    @Override
    public List<String> getUnformattedGUILore() {
        return EnchantConf.get().PickaxeExplosiveEnchantGUILore;
    }

    @Override
    public String getMagnitudeString(int level) {
        return String.valueOf(level + 2);
    }

    @Override
    public String getName() {
        return EnchantConf.get().PickaxeExplosiveEnchantName;
    }

    @Override
    public String getLore() {
        return EnchantConf.get().PickaxeExplosiveEnchantLore;
    }

    @Override
    public Material getIcon() {
        return EnchantConf.get().PickaxeExplosiveEnchantIcon;
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
