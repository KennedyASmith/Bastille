package com.kennedysmithjava.prisoncore.tools.ability;

import com.kennedysmithjava.prisoncore.PrisonCore;
import com.kennedysmithjava.prisoncore.entity.mines.Distribution;
import com.kennedysmithjava.prisoncore.entity.tools.BufferConf;
import com.kennedysmithjava.prisoncore.event.EventAbilityUse;
import com.kennedysmithjava.prisoncore.tools.Buffer;
import com.kennedysmithjava.prisoncore.util.regions.BlockArea;
import com.kennedysmithjava.prisoncore.util.regions.BlockPos;
import com.kennedysmithjava.prisoncore.util.regions.LazyRegion;
import com.kennedysmithjava.prisoncore.util.regions.Scaling;
import com.kennedysmithjava.prisoncore.util.vfx.CuboidSequence;
import com.kennedysmithjava.prisoncore.util.vfx.ParticleFn;
import com.kennedysmithjava.prisoncore.util.vfx.ParticleSequence;
import com.kennedysmithjava.prisoncore.util.vfx.SphereSequence;
import com.massivecraft.massivecore.util.MUtil;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.npc.NPCRegistry;
import net.citizensnpcs.trait.ArmorStandTrait;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import xyz.xenondevs.particle.ParticleEffect;

import java.awt.*;
import java.util.*;
import java.util.List;

public class AbilityBlackhole extends Ability<AbilityBlackhole> {

    public static AbilityBlackhole INSTANCE = new AbilityBlackhole();

    public static AbilityBlackhole get() {
        return INSTANCE;
    }

    private final static ParticleFn PARTICLE_FN = (location, playerList) -> {
        ParticleEffect.SPELL_MOB.display(location, Color.BLACK, playerList);
    };

    private final static ParticleSequence EFFECT = new SphereSequence(1, 8, 8);

    static {
        EFFECT.setParticleFn(PARTICLE_FN);
    }

    public AbilityBlackhole() {
        super(AbilityType.BLACKHOLE);
    }

    @Override
    public String getDisplayName() {
        return "&5&l☀ Black Hole";
    }

    @Override
    public Collection<String> getAllowedToolTypes() {
        return null;
    }

    @Override
    public void run(EventAbilityUse event, Map<Buffer, Integer> buffers) {
        int power = buffers.getOrDefault(BufferConf.POWER, 1);
        int speed = buffers.getOrDefault(BufferConf.SPEED, 1);
        int efficiency = buffers.getOrDefault(BufferConf.EFFICIENCY, 1);

        Player player = event.getPlayer();
        player.sendMessage("Power: " + power + " Speed: " + speed + " Efficiency: " + efficiency);

        LazyRegion region = event.getMineRegion();

        Block clickedBlock = event.getBlock();
        if (clickedBlock == null)
            clickedBlock = event.getPlayer().getTargetBlock((Set<Material>) null, 7);

        World world = clickedBlock.getWorld();

        BlockPos blockPos = new BlockPos(clickedBlock);

        BlockArea suckArea = BlockArea.from(blockPos.sub(power, power, power), blockPos.add(power, power, power));
        CuboidSequence cuboidSequence = new CuboidSequence(suckArea);

        Random random = new Random();

        Location blackHoleLoc = blockPos.centerLocation(world).add(0, 2, 0);
        EFFECT.draw(blackHoleLoc);
        ParticleEffect.PORTAL.display(blackHoleLoc, Color.BLACK, player);
        List<Entity> spawnedEntities = new ArrayList<>();
        Distribution distribution = event.getDistribution();
        NPCRegistry registry = PrisonCore.getNonPersistNPCRegistry();

        int task = Bukkit.getScheduler().scheduleSyncRepeatingTask(PrisonCore.get(), () -> {
            EFFECT.draw(blackHoleLoc);
            cuboidSequence.draw(new Location(world, 0, 0, 0));
            ParticleEffect.PORTAL.display(blackHoleLoc, player);
            for(Entity spawnedItem : spawnedEntities) {
                Location entityLoc = spawnedItem.getLocation();
                Vector distance = blackHoleLoc.clone().subtract(entityLoc).toVector();
                if(distance.lengthSquared() < 1.5*1.5*3){
                    NPC npc = registry.getNPC(spawnedItem);
                    npc.destroy();
                } else
                    spawnedItem.setVelocity(distance.multiply(0.25));
            }

            suckArea.getRandom(random, Scaling.linear(efficiency, 10))
                    .map(suckPos -> suckPos.getBlock(world)).filter(region::contains)
                    .forEach(suckBlock -> {
                        Material m = suckBlock.getType();
                        BlockData data = suckBlock.getBlockData();
                        if(!m.isSolid() || !m.isBlock()) return;
                        suckBlock.setType(Material.AIR, false);
                        Location itemLoc = suckBlock.getLocation().add(0.5, 0.5, 0.5);
                        NPC npc = registry.createNPC(EntityType.ARMOR_STAND, "");
                        npc.setProtected(true);
                        npc.data().set(NPC.NAMEPLATE_VISIBLE_METADATA, false);
                        ArmorStandTrait armorStandTrait = npc.getTrait(ArmorStandTrait.class);
                        armorStandTrait.setVisible(false);
                        armorStandTrait.setSmall(true);
                        npc.spawn(itemLoc);
                        ArmorStand armorStand = (ArmorStand) npc.getEntity();
                        armorStand.setHelmet(new ItemStack(m, 1));
                        armorStand.setVelocity(blackHoleLoc.clone().subtract(itemLoc).toVector().multiply(0.25));
                        spawnedEntities.add(armorStand);
                        event.addReward(distribution.generatePrisonBlock(m, data));
                    });
        }, 1, 5);

        Bukkit.getScheduler().scheduleSyncDelayedTask(PrisonCore.get(), () -> {
            Bukkit.getScheduler().cancelTask(task);
            spawnedEntities.forEach(Entity::remove);
            this.onFinish(event);
        }, 20L * 5L); //Duration buffer
    }

    @Override
    public Material getItemMaterial() {
        return Material.ENDER_PEARL;
    }

    @Override
    public Map<Buffer, Integer> getMaxBufferLevels() {
        return MUtil.map(
                BufferConf.EFFICIENCY, 10,
                BufferConf.SPEED, 10,
                BufferConf.POWER, 10);
    }
}
