package com.kennedysmithjava.prisoncore.crafting.objects.type;

import com.massivecraft.massivecore.util.MUtil;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;

public enum RawFoodType {
    //USED FOR CRAFTING
    ANY(Material.PORKCHOP, "&7Any Raw Food", new ArrayList<>()),
    PORKCHOP(Material.PORKCHOP, "&fRaw Porkchop", MUtil.list("&7Raw Food", " &r", "&7Common &ePig&7 drop!&7")),
    CHICKEN(Material.CHICKEN, "&fRaw Chicken", MUtil.list("&7Raw Food", " &r", "&7Common &eChicken&7 drop!&7")),
    BEEF(Material.BEEF, "&fRaw Beef", MUtil.list("&7Raw Food", " &r", "&7Common &eCow&7 drop!&7")),
    RABBIT(Material.RABBIT, "&fRaw Rabbit", MUtil.list("&7Raw Food", " &r", "&7Common &eRabbit&7 drop!&7")),
    MUTTON(Material.MUTTON, "&fRaw Mutton", MUtil.list("&7Raw Food", " &r", "&7Common &eSheep&7 drop!&7"));
    final String displayName;
    final List<String> lore;
    final Material material;

    RawFoodType(Material material, String displayName, List<String> lore) {
        this.displayName = displayName;
        this.lore = lore;
        this.material = material;
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
