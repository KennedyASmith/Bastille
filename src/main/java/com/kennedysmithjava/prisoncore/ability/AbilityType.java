package com.kennedysmithjava.prisoncore.ability;

import java.util.HashMap;
import java.util.Map;

public enum AbilityType {

    ASTEROID("astr"),
    TETRIS("tetris"),
    PULVERIZE("pulv"),
    BLACKHOLE("blckhle");

    private final String id;

    AbilityType(String id){
        this.id = id;
    }

    public String getId() {
        return id;
    }

    private static final Map<String, AbilityType> idToTypeMap = new HashMap<>();

    static {
        for(AbilityType abilityType : values()) {
            idToTypeMap.put(abilityType.getId(), abilityType);
        }
    }

    public static AbilityType getFromId(String id) {
        return idToTypeMap.get(id);
    }

}
