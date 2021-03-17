package com.kennedysmithjava.prisonmines.entity;

import com.boydti.fawe.bukkit.v0.FaweAdapter_All;
import com.boydti.fawe.util.EditSessionBuilder;
import com.kennedysmithjava.prisonmines.MineRegenCountdown;
import com.kennedysmithjava.prisonmines.MinesParticipator;
import com.kennedysmithjava.prisonmines.PrisonMines;
import com.kennedysmithjava.prisonmines.util.LocSerializer;
import com.kennedysmithjava.prisonmines.util.MiscUtil;
import com.massivecraft.massivecore.Named;
import com.massivecraft.massivecore.collections.MassiveMapDef;
import com.massivecraft.massivecore.store.Entity;
import com.massivecraft.massivecore.util.MUtil;
import com.sk89q.worldedit.BlockVector;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.blocks.BaseBlock;
import com.sk89q.worldedit.bukkit.BukkitUtil;
import com.sk89q.worldedit.function.pattern.RandomPattern;
import com.sk89q.worldedit.regions.CuboidRegion;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;

import java.util.logging.Level;

public class Mine extends Entity<Mine> implements Named {
    // -------------------------------------------- //
    // CONSTANTS
    // -------------------------------------------- //

    // The actual mine id looks something like "54947df8-0e9e-4471-a2f9-9af509fb5889" and that is not too easy to remember for humans.
    // Null should never happen. The name must not be null.
    private String name = null;
    private long regenTimer = 120;
    private boolean hasTimer = true;
    private String world;
    private String min;
    private String max;
    private String spawnPoint;
    private MassiveMapDef<String, Double> blockDistribution;

    public static Mine get(Object oid) {
        return MineColl.get().get(oid);
    }

    public static String clean(String message) {
        String target = message;
        if (target == null) return null;

        target = target.trim();
        if (target.isEmpty()) target = null;

        return target;
    }

    public void update() {
        this.changed();
    }

    @Override
    public Mine load(Mine that) {
        MineColl.get().countdowns.put(this, new MineRegenCountdown(this, 0));
        Bukkit.getLogger().log(Level.SEVERE, "MINE " + this.getName() + " LOADED");
        this.setName(that.name);
        this.setRegenTimer(that.regenTimer);
        this.setBlockDistribution(that.blockDistribution);
        this.min = that.min;
        this.max = that.max;
        this.setHasTimer(that.hasTimer);
        this.spawnPoint = that.spawnPoint;

        return this;
    }

    public String getName() {
        String ret = this.name;
        return ret;
    }

    public void setName(String name) {
        // Clean input
        String target = name;

        // Detect Nochange
        if (MUtil.equals(this.name, target)) return;

        // Apply
        this.name = target;

        // Mark as changed
        this.changed();
    }

    public long getRegenTimer() {
        return regenTimer;
    }

    public void setRegenTimer(long resetTimer) {
        this.regenTimer = resetTimer;
        this.changed();
    }

    public boolean isHasTimer() {
        return hasTimer;
    }

    public void setHasTimer(boolean hasTimer) {
        this.hasTimer = hasTimer;
        this.changed();
    }

    public String getComparisonName() {
        return MiscUtil.getComparisonString(this.getName());
    }

    public String getName(String prefix) {
        return prefix + this.getName();
    }

    public String getMin() {
        return min;
    }

    public void setMin(Location location) {
        this.min = LocSerializer.getLiteStringFromLocation(location);
        this.changed();
    }

    public String getMax() {
        return max;
    }

    public void setMax(Location location) {
        this.max = LocSerializer.getLiteStringFromLocation(location);
        this.changed();
    }

    public MassiveMapDef<String, Double> getBlockDistribution() {
        return blockDistribution;
    }

    public void setBlockDistribution(MassiveMapDef<String, Double> blockDistribution) {
        this.blockDistribution = blockDistribution;
        this.changed();
    }

    public MineRegenCountdown getRegenCountdown() {
        return MineColl.get().countdowns.get(this);
    }

    public Location getMaxLoc() {
        return LocSerializer.getLiteLocationFromString(getMax());
    }

    public Location getMinLoc() {
        return LocSerializer.getLiteLocationFromString(getMin());
    }

    public Location getSpawnPoint() {
        return LocSerializer.getLocationFromString(this.spawnPoint);
    }

    public void setSpawnPoint(Location location) {
        this.spawnPoint = LocSerializer.getStringFromLocation(location);
        this.changed();
    }

    public World getWorld() {
        return getMaxLoc().getWorld();
    }

    public void regen() {
        RandomPattern pat = new RandomPattern();
        try {
            FaweAdapter_All adapter = new FaweAdapter_All();
            this.getBlockDistribution().forEach((material, aDouble) -> {
                String[] materialArgs = material.split(":");

                BaseBlock baseBlock = new BaseBlock(adapter.getBlockId(Material.valueOf(materialArgs[0])), Integer.parseInt(materialArgs[1]));

                pat.add(baseBlock, (aDouble / 100));
            });
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }

        World world = getWorld();

        CuboidRegion cuboidRegion = new CuboidRegion(BukkitUtil.getLocalWorld(world),
                new BlockVector(getMinLoc().getBlockX(), getMinLoc().getBlockY(), getMinLoc().getBlockZ()),
                new BlockVector(getMaxLoc().getBlockX(), getMaxLoc().getBlockY(), getMaxLoc().getBlockZ()));
        EditSession editSession = (new EditSessionBuilder(BukkitUtil.getLocalWorld(world))).fastmode(true).build();
        Bukkit.getScheduler().runTaskAsynchronously(PrisonMines.get(), () -> {
            editSession.setBlocks(cuboidRegion, pat);
            editSession.flushQueue();
        });
    }
}
