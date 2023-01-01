package com.kennedysmithjava.prisoncore.crafting.objects;

import com.kennedysmithjava.prisoncore.crafting.PrisonObject;
import com.kennedysmithjava.prisoncore.util.ItemBuilder;
import com.massivecraft.massivecore.util.MUtil;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class PrisonString extends PrisonObject {
    @Override
    public ItemStack giveRawItem() {
        return new ItemBuilder(Material.STRING)
                .name(getName())
                .lore(MUtil.list("&7Crafting Material", " &r", "&7Works as floss"))
                .build();
    }

    @Override
    public String getKey() {
        return "prisonString";
    }

    @Override
    public String getName() {
        return "&6String";
    }
}
