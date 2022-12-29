package com.kennedysmithjava.prisoncore.util;

import org.bukkit.Sound;
import org.bukkit.inventory.ItemStack;

public final class RemovableItem extends ClickItem {
    public RemovableItem(ItemStack stack) throws IllegalArgumentException {
        super(stack, (player, clickEvent) -> {
            player.getInventory().addItem(clickEvent.getCurrentItem());
            clickEvent.setCurrentItem(null);
            player.playSound(player.getLocation(), Sound.BLOCK_LAVA_POP, 1, 1);
            return true;
        });
    }
}
