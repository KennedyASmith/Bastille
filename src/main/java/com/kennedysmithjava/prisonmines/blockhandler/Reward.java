package com.kennedysmithjava.prisonmines.blockhandler;

import org.bukkit.inventory.ItemStack;

public interface Reward {
    ItemStack getProductItem(int amount);
}
