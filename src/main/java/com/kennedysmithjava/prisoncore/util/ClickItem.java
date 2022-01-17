package com.kennedysmithjava.prisoncore.util;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class ClickItem extends ItemStack {

    private final ClickHandler clickHandler;

    public ClickItem(Material type, ClickHandler clickHandler) {
        super(type);
        this.clickHandler = clickHandler;
    }

    public ClickItem(Material type, int amount, ClickHandler clickHandler) {
        super(type, amount);

        this.clickHandler = clickHandler;
    }

    public ClickItem(Material type, int amount, short damage, ClickHandler clickHandler) {
        super(type, amount, damage);
        this.clickHandler = clickHandler;
    }

    public ClickItem(ItemStack stack, ClickHandler clickHandler) throws IllegalArgumentException {
        super(stack);
        this.clickHandler = clickHandler;
    }

    public ClickHandler getClickHandler() {
        return clickHandler;
    }
}
