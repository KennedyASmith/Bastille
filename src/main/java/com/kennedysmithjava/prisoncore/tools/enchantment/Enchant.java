package com.kennedysmithjava.prisoncore.tools.enchantment;

import org.bukkit.Material;

import java.util.*;

public abstract class Enchant<E extends Enchant<E>> {

    // -------------------------------------------- //
    // ENCHANTMENT REGISTRY
    // -------------------------------------------- //

    public static Map<String, Enchant<?>> activeEnchants = new HashMap<>();

    public static void register(Enchant<?> enchant){
        activeEnchants.put(enchant.getID(), enchant);
    }

    public static Map<String, Enchant<?>> getActiveEnchants() {
        return activeEnchants;
    }

    public static Enchant<?> getByName(String name){
        Enchant<?> e = activeEnchants.get(name);
        if(e != null){
            return e;
        }
        throw new NullPointerException();
    }

    public static boolean exists(String name){
        return activeEnchants.containsKey(name);
    }

    // -------------------------------------------- //
    // ABSTRACT METHODS
    // -------------------------------------------- //

    public Enchant(){
        register(this);
    }

    public abstract String getID();

    public abstract String getDisplayName();

    public abstract int getMaxLevel();

    public abstract int getMinLevel();

    public abstract Collection<String> getAllowedToolIDs();

    public abstract int getEnchantGUISlot();

    protected abstract List<String> getUnformattedGUILore();
    //Used for placeholders. How much is this enchant effected by its level?
    //Ex. For a level 1 ExplosiveEnchant it might return "Radius: 4" (Radius = 3 + level)
    public abstract String getMagnitudeString(int level);

    public abstract String getName();

    public abstract String getLore();

    public abstract Material getIcon();

    public String getSimpleName(){
        return getClass().getSimpleName();
    }

    public List<String> getGUILore(int level){
        List<String> lore = new ArrayList<>();
        getUnformattedGUILore().forEach(s ->
                lore.add(s
                        .replaceAll("%level%", String.valueOf(level))
                        .replaceAll("%max_level%", String.valueOf(getMaxLevel()))
                        .replaceAll("%min_level%", String.valueOf(getMinLevel()))
                        .replaceAll("%magnitude%", getMagnitudeString(level))
                ));
        return lore;
    }

    public int incrementLevel(int level){
        return Math.min(level, getMaxLevel());
    }

}
