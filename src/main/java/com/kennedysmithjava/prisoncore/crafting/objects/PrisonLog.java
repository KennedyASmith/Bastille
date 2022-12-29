package com.kennedysmithjava.prisoncore.crafting.objects;

import com.kennedysmithjava.prisoncore.PrisonCore;
import com.kennedysmithjava.prisoncore.crafting.PrisonObject;
import com.kennedysmithjava.prisoncore.util.ItemBuilder;
import com.massivecraft.massivecore.util.MUtil;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;
import java.util.Map;

public class PrisonLog extends PrisonObject<PrisonLog> {

    private static final NamespacedKey logKey = new NamespacedKey(PrisonCore.get(), "logKey");

    private final LogType type;

    public PrisonLog(LogType type){
        this.type = type;
    }

    @Override
    public ItemStack giveRawItem() {
        return new ItemBuilder(type.getMaterial()).lore(type.getLore()).name(type.getName()).build();
    }

    @Override
    public String getKey() {
        return "log";
    }

    @Override
    public Map<PersistentDataType<?, ?>, Map<NamespacedKey, ?>> getStoredData() {
        return MUtil.map(PersistentDataType.STRING, MUtil.map(logKey, type.name()));
    }

    public static ItemStack giveFromMaterial(Material material) {
        for (LogType value : LogType.values()) {
            if(value.getMaterial().equals(material)){
                return new PrisonLog(value).give();
            }
        }
        return new ItemStack(Material.STONE);
    }
}

enum LogType {
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
}

