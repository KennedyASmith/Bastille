package com.kennedysmithjava.prisoncore.crafting.objects;

import com.kennedysmithjava.prisoncore.crafting.PrisonObject;
import com.kennedysmithjava.prisoncore.util.ItemBuilder;
import com.massivecraft.massivecore.util.MUtil;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class PrisonEye extends PrisonObject {
    @Override
    public ItemStack giveRawItem() {
        return new ItemBuilder(Material.SPIDER_EYE)
                .name(getName())
                .lore(MUtil.list("&7Crafting Material", " &r", "&7With this, you can keep a watchful eye!"))
                .build();
    }

    @Override
    public String getKey() {
        return "prisonEye";
    }

    @Override
    public String getName() {
        return "&fEye";
    }
}
