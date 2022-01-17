package com.kennedysmithjava.prisoncore.tools.enchantment;

import org.bukkit.inventory.ItemStack;

public abstract class DynamicEnchant<E extends DynamicEnchant<E>> extends Enchant<E> {
    public abstract void onApply(ItemStack tool, int level);
    public abstract void onRemove(ItemStack tool);
}
