package com.kennedysmithjava.prisonmines.pouch;

import com.kennedysmithjava.prisonmines.util.LazyConsumer;
import com.mcrivals.prisoncore.eco.CurrencyType;
import de.tr7zw.nbtapi.NBTCompound;
import de.tr7zw.nbtapi.NBTCompoundList;
import de.tr7zw.nbtapi.NBTItem;
import de.tr7zw.nbtapi.NBTListCompound;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Pouch {

    private final LazyConsumer<ItemStack> loreUpdater = new LazyConsumer<>(5 * 1000, this::updateLore, true);
    private final LazyConsumer<ItemStack> nbtUpdater = new LazyConsumer<>(60 * 1000, this::updateNbt, true);

    private final UUID uuid;

    private final PouchType type;
    private Map<DatalessPouchable, Integer> pouched;

    private int count;

    public Pouch(final ItemStack itemStack, final UUID uuid) {
        NBTItem nbtItem = new NBTItem(itemStack, true);

        this.uuid = uuid;

        this.type = getPouchType(nbtItem);
        this.pouched = getPouchedItems(nbtItem);

        this.updateLore(itemStack);
        this.updateCount();
    }

    public UUID getUuid() {
        return uuid;
    }

    public PouchType getType() {
        return type;
    }

    protected void updateNbt(ItemStack item) {
        NBTItem nbtItem = new NBTItem(item);

        nbtItem.setInteger(PouchConf.POUCH_TYPE_NBT_TAG, PouchConf.get().getPouchTypeID(this.type));
        NBTCompoundList list = nbtItem.getCompoundList(PouchConf.POUCH_DATA_TAG);
        list.clear();

        this.pouched.forEach((key, value) -> {
            NBTCompound compound = list.addCompound();
            compound.setInteger(PouchConf.POUCH_DATA_QUANTITY_TAG, value);

            compound.setString(PouchConf.POUCH_DATA_DISPLAY_TAG, key.getDisplayName());
            compound.setString(PouchConf.POUCH_DATA_NBT_VALUE_TAG, key.getUniqueNbt());
            compound.setString(PouchConf.POUCH_DATA_NBT_CURRENCY_TAG, key.getCurrencyType().toString());
            compound.setDouble(PouchConf.POUCH_DATA_VALUE_TAG, key.getValue());
        });

        nbtItem.applyNBT(item);
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
        Bukkit.broadcastMessage(newAmount + " blocks");
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

    private static PouchType getPouchType(NBTItem item) {
        return PouchType.from(item.getInteger(PouchConf.POUCH_TYPE_NBT_TAG));
    }

    private static Map<DatalessPouchable, Integer> getPouchedItems(NBTItem item) {
        NBTCompoundList list = item.getCompoundList(PouchConf.POUCH_DATA_TAG);

        Map<DatalessPouchable, Integer> pouchedItems = new HashMap<>(10);

        for (NBTListCompound nbtListCompound : list) {

            double value = nbtListCompound.getDouble(PouchConf.POUCH_DATA_VALUE_TAG);
            String nbt = nbtListCompound.getString(PouchConf.POUCH_DATA_NBT_VALUE_TAG);
            String display = nbtListCompound.getString(PouchConf.POUCH_DATA_DISPLAY_TAG);
            CurrencyType currencyType = CurrencyType.valueOf(nbtListCompound.getString(PouchConf.POUCH_DATA_NBT_CURRENCY_TAG));

            DatalessPouchable pouchable = new DatalessPouchable(nbt, value, currencyType, display);

            int quantity = nbtListCompound.getInteger(PouchConf.POUCH_DATA_QUANTITY_TAG);

            pouchedItems.put(pouchable, quantity);
        }

        return pouchedItems;
    }

    public static boolean isPouch(ItemStack item) {
        return item != null && !item.getType().equals(Material.AIR) && new NBTItem(item).hasKey(PouchConf.POUCH_TYPE_NBT_TAG);
    }

}
