package com.kennedysmithjava.prisoncore.tools.enchantment;

import com.kennedysmithjava.prisoncore.entity.tools.EnchantConf;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Collection;
import java.util.List;

public class PickaxeNightVisionEnchant extends HandEquipEnchant<PickaxeNightVisionEnchant> {

    public static PickaxeNightVisionEnchant i = new PickaxeNightVisionEnchant();
    public static PickaxeNightVisionEnchant get() { return i; }
    public static PotionEffect potionEffect = PotionEffectType.NIGHT_VISION.createEffect(Integer.MAX_VALUE, 0);

    @Override
    public void onEquip(Player player, int level) {
        player.addPotionEffect(potionEffect, true);
    }

    @Override
    public void onDequip(Player player, int level) {
        player.removePotionEffect(potionEffect.getType());
    }

    @Override
    public void onApply(ItemStack tool, int level) {

    }

    @Override
    public void onRemove(ItemStack tool) {

    }

    @Override
    public String getID() {
        return "NV";
    }

    @Override
    public String getDisplayName() {
        return EnchantConf.get().PickaxeVisionEnchantDisplayName;
    }

    @Override
    public int getMaxLevel() {
        return 1;
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
        return EnchantConf.get().PickaxeVisionEnchantGUISlot;
    }

    @Override
    protected List<String> getUnformattedGUILore() {
        return EnchantConf.get().PickaxeVisionEnchantGUILore;
    }

    @Override
    public String getMagnitudeString(int level) {
        return String.valueOf(level);
    }

    @Override
    public String getName() {
        return EnchantConf.get().pickaxeVisionEnchantName;
    }

    @Override
    public String getLore() {
        return EnchantConf.get().PickaxeVisionEnchantLore;
    }

    @Override
    public Material getIcon() {
        return EnchantConf.get().PickaxeVisionEnchantIcon;
    }
}
