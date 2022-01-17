package com.kennedysmithjava.prisoncore.util;

import com.massivecraft.massivecore.chestgui.ChestAction;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

@FunctionalInterface
public interface ClickHandler {
    boolean onClick(Player player, InventoryClickEvent clickEvent);

    static ChestAction toChestAction(ClickHandler handler) {
        return i -> handler.onClick((Player) i.getWhoClicked(), i);

    }
}
