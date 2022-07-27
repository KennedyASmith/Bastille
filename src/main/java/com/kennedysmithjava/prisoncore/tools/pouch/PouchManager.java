package com.kennedysmithjava.prisoncore.tools.pouch;

import com.jeff_media.morepersistentdatatypes.DataType;
import com.kennedysmithjava.prisoncore.PrisonCore;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PouchManager {

    private static final NamespacedKey uuidKey = new NamespacedKey(PrisonCore.get(), "POUCH_UUID");

    private static final PouchManager i = new PouchManager();

    public static PouchManager get() {
        return i;
    }

    public PouchManager() {
        this.pouches = new HashMap<>();
    }

    private final Map<UUID, Pouch> pouches;

    public Pouch getPouch(ItemStack itemStack) {
       UUID uuid;
        try {
            uuid = itemStack.getItemMeta().getPersistentDataContainer().get(uuidKey, DataType.UUID);
        } catch (IllegalArgumentException e) {
            return null;
        }

       Pouch pouch = this.pouches.get(uuid);
       if (pouch == null) {
           pouch = new Pouch(itemStack, uuid);
           this.pouches.put(uuid, pouch);
       }

       return pouch;
    }

    public UUID generateUUID(ItemStack itemStack) {
        UUID uuid;
        do {
            uuid = UUID.randomUUID();
        } while (this.pouches.containsKey(uuid));

        itemStack.getItemMeta().getPersistentDataContainer().set(uuidKey, DataType.UUID, uuid);

        return uuid;
    }


}
