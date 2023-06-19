package com.kennedysmithjava.prisoncore.crafting.objects;

import com.kennedysmithjava.prisoncore.crafting.PrisonObject;
import com.kennedysmithjava.prisoncore.util.ItemBuilder;
import com.massivecraft.massivecore.util.MUtil;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class PrisonFoot extends PrisonObject {
    @Override
    public ItemStack giveRawItem() {
        return new ItemBuilder(Material.RABBIT_FOOT)
                .name(getName())
                .lore(MUtil.list("&7Crafting Material", " &r", "&7The foot of an animal!"))
                .build();
    }

    @Override
    public String getKey() {
        return "prisonFoot";
    }

    @Override
    public String getName() {
        return "&fAnimal Foot";
    }
}
