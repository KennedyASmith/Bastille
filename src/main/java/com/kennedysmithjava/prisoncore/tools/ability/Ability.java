package com.kennedysmithjava.prisoncore.tools.ability;

import com.kennedysmithjava.prisoncore.engine.EngineTools;
import com.kennedysmithjava.prisoncore.event.EventAbilityUse;
import org.bukkit.Material;
import com.kennedysmithjava.prisoncore.tools.Buffer;

import java.util.*;

public abstract class Ability<E extends Ability<E>>{


    // -------------------------------------------- //
    // ABILITY REGISTRY
    // -------------------------------------------- //

    public static Map<String, Ability<?>> activeAbilities = new HashMap<>();
    public static Map<AbilityType, Ability<?>> typeAbilityMap = new HashMap<>();


    public static void register(Ability<?> ability){
        activeAbilities.put(ability.getID(), ability);
        typeAbilityMap.put(ability.getAbilityType(), ability);
    }

    public static Map<String, Ability<?>> getActiveAbilities() {
        return activeAbilities;
    }

    public static Ability<?> getByName(String name) throws NullPointerException {
        Ability<?> a = activeAbilities.get(name);
        if(a != null) return a;
        throw new NullPointerException();
    }

    public static Ability<?> getByType(AbilityType abilityType) throws NullPointerException {
        Ability<?> a = typeAbilityMap.get(abilityType);
        if(a != null) return a;
        throw new NullPointerException();
    }

    public static boolean exists(String name){
        return activeAbilities.containsKey(name);
    }

    protected final AbilityType abilityType;

    public Ability(AbilityType abilityType){
        this.abilityType = abilityType;
        register(this);
    }

    // -------------------------------------------- //
    // METHODS
    // -------------------------------------------- //

    public String getID() {
        return this.abilityType.getId();
    }

    public AbilityType getAbilityType() {
        return abilityType;
    }

    public Material getItemMaterial() {
        return Material.FIRE_CHARGE;
    }

    public Set<Buffer> getAllowedBuffers() {
        return getMaxBufferLevels().keySet();
    }

    public int getMaxBufferLevel(Buffer buffer) {
        return getMaxBufferLevels().get(buffer);
    }

    public boolean isAllowedBuffer(Buffer buffer) {
        return getAllowedBuffers().contains(buffer);
    }

    public String getDescription() {
        return "Does some sort of ability!";
    }

    public abstract Map<Buffer, Integer> getMaxBufferLevels();

    public abstract String getDisplayName();

    public abstract Collection<String> getAllowedToolTypes();

    public abstract void run(EventAbilityUse event, Map<Buffer, Integer> buffers);
    public void onFinish(EventAbilityUse event){
        EngineTools.fulfillAbility(event);
    }

}
