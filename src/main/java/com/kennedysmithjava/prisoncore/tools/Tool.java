package com.kennedysmithjava.prisoncore.tools;

import com.kennedysmithjava.prisoncore.entity.tools.PickaxeTypeColl;
import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public interface Tool {

    //TODO: Scrap this and replace with pickaxe class. Not needed.

    ItemStack getItem();
    String itemString = "type-id";

    static Tool get(ItemStack i) {
        NBTItem item = new NBTItem(i);
        String type = item.getString(itemString);
        String uuid = item.getString("uuid");
        //TODO: So far pickaxes are the only tool. This will change!
        Pickaxe tool = Pickaxe.getByUUID(uuid);
        return (tool != null) ? tool : new Pickaxe(PickaxeTypeColl.get().get(type), i, uuid);
    }

    static boolean isTool(ItemStack i) {
        if (i == null || i.getType() == Material.AIR) return false;
        if(!isPickaxe(i.getType())) return false;

        NBTItem n = new NBTItem(i);

        if (!n.hasNBTData()) { return false; }
        String id = n.getString(itemString);
        return (id != null && !id.equals(""));
    }

    static boolean isPickaxe(Material material){
        switch(material){
            case DIAMOND_PICKAXE:
            case WOODEN_PICKAXE:
            case STONE_PICKAXE:
            case IRON_PICKAXE:
            case GOLDEN_PICKAXE:
                return true;
            default:
                return false;
        }
    }

}
