package com.kennedysmithjava.prisonmines.pouch;

import com.massivecraft.massivecore.store.Entity;
import com.massivecraft.massivecore.util.MUtil;
import org.bukkit.Material;

import java.util.Map;

public class PouchConf extends Entity<PouchConf>  {

    public static final transient String POUCH_DATA_VALUE_TAG = "VALUE";
    public static final transient String POUCH_DATA_DISPLAY_TAG = "DISPLAY";
    public static final transient String POUCH_DATA_QUANTITY_TAG = "QUANTITY";
    public static final transient String POUCH_DATA_NBT_VALUE_TAG = "NBT";
    public static final transient String POUCH_DATA_NBT_CURRENCY_TAG = "NBT";

    public static final transient String POUCH_DATA_TAG = "POUCH_DATA";
    public static final transient String POUCH_TYPE_NBT_TAG = "POUCH_TYPE";

    protected static transient PouchConf i = new PouchConf();

    public static PouchConf get() {
        return i;
    }

    public int getPouchTypeID(PouchType type) {
        return pouches.entrySet().stream().filter(e -> e.getValue().equals(type)).map(Map.Entry::getKey).findAny().orElse(-1);
    }

    public Map<Integer, PouchType> pouches = MUtil.map(
            0, new PouchType("Test Pouch", Material.INK_SACK, MUtil.list("Test1"), "%dx - %s", 2000, 10)
    );

}
