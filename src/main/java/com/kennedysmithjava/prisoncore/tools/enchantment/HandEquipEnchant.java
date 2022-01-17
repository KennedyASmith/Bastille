package com.kennedysmithjava.prisoncore.tools.enchantment;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public abstract class HandEquipEnchant<E extends HandEquipEnchant<E>> extends DynamicEnchant<E>{
    public abstract void onEquip(Player player, int level);
    public abstract void onDequip(Player player, int level);
    public abstract void onApply(ItemStack tool, int level);
    public abstract void onRemove(ItemStack tool);
}
