package com.kennedysmithjava.prisoncore.crafting;

import com.kennedysmithjava.prisoncore.util.MiscUtil;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public class CraftingRequest {
    Player player;
    Map<Integer, ItemStack> ingredients;
    Recipe recipe;
    public CraftingRequest(Recipe recipe, Player player, Map<Integer, ItemStack> ingredients) {
        this.recipe = recipe;
        this.player = player;
        this.ingredients = ingredients;
    }

    public Map<Integer, ItemStack> getIngredients() {
        return ingredients;
    }

    public Player getPlayer() {
        return player;
    }

    public void give(ItemStack item){
        MiscUtil.givePlayerItem(player, item, item.getAmount());
    }

    public Recipe getRecipe() {
        return recipe;
    }
}


