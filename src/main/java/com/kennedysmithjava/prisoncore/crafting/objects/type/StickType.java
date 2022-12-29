package com.kennedysmithjava.prisoncore.crafting.objects.type;

import com.kennedysmithjava.prisoncore.util.IntRange;
import com.massivecraft.massivecore.util.MUtil;

import java.util.List;

public enum StickType {
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


    public int getDurability() {
        return durabilityRange.getRandom();
    }

    public List<String> getLore() {
        return lore;
    }

    public String getDisplayName() {
        return displayName;
    }
}
