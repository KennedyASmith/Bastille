package com.kennedysmithjava.prisoncore.crafting;

import java.util.HashMap;
import java.util.Map;

public enum Recipe {

    STICK,
    WOODEN_CHESTPLATE,
    WOODEN_LEGGINGS,
    WOODEN_BOOTS,
    WOODEN_HELMET;

    //Key: GUI Slot ||| Value: Ingredient for that slot
    public Map<Integer, PrisonObject<?>> ingredients = new HashMap<>();



}
