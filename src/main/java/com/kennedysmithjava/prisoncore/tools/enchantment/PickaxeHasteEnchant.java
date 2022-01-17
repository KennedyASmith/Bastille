package com.kennedysmithjava.prisoncore.tools.enchantment;

import com.kennedysmithjava.prisoncore.entity.tools.EnchantConf;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Collection;
import java.util.List;

public class PickaxeHasteEnchant extends HandEquipEnchant<PickaxeHasteEnchant> {

    public static PickaxeHasteEnchant i = new PickaxeHasteEnchant();
    public static PickaxeHasteEnchant get() { return i; }

    @Override
    public void onEquip(Player player, int level) {
        PotionEffect potionEffect = PotionEffectType.FAST_DIGGING.createEffect(Integer.MAX_VALUE, level-1);
        player.addPotionEffect(potionEffect, true);
    }

    @Override
    public void onDequip(Player player, int level) {
        player.removePotionEffect(PotionEffectType.FAST_DIGGING);
    }

    @Override
    public void onApply(ItemStack tool, int level) {

    }

    @Override
    public void onRemove(ItemStack tool) {

    }

    @Override
    public String getID() {
        return "Haste";
    }

    @Override
    public String getDisplayName() {
        return EnchantConf.get().PickaxeHasteEnchantDisplayName;
    }

    @Override
    public int getMaxLevel() {
        return EnchantConf.get().PickaxeHasteEnchantMaxLevel;
    }

    @Override
    public int getMinLevel() {
        return EnchantConf.get().PickaxeHasteEnchantMinLevel;
    }

    @Override
    public Collection<String> getAllowedToolIDs() {
        return null;
    }

    @Override
    public int getEnchantGUISlot() {
        return EnchantConf.get().PickaxeHasteEnchantGUISlot;
    }

    @Override
    protected List<String> getUnformattedGUILore() {
        return EnchantConf.get().PickaxeHasteEnchantGUILore;
    }

    @Override
    public String getMagnitudeString(int level) {
        return String.valueOf(level);
    }

    @Override
    public String getName() {
        return EnchantConf.get().PickaxeHasteEnchantName;
    }

    @Override
    public String getLore() {
        return EnchantConf.get().PickaxeHasteEnchantLore;
    }

    @Override
    public Material getIcon() {
        return EnchantConf.get().PickaxeHasteEnchantIcon;
    }
}
