package com.kennedysmithjava.prisonmines.entity;

import com.boydti.fawe.bukkit.v0.FaweAdapter_All;
import com.boydti.fawe.util.EditSessionBuilder;
import com.kennedysmithjava.prisonmines.MineRegenCountdown;
import com.kennedysmithjava.prisonmines.MinesWorldManager;
import com.kennedysmithjava.prisonmines.PrisonMines;
import com.kennedysmithjava.prisonmines.util.BlockMaterial;
import com.kennedysmithjava.prisonmines.util.FAWETracker;
import com.kennedysmithjava.prisonmines.util.MiscUtil;
import com.kennedysmithjava.prisonnpcs.PrisonNPCs;
import com.massivecraft.massivecore.Named;
import com.massivecraft.massivecore.ps.PS;
import com.massivecraft.massivecore.store.Entity;
import com.massivecraft.massivecore.util.MUtil;
import com.sk89q.worldedit.BlockVector;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.blocks.BaseBlock;
import com.sk89q.worldedit.bukkit.BukkitUtil;
import com.sk89q.worldedit.function.pattern.RandomPattern;
import com.sk89q.worldedit.regions.CuboidRegion;
import jdk.nashorn.internal.ir.Block;
import net.citizensnpcs.api.event.DespawnReason;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Mine extends Entity<Mine> implements Named {

    // -------------------------------------------- //
    //  CONSTRUCTION & VARIABLES
    // -------------------------------------------- //

    public static Mine get(Object oid) {
        return MineColl.get().get(oid);
    }

    @Override
    public Mine load(Mine that) {

        this.setRegenTimer(that.regenTimer);
        this.setAlwaysActive(that.alwaysActive);
        this.setOrigin(that.origin);
        this.setMinVar(that.mineMin);
        this.setMaxVar(that.mineMax);
        this.setSpawnPoint(that.spawnPoint);
        this.setArchitectLocation(that.architectLocation);
        this.setUpgrades(that.upgrades);
        this.setMineCenter(that.mineCenter);
        this.setArchitectUUID(that.architectUUID);
        this.setResearcherUUID(that.researcherUUID);
        this.setBlockDistribution(that.blockDistribution);
        this.setLevel(that.level);
        this.setPathIDVar(that.pathID);
        this.setWallIDVar(that.wallID);
        this.setHeightVar(that.height);
        this.setWidthVar(that.width);
        this.setUnlockedDistributions(that.unlockedDistributions);
        this.setCurrentDistributionIDVar(that.currentDistributionID);

        if(alwaysActive) MineColl.get().addCountdown(this);

        return this;
    }

    // MISC
    private String name;
    private long regenTimer;
    private boolean alwaysActive = false;

    // LEVEL & UPGRADES
    private int level = 1;
    private transient List<String> upgrades = new ArrayList<>();
    private List<Integer> unlockedDistributions = new ArrayList<>();

    // LOCATION INFORMATION
    private PS spawnPoint;
    private PS origin;
    private PS mineMin;
    private PS mineMax;
    private PS mineCenter;
    private PS architectLocation;
    private PS researcherLocation;

    // NPC INFORMATION
    private String architectUUID;
    private String researcherUUID;

    // PHYSICAL INFORMATION
    private int wallID;
    private int pathID;
    private int width;
    private int height;
    private Map<String, Double> blockDistribution;
    private int currentDistributionID;

    // -------------------------------------------- //
    //  MINE ARCHITECTURE
    // -------------------------------------------- //

    /**
     * Regenerates the blocks in this mine.
     * @param resetTimer Set true to manually reset the mine's regeneration timer, false if otherwise.
     */
    public void regen(boolean resetTimer) {
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
                new BlockVector(mineMin.getLocationX(), mineMin.getLocationY(), mineMin.getLocationZ()),
                new BlockVector(mineMax.getLocationX(), mineMax.getLocationY(), mineMax.getLocationZ()));

        EditSession editSession = (new EditSessionBuilder(BukkitUtil.getLocalWorld(world))).fastmode(true).build();
        Bukkit.getScheduler().runTaskAsynchronously(PrisonMines.get(), () -> {
            editSession.setBlocks(cuboidRegion, pat);
            editSession.flushQueue();
        });

        this.changed();
    }

    /**
     * Sets the height of this mine, updating the values stored in the regeneration timer.
     * Regenerates the unbreakable border around the mine.
     * @param h The height of the mine in blocks.
     */
    public void setHeight(int h) {
        generateBorder(getWidth(), getHeight(), new BlockMaterial(Material.AIR, (byte) 0));
        generateBorder(getWidth(), h, MinesConf.get().minesBorderMaterial);
        this.setHeightVar(h);
        this.regen(false);
    }

    /**
     * Sets the width of this mine, updating the values stored in the regeneration timer.
     * Regenerates the unbreakable border around the mine.
     * @param w The width of the mine in blocks.
     */
    public FAWETracker setWidth(int w) {
        generateBorder(getWidth(), getHeight(), new BlockMaterial(Material.AIR, (byte) 0));
        generateBorder(w, getHeight(), MinesConf.get().minesBorderMaterial);
        this.setWidthVar(w);
        this.regen(false);
        return MiscUtil.pasteSchematic(LayoutConf.get().getPath(getPathID()).getSchematic(getWidth()), new Vector(origin.getLocationX(), origin.getLocationY(), origin.getLocationZ()));
    }

    /**
     * Generates the unbreakable border around the mine.
     * @param w The width of the mine in blocks.
     * @param h The height of the mine in blocks.
     */
    public void generateBorder(int w, int h, BlockMaterial borderMaterial){

        //WALL LOCATIONS
        Location westTop = getMineCenter().clone().add(-w, 0, -w);
        Location westBottom = westTop.clone().add(0, -h, w * 2);
        Location northTop = westBottom.clone().add(0, h, 0);
        Location northBottom = northTop.clone().add(w * 2, -h, 0);
        Location eastTop = northBottom.clone().add(0, h, 0);
        Location eastBottom = eastTop.clone().add(0, -h, -w * 2);
        Location southTop = eastBottom.clone().add(0, h, 0);
        Location southBottom = southTop.clone().add(-w * 2, -h, 0);
        //COLUMN LOCATIONS
        Location northWest = westTop.clone().add(1, 0, 1);
        Location northEast = northTop.clone().add(1, 0, -1);
        Location southEast = eastTop.clone().add(-1, 0, -1);
        Location southWest = southTop.clone().add(-1, 0, 1);

        //GENERATE THE WALLS
        MiscUtil.blockFill(westBottom, westTop, borderMaterial);
        MiscUtil.blockFill(northBottom, northTop, borderMaterial);
        MiscUtil.blockFill(eastBottom, eastTop, borderMaterial);
        MiscUtil.blockFill(southBottom, southTop, borderMaterial);
        MiscUtil.blockFill(northBottom, southBottom, borderMaterial);

        //GENERATE THE CORNER COLUMNS
        MiscUtil.blockFill(northWest, northWest.clone().add(0,-h,0), borderMaterial);
        MiscUtil.blockFill(northEast, northEast.clone().add(0,-h,0), borderMaterial);
        MiscUtil.blockFill(southEast, southEast.clone().add(0,-h,0), borderMaterial);
        MiscUtil.blockFill(southWest, southWest.clone().add(0,-h,0), borderMaterial);
    }

    public void setMineMax(Location mineMax) {
        this.mineMax = PS.valueOf(mineMax);
    }

    public void setMineMin(Location mineMin) {
        this.mineMin = PS.valueOf(mineMin);
    }

    // -------------------------------------------- //
    //  LEVEL & UPGRADES
    // -------------------------------------------- //

    public void addUpgrade(String upgradeID) {
        upgrades.add(upgradeID);
        this.changed();
    }

    public List<String> getUpgrades() {
        return upgrades;
    }

    public void setUpgrades(List<String> upgrades) {
        this.upgrades = upgrades;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public Map<String, Double> getBlockDistribution() {
        return blockDistribution;
    }

    public void setBlockDistribution(int id) {
        this.blockDistribution = DistributionConf.get().distribution.get(id).getRates();
        this.setCurrentDistributionID(id);
        this.changed();
    }

    public void setBlockDistribution(Map<String, Double> blockDistribution) {
        this.blockDistribution = blockDistribution;
        this.changed();
    }

    public void setCurrentDistributionID(int id) {
        this.currentDistributionID = id;
        setBlockDistribution(DistributionConf.get().distribution.get(id).getRates());
        this.changed();
    }
    public void setCurrentDistributionIDVar(int id) {
        this.currentDistributionID = id;
        this.changed();
    }

    public int getCurrentDistributionID() {
        return currentDistributionID;
    }

    // -------------------------------------------- //
    //  LOAD/SAVE METHODS
    //  These methods only exist for saving data.
    // -------------------------------------------- //

    /** Sets the saved value for this mine's height.
     *  Use {@link #setHeight(int)} for physically adjusting the mine's height.
     * @param height The height of the mine in blocks.
     * */
    public void setHeightVar(int height) {
        this.height = height;
        this.changed();
    }

    /** Sets the saved value for this mine's width.
     *  Use {@link #setWidth(int)} for physically adjusting the mine's width.
     * @param width The width of the mine in blocks.
     * */
    public void setWidthVar(int width) {
        this.width = width;
        this.changed();
    }

    /** Sets the saved value for this mine's path ID.
     *  Use {@link #setPathID(int)} or {@link #setWidth(int)} for physically adjusting the mine's paths.
     * @param pathID a key for the path's schematic in the {@link LayoutConf} file.
     * */
    public void setPathIDVar(int pathID) {
        this.pathID = pathID;
        this.changed();
    }

    /** Sets the saved value for this mine's wall ID.
     *  Use {@link #setWallID(int)} for physically adjusting the mine's paths.
     * @param wallID a key for the wall's schematic in the {@link LayoutConf} file.
     * */
    public void setWallIDVar(int wallID) {
        this.wallID = wallID;
        this.changed();
    }

    /** Sets the saved value for this mine's lowest XYZ position.
     *  Use {@link #setMineMin(Location)} for physically adjusting the mine's minimum location.
     * @param location Bukkit location of the mine's lowest XYZ position.
     * */
    public void setMinVar(Location location) {
        setMinVar(PS.valueOf(location));
    }

    /** Sets the saved value for this mine's lowest XYZ position.
     *  Use {@link #setMineMin(Location)} for physically adjusting the mine's minimum location.
     * @param location MassiveCore location of the mine's lowest XYZ position.
     * */
    public void setMinVar(PS location) {
        this.mineMin = location;
        this.changed();
    }

    /** Sets the saved value for this mine's greatest XYZ position.
     *  Use {@link #setMineMax(Location)} for physically adjusting the mine's maximum location.
     * @param location Bukkit location of the mine's greatest XYZ position.
     * */
    public void setMaxVar(Location location) {
        setMaxVar(PS.valueOf(location));
    }

    /** Sets the saved value for this mine's greatest XYZ position.
     *  Use {@link #setMineMax(Location)} for physically adjusting the mine's maximum location.
     * @param location MassiveCore location of the mine's greatest XYZ position.
     * */
    public void setMaxVar(PS location) {
        this.mineMax = location;
        this.changed();
    }

    /**
     * @return Height of the mine counted in blocks.
     */
    public int getHeight() {
        if(height <= 0) setHeightVar(MinesConf.get().mineDefaultHeight);
        return height;
    }

    /**
     * @return Width of the mine counted in blocks.
     */
    public int getWidth() {
        if(width <= 0) setWidthVar(MinesConf.get().mineDefaultWidth);
        return width;
    }

    /**
     * @return a key for the path's schematic in the {@link LayoutConf} file.
     */
    public int getPathID() {
        return pathID;
    }

    public void setPathID(int pathID) {
        this.pathID = pathID;
    }

    public int getWallID() {
        return wallID;
    }

    public void setWallID(int wallID) {
        this.wallID = wallID;
    }

    // -------------------------------------------- //
    //  LOCATIONS
    // -------------------------------------------- //

    public World getWorld() {
        return getMineMax().getWorld();
    }

    public Location getMineMin() {
        return new Location(MinesWorldManager.get().getWorld(), mineMin.getLocationX(), mineMin.getLocationY(), mineMin.getLocationZ());
    }

    public Location getMineMax() {
        return new Location(MinesWorldManager.get().getWorld(), mineMax.getLocationX(), mineMax.getLocationY(), mineMax.getLocationZ());
    }

    public Location getSpawnPointLoc() {
        return new Location(MinesWorldManager.get().getWorld(), spawnPoint.getLocationX(), spawnPoint.getLocationY(), spawnPoint.getLocationZ());
    }

    public void setOrigin(Location origin) {
        setOrigin(PS.valueOf(origin));
    }

    public void setOrigin(PS origin) {
        this.origin = origin;
        this.changed();
    }

    public Location getOrigin() {
        return new Location(MinesWorldManager.get().getWorld(), origin.getLocationX(), origin.getLocationY(), origin.getLocationZ());
    }

    public void setMineCenter(Location mineCenter) {
        setMineCenter(PS.valueOf(mineCenter));
    }

    public void setMineCenter(PS mineCenter) {
        this.mineCenter = mineCenter;
        this.changed();
    }

    public Location getMineCenter() {
        return new Location(MinesWorldManager.get().getWorld(), mineCenter.getLocationX(), mineCenter.getLocationY(), mineCenter.getLocationZ());
    }

    public Location getSpawnPoint() {
        return new Location(MinesWorldManager.get().getWorld(), spawnPoint.getLocationX(), spawnPoint.getLocationY(), spawnPoint.getLocationZ(), spawnPoint.getYaw(), spawnPoint.getPitch());
    }

    public void setSpawnPoint(Location location) {
        setSpawnPoint(PS.valueOf(location));
    }

    public void setSpawnPoint(PS location) {
        this.spawnPoint = location;
        this.changed();
    }

    // -------------------------------------------- //
    //  NPCs
    // -------------------------------------------- //

    public String getArchitectUUID() {
        return architectUUID;
    }

    public void setArchitectUUID(String architectUUID) {
        this.architectUUID = architectUUID;
        this.changed();
    }

    public String getResearcherUUID() {
        return researcherUUID;
    }

    public void setResearcherUUID(String researcherUUID) {
        this.researcherUUID = researcherUUID;
        this.changed();
    }

    public Location getArchitectLocation() {
        return new Location(MinesWorldManager.get().getWorld(), architectLocation.getLocationX(), architectLocation.getLocationY(), architectLocation.getLocationZ(), architectLocation.getYaw(), architectLocation.getPitch());
    }

    public void setArchitectLocation(Location location) {
        setArchitectLocation(PS.valueOf(location));
    }

    public void setArchitectLocation(PS location) {
        this.architectLocation = location;
        this.changed();
    }

    public Location getResearcherLocation() {
        return new Location(MinesWorldManager.get().getWorld(), researcherLocation.getLocationX(), researcherLocation.getLocationY(), researcherLocation.getLocationZ(), researcherLocation.getYaw(), researcherLocation.getPitch());
    }

    public void setResearcherLocation(Location researcherLocation) {
        this.researcherLocation = PS.valueOf(researcherLocation);
    }

    // -------------------------------------------- //
    //  EXTRA METHODS
    // -------------------------------------------- //

    public MineRegenCountdown getRegenCountdown() {
        return MineColl.get().countdowns.get(this);
    }


    // -------------------------------------------- //
    //  MISC
    // -------------------------------------------- //

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        // Detect Nochange
        if (MUtil.equals(this.name, name)) return;

        // Apply
        this.name = name;

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

    public void setUnlockedDistributions(List<Integer> unlockedDistributions) {
        this.unlockedDistributions = unlockedDistributions;
        this.changed();
    }

    public void addUnlockedDistribution(Integer integer){
        unlockedDistributions.add(integer);
        this.changed();
    }

    public List<Integer> getUnlockedDistributions() {
        return unlockedDistributions;
    }

    public void spawnArchitect(){
        PrisonNPCs.spawnArchitectNPC(this, getArchitectUUID());
    }

    public void despawnArchitect(DespawnReason reason){
        PrisonNPCs.despawnNPC(getArchitectUUID(), reason);
    }

    public void spawnResearcher(){
        PrisonNPCs.spawnResearcherNPC(this, getResearcherUUID());
    }

    public void despawnResearcher(DespawnReason reason){
        PrisonNPCs.despawnNPC(getResearcherUUID(), reason);
    }
}
