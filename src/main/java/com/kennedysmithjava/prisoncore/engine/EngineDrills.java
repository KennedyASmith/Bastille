package com.kennedysmithjava.prisoncore.engine;

import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import com.kennedysmithjava.prisoncore.PrisonCore;
import com.kennedysmithjava.prisoncore.util.packetwrapper.EntityMetadata;
import com.kennedysmithjava.prisoncore.util.packetwrapper.WrapperPlayServerEntityEquipment;
import com.kennedysmithjava.prisoncore.util.packetwrapper.WrapperPlayServerEntityLook;
import com.kennedysmithjava.prisoncore.util.packetwrapper.WrapperPlayServerSpawnEntityLiving;
import com.massivecraft.massivecore.Engine;
import com.massivecraft.massivecore.util.MUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class EngineDrills extends Engine {

    private static final EngineDrills i = new EngineDrills();

    public static EngineDrills get() {
        return i;
    }

    public static Random random = new Random();

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onDispenserPlace(BlockPlaceEvent event) {
        if(event.getBlockPlaced().getType() != Material.DISPENSER) return;

        double offset = -1.5;
        Location dropperLoc = event.getBlockPlaced().getLocation().clone().add(0.5, -0.75 + offset, 0.5);
        Location fenceLoc = event.getBlockPlaced().getLocation().clone().add(0.5, -0.25 + offset, 0.5);

        spawnHopper(dropperLoc, event.getPlayer());
        spawnArmorStand(fenceLoc, event.getPlayer(), new ItemStack(Material.LEVER));
    }

    public void spawnHopper(Location location, Player player){


        List<Integer> drillBits = MUtil.list(
                spawnArmorStand(location, player, new ItemStack(Material.HOPPER)),
                spawnArmorStand(location, player, new ItemStack(Material.HOPPER))
        );

        float startAngle = 0;

        for (Integer idInt : drillBits) {
            final AtomicInteger entityId = new AtomicInteger(idInt);
            float finalStartAngle = startAngle;
            new BukkitRunnable() {

                int id = entityId.get();
                float angle = finalStartAngle;

                @Override
                public void run() {
                    WrapperPlayServerEntityLook entityLook = new WrapperPlayServerEntityLook();
                    entityLook.setEntityID(id);

                    angle = angle + 35;

                    // Reduce the angle
                    angle =  angle % 360;

                    // Force it to be the positive remainder, so that 0 <= angle < 360
                    angle = (angle +360)%360;

                    // Force into the minimum absolute value residue class, so that -180 < angle <= 180
                    if(angle >180)
                        angle -= 360;

                    entityLook.setYaw(angle);
                    id = entityLook.getEntityID();
                    entityLook.sendPacket(player);
                }

            }.runTaskTimerAsynchronously(PrisonCore.get(), 10, 8);
            startAngle = startAngle + 90;
        }
    }

    public int spawnArmorStand(Location location, Player player, ItemStack head){
        WrapperPlayServerSpawnEntityLiving spawnPacket = new WrapperPlayServerSpawnEntityLiving();
        int entityId = random.nextInt(0xFFFF) + Integer.MAX_VALUE - 0xFFFF;
        spawnPacket.setEntityID(entityId);
        spawnPacket.setType(EntityType.ARMOR_STAND);
        spawnPacket.setXYZ(location);

        WrappedDataWatcher dataWatcher = spawnPacket.getMetadata();
        EntityMetadata.EntityStatus.INVISIBLE.set(dataWatcher, true);
        spawnPacket.setMetadata(dataWatcher);
        spawnPacket.sendPacket(player);

        WrapperPlayServerEntityEquipment wrapperPlayServerEntityEquipment = new WrapperPlayServerEntityEquipment();
        wrapperPlayServerEntityEquipment.setEntityID(entityId);
        wrapperPlayServerEntityEquipment.setItem(head);
        wrapperPlayServerEntityEquipment.setSlot(EnumWrappers.ItemSlot.HEAD);
        wrapperPlayServerEntityEquipment.sendPacket(player);
        return entityId;
    }

}
