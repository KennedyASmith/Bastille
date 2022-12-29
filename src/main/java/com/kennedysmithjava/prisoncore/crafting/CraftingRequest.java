package com.kennedysmithjava.prisoncore.crafting;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public record CraftingRequest(Player player, Map<Integer, ItemStack> ingredients) {


}
