package com.kennedysmithjava.prisoncore.crafting.objects;

import com.kennedysmithjava.prisoncore.crafting.PrisonObject;
import com.kennedysmithjava.prisoncore.crafting.objects.type.EssenceType;
import com.kennedysmithjava.prisoncore.util.ItemBuilder;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import java.util.HashMap;
import java.util.Map;

public class PrisonEssence extends PrisonObject {
    private EssenceType type;

    public PrisonEssence(EssenceType type){
        this.type = type;
    }

    public EssenceType getType() {
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
        return "essenceKey";
    }

    @Override
    public String getName() {
        return type.getDisplayName();
    }

    @Override
    public Map<PersistentDataType<?, ?>, Map<NamespacedKey, ?>> getStoredData() {
        return new HashMap<>();
    }

    public static ItemStack get(EssenceType type){
        return new PrisonEssence(type).give(1);
    }
}

