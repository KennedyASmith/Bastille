package com.kennedysmithjava.prisoncore.crafting;

import com.kennedysmithjava.prisoncore.PrisonCore;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.HashMap;
import java.util.Map;

public abstract class PrisonObject {


    public abstract ItemStack giveRawItem();
    public static NamespacedKey prisonObjectKey = new NamespacedKey(PrisonCore.get(), "pObj");

    public abstract String getKey();

    public <Z, T> ItemStack give(int amount) {
        ItemStack item = this.giveRawItem();
        item.setAmount(amount);
        ItemMeta meta = item.getItemMeta();

        if (meta != null){
            PersistentDataContainer pdc = meta.getPersistentDataContainer();
            getStoredData().forEach((dataType, objMap) ->
                    objMap.forEach((namespacedKey, o) ->
                            pdc.set(namespacedKey, (PersistentDataType<T, Z>) dataType, (Z) o)));
            pdc.set(prisonObjectKey, PersistentDataType.STRING, getKey());
        }
        item.setItemMeta(meta);
        return item;
    }

    public abstract String getName();

    public Map<PersistentDataType<?, ?>, Map <NamespacedKey, ?>>  getStoredData() {
        return new HashMap<>();
    };

    /**
     * Checks if this ItemStack is a PrisonObject of this type.
     * @param itemStack the item stack to be checked
     * @return true if it is
     */
    public boolean is(ItemStack itemStack){
        ItemMeta meta = itemStack.getItemMeta();
        if(meta == null) return false;
        PersistentDataContainer pdc = meta.getPersistentDataContainer();
        String key = pdc.get(prisonObjectKey, PersistentDataType.STRING);
        if(key == null) return false;
        return key.equals(getKey());
    }

    public static boolean isPrisonObj(ItemStack itemStack){
        ItemMeta meta = itemStack.getItemMeta();
        if(meta == null) return false;
        PersistentDataContainer pdc = meta.getPersistentDataContainer();
        String key = pdc.get(prisonObjectKey, PersistentDataType.STRING);
        return (key != null);
    }
}
