package com.kennedysmithjava.prisoncore.tools.enchantment;

import com.kennedysmithjava.prisoncore.entity.tools.EnchantConf;
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
    protected List<String> getUnformattedGUILore() {
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
}
