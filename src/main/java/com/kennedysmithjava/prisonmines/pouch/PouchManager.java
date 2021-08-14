package com.kennedysmithjava.prisonmines.pouch;

import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PouchManager {

    private static final String NBT_POUCH_UUID = "POUCH_UUID";

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
            uuid = UUID.fromString(new NBTItem(itemStack).getString(NBT_POUCH_UUID));
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

    public UUID generateUUID(NBTItem itemStack) {
        UUID uuid;
        do {
            uuid = UUID.randomUUID();
        } while (this.pouches.containsKey(uuid));

        itemStack.setString(NBT_POUCH_UUID, uuid.toString());

        return uuid;
    }


}
