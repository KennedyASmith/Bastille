package com.kennedysmithjava.prisoncore.crafting.objects;

import com.kennedysmithjava.prisoncore.crafting.PrisonObject;
import com.kennedysmithjava.prisoncore.crafting.objects.type.FishType;
import com.kennedysmithjava.prisoncore.entity.farming.objects.FishingArea;
import com.kennedysmithjava.prisoncore.util.ItemBuilder;
import org.apache.commons.math3.distribution.EnumeratedDistribution;
import org.apache.commons.math3.util.Pair;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PrisonFish extends PrisonObject {
    private final FishType type;

    public PrisonFish(FishType type){
        this.type = type;
    }

    public FishType getType() {
        return type;
    }

    @Override
    public ItemStack giveRawItem() {
        ItemStack item = new ItemStack(getType().getMaterial());
        return new ItemBuilder(item)
                .name(type.getDisplayName())
                 .lore(type.getLore())
                 .build();
    }

    @Override
    public String getKey() {
        return "fishKey";
    }

    @Override
    public String getName() {
        return type.getDisplayName();
    }


    @Override
    public Map<PersistentDataType<?, ?>, Map<NamespacedKey, ?>> getStoredData() {
        return new HashMap<>();
    }

    public static ItemStack get(FishType type){
        return new PrisonFish(type).give(1);
    }



    /**
     * Picks a prize from the provided rarities map based on their probabilities.
     *
     * @return          The selected prize.
     */
    public static PrisonFish pickRandom(FishingArea area) {
        List<Pair<FishType, Double>> itemProbPairs = new ArrayList<>();
        for (FishType t : area.getFishObtainable()) {
            Double rarity = t.getRarity().getRarityDouble();
            itemProbPairs.add(new Pair<>(t, rarity));
        }
        EnumeratedDistribution<FishType> distribution = new EnumeratedDistribution<>(itemProbPairs);
        return new PrisonFish(distribution.sample());
    }
}

