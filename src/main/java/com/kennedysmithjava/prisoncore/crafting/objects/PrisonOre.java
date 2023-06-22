package com.kennedysmithjava.prisoncore.crafting.objects;

import com.kennedysmithjava.prisoncore.crafting.PrisonObject;
import com.kennedysmithjava.prisoncore.crafting.objects.type.OreType;
import com.kennedysmithjava.prisoncore.util.Color;
import com.kennedysmithjava.prisoncore.util.ItemBuilder;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class PrisonOre extends PrisonObject {

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
    public String getKey() {
        return "oreKey";
    }

    @Override
    public String getName() {
        return type.getDisplayName();
    }

}

