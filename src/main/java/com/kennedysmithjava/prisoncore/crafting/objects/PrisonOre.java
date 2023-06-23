package com.kennedysmithjava.prisoncore.crafting.objects;

import com.kennedysmithjava.prisoncore.PrisonCore;
import com.kennedysmithjava.prisoncore.crafting.PrisonObject;
import com.kennedysmithjava.prisoncore.crafting.objects.type.OreType;
import com.kennedysmithjava.prisoncore.util.Color;
import com.kennedysmithjava.prisoncore.util.ItemBuilder;
import com.massivecraft.massivecore.util.MUtil;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PrisonOre extends PrisonObject {

    private static NamespacedKey TYPE_KEY = new NamespacedKey(PrisonCore.get(), "oreType");
    private OreType type;

    public PrisonOre(OreType type){
        this.type = type;
    }

    public OreType getType() {
        return type;
    }

    @Override
    public ItemStack giveRawItem() {
        ItemStack item = new ItemStack(getType().getMaterial());
        List<String> lore = new ArrayList<>();
        for (String line : type.getDescription()) {
            lore.addAll(Color.wordWrap(line, 30, 100));
        }
        return new ItemBuilder(item)
                .name(type.getDisplayName())
                 .lore(lore)
                 .build();
    }

    @Override
    public Map<PersistentDataType<?, ?>, Map<NamespacedKey, ?>> getStoredData() {
        return MUtil.map(PersistentDataType.STRING, MUtil.map(TYPE_KEY, type.getMetalType().toString()));
    }

    @Override
    public String getKey() {
        return "oreKey";
    }

    @Override
    public String getName() {
        return type.getDisplayName();
    }

}

