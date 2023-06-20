package com.kennedysmithjava.prisoncore.crafting.objects;

import com.kennedysmithjava.prisoncore.crafting.PrisonObject;
import com.kennedysmithjava.prisoncore.tools.Pickaxe;
import com.kennedysmithjava.prisoncore.util.ItemBuilder;
import com.massivecraft.massivecore.util.MUtil;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class PrisonEnchantable extends PrisonObject {
    @Override
    public ItemStack giveRawItem() {
        return new ItemBuilder(Material.STONE_SHOVEL)
                .name(getName())
                .lore(MUtil.list("&7Debug Item", " &r"))
                .build();
    }

    @Override
    public String getKey() {
        return "prisonEnchantable";
    }

    @Override
    public String getName() {
        return "&6Any Enchantable Tool";
    }

    @Override
    public boolean is(ItemStack itemStack) {
        return Pickaxe.isPickaxe(itemStack);
    }
}
