package com.kennedysmithjava.prisoncore.crafting;

import com.kennedysmithjava.prisoncore.eco.Cost;
import com.kennedysmithjava.prisoncore.util.MiscUtil;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Map;

public class CraftingRequest {
    Player player;
    Map<Integer, ItemStack> ingredients;

    private ProductItem product;

    private final List<Cost> additionalCosts;

    public CraftingRequest(Player player, Map<Integer, ItemStack> ingredients, ProductItem product, List<Cost> additionalCosts) {
        this.product = product;
        this.additionalCosts = additionalCosts;
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

    public List<Cost> getAdditionalCosts() {
        return additionalCosts;
    }

    public ProductItem getProduct() {
        return product;
    }
}


