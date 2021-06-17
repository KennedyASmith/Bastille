package com.kennedysmithjava.prisonmines.entity;

import com.boydti.fawe.bukkit.v0.FaweAdapter_All;
import com.boydti.fawe.util.EditSessionBuilder;
import com.kennedysmithjava.prisonmines.MineRegenCountdown;
import com.kennedysmithjava.prisonmines.PrisonMines;
import com.kennedysmithjava.prisonmines.upgrades.Upgrade;
import com.kennedysmithjava.prisonmines.util.LocSerializer;
import com.kennedysmithjava.prisonmines.util.MiscUtil;
import com.massivecraft.massivecore.Named;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

public class Mine extends Entity<Mine> implements Named {
    // -------------------------------------------- //
    // CONSTANTS
    // -------------------------------------------- //

    // MISC
    private String name = null;
    private long regenTimer = 120;
    private boolean alwaysActive = false;

    // LEVEL & UPGRADES
    private int level = 1;
    private List<String> upgrades = new ArrayList<>();

    // LOCATION INFORMATION
    private String world;
    private Location spawnPoint;
    private Location origin;
    private Location mineMin;
    private Location mineMax;
    private Location architectLocation;
    private Location researcherLocation;

    // NPC INFORMATION
    private String architectUUID = null;

    // PHYSICAL INFORMATION
    private int wallID;
    private int pathID;
    private int pathLVL;
    private int width;
    private int height;
    private Map<String, Double> blockDistribution;

    // -------------------------------------------- //
    //  CONSTRUCTION
    // -------------------------------------------- //

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
        this.setName(that.name);
        this.setRegenTimer(that.regenTimer);
        this.setOrigin(that.origin);
        this.setMin(that.mineMin);
        this.setMax(that.mineMax);
        this.setSpawnPoint(that.spawnPoint);
        this.setAlwaysActive(that.alwaysActive);
        this.setBlockDistribution(that.blockDistribution);
        this.setArchitectUUID(that.architectUUID);
        this.setArchitectLocation(that.architectLocation);
        this.setUpgrades(that.upgrades);
        this.setLevel(that.level);
        this.setPathID(that.pathID);
        this.setWallID(that.wallID);
        this.setPathLVL(that.pathLVL);

        MineColl.get().countdowns.put(this, new MineRegenCountdown(this, 0));
        return this;
    }

    // -------------------------------------------- //
    //  MISC INFORMATION
    // -------------------------------------------- //

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

    public String getComparisonName() {
        return MiscUtil.getComparisonString(this.getName());
    }

    public String getName(String prefix) {
        return prefix + this.getName();
    }

    public boolean isAlwaysActive() {
        return alwaysActive;
    }

    public void setAlwaysActive(boolean alwaysActive) {
        this.alwaysActive = alwaysActive;
        this.changed();
    }

    // -------------------------------------------- //
    //  LEVEL & UPGRADES
    // -------------------------------------------- //

    public void addUpgrade(String upgradeID){
        upgrades.add(upgradeID);
        this.changed();
    }

    public void addUpgrade(Upgrade upgrade){
        addUpgrade(upgrade.getName());
    }

    public List<String> getUpgrades() {
        return upgrades;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }


    public Location getMineMin() {
        return mineMin;
    }

    public void setMin(Location location) {
        this.mineMin = location;
        this.changed();
    }

    public Location getMineMax() {
        return mineMax;
    }

    public void setMax(Location location) {
        this.mineMax = location;
        this.changed();
    }

    public Map<String, Double> getBlockDistribution() {
        return blockDistribution;
    }

    public void setBlockDistribution(Map<String, Double> blockDistribution) {
        this.blockDistribution = blockDistribution;
        this.changed();
    }

    public MineRegenCountdown getRegenCountdown() {
        return MineColl.get().countdowns.get(this);
    }

    public Location getSpawnPointLoc() {
        return spawnPoint;
    }

    public void setSpawnPoint(Location location) {
        this.spawnPoint = location;
        this.changed();
    }

    public World getWorld() {
        return getMineMax().getWorld();
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
                new BlockVector(getMineMin().getBlockX(), getMineMin().getBlockY(), getMineMin().getBlockZ()),
                new BlockVector(getMineMax().getBlockX(), getMineMax().getBlockY(), getMineMax().getBlockZ()));
        EditSession editSession = (new EditSessionBuilder(BukkitUtil.getLocalWorld(world))).fastmode(true).build();
        Bukkit.getScheduler().runTaskAsynchronously(PrisonMines.get(), () -> {
            editSession.setBlocks(cuboidRegion, pat);
            editSession.flushQueue();
        });
        this.changed();
    }

    public void setArchitectUUID(String architectUUID) {
        this.architectUUID = architectUUID;
        this.changed();
    }

    public String getArchitectUUID() {
        return architectUUID;
    }


    public void setArchitectLocation(Location location) {
        this.architectLocation = location;
        this.changed();
    }

    public Location getArchitectLocation() {
        return architectLocation;
    }

    public Location getResearcherLocation() {
        return researcherLocation;
    }

    public void setOrigin(Location origin) {
        this.origin = origin;
        this.changed();
    }

    public void setUpgrades(List<String> upgrades) {
        this.upgrades = upgrades;
    }

    public Location getOrigin() {
        return origin;
    }

    public void setWall(){

    }

    public void setPath(){

    }

    public int getPathID() {
        return pathID;
    }

    public int getPathLVL() {
        return pathLVL;
    }

    public int getWallID() {
        return wallID;
    }

    public void setPathID(int pathID) {
        this.pathID = pathID;
        this.changed();
    }

    public void setPathLVL(int pathLVL) {
        this.pathLVL = pathLVL;
        this.changed();
    }

    public void setWallID(int wallID) {
        this.wallID = wallID;
        this.changed();
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }


}
