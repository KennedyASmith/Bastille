package com.kennedysmithjava.prisoncore.util;

import com.massivecraft.massivecore.util.MUtil;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class CrateAnimation {


    //TODO: Calculate all potential prize pools for pickaxes by "draws" and hardness, then store

    public static void givePrizeItem(Player player, Map<ItemStack, Integer> prizeOptions, int draws){

        //Map<ItemStack, Integer>
        //Key: Possible item to be won
        //Integer: the number of "entries" this item has in the lottery

        //Sum of entries in the prizes, i.e larger than the largest possible rarity.
        int totalEntries = prizeOptions.values().stream().mapToInt(Integer::intValue).sum();

        //All prizes mapped to a unique ID
        Map<Integer, ItemStack> idMappedPool = new HashMap<>();
        List<Integer> prizePool = new ArrayList<>();

        AtomicInteger id = new AtomicInteger();
        prizeOptions.forEach((potentialPrize, entries) -> {
            prizePool.addAll(MUtil.repeat(id.get(), entries));
            idMappedPool.put(id.get(), potentialPrize);
            id.getAndIncrement();
        });

        List<ItemStack> prizes = new ArrayList<>();
        Random random = new Random();
        for (int draw = 0; draw == draws; draw++) {
            int randomItemId = prizePool.get(random.nextInt(prizePool.size()));
            ItemStack potentialPrize = idMappedPool.get(randomItemId);
            prizes.add(potentialPrize);
        }

        ItemStack rarestItem = new ItemStack(Material.AIR);
        int rarestRarity = totalEntries;

        for (ItemStack prize : prizes) {
            int rarity = prizeOptions.get(prize);
            if(rarity < rarestRarity){ //Terminology doesn't make sense here, but trust it works
                rarestItem = prize;
                rarestRarity = rarity;
            }
        }

        player.getInventory().addItem(rarestItem);
    }




}
