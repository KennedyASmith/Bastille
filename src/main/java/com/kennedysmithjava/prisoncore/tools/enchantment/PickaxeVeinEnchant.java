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
import com.kennedysmithjava.prisoncore.util.regions.VBlockFace;
import com.massivecraft.massivecore.util.MUtil;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.*;

public class PickaxeVeinEnchant extends BlockBreakEnchant<PickaxeVeinEnchant> {

    public static PickaxeVeinEnchant i = new PickaxeVeinEnchant();

    public static PickaxeVeinEnchant get() {
        return i;
    }

    private static final VBlockFace[] LIMITED_FACES = {
            VBlockFace.UP, VBlockFace.DOWN, VBlockFace.NORTH, VBlockFace.SOUTH, VBlockFace.EAST,
            VBlockFace.WEST, VBlockFace.NORTH_EAST, VBlockFace.NORTH_WEST, VBlockFace.SOUTH_EAST,
            VBlockFace.SOUTH_WEST
    };

    private static final int maxVeinSize = 25;

    @Override
    public void onBreak(EventMineBlockBreak event, int enchantLevel) {
        Block origin = event.getBlock();
        Player player = event.getPlayer();
        LazyRegion region = event.getMineRegion();
        Distribution distribution = event.getDistribution();

        // How much exhaustion is applied for every block broken while vein mining. Every point is 0.025 hunger/exhaustion per block
        //TODO: Make configurable
        float hungerModifier = ((float) (4.0D) * 0.025F);
        int minimumFoodLevel = 2;

        //Allocate blocks to be vein-mined
        Set<Block> blocks = new HashSet<>();
        this.allocateBlocks(blocks, origin, region);

        blocks.removeIf(Block::isEmpty);
        if (blocks.isEmpty()) {
            return;
        }

        for (Block block : blocks) {
            // Apply hunger
            this.applyHungerDebuff(player, hungerModifier);

            if (player.getFoodLevel() <= minimumFoodLevel) {
                player.sendMessage("You are out of food.");
                break;
            }

            //TODO: Check for tool damage

            // Break the block
            PrisonBlock pb = distribution.generatePrisonBlock(block.getType(), block.getBlockData());
            event.addReward(pb);
            block.setType(Material.AIR);
        }
    }

    public void allocateBlocks(Set<Block> blocks, Block origin, LazyRegion region) {

        final List<Block>
                buffer = new ArrayList<>(32),
                recent = new ArrayList<>(32);

        recent.add(origin);

        while (blocks.size() < maxVeinSize) {
            recentSearch:
            for (Block current : recent) {
                for (VBlockFace face : LIMITED_FACES) {
                    Block relative = face.getRelative(current);
                    if (blocks.contains(relative) || !isOfType(origin, relative) || !region.contains(relative)) {
                        continue;
                    }

                    if (blocks.size() + buffer.size() >= maxVeinSize) {
                        break recentSearch;
                    }

                    buffer.add(relative);
                }
            }

            if (buffer.size() == 0) { // No more blocks to allocate
                break;
            }

            recent.clear();
            recent.addAll(buffer);
            blocks.addAll(buffer);
            buffer.clear();
        }
    }

    public boolean isOfType(Block origin, Block checkable){
        if(checkable == null) return false;
        if(origin.getType() != checkable.getType()) return false;
        return origin.getData() == checkable.getData();
    }

    @Override
    public int getMaxLevel() {
        return EnchantConf.get().PickaxeVeinEnchantMaxLevel;
    }

    @Override
    public int getMinLevel() {
        return EnchantConf.get().PickaxeVeinEnchantMinLevel;
    }

    @Override
    public String getID() {
        return "Vein";
    }

    @Override
    public String getDisplayName() {
        return EnchantConf.get().PickaxeVeinEnchantDisplayName;
    }

    @Override
    public Collection<String> getAllowedToolIDs() {
        return null;
    }

    @Override
    public int getEnchantGUISlot() {
        return EnchantConf.get().PickaxeVeinEnchantGUISlot;
    }

    @Override
    public List<String> getUnformattedGUILore() {
        return EnchantConf.get().PickaxeVeinEnchantGUILore;
    }

    @Override
    public String getMagnitudeString(int level) {
        return String.valueOf(level + 2);
    }

    @Override
    public String getName() {
        return EnchantConf.get().PickaxeVeinEnchantName;
    }

    @Override
    public String getLore() {
        return EnchantConf.get().PickaxeVeinEnchantLore;
    }

    @Override
    public Material getIcon() {
        return EnchantConf.get().PickaxeVeinEnchantIcon;
    }

    private void applyHungerDebuff(Player player, float hungerModifier) {
        int foodLevel = player.getFoodLevel();
        float saturation = player.getSaturation();
        float exhaustion = player.getExhaustion();

        exhaustion = (exhaustion + hungerModifier) % 4;
        saturation -= (int) ((exhaustion + hungerModifier) / 4);

        if (saturation < 0) {
            foodLevel += saturation;
            saturation = 0;
        }

        player.setFoodLevel(foodLevel);
        player.setSaturation(saturation);
        player.setExhaustion(exhaustion);
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
