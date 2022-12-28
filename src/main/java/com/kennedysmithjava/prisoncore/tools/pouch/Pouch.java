package com.kennedysmithjava.prisoncore.tools.pouch;

import com.jeff_media.morepersistentdatatypes.DataType;
import com.kennedysmithjava.prisoncore.PrisonCore;
import com.kennedysmithjava.prisoncore.util.LazyConsumer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;

import java.util.*;

public class Pouch {

    private static final NamespacedKey pouchTypeKey  = new NamespacedKey(PrisonCore.get(), "pType");
    private static final NamespacedKey dataKey = new NamespacedKey(PrisonCore.get(), "pData");
    private static final NamespacedKey quantityKey = new NamespacedKey(PrisonCore.get(), "pQuantity");

    private final LazyConsumer<ItemStack> loreUpdater = new LazyConsumer<>(5 * 1000, this::updateLore, true);
    private final LazyConsumer<ItemStack> nbtUpdater = new LazyConsumer<>(60 * 1000, this::updateStoredData, true);

    private final UUID uuid;

    private final PouchType type;
    private final Map<DatalessPouchable, Integer> pouched;

    private int count;

    public Pouch(final ItemStack itemStack, final UUID uuid) {
        this.uuid = uuid;

        this.type = getPouchType(itemStack);
        this.pouched = getPouchedItems(itemStack);

        this.updateLore(itemStack);
        this.updateCount();
    }

    public UUID getUuid() {
        return uuid;
    }

    public PouchType getType() {
        return type;
    }

    protected void updateStoredData(ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        PersistentDataContainer pdc = meta.getPersistentDataContainer();

        List<ConfigurationSerializable> pouchables = new ArrayList<>();
        Map<String, Integer> quantity = new HashMap<>();

        // Add back in each pouched item
        this.pouched.forEach((key, value) -> {
            pouchables.add(key);
            quantity.put(key.getUniqueID(), value);
        });

        // Reset pouched items
        pdc.set(dataKey, DataType.asList(DataType.CONFIGURATION_SERIALIZABLE), pouchables);
        pdc.set(quantityKey, DataType.asMap(DataType.STRING, DataType.INTEGER), quantity);
        item.setItemMeta(meta);
    }

    public void updateCount() {
        this.count = this.pouched.values().stream().mapToInt(i -> i).sum();
    }

    public void emptyPouch(ItemStack item) {
        this.pouched.clear();

        //TODO: LORE AND NBT UPDATE TRIGGER
        loreUpdater.forceTrigger(item);
        nbtUpdater.forceTrigger(item);
    }

    public void removeAllPouched(ItemStack pouch, Map<DatalessPouchable, Integer> toRemove){
        toRemove.forEach((p, a) -> {
            int amount = this.pouched.get(p);
            int diff = (amount - a);
            if(diff == 0){
                pouched.remove(p);
            } else{
                pouched.put(p, diff);
            }
        });
        //TODO: LORE AND NBT UPDATE TRIGGER
        loreUpdater.forceTrigger(pouch);
        nbtUpdater.forceTrigger(pouch);
    }

    public void removePouched(ItemStack pouch, DatalessPouchable p, int a) {
        int amount = this.pouched.get(p);
        int diff = (amount - a);
        if(diff == 0){
            pouched.remove(p);
        } else{
            pouched.put(p, diff);
        }

        //TODO: LORE AND NBT UPDATE TRIGGER
        loreUpdater.forceTrigger(pouch);
        nbtUpdater.forceTrigger(pouch);
    }

    public void pouch(Pouchable pouchable, int amount, ItemStack item) throws PouchFullException {
        int capacity = (this.type.getCapacity() - this.count) / pouchable.getPouchWeight();

        if (amount > capacity) {
            this.pouch(pouchable, capacity, item);
            throw new PouchFullException(amount - capacity);
        }

        DatalessPouchable key = pouchable.toDataless();
        int newAmount = pouched.getOrDefault(key, 0) + amount;
        pouched.put(key, newAmount);

        count += amount;

        this.nbtUpdater.trigger(item);
        this.loreUpdater.forceTrigger(item);

    }

    public void pouch(Pouchable pouchable, ItemStack item) throws PouchFullException {
        if (pouchable.getPouchWeight() + this.count > this.type.getCapacity()) {
            throw new PouchFullException();
        }

        DatalessPouchable key = pouchable.toDataless();
        int newAmount = pouched.getOrDefault(key, 0) + 1;
        pouched.put(key, newAmount);
        count++;

        this.updateLore(item);
        this.nbtUpdater.trigger(item);
    }

    public Map<DatalessPouchable, Integer> getPouched() {
        return this.pouched;
    }

    public void updateLore(ItemStack item) {
        //this.pouched = MiscUtil.sortByValue(this.pouched);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setLore(this.type.getCompiledLore(this));
        item.setItemMeta(itemMeta);
    }

    private static PouchType getPouchType(ItemStack item) {
        return PouchType.from(item.getItemMeta().getPersistentDataContainer().get(pouchTypeKey, DataType.INTEGER));
    }

    private static Map<DatalessPouchable, Integer> getPouchedItems(ItemStack item) {
        PersistentDataContainer pdc = item.getItemMeta().getPersistentDataContainer();

        List<ConfigurationSerializable> pouchableList = pdc.getOrDefault(dataKey, DataType.asList(DataType.CONFIGURATION_SERIALIZABLE), new ArrayList<>());
        Map<DatalessPouchable, Integer> pouchedItems = new HashMap<>(10);
        Map<String, Integer> quantities = pdc.getOrDefault(quantityKey, DataType.asMap(DataType.STRING, DataType.INTEGER), new HashMap<>());

        for (ConfigurationSerializable pouchableSerialized : pouchableList) {
            DatalessPouchable pouchable = (DatalessPouchable) pouchableSerialized;
            int quantity = quantities.get(pouchable.getUniqueID());
            pouchedItems.put(pouchable, quantity);
        }

        return pouchedItems;
    }

    public static boolean isPouch(ItemStack item) {
        return item != null && !item.getType().equals(Material.AIR) && item.getItemMeta().getPersistentDataContainer().has(pouchTypeKey, DataType.INTEGER);
    }

    public static NamespacedKey getPouchTypeKey() {
        return pouchTypeKey;
    }
}
