package com.kennedysmithjava.prisoncore.crafting.objects;

import com.kennedysmithjava.prisoncore.crafting.PrisonObject;
import com.kennedysmithjava.prisoncore.util.ItemBuilder;
import com.massivecraft.massivecore.util.MUtil;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class PrisonWoodenAxe extends PrisonObject {
    @Override
    public ItemStack giveRawItem() {
        return new ItemBuilder(Material.WOODEN_AXE)
                .name(getName())
                .lore(MUtil.list("&7Woodcutting Tool", " &r", "&7Not very sharp"))
                .build();
    }

    @Override
    public String getKey() {
        return "prisonWoodenAxe";
    }

    @Override
    public String getName() {
        return "&6Wooden Axe";
    }
}
