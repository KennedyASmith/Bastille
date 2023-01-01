package com.kennedysmithjava.prisoncore.crafting.objects;

import com.kennedysmithjava.prisoncore.crafting.PrisonObject;
import com.kennedysmithjava.prisoncore.util.ItemBuilder;
import com.massivecraft.massivecore.util.MUtil;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class PrisonBowl extends PrisonObject {
    @Override
    public ItemStack giveRawItem() {
        return new ItemBuilder(Material.BOWL)
                .name(getName())
                .lore(MUtil.list("&7Crafting Material", " &r", "&7Fill with soup!"))
                .build();
    }

    @Override
    public String getKey() {
        return "prisonBowl";
    }

    @Override
    public String getName() {
        return "&6Wooden Bowl";
    }
}
