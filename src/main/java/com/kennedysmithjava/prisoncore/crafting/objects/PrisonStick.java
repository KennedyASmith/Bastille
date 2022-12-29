package com.kennedysmithjava.prisoncore.crafting.objects;

import com.kennedysmithjava.prisoncore.PrisonCore;
import com.kennedysmithjava.prisoncore.crafting.PrisonObject;
import com.kennedysmithjava.prisoncore.crafting.objects.type.StickType;
import com.massivecraft.massivecore.util.MUtil;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import java.util.Map;

public class PrisonStick extends PrisonObject<PrisonStick> {
    private static final NamespacedKey durabilityKey = new NamespacedKey(PrisonCore.get(), "stickDurability");

    private final Integer durability;
    private final StickType type;

    public PrisonStick(StickType type, int durability){
        this.durability = durability;
        this.type = type;
    }

    public StickType getType() {
        return type;
    }

    @Override
    public ItemStack giveRawItem() {
        return null;
    }

    @Override
    public String getKey() {
        return "stickKey";
    }

    @Override
    public String getName() {
        return type.getDisplayName();
    }

    @Override
    public Map<PersistentDataType<?, ?>, Map<NamespacedKey, ?>> getStoredData() {
        return MUtil.map(PersistentDataType.INTEGER, MUtil.map(durabilityKey, durability));
    }

    @Override
    public ItemStack give() {
        return null;
    }

    public static ItemStack get(StickType type){
        return new PrisonStick(type, type.getDurability()).give();
    }
}

