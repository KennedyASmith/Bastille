package com.kennedysmithjava.prisoncore.blockhandler;

import org.bukkit.inventory.ItemStack;

public interface Reward {
    ItemStack getProductItem(int amount);
}
