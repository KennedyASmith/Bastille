package com.kennedysmithjava.prisoncore.util;

import de.tr7zw.nbtapi.NBTEntity;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Entity;

import java.util.*;

public class EntityTracker {

    private static final Map<World, Set<UUID>> entities = new HashMap<>();
    private static final String NBT_TRACKED_NAME = "trackedEntity";

    public static void init() {
        for(World world : Bukkit.getWorlds()) {
            entities.put(world, new HashSet<>());
        }
    }

    public static void trackEntity(Entity entity) {
        entities.get(entity.getWorld()).add(entity.getUniqueId());
        NBTEntity nbtEntity = new NBTEntity(entity);
        nbtEntity.setBoolean(NBT_TRACKED_NAME, true);
    }

    public static boolean isTracked(Entity entity) {
        return entities.get(entity.getWorld()).contains(entity.getUniqueId())
                || new NBTEntity(entity).getBoolean(NBT_TRACKED_NAME);
    }

    public static void cleanup() {
        for (World world : Bukkit.getWorlds()) {
            Set<UUID> toRemove = entities.get(world);
            world.getEntities().stream()
                    .filter(entity -> toRemove.contains(entity.getUniqueId()))
                    .forEach(Entity::remove);
        }
    }


}
