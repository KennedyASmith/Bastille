package com.kennedysmithjava.prisoncore.tools.pouch;

import com.kennedysmithjava.prisoncore.blockhandler.ValueMutable;
import com.kennedysmithjava.prisoncore.eco.CurrencyType;
import com.massivecraft.massivecore.util.MUtil;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public class DatalessPouchable implements Pouchable, ValueMutable, Comparable<DatalessPouchable>, ConfigurationSerializable {

    private double value;
    private final String uuid;
    private final String displayName;
    private final CurrencyType currencyType;

    public DatalessPouchable(String uuid, double value, CurrencyType currencyType, String displayName) {
        this.value = value;
        this.uuid = uuid;
        this.displayName = displayName;
        this.currencyType = currencyType;
    }

    public DatalessPouchable(Map<String, Object> serialized) {
        this((String) serialized.get("UUID"),
                (Double) serialized.get("VALUE"),
                CurrencyType.valueOf((String) serialized.get("CURRENCYTYPE")),
                (String) serialized.get("DISPLAYNAME"));
    }

    @Override
    public double getValue() {
        return this.value;
    }

    @Override
    public void setValue(double amount) {
        this.value = amount;
    }

    @Override
    public String getUniqueID() {
        return this.uuid;
    }

    @Override
    public String getDisplayName() {
        return this.displayName;
    }

    @Override
    public CurrencyType getCurrencyType() {
        return currencyType;
    }

    @Override
    public String toString() {
        return "DatalessPouchable{" +
                "value=" + value +
                ", uuid='" + uuid + '\'' +
                ", displayName='" + displayName + '\'' +
                ", currencyType=" + currencyType +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DatalessPouchable pouchable = (DatalessPouchable) o;
        return uuid.equals(pouchable.uuid);
    }

    @Override
    public int hashCode() {
        return this.uuid.hashCode();
    }

    @Override
    public int compareTo(DatalessPouchable o) {
        return uuid.compareTo(o.getUniqueID());
    }

    @Override
    public ItemStack getProductItem(int amount) {
        return null;
    }

    public Map<String, Object> serialize() {
        return MUtil.map(
                "VALUE", value,
                    "UUID", uuid,
                "DISPLAYNAME", displayName,
                "CURRENCYTYPE", currencyType.toString()
        );
    }
    public static DatalessPouchable valueOf(Map<String, Object> serializable) {
        return deserialize(serializable);
    }
    public static DatalessPouchable deserialize(Map<String, Object> serializable) {
        return new DatalessPouchable(
                (String) serializable.get("UUID"),
                (Double) serializable.get("VALUE"),
                CurrencyType.valueOf((String) serializable.get("CURRENCYTYPE")),
                (String) serializable.get("DISPLAYNAME")
        );
    }
}
