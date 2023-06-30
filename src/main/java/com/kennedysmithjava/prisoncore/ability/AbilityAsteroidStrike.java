package com.kennedysmithjava.prisoncore.ability;

import com.kennedysmithjava.prisoncore.entity.tools.BufferConf;
import com.kennedysmithjava.prisoncore.PrisonCore;
import com.kennedysmithjava.prisoncore.entity.mines.Distribution;
import com.kennedysmithjava.prisoncore.entity.mines.objects.PrisonBlock;
import com.kennedysmithjava.prisoncore.event.EventAbilityUse;
import com.kennedysmithjava.prisoncore.tools.Buffer;
import com.kennedysmithjava.prisoncore.regions.LazyRegion;
import com.kennedysmithjava.prisoncore.regions.VBlockFace;
import com.massivecraft.massivecore.util.MUtil;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.npc.NPCRegistry;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Giant;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import xyz.xenondevs.particle.ParticleEffect;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

/*Giant giant = (Giant) w.spawnEntity(start, EntityType.GIANT);
        giant.setCanPickupItems(false);
        giant.setMaxHealth(Double.MAX_VALUE);
        giant.setHealth(Double.MAX_VALUE);
        giant.setNoDamageTicks(Integer.MAX_VALUE);
        giant.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 1, false, false));
        giant.getEquipment().setItemInHand(new ItemStack(Material.NETHERRACK, 1));*/

public class AbilityAsteroidStrike extends Ability<AbilityAsteroidStrike> {

    public static AbilityAsteroidStrike i = new AbilityAsteroidStrike();

    public AbilityAsteroidStrike() {
        super(AbilityType.ASTEROID);
    }

    public static AbilityAsteroidStrike get() {
        return i;
    }

    Set<VBlockFace> blockFaces = MUtil.set(VBlockFace.DOWN, VBlockFace.EAST, VBlockFace.WEST, VBlockFace.SOUTH, VBlockFace.NORTH);

    @Override
    public void run(EventAbilityUse event, Map<Buffer, Integer> buffers) {

        Block block = event.getBlock();
        LazyRegion region = event.getMineRegion();
        Distribution distribution = event.getDistribution();
        Player player = event.getPlayer();
        Random random = new Random();
        String worldName = block.getWorld().getName();

        int xOffset = 2;
        int yOffset = -8;
        int zOffset = -4;
        Location clickedLocation = block.getLocation(); //Where the ability was used
        //clickedLocation.setY(region.getMaxY()); //The block has to be on the surface of the mine
        Location goal = clickedLocation.clone().add(xOffset, yOffset, zOffset); //The clicked block, with the offset added
        Location start = clickedLocation.clone().add(random.nextInt(60) - 10, 150, random.nextInt(60) - 10); //Random start location of the meteor

        NPCRegistry registry = PrisonCore.getNonPersistNPCRegistry(); //Used for storing non-persistent NPCs
        NPC giant = registry.createNPC(EntityType.GIANT, " ");
        giant.data().set(NPC.NAMEPLATE_VISIBLE_METADATA, false);
        giant.setProtected(true);
        giant.spawn(start);
        Giant giantEntity = (Giant) giant.getEntity();
        giantEntity.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 9999, 0));
        giantEntity.getEquipment().setItemInHand(new ItemStack(Material.NETHERRACK, 1));

        AtomicBoolean completed = new AtomicBoolean(false);

        ArrayList<Block> blownBlocks = new ArrayList<>();
        ArrayList<Block> affectedBlocks = new ArrayList<>();
        new BukkitRunnable() {
            @Override
            public void run() {
                if (completed.get()) {
                    giantEntity.remove();
                    Location finalLocation = giantEntity.getLocation().add(-xOffset, -yOffset, -zOffset);
                    ParticleEffect.EXPLOSION_LARGE.display(finalLocation);
                    int radius = 3;
                    int bx = finalLocation.getBlockX();
                    int by = finalLocation.getBlockY();
                    int bz = finalLocation.getBlockZ();
                    if (player != null) {
                        for (int x = bx - radius; x <= bx + radius; x++) {
                            for (int y = by - radius; y <= by + radius; y++) {
                                for (int z = bz - radius; z <= bz + radius; z++) {
                                    double dist = ((bx - x) * (bx - x) + ((bz - z) * (bz - z)) + ((by - y) * (by - y)));
                                    if (dist < radius * radius) {
                                        if (region.contains(x, y, z, worldName)) {
                                            Location location = new Location(start.getWorld(), x, y, z);
                                            Block block = location.getBlock();
                                            PrisonBlock pb = distribution.generatePrisonBlock(block.getType(), block.getBlockData());
                                            event.addReward(pb);
                                            block.setType(Material.AIR);
                                            blownBlocks.add(block);
                                            event.addAffectedBlock(location);
                                        }
                                    }
                                }
                            }
                        }

                        blownBlocks.forEach(blownBlock -> blockFaces.forEach(blockFace -> {
                            Block nearby = blockFace.getRelative(blownBlock);
                            if (nearby.getType() != Material.AIR && region.contains(nearby)) {
                                player.sendBlockChange(nearby.getLocation(), Material.NETHERRACK, (byte) 0);
                                affectedBlocks.add(nearby);
                                Block above = VBlockFace.UP.getRelative(nearby);
                                if (above.getType() == Material.AIR) { //If there is an empty block above
                                    if (random.nextDouble() < 0.5) { //Randomly decide if there should be fire
                                        player.sendBlockChange(above.getLocation(), Material.FIRE, (byte) 0); //Set fire
                                        affectedBlocks.add(above); //Add it to blocks effected
                                    }
                                }
                            }
                        }));

                        if (affectedBlocks.size() > 0) {
                            affectedBlocks.forEach(b -> new BukkitRunnable() {
                                @Override
                                public void run() {
                                    // noinspection ConstantConditions
                                    if (player != null) {
                                        player.sendBlockChange(b.getLocation(), b.getType(), b.getData());
                                    }
                                }

                                ;
                            }.runTaskLaterAsynchronously(PrisonCore.get(), (random.nextInt(3) + 2) * 20));
                        }
                    }
                    onFinish(event);
                    this.cancel();
                    return;
                }
                giantEntity.setVelocity(new Vector(0, 0, 0));
            }
        }.runTaskTimer(PrisonCore.get(), 1L, 2L);

        new BukkitRunnable() {
            @Override
            public void run() {
                if (completed.get()) {
                    this.cancel();
                    return;
                }
                Vector distance = goal.clone().subtract(giantEntity.getLocation()).toVector();
                if (distance.lengthSquared() < 1.5 * 3) {
                    completed.set(true);
                    return;
                } else {
                    giantEntity.teleport(giantEntity.getLocation().add(distance.multiply(0.25)));
                }
            }

            ;
        }.runTaskTimer(PrisonCore.get(), 1L, 3L);


        //Ensure that the magma cube is removed if something goes wrong.
        new BukkitRunnable() {
            @Override
            public void run() {
                completed.set(true);
            }
        }.runTaskLater(PrisonCore.get(), 5 * 20L);

    }

    @Override
    public String getDisplayName() {
        return "&cAsteroid";
    }

    @Override
    public Collection<String> getAllowedToolTypes() {
        return null;
    }

    @Override
    public Material getItemMaterial() {
        return Material.FIRE_CHARGE;
    }

    @Override
    public Map<Buffer, Integer> getMaxBufferLevels() {
        return MUtil.map(
                BufferConf.EFFICIENCY, 10,
                BufferConf.SPEED, 10,
                BufferConf.POWER, 10);
    }
}
