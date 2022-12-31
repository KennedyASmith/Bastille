package com.kennedysmithjava.prisoncore.crafting.objects.type;

import com.kennedysmithjava.prisoncore.util.IntRange;
import com.massivecraft.massivecore.util.MUtil;
import org.bukkit.Material;

import java.util.List;

public enum StickType {
    WOOD(Material.STICK, "&c&lWooden Stick", MUtil.list("&7Crafting Material", " &r", "&7Durability: %durability%", "&fCommon &7material used for crafting &etools&7!"), new IntRange(100, 500)),
    BAMBOO(Material.BAMBOO, "&a&lBamboo Stick", MUtil.list("&7Crafting Material", " &r", "&7Durability: %durability%", "&aUncommon &7material used for crafting &etools&7!"), new IntRange(500, 1000)),
    BONE(Material.BONE, "&f&lBone Stick", MUtil.list("&7Crafting Material", " &r", "&7Durability: %durability%", "&bRare &7material used for crafting &etools&7!"), new IntRange(1000, 2000)),
    BLAZE(Material.BLAZE_ROD, "&6&lSoul Stick", MUtil.list("&7Crafting Material", " &r", "&7Durability: %durability%", "&dEpic &7material used for crafting &etools&7!"), new IntRange(1500, 5000));

    final String displayName;
    final List<String> lore;
    final IntRange durabilityRange;
    final Material material;

    StickType(Material material, String displayName, List<String> lore, IntRange durabilityRange) {
        this.displayName = displayName;
        this.lore = lore;
        this.durabilityRange = durabilityRange;
        this.material = material;
    }

    public IntRange getDurabilityRange() {
        return durabilityRange;
    }


    public int getDurability() {
        return this.durabilityRange.getRandom();
    }

    public List<String> getLore() {
        return lore;
    }

    public String getDisplayName() {
        return displayName;
    }

    public Material getMaterial() {
        return material;
    }
}
