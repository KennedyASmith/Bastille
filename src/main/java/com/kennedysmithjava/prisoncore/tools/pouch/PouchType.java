package com.kennedysmithjava.prisoncore.tools.pouch;

import com.kennedysmithjava.prisoncore.entity.tools.PouchConf;
import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public final class PouchType {

    private final int capacity;
    private final int typeCapacity;

    private final String format;

    private final String name;
    private final Material icon;
    private final List<String> lore;

    public PouchType(String name, Material icon, List<String> lore, String format, int capacity, int typeCapacity) {
        this.capacity = capacity;
        this.typeCapacity = typeCapacity;
        this.format = format;
        this.name = name;
        this.icon = icon;
        this.lore = lore;
    }

    public static PouchType from(int id) {
        return PouchConf.get().pouches.getOrDefault(id, null);
    }

    public int getCapacity() {
        return capacity;
    }

    public int getTypeCapacity() {
        return typeCapacity;
    }

    public String getName() {
        return name;
    }

    public Material getIcon() {
        return icon;
    }

    public List<String> getLore() {
        return lore;
    }

    public List<String> getCompiledLore(Pouch pouch) {
        List<String> res = new ArrayList<>(this.lore);
        res.add(" ");
        pouch.getPouched().forEach((p, i) ->
            res.add(ChatColor.translateAlternateColorCodes('&', String.format(this.format, i, p.getDisplayName())))
        );

        return res;
    }

    public ItemStack getNewPouchItem() {
        ItemStack itemStack = new ItemStack(this.icon);

        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setLore(this.getLore());
        itemMeta.setDisplayName(this.name);
        itemStack.setItemMeta(itemMeta);

        NBTItem nbtItem = new NBTItem(itemStack);

        PouchManager.get().generateUUID(nbtItem);

        nbtItem.setInteger(PouchConf.POUCH_TYPE_NBT_TAG, PouchConf.get().getPouchTypeID(this));
        nbtItem.getCompoundList(PouchConf.POUCH_DATA_TAG);

        nbtItem.applyNBT(itemStack);
        return itemStack;
    }

}
