package com.kennedysmithjava.prisoncore.tools.enchantment;

import com.kennedysmithjava.prisoncore.crafting.Recipe;
import com.kennedysmithjava.prisoncore.eco.Cost;
import com.kennedysmithjava.prisoncore.eco.CostCurrency;
import com.kennedysmithjava.prisoncore.eco.CostSkillLevel;
import com.kennedysmithjava.prisoncore.eco.CurrencyType;
import com.kennedysmithjava.prisoncore.entity.tools.EnchantConf;
import com.kennedysmithjava.prisoncore.skill.SkillType;
import com.massivecraft.massivecore.util.MUtil;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Collection;
import java.util.List;

public class PickaxeSpeedEnchant extends HandEquipEnchant<PickaxeSpeedEnchant> {

    public static PickaxeSpeedEnchant i = new PickaxeSpeedEnchant();
    public static PickaxeSpeedEnchant get() { return i; }

    @Override
    public void onEquip(Player player, int level) {
        PotionEffect potionEffect = PotionEffectType.SPEED.createEffect(Integer.MAX_VALUE, level-1);
        player.addPotionEffect(potionEffect, true);
    }

    @Override
    public void onDequip(Player player, int level) {
        player.removePotionEffect(PotionEffectType.SPEED);
    }

    @Override
    public void onApply(ItemStack tool, int level) {

    }

    @Override
    public void onRemove(ItemStack tool) {

    }

    @Override
    public String getID() {
        return "Speed";
    }

    @Override
    public String getDisplayName() {
        return EnchantConf.get().PickaxeSpeedEnchantDisplayName;
    }

    @Override
    public int getMaxLevel() {
        return 10;
    }

    @Override
    public int getMinLevel() {
        return 1;
    }

    @Override
    public Collection<String> getAllowedToolIDs() {
        return null;
    }

    @Override
    public int getEnchantGUISlot() {
        return EnchantConf.get().PickaxeSpeedEnchantGUISlot;
    }

    @Override
    public List<String> getUnformattedGUILore() {
        return EnchantConf.get().PickaxeSpeedEnchantGUILore;
    }

    @Override
    public String getMagnitudeString(int level) {
        return String.valueOf(level);
    }

    @Override
    public String getName() {
        return EnchantConf.get().PickaxeSpeedEnchantName;
    }

    @Override
    public String getLore() {
        return EnchantConf.get().PickaxeSpeedEnchantLore;
    }

    @Override
    public Material getIcon() {
        return EnchantConf.get().PickaxeSpeedEnchantIcon;
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
