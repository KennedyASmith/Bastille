package com.kennedysmithjava.prisonmines.pouch;

import com.kennedysmithjava.prisonmines.util.LazyActor;
import com.mcrivals.prisoncore.CurrencyType;
import de.tr7zw.nbtapi.NBTCompound;
import de.tr7zw.nbtapi.NBTCompoundList;
import de.tr7zw.nbtapi.NBTItem;
import de.tr7zw.nbtapi.NBTListCompound;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.Map;

public class Pouch {

    private final LazyActor loreUpdater = new LazyActor(5 * 1000, this::updateLore, true);
    private final LazyActor nbtUpdater = new LazyActor(60 * 1000, this::updateNbt, true);

    private final PouchType type;
    private final Map<DatalessPouchable, Integer> pouched;

    private int count;

    private final transient NBTItem item;

    public Pouch(ItemStack itemStack) {
        this.item = new NBTItem(itemStack);

        this.type = getPouchType(this.item);
        this.pouched = getPouchedItems(this.item);

        this.updateLore();
        this.updateCount();
    }

    protected void updateNbt() {
        this.item.setInteger(PouchConf.POUCH_TYPE_NBT_TAG, PouchConf.get().getPouchTypeID(this.type));
        NBTCompoundList list = this.item.getCompoundList(PouchConf.POUCH_DATA_TAG);
        list.clear();

        this.pouched.forEach((key, value) -> {
            NBTCompound compound = list.addCompound();
            compound.setInteger(PouchConf.POUCH_DATA_QUANTITY_TAG, value);

            compound.setString(PouchConf.POUCH_DATA_DISPLAY_TAG, key.getDisplayName());
            compound.setString(PouchConf.POUCH_DATA_NBT_VALUE_TAG, key.getUniqueNbt());
            compound.setString(PouchConf.POUCH_DATA_NBT_CURRENCY_TAG, key.getCurrencyType().toString());
            compound.setDouble(PouchConf.POUCH_DATA_VALUE_TAG, key.getValue());
        });
    }

    public void updateCount() {
        this.count = this.pouched.values().stream().mapToInt(i -> i).sum();
    }

    public void emptyPouch() {
        this.pouched.clear();

        this.loreUpdater.forceTrigger();
        this.nbtUpdater.forceTrigger();
    }

    public void pouch(Pouchable pouchable, int amount) throws PouchFullException {
        int capacity = (this.type.getCapacity() - this.count) / pouchable.getPouchWeight();

        if (amount > capacity) {
            this.pouch(pouchable, capacity);
            throw new PouchFullException(amount - capacity);
        }

        this.loreUpdater.trigger();
        this.nbtUpdater.trigger();
        pouched.compute(pouchable.toDataless(), (p, i) -> i == null ? amount : i + amount);
    }

    public void pouch(Pouchable pouchable) throws PouchFullException {
        if (pouchable.getPouchWeight() + this.count > this.type.getCapacity()) {
            throw new PouchFullException();
        }

        this.loreUpdater.trigger();
        this.nbtUpdater.trigger();
        pouched.compute(pouchable.toDataless(), (p, i) -> i == null ? 1 : i + 1);
    }

    public Map<DatalessPouchable, Integer> getPouched() {
        return pouched;
    }

    private void updateLore() {
        ItemStack item = this.item.getItem();
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
        return getPouchType(new NBTItem(item)) != null;
    }

}
