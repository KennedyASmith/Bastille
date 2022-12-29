package com.kennedysmithjava.prisoncore.crafting.objects.type;

import com.massivecraft.massivecore.util.MUtil;
import org.bukkit.Material;

import java.util.List;

public enum LogType {
    ANY(Material.AIR, "&7Any Log Type", MUtil.list()),
    SPRUCE_LOG(Material.SPRUCE_LOG, "&6Spruce Log", MUtil.list("&7Crafting Material", " &r", "&7Common material used for", "&7crafting &eSticks &7and other items!")),
    ACACIA_LOG(Material.ACACIA_LOG, "&6Acacia Log", MUtil.list("&7Crafting Material", " &r", "&7Common material used for", "&7crafting &eSticks &7and other items!")),
    OAK_LOG(Material.OAK_LOG, "&6Oak Log", MUtil.list("&7Crafting Material", " &r", "&7Common material used for", "&7crafting &eSticks &7and other items!")),
    JUNGLE_LOG(Material.JUNGLE_LOG, "&6Oak Log", MUtil.list("&7Crafting Material", " &r", "&7Common material used for", "&7crafting &eSticks &7and other items!")),
    DARK_OAK_LOG(Material.DARK_OAK_LOG, "&6Oak Log", MUtil.list("&7Crafting Material", " &r", "&7Common material used for", "&7crafting &eSticks &7and other items!")),
    BIRCH_LOG(Material.BIRCH_LOG, "&6Birch Log", MUtil.list("&7Crafting Material", " &r", "&7Common material used for", "&7crafting &eSticks &7and other items!"));

    private final Material material;
    private final String name;
    private final List<String> lore;

    LogType(Material material, String name, List<String> lore) {
        this.material = material;
        this.name = name;
        this.lore = lore;
    }

    public List<String> getLore() {
        return lore;
    }

    public Material getMaterial() {
        return material;
    }

    public String getName() {
        return name;
    }

    public static LogType getFromMaterial(Material material) {
        for (LogType value : LogType.values()) {
            if (value.getMaterial().equals(material)) {
                return value;
            }
        }
        return null;
    }
}
