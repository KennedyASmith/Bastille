package com.kennedysmithjava.prisoncore.storage;

public enum StorageType {

    PRESTIGE(0, 30),
    QUEST(1, 32),
    STORAGE_1(2, 10),

    STORAGE_2(3, 11),

    STORAGE_3(4, 12),

    STORAGE_4(5, 13),

    STORAGE_5(6, 14),

    STORAGE_6(7, 15),

    STORAGE_7(8, 16);

    private final int id;
    private final int slot;

    StorageType(int id, int slot){
        this.id = id;
        this.slot = slot;
    }

    public int getId() {
        return id;
    }

    public int getSlot() {
        return slot;
    }
}
