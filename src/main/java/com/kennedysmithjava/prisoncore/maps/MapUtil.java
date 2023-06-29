package com.kennedysmithjava.prisoncore.maps;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.UUID;

public class MapUtil {

    public static boolean insideMapBounds(int x, int z){
        return (x > 4 && x < 125) && (z > 14 && z < 107);
    }

    public static byte getDirectionFrom(Location origin, Location target) {
        double deltaX = target.getX() - origin.getX();
        double deltaZ = target.getZ() - origin.getZ();

        double angle = Math.atan2(-deltaX, deltaZ);
        double radians = (angle + Math.PI * 2) % (Math.PI * 2);
        double degrees = Math.toDegrees(radians);
        double normalizedDegrees = (degrees + 360) % 360;

        int index = (int) Math.round(normalizedDegrees / 22.5) % 16;
        MapDirection[] directions = MapDirection.values();

        return directions[index].d;
    }

    public static void refreshMapName(Player player, String name){
        UUID uuid = player.getUniqueId();
        if(!PrisonMapRenderer.mapRenderers.containsKey(uuid)) return;
        if(!hasMap(player)) return;
        PrisonMapRenderer renderer = PrisonMapRenderer.mapRenderers.get(uuid);
        renderer.setAreaName(name);
    }

    public static void removeMaps(Player player){
        Inventory inv = player.getInventory();
        if(!inv.contains(Material.FILLED_MAP)) return;
        inv.remove(Material.FILLED_MAP);
        removeMaps(player);
        removeRenderer(player.getUniqueId());
    }

    public static void removeRenderer(UUID uuid){
        PrisonMapRenderer.mapRenderers.remove(uuid);
    }

    public static boolean hasMap(Player player){
        Inventory inv = player.getInventory();
        return inv.contains(Material.FILLED_MAP);
    }
}

enum MapDirection {
    NORTH((byte) 8),
    NORTHEASTNORTH((byte) 9),
    NORTHEAST((byte) 10),
    NORTHEASTEAST((byte) 11),
    EAST((byte) 12),
    SOUTHEASTEAST((byte) 13),
    SOUTHEAST((byte) 14),
    SOUTHEASTSOUTH((byte) 15),
    SOUTH((byte) 0),
    SOUTHWESTSOUTH((byte) 1),
    SOUTHWEST((byte) 2),
    SOUTHWESTWEST((byte) 3),
    WEST((byte) 4),
    NORTHWESTWEST((byte) 5),
    NORTHWEST((byte) 6),
    NORTHWESTNORTH((byte) 7);

    final byte d;
    MapDirection(byte d){
        this.d = d;
    }
}
