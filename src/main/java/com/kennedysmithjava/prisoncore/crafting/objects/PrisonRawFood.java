package com.kennedysmithjava.prisoncore.crafting.objects;

import com.kennedysmithjava.prisoncore.crafting.PrisonObject;
import com.kennedysmithjava.prisoncore.crafting.objects.type.RawFoodType;
import com.kennedysmithjava.prisoncore.util.ItemBuilder;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import java.util.HashMap;
import java.util.Map;

public class PrisonRawFood extends PrisonObject {
    private RawFoodType type;

    public PrisonRawFood(RawFoodType type){
        this.type = type;
    }

    public RawFoodType getType() {
        return type;
    }

    @Override
    public ItemStack giveRawItem() {
        ItemStack item = new ItemStack(getType().getMaterial());
        return new ItemBuilder(item)
                .name(getName())
                 .lore(type.getLore())
                 .build();
    }

    @Override
    public String getKey() {
        return "rawFoodKey";
    }

    @Override
    public String getName() {
        return type.getDisplayName();
    }

    @Override
    public Map<PersistentDataType<?, ?>, Map<NamespacedKey, ?>> getStoredData() {
        return new HashMap<>();
    }

    public static ItemStack get(RawFoodType type){
        return new PrisonRawFood(type).give(1);
    }
}

