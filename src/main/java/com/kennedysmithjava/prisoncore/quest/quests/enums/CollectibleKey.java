package com.kennedysmithjava.prisoncore.quest.quests.enums;

public enum CollectibleKey {

    MINE_ITEM("mineItem");

    final String key;

    CollectibleKey(String key){
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
