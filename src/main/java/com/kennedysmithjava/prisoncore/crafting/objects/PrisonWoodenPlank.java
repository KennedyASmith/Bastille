package com.kennedysmithjava.prisoncore.crafting.objects;

import com.kennedysmithjava.prisoncore.crafting.PrisonObject;
import com.kennedysmithjava.prisoncore.util.ItemBuilder;
import com.massivecraft.massivecore.util.MUtil;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class PrisonWoodenPlank extends PrisonObject {
    @Override
    public ItemStack giveRawItem() {
        return new ItemBuilder(Material.OAK_PLANKS)
                .name(getName())
                .lore(MUtil.list("&7Crafting Material", " &r", "&7Might give splinters."))
                .build();
    }

    @Override
    public String getKey() {
        return "prisonWoodenPlank";
    }

    @Override
    public String getName() {
        return "&6Wooden Plank";
    }
}
