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

public class PickaxeConeEnchant extends BlockBreakEnchant<PickaxeConeEnchant> {

    public static PickaxeConeEnchant i = new PickaxeConeEnchant();
    public static PickaxeConeEnchant get() {
        return i;
    }

    @Override
    public void onBreak(EventMineBlockBreak event, int enchantLevel) {
        Location sL = event.getBlock().getLocation();
        int radius = 4;
        LazyRegion region = event.getMineRegion();
        Distribution distribution = event.getDistribution();
        for (double x = sL.getBlockX(); x < sL.getBlockX() + (radius * 2); x++) {
            for (double y = sL.getBlockY(); y < sL.getBlockY() + (radius * 2); y++) {
                for (double z = sL.getBlockZ(); z < sL.getBlockZ() + (radius * 2); z++) {
                    if (Math.pow(x, 2) + Math.pow(z, 2) == Math.pow(y, 2)) {
                        if(region.contains((int)x,(int)y,(int) z, sL.getWorld().getName())) {
                            Block block = sL.getWorld().getBlockAt((int) x,(int) y,(int) z);
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
        return EnchantConf.get().PickaxeConeEnchantMaxLevel;
    }

    @Override
    public int getMinLevel() {
        return EnchantConf.get().PickaxeConeEnchantMinLevel;
    }

    @Override
    public String getID() {
        return "Cone";
    }

    @Override
    public String getDisplayName() {
        return EnchantConf.get().PickaxeConeEnchantDisplayName;
    }

    @Override
    public Collection<String> getAllowedToolIDs() {
        return null;
    }

    @Override
    public int getEnchantGUISlot() {
        return EnchantConf.get().PickaxeConeEnchantGUISlot;
    }

    @Override
    public List<String> getUnformattedGUILore() {
        return EnchantConf.get().PickaxeConeEnchantGUILore;
    }

    @Override
    public String getMagnitudeString(int level) {
        return String.valueOf(level + 2);
    }

    @Override
    public String getName() {
        return EnchantConf.get().PickaxeConeEnchantName;
    }

    @Override
    public String getLore() {
        return EnchantConf.get().PickaxeConeEnchantLore;
    }

    @Override
    public Material getIcon() {
        return EnchantConf.get().PickaxeConeEnchantIcon;
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
