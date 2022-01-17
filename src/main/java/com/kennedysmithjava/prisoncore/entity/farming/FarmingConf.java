package com.kennedysmithjava.prisoncore.entity.farming;

import com.kennedysmithjava.prisoncore.PrisonCore;
import com.kennedysmithjava.prisoncore.entity.farming.objects.Seed;
import com.kennedysmithjava.prisoncore.util.MItem;
import com.kennedysmithjava.prisoncore.util.MiscUtil;
import com.massivecraft.massivecore.command.editor.annotation.EditorName;
import com.massivecraft.massivecore.ps.PS;
import com.massivecraft.massivecore.store.Entity;
import com.massivecraft.massivecore.util.MUtil;
import lombok.Getter;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.material.Crops;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@EditorName("config")
public class FarmingConf extends Entity<FarmingConf> {

    // -------------------------------------------- //
    // META
    // -------------------------------------------- //

    protected static transient FarmingConf i;

    public static FarmingConf get() { return i; }

    @Override
    public FarmingConf load(FarmingConf that)
    {
        super.load(that);
        return this;
    }

    private String world = "Spawn";
    private long maxAmountOfMilisForASeedToGrow = 150000;

    @Getter
    private MItem wheatItem = new MItem("&eWheat", MUtil.list("&6Trade this with the farmer."), Material.WHEAT, 0, false, MUtil.list(), MUtil.list());

    public List<Seed> seeds = MUtil.list();

    /**
     * Called when wheat is broken
     * @param block
     */
    private void addSeed(Block block) {
        // Sets the block below to soil
        Location blockBelow = block.getLocation().clone().add(0, -1, 0);
        blockBelow.getBlock().setType(Material.FARMLAND);

        Bukkit.getScheduler().runTaskLater(PrisonCore.get(), () -> {
            // Updates the block state to a seed
            block.setType(Material.WHEAT_SEEDS, false);
        }, 1l);

        // Adds the seed to the seeds list
        seeds.add(new Seed(PS.valueOf(block.getLocation()),
                System.currentTimeMillis() + 60000 + (ThreadLocalRandom.current().nextLong(1000, (maxAmountOfMilisForASeedToGrow-60000)))));

        // Updates the config entity
        save();
    }

    /**
     * Called when a seed spawns back
     * @param seed
     */
    public void addWheat(Seed seed) {
        Location loc = seed.getPs().asBukkitLocation();
        World world = loc.getWorld();
        Block block = loc.getBlock();

        // Sets the block below to soil
        Location blockBelow = loc.clone().add(0, -1, 0);
        blockBelow.getBlock().setType(Material.FARMLAND);

        Bukkit.getScheduler().runTaskLater(PrisonCore.get(), () -> {
            // Updates the block state to a Rippend Crop
            block.setType(Material.WHEAT_SEEDS, false);

            BlockState blockState = block.getState();
            Crops crop = (Crops) blockState.getData();
            crop.setState(CropState.RIPE);
            block.getState().setData(crop);
            blockState.update();
            block.getState().update();

        }, 1l);

        // Plays Sound and particles
        world.spawnParticle(Particle.VILLAGER_HAPPY, loc.clone().add(0, 0.5, 0), 1);
        world.spawnParticle(Particle.VILLAGER_HAPPY, loc.clone().add(0, 0.3, 0), 1);
        world.playSound(loc, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 0.2f);
    }

    /**
     * Called when a block is broken
     * @param evt
     */
    public void onWheatBreak(BlockBreakEvent evt) {

        // Must be material wheat
        if(evt.getBlock().getType() != Material.WHEAT_SEEDS)
            return;

        // Objects
        Block block = evt.getBlock();
        Location loc = block.getLocation();

        if(!(block.getState().getData() instanceof Crops)) {
            System.out.println("This crop is not a crop. issue.");
            return;
        }

        // If the crop is not ripe, don't do this
        if(((Crops) block.getState().getData()).getState() != CropState.RIPE)
            return;

        // Must be in spawn world
        if(!loc.getWorld().getName().equals(world))
            return;

        // Clears the drops and removes the event
        evt.getBlock().getDrops().clear();
        evt.setCancelled(true);

        // 20% chance to get 2x wheat
        int wheatToGive = ThreadLocalRandom.current().nextInt(0, 5) == 1 ? 2 : 1;

        // Gives player the wheat
        MiscUtil.givePlayerItem(evt.getPlayer(), wheatItem.build(), wheatToGive);

        // Seeds the ground
        addSeed(block);
    }

    public void save() {
        this.changed();
    }

}
