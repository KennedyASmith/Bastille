package com.kennedysmithjava.prisoncore.crafting.objects;

import com.kennedysmithjava.prisoncore.PrisonCore;
import com.kennedysmithjava.prisoncore.crafting.PrisonObject;
import com.kennedysmithjava.prisoncore.util.IntRange;
import com.massivecraft.massivecore.util.MUtil;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;
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

enum StickType {
    WOOD("&c&lWooden Stick", MUtil.list("&7Crafting Material", " &r", "&7Common material used for", "&7crafting &etools&7!"), new IntRange(100, 500)),
    BAMBOO("&a&lBamboo Stick", MUtil.list("&7Crafting Material", " &r", "&aUncommon &7material used for", "&7crafting &etools&7!"), new IntRange(500, 1000)),
    BONE("&f&lBone Stick", MUtil.list("&7Crafting Material", " &r", "&bRare &7material used for", "&7crafting &etools&7!"), new IntRange(1000, 2000)),
    BLAZE("&6&lBlaze Stick", MUtil.list("&7Crafting Material", " &r", "&dEpic &7material used for", "&7crafting &etools&7!"), new IntRange(1500, 5000));

    final String displayName;
    final List<String> lore;
    final IntRange durabilityRange;

    StickType(String displayName, List<String> lore, IntRange durabilityRange) {
        this.displayName = displayName;
        this.lore = lore;
        this.durabilityRange = durabilityRange;
    }

    public IntRange getDurabilityRange() {
        return durabilityRange;
    }


    public int getDurability(){
        return durabilityRange.getRandom();
    }

    public List<String> getLore() {
        return lore;
    }

    public String getDisplayName() {
        return displayName;
    }
}
