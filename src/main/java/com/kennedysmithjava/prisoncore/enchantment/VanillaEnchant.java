package com.kennedysmithjava.prisoncore.enchantment;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

public abstract class VanillaEnchant<E extends VanillaEnchant<E>> extends DynamicEnchant<E>{

    public void onApply(ItemStack tool, int level){
        tool.addUnsafeEnchantment(getEnchantment(), level);
    }

    public void onRemove(ItemStack tool){
        tool.removeEnchantment(getEnchantment());
    }

    public abstract Enchantment getEnchantment();
}
