package com.kennedysmithjava.prisoncore.enchantment;

import com.fastasyncworldedit.core.math.LocalBlockVectorSet;
import com.fastasyncworldedit.core.util.MathMan;
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
import com.kennedysmithjava.prisoncore.regions.LazyRegion;
import com.massivecraft.massivecore.util.MUtil;
import com.sk89q.worldedit.math.BlockVector3;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.*;

public class PickaxeLaserEnchant extends BlockBreakEnchant<PickaxeLaserEnchant> {

    public static PickaxeLaserEnchant i = new PickaxeLaserEnchant();
    public static PickaxeLaserEnchant get() {
        return i;
    }

    @Override
    public void onBreak(EventMineBlockBreak event, int enchantLevel) {
        Player player = event.getPlayer();
        Location start = event.getBlock().getLocation();
        Distribution distribution = event.getDistribution();
        LazyRegion region = event.getMineRegion();
        String worldName = start.getWorld().getName();

        int length = 8;
        double radius = 1.1;

        Location eyeLoc = player.getEyeLocation();
        Location pos1 = player.getLocation();
        Location pos2 = eyeLoc.add(eyeLoc.getDirection().multiply(length));
        Set<BlockVector3> vset = new HashSet<>();

        int x1 = pos1.getBlockX();
        int y1 = pos1.getBlockY();
        int z1 = pos1.getBlockZ();
        int x2 = pos2.getBlockX();
        int y2 = pos2.getBlockY();
        int z2 = pos2.getBlockZ();
        int tipx = x1;
        int tipy = y1;
        int tipz = z1;
        int dx = Math.abs(x2 - x1);
        int dy = Math.abs(y2 - y1);
        int dz = Math.abs(z2 - z1);

        if (dx + dy + dz == 0) {
            vset.add(BlockVector3.at(tipx, tipy, tipz));
        }

        int dMax = Math.max(Math.max(dx, dy), dz);
        if (dMax == dx) {
            for (int domstep = 0; domstep <= dx; domstep++) {
                tipx = x1 + domstep * (x2 - x1 > 0 ? 1 : -1);
                tipy = (int) Math.round(y1 + domstep * ((double) dy) / ((double) dx) * (y2 - y1 > 0 ? 1 : -1));
                tipz = (int) Math.round(z1 + domstep * ((double) dz) / ((double) dx) * (z2 - z1 > 0 ? 1 : -1));

                vset.add(BlockVector3.at(tipx, tipy, tipz));
            }
        } else if (dMax == dy) {
            for (int domstep = 0; domstep <= dy; domstep++) {
                tipy = y1 + domstep * (y2 - y1 > 0 ? 1 : -1);
                tipx = (int) Math.round(x1 + domstep * ((double) dx) / ((double) dy) * (x2 - x1 > 0 ? 1 : -1));
                tipz = (int) Math.round(z1 + domstep * ((double) dz) / ((double) dy) * (z2 - z1 > 0 ? 1 : -1));

                vset.add(BlockVector3.at(tipx, tipy, tipz));
            }
        } else  {
            for (int domstep = 0; domstep <= dz; domstep++) {
                tipz = z1 + domstep * (z2 - z1 > 0 ? 1 : -1);
                tipy = (int) Math.round(y1 + domstep * ((double) dy) / ((double) dz) * (y2 - y1 > 0 ? 1 : -1));
                tipx = (int) Math.round(x1 + domstep * ((double) dx) / ((double) dz) * (x2 - x1 > 0 ? 1 : -1));

                vset.add(BlockVector3.at(tipx, tipy, tipz));
            }
        }

        Set<BlockVector3> newvSet = getBallooned(vset, radius);

        newvSet.forEach(vector -> {
            if (region.contains(vector.getBlockX(), vector.getBlockY(), vector.getBlockZ(), worldName)) {
                Location loc = new Location(pos1.getWorld(), vector.getX(), vector.getY(), vector.getZ());
                Block block = loc.getBlock();
                PrisonBlock pb = distribution.generatePrisonBlock(block.getType(), block.getBlockData());
                event.addReward(pb);
                block.setType(Material.AIR);
                event.addAffectedBlock(loc);
            }
        });

    }

    private Set<BlockVector3> getBallooned(Set<BlockVector3> vset, double radius) {
        if (radius < 1) {
            return vset;
        }


        LocalBlockVectorSet returnset = new LocalBlockVectorSet();
        int ceilrad = (int) Math.ceil(radius);

        for (BlockVector3 v : vset) {
            int tipx = v.getBlockX();
            int tipy = v.getBlockY();
            int tipz = v.getBlockZ();

            for (int loopx = tipx - ceilrad; loopx <= tipx + ceilrad; loopx++) {
                for (int loopy = tipy - ceilrad; loopy <= tipy + ceilrad; loopy++) {
                    for (int loopz = tipz - ceilrad; loopz <= tipz + ceilrad; loopz++) {
                        if (MathMan.hypot(loopx - tipx, loopy - tipy, loopz - tipz) <= radius) {
                            returnset.add(loopx, loopy, loopz);
                        }
                    }
                }
            }
        }
        return returnset;
    }

    @Override
    public int getMaxLevel() {
        return EnchantConf.get().PickaxeLaserEnchantMaxLevel;
    }

    @Override
    public int getMinLevel() {
        return EnchantConf.get().PickaxeLaserEnchantMinLevel;
    }

    @Override
    public String getID() {
        return "Laser";
    }

    @Override
    public String getDisplayName() {
        return EnchantConf.get().PickaxeLaserEnchantDisplayName;
    }

    @Override
    public Collection<String> getAllowedToolIDs() {
        return null;
    }

    @Override
    public int getEnchantGUISlot() {
        return EnchantConf.get().PickaxeLaserEnchantGUISlot;
    }

    @Override
    public List<String> getUnformattedGUILore() {
        return EnchantConf.get().PickaxeLaserEnchantGUILore;
    }

    @Override
    public String getMagnitudeString(int level) {
        return String.valueOf(level + 2);
    }

    @Override
    public String getName() {
        return EnchantConf.get().PickaxeLaserEnchantName;
    }

    @Override
    public String getLore() {
        return EnchantConf.get().PickaxeLaserEnchantLore;
    }

    @Override
    public Material getIcon() {
        return EnchantConf.get().PickaxeLaserEnchantIcon;
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
