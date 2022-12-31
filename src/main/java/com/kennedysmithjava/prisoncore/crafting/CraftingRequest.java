package com.kennedysmithjava.prisoncore.crafting;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public class CraftingRequest {
    Player player;
    Map<Integer, ItemStack> ingredients;
    public CraftingRequest(Player player, Map<Integer, ItemStack> ingredients) {
        this.player = player;
        this.ingredients = ingredients;
    }

    public Map<Integer, ItemStack> getIngredients() {
        return ingredients;
    }

    public Player getPlayer() {
        return player;
    }
}


