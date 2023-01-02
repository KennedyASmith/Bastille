package com.kennedysmithjava.prisoncore.crafting.objects;

import com.kennedysmithjava.prisoncore.PrisonCore;
import com.kennedysmithjava.prisoncore.crafting.PrisonObject;
import com.kennedysmithjava.prisoncore.crafting.objects.type.StickType;
import com.kennedysmithjava.prisoncore.util.ItemBuilder;
import com.massivecraft.massivecore.util.MUtil;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PrisonStick extends PrisonObject {
    public static final NamespacedKey durabilityKey = new NamespacedKey(PrisonCore.get(), "stickDurability");
    //private static final NamespacedKey typeKey = new NamespacedKey(PrisonCore.get(), "stickType");

    private Integer durability;
    private StickType type;

    public PrisonStick(StickType type, int durability){
        this.durability = durability;
        this.type = type;
    }

    public PrisonStick(StickType type){
        this.durability = type.getDurability();
        this.type = type;
    }

    public StickType getType() {
        return type;
    }

    @Override
    public ItemStack giveRawItem() {
        ItemStack item = new ItemStack(getType().getMaterial());
        return new ItemBuilder(item)
                 .lore(placeholderReplacer(type.getLore()))
                 .build();
    }

    private List<String> placeholderReplacer(List<String> lore) {
        int dur = getDurability();
       List<String> newLore = new ArrayList<>();
        for (String s : lore) {
            newLore.add(s.replaceAll("%durability%", String.valueOf(dur)));
        }
       return newLore;
    }
    @Override
    public String getKey() {
        return "stickKey";
    }

    @Override
    public String getName() {
        return type.getDisplayName();
    }

    public Integer getDurability() {
        return durability;
    }

    @Override
    public Map<PersistentDataType<?, ?>, Map<NamespacedKey, ?>> getStoredData() {
        return MUtil.map(PersistentDataType.INTEGER, MUtil.map(durabilityKey, durability));
    }

    public static ItemStack get(StickType type){
        return new PrisonStick(type).give(1);
    }
}

