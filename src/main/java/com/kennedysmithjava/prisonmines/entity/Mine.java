package com.kennedysmithjava.prisonmines.entity;

import com.boydti.fawe.bukkit.v0.FaweAdapter_All;
import com.boydti.fawe.util.EditSessionBuilder;
import com.kennedysmithjava.prisonmines.MineRegenCountdown;
import com.kennedysmithjava.prisonmines.MinesWorldManager;
import com.kennedysmithjava.prisonmines.PrisonMines;
import com.kennedysmithjava.prisonmines.util.BlockMaterial;
import com.kennedysmithjava.prisonmines.util.FAWETracker;
import com.kennedysmithjava.prisonmines.util.LazyRegion;
import com.kennedysmithjava.prisonmines.util.MiscUtil;
import com.kennedysmithjava.prisonnpcs.entity.ArchitectConf;
import com.kennedysmithjava.prisonnpcs.entity.WarrenConf;
import com.kennedysmithjava.prisonnpcs.npcs.NPCArchitect;
import com.kennedysmithjava.prisonnpcs.npcs.NPCWarren;
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
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

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
        this.setOrigin(that.origin);
        this.setMineMin(that.mineMin);
        this.setMineMax(that.mineMax);
        this.setSpawnPoint(that.spawnPoint);
        this.setArchitectLocation(that.architectLocation);
        this.setResearcherLocation(that.researcherLocation);
        this.setUpgrades(that.upgrades);
        this.setMineCenter(that.mineCenter);
        this.setArchitectID(that.architectID);
        this.setResearcherID(that.researcherID);
        this.setLevel(that.level);
        this.setFloorID(that.pathID);
        this.setWallID(that.wallID);
        this.setHeightVar(that.height);
        this.setWidthVar(that.width);
        this.setUnlockedDistributions(that.unlockedDistributions);
        this.setBlockDistribution(that.currentDistributionID);

        return this;
    }

    // MISC
    private String name;
    private long regenTimer;
    private transient MineRegenCountdown countdown;

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
    private int architectID;
    private int researcherID;

    // PHYSICAL INFORMATION
    private int wallID;
    private int pathID;
    private int width;
    private int height;
    private int currentDistributionID;

    // -------------------------------------------- //
    //  MINE ARCHITECTURE
    // -------------------------------------------- //

    /**
     * Regenerates the blocks in this mine.
     */
    public void regen() {
        try {
            RandomPattern pat = new RandomPattern();
            FaweAdapter_All adapter = new FaweAdapter_All();
            this.getBlockDistribution().forEach((material, aDouble) -> {
                BaseBlock baseBlock = new BaseBlock(adapter.getBlockId(material.getMaterial()), material.getData());
                pat.add(baseBlock, (aDouble / 100));
            });
            World world = getWorld();

            Location mineMin = getMineMin();
            Location mineMax = getMineMax();

            CuboidRegion cuboidRegion = new CuboidRegion(BukkitUtil.getLocalWorld(world), new BlockVector(mineMin.getBlockX(), mineMin.getBlockY(), mineMin.getBlockZ()), new BlockVector(mineMax.getBlockX(), mineMax.getBlockY(), mineMax.getBlockZ()));
            EditSession editSession = (new EditSessionBuilder(BukkitUtil.getLocalWorld(world))).fastmode(true).build();
            Bukkit.getScheduler().runTaskAsynchronously(PrisonMines.get(), () -> {
                editSession.setBlocks(cuboidRegion, pat);
                editSession.flushQueue();
            });
            this.getRegenCountdown().resetCounter();
        } catch (Throwable throwable) { throwable.printStackTrace(); }
    }

    /**
     * Sets the height of this mine, updating the values stored in the regeneration timer.
     * Regenerates the unbreakable border around the mine.
     * @param h The height of the mine in blocks.
     */
    public void setHeight(int h) {
        this.pauseRegenCountdown(true);
        this.clearMine();
        this.clearBorder();
        this.generateBorder(getWidth(), h, MinesConf.get().minesBorderMaterial);
        int heightDifference = h - getHeight();
        this.setMineMin(getMineMin().clone().add(0, -heightDifference, 0));
        this.setHeightVar(h);
        this.regen();
        this.pauseRegenCountdown(false);
    }

    /**
     * Sets the width of this mine, updating the values stored in the regeneration timer.
     * Regenerates the unbreakable border around the mine.
     * @param w The width of the mine in blocks.
     */
    public void setWidth(int w, Runnable onFinish) {

        this.despawnResearcherNPC();
        this.despawnArchitectNPC();
        this.pauseRegenCountdown(true);
        this.clearBorder();
        this.clearMine();

        pasteFloor(getPathID(), w, () -> {
            regen();
            spawnArchitectNPC();
            spawnResearcherNPC();
            pauseRegenCountdown(false);
            onFinish.run();
        });

    }

    public void rebuildSchematics(int floorID, int wallID, Runnable onFinish){

        boolean rebuildFloor = floorID != this.getPathID();
        boolean rebuildWall = wallID != this.getWallID();

        List<Boolean> pasteTrackers = new ArrayList<>();

        if(rebuildFloor){
            pasteTrackers.add(false);
            Floor floor = LayoutConf.get().getPath(getPathID());
            this.despawnArchitectNPC();
            this.despawnResearcherNPC();
            this.pauseRegenCountdown(true);
            this.clearBorder();
            this.clearMine();
            this.setLocations(floor);
            this.pasteFloor(floorID, getWidth(), () -> {
                regen();
                spawnArchitectNPC();
                spawnResearcherNPC();
                pauseRegenCountdown(false);
                pasteTrackers.add(true);
            });
        }

        if(rebuildWall){
            pasteTrackers.add(false);
            this.pasteWall(wallID, () -> {
                pasteTrackers.add(true);
            });
        }

        new BukkitRunnable() {
            @Override
            public void run() {
                //The count of true/false booleans must be equal for all pastes to be complete.
                int done = 0; //True values
                int notDone = 0; //False values
                for (Boolean isDone : pasteTrackers) {
                    if (isDone) {
                        done++;
                    } else {
                        notDone++;
                    }
                }
                if(done == notDone) { onFinish.run(); this.cancel(); }
            }
        }.runTaskTimer(PrisonMines.get(), 0, 10);

    }

    /**
     * Pastes the floor schematic for the currently set FloorID given a new width.
     * Unsafe for NPCs.
     */
    private void pasteFloor(int floorID, int width, Runnable onFinish){
        FAWETracker clearTracker = MiscUtil.pasteSchematic(LayoutConf.get().getPath(getPathID()).getSchematic(getWidth()), new Vector(origin.getLocationX(), origin.getLocationY(), origin.getLocationZ()), true);

        new BukkitRunnable() {
            @Override
            public void run() {
                if(clearTracker.isDone()){
                    setFloorID(floorID);
                    setWidthVar(width);
                    FAWETracker floorTracker = MiscUtil.pasteSchematic(LayoutConf.get().getPath(floorID).getSchematic(width), new Vector(origin.getLocationX(), origin.getLocationY(), origin.getLocationZ()));
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            if(floorTracker.isDone()){
                                generateBorder(width, getHeight(), MinesConf.get().minesBorderMaterial);
                                Location newMax = getMineCenter().add(-(width - 2), 0, -(width - 2));
                                Location newMin = getMineCenter().add(width - 2, -(getHeight() - 1), width - 2);
                                setMineMax(newMax);
                                setMineMin(newMin);
                                onFinish.run();
                                this.cancel();
                            }
                        }
                    }.runTaskTimer(PrisonMines.get(), 0, 10);
                    this.cancel();
                }
            }
        }.runTaskTimer(PrisonMines.get(), 0, 10);



    }

    /**
     * Pastes the wall schematic for the currently set WallID.
     * Unsafe for NPCs.
     */
    private void pasteWall(int wallID, Runnable onFinish){
        FAWETracker clearTracker = MiscUtil.pasteSchematic(LayoutConf.get().getWall(getWallID()).getSchematic(), new Vector(origin.getLocationX(), origin.getLocationY(), origin.getLocationZ()), true);
        new BukkitRunnable() {
            @Override
            public void run() {
                if(clearTracker.isDone()){
                    setWallID(wallID);
                    FAWETracker wallTracker = MiscUtil.pasteSchematic(LayoutConf.get().getWall(wallID).getSchematic(), new Vector(origin.getLocationX(), origin.getLocationY(), origin.getLocationZ()));
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            if(wallTracker.isDone()){
                                onFinish.run();
                                this.cancel();
                            }
                        }
                    }.runTaskTimer(PrisonMines.get(), 0, 10);
                    this.cancel();
                }
            }
        }.runTaskTimer(PrisonMines.get(), 0, 10);
    }

    private void setLocations(Floor floor){
        //TODO: Set building locations
        this.setSpawnPoint(floor.getSpawn().get(getOrigin()));
        this.setArchitectLocation(floor.getArchitectNPC().get(getOrigin()));
        this.setResearcherLocation(floor.getResearcherNPC().get(getOrigin()));
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

    public void clearBorder(){
        this.generateBorder(getWidth(), getHeight(), new BlockMaterial(Material.AIR, (byte) 0));
    }

    public void setMineMax(Location mineMax) {
        this.mineMax = PS.valueOf(mineMax);
        this.changed();
    }

    public void setMineMin(Location mineMin) {
        this.mineMin = PS.valueOf(mineMin);
        this.changed();
    }

    // -------------------------------------------- //
    //  LEVEL & UPGRADES
    // -------------------------------------------- //

    public void addUpgrade(String upgradeID) {
        this.upgrades.add(upgradeID);
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

    public Map<BlockMaterial, Double> getBlockDistribution() {
        return DistributionConf.get().distribution.get(getCurrentDistributionID()).getRates();
    }

    public void setBlockDistribution(int id) {
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
     *  Use {@link #setWidth(int, Runnable)} for physically adjusting the mine's width.
     * @param width The width of the mine in blocks.
     * */
    public void setWidthVar(int width) {
        this.width = width;
        this.changed();
    }

    /** Sets the saved value for this mine's path ID.
     *  Use {@link #setFloorID(int)} or {@link #setWidth(int, Runnable)} for physically adjusting the mine's paths.
     * @param pathID a key for the path's schematic in the {@link LayoutConf} file.
     * */
    public void setFloorID(int pathID) {
        this.pathID = pathID;
        this.changed();
    }

    /** Sets the saved value for this mine's wall ID.
     * @param wallID a key for the wall's schematic in the {@link LayoutConf} file.
     * */
    public void setWallID(int wallID) {
        this.wallID = wallID;
        this.changed();
    }

    /** Sets the saved value for this mine's lowest XYZ position.
     *  Use {@link #setMineMin(Location)} for physically adjusting the mine's minimum location.
     * @param location MassiveCore location of the mine's lowest XYZ position.
     * */
    public void setMineMin(PS location) {
        this.mineMin = location;
        this.changed();
    }

    /** Sets the saved value for this mine's greatest XYZ position.
     *  Use {@link #setMineMax(Location)} for physically adjusting the mine's maximum location.
     * @param location MassiveCore location of the mine's greatest XYZ position.
     * */
    public void setMineMax(PS location) {
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

    public int getWallID() {
        return wallID;
    }

    // -------------------------------------------- //
    //  LOCATIONS
    // -------------------------------------------- //

    public World getWorld() {
        return getMineMax().getWorld();
    }

    public LazyRegion getLazyRegion() {
        return new LazyRegion(this.getMineMin(), this.getMineMax());
    }

    public Location getMineMin() {
        return new Location(MinesWorldManager.get().getWorld(), mineMin.getLocationX(), mineMin.getLocationY(), mineMin.getLocationZ());
    }

    public int getMinX() {
        return (int) Math.ceil(mineMin.getLocationX());
    }

    public Location getMineMax() {
        return new Location(MinesWorldManager.get().getWorld(), mineMax.getLocationX(), mineMax.getLocationY(), mineMax.getLocationZ());
    }

    public Location getSpawnPointLoc() {
        return new Location(MinesWorldManager.get().getWorld(), spawnPoint.getLocationX(), spawnPoint.getLocationY(), spawnPoint.getLocationZ());
    }

    public void setOrigin(Location origin) {
        this.setOrigin(PS.valueOf(origin));
    }

    public void setOrigin(PS origin) {
        this.origin = origin;
        this.changed();
    }

    public Location getOrigin() {
        return new Location(MinesWorldManager.get().getWorld(), origin.getLocationX(), origin.getLocationY(), origin.getLocationZ());
    }

    public void setMineCenter(Location mineCenter) {
        this.setMineCenter(PS.valueOf(mineCenter));
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
        this.setSpawnPoint(PS.valueOf(location));
    }

    public void setSpawnPoint(PS location) {
        this.spawnPoint = location;
        this.changed();
    }

    // -------------------------------------------- //
    //  NPCs
    // -------------------------------------------- //

    public int getArchitectID() {
        return architectID;
    }

    public void setArchitectID(int architectID) {
        this.architectID = architectID;
        this.changed();
    }

    public int getResearcherID() {
        return researcherID;
    }

    public void setResearcherID(int researcherID) {
        this.researcherID = researcherID;
        this.changed();
    }

    public Location getArchitectLocation() {
        return new Location(MinesWorldManager.get().getWorld(), architectLocation.getLocationX(), architectLocation.getLocationY(), architectLocation.getLocationZ(), architectLocation.getYaw(), architectLocation.getPitch());
    }

    public void setArchitectLocation(Location location) {
        this.setArchitectLocation(PS.valueOf(location));
    }

    public void setArchitectLocation(PS location) {
        this.architectLocation = location;
        this.changed();
    }

    public Location getResearcherLocation() {
        return new Location(MinesWorldManager.get().getWorld(), researcherLocation.getLocationX(), researcherLocation.getLocationY(), researcherLocation.getLocationZ(), researcherLocation.getYaw(), researcherLocation.getPitch());
    }

    public void setResearcherLocation(Location researcherLocation) {
        this.setResearcherLocation(PS.valueOf(researcherLocation));
    }

    public void setResearcherLocation(PS researcherLocation) {
        this.researcherLocation = researcherLocation;
        this.changed();
    }

    // -------------------------------------------- //
    //  EXTRA METHODS
    // -------------------------------------------- //

    public MineRegenCountdown getRegenCountdown() {
        return countdown;
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

    @SuppressWarnings("unused")
    public void spawnArchitectNPC(){
        NPC npc = new NPCArchitect().spawn(ArchitectConf.get().architectName,  getArchitectLocation(), 1);
        setArchitectID(npc.getId());
    }

    @SuppressWarnings("unused")
    public void spawnResearcherNPC(){
        NPC npc = new NPCWarren().spawn(WarrenConf.get().researcherName, getResearcherLocation(), 1);
        setResearcherID(npc.getId());
    }

    @SuppressWarnings("unused")
    public void despawnArchitectNPC(){
        NPC npc = CitizensAPI.getNPCRegistry().getById(architectID);
        if(npc != null) npc.destroy();
        setArchitectID(0);
    }

    @SuppressWarnings("unused")
    public void despawnResearcherNPC(){
        NPC npc = CitizensAPI.getNPCRegistry().getById(researcherID);
        if(npc != null) npc.destroy();
        setResearcherID(0);
    }

    public void spawnNPCs(){
        this.spawnResearcherNPC();
        this.spawnArchitectNPC();
        Bukkit.broadcastMessage("Spawning all NPCs.");
    }

    public void despawnNPCs(){
        this.despawnResearcherNPC();
        this.despawnArchitectNPC();
        Bukkit.broadcastMessage("Despawning all NPCs.");
    }

    public boolean npcsSpawned(){
        return getArchitectID() != 0 && getResearcherID() != 0;
    }

    public void clearMine(){
        MiscUtil.blockFill(getMineMin(), getMineMax(), new BlockMaterial(Material.AIR, (byte) 0));
    }

    public MineRegenCountdown createRegenCountdown(){
        this.countdown = new MineRegenCountdown(this, 0);
        return getRegenCountdown();
    }

    public void pauseRegenCountdown(boolean paused){
        if(this.countdown != null) this.countdown.pause(paused);
    }

    public void removeCountdown(){
        this.countdown.stop();
        this.countdown = null;
    }

    public boolean isRegenCountdownActive(){
        return this.countdown != null && !this.countdown.isPaused();
    }
}

