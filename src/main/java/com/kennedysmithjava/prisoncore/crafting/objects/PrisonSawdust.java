package com.kennedysmithjava.prisoncore.crafting.objects;

import com.kennedysmithjava.prisoncore.crafting.PrisonObject;
import com.kennedysmithjava.prisoncore.util.ItemBuilder;
import com.massivecraft.massivecore.util.MUtil;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class PrisonSawdust extends PrisonObject {
    @Override
    public ItemStack giveRawItem() {
        return new ItemBuilder(Material.BEETROOT_SEEDS)
                .name(getName())
                .lore(MUtil.list("&7Crafting Material", " &r", "&7Please don't eat.."))
                .build();
    }

    @Override
    public String getKey() {
        return "prisonSawdust";
    }

    @Override
    public String getName() {
        return "&6Sawdust";
    }
}
