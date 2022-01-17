package com.kennedysmithjava.prisoncore.util;


import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import java.util.ArrayList;
import java.util.List;

/**
 * Easily create itemstacks, without messing your hands.
 * <i>Note that if you do use this in one of your projects, leave this notice.</i>
 * <i>Please do credit me if you do use this in one of your projects.</i>
 * @author NonameSL
 */

public class ItemBuilder
{
    private ItemStack item;

    public ItemBuilder(final ItemStack is) {
        this.item = is;
    }

    public ItemBuilder(final Material mat) {
        this.item = new ItemStack(mat);
    }

    public ItemBuilder(final Material mat, final int amount) {
        this.item = new ItemStack(mat, amount);
    }


    public ItemBuilder name(final String name) {
        final ItemMeta im = this.item.getItemMeta();
        im.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
        this.item.setItemMeta(im);
        return this;
    }

    public ItemBuilder lore(final String lore) {
        final List<String> itemLore = this.item.getItemMeta().hasLore() ? this.item.getItemMeta().getLore() : new ArrayList<String>();
        itemLore.add(ChatColor.translateAlternateColorCodes('&', lore));
        this.lore(itemLore);
        return this;
    }

    public ItemBuilder lore(final List<String> lore) {
        final ItemMeta im = this.item.getItemMeta();
        final List<String> clone = new ArrayList<String>();
        for (final String lores : lore) {
            clone.add(ChatColor.translateAlternateColorCodes('&', lores));
        }
        im.setLore((List)clone);
        this.item.setItemMeta(im);
        return this;
    }

    public ItemBuilder clearLore() {
        final List<String> emptyList = new ArrayList<String>();
        this.lore(emptyList);
        return this;
    }

    public ItemBuilder clearEnchantments() {
        if (this.item.getItemMeta().hasEnchants()) {
            for (final Enchantment enchantments : this.item.getEnchantments().keySet()) {
                this.item.removeEnchantment(enchantments);
            }
        }
        return this;
    }

    public ItemBuilder clearEnchantment(final Enchantment enchant) {
        if (this.item.getItemMeta().hasEnchants() && this.item.getEnchantments().containsKey(enchant)) {
            final ItemMeta im = this.item.getItemMeta();
            im.removeEnchant(enchant);
            this.item.setItemMeta(im);
        }
        return this;
    }

    public ItemBuilder unsafeEnchants(final List<String> enchantments, final List<Integer> levels) {
        for(int i = 0; i < enchantments.size(); i++) {
            this.item.addUnsafeEnchantment(Enchantment.getByName(enchantments.get(i)), levels.get(i));
        }
        return this;
    }

    public ItemBuilder unsafeEnchant(final Enchantment enchantment, final int level) {
        this.item.addUnsafeEnchantment(enchantment, level);
        return this;
    }

    public ItemBuilder enchant(final Enchantment enchantment, final int level) {
        this.item.addUnsafeEnchantment(enchantment, level);
        return this;
    }

    public ItemBuilder amount(final int amount) {
        this.item.setAmount((amount > 0) ? amount : 1);
        return this;
    }

    public ItemBuilder durability(final int durability) {
        this.item.setDurability((short)durability);
        return this;
    }

    public ItemBuilder color(final Color color) {
        if (this.item.getType().toString().contains("LEATHER_")) {
            final LeatherArmorMeta meta = (LeatherArmorMeta)this.item.getItemMeta();
            meta.setColor(color);
            this.item.setItemMeta((ItemMeta)meta);
        }
        return this;
    }

    @Deprecated
    public ItemBuilder data(final int data) {
        this.item.getData().setData((byte)data);
        return this;
    }

    public ItemBuilder addGlow() {
        this.item.addUnsafeEnchantment(new Glow(), 1);
        return this;
    }

    public ItemStack build() {
        return this.item;
    }
}
