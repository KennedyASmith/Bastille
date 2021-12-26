package com.kennedysmithjava.prisonmines.entity;

import com.boydti.fawe.bukkit.v0.FaweAdapter_All;
import com.boydti.fawe.util.EditSessionBuilder;
import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import com.kennedysmithjava.prisonmines.MineRegenCountdown;
import com.kennedysmithjava.prisonmines.MinesWorldManager;
import com.kennedysmithjava.prisonmines.PrisonMines;
import com.kennedysmithjava.prisonmines.cmd.type.TypeMobility;
import com.kennedysmithjava.prisonmines.event.EventMineChanged;
import com.kennedysmithjava.prisonmines.util.*;
import com.kennedysmithjava.prisonnpcs.PrisonNPCs;
import com.kennedysmithjava.prisonnpcs.npcs.NPCArchitect;
import com.kennedysmithjava.prisonnpcs.npcs.NPCCoinCollector;
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
import com.sk89q.worldedit.util.Direction;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.managers.RegionManager;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.npc.NPCRegistry;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

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
        this.setCollectorLocation(that.collectorLocation);
        this.setEnchantTableLocation(that.enchantTableLocation);
        this.setBeaconLocation(that.beaconLocation);
        this.setChestLocation(that.chestLocation);
        this.setPortalMaxLocation(that.portalMaxLocation);
        this.setPortalMinLocation(that.portalMinLocation);
        this.setUpgrades(upgrades);
        this.setMineCenter(that.mineCenter);
        this.setArchitectID(that.architectID);
        this.setResearcherID(that.researcherID);
        this.setCollectorID(that.collectorID);
        this.setLevel(that.level);
        this.setFloorID(that.pathID);
        this.setWallID(that.wallID);
        this.setHeightVar(that.height);
        this.setWidthVar(that.width);
        this.setUnlockedDistributions(that.unlockedDistributions);
        this.setBlockDistribution(that.currentDistributionID);
        this.setAutoRegenEnabled(that.autoRegenEnabled);
        this.setSelectedMobility(that.selectedMobility);
        this.setUnlockedBuildings(that.unlockedBuildings);

        return this;
    }

    // MISC
    private String name;
    private long regenTimer;
    private transient MineRegenCountdown countdown;
    private transient Hologram regenHologram;

    // LEVEL & UPGRADES
    private int level = 1;
    private Map<String, UpgradeStatus> upgrades = MUtil.map("LADDER_1_ON", new UpgradeStatus(true, true));
    private List<Integer> unlockedDistributions = new ArrayList<>();
    private TypeMobility selectedMobility = TypeMobility.LADDER_1;
    private List<BuildingType> unlockedBuildings = new ArrayList<>();

    // LOCATION INFORMATION
    private PS spawnPoint;
    private PS origin;
    private PS mineMin;
    private PS mineMax;
    private PS mineCenter;
    private PS architectLocation;
    private PS researcherLocation;
    private PS collectorLocation;
    private PS enchantTableLocation;
    private PS beaconLocation;
    private PS chestLocation;
    private PS portalMaxLocation;
    private PS portalMinLocation;

    // NPC INFORMATION
    private int architectID;
    private int researcherID;
    private int collectorID;

    // PHYSICAL INFORMATION
    private int wallID;
    private int pathID;
    private int width;
    private int height;
    private int currentDistributionID;

    private boolean autoRegenEnabled;

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
        } catch (Throwable throwable) { throwable.printStackTrace(); }
    }

    public void regenAnimation(){

        final BlockMaterial material = new BlockMaterial(Material.STEP, 0);

        final World world = getWorld();
        final Location mineMin = getMineMin();
        final Location mineMax = getMineMax();
        final int width = Math.abs(mineMax.getBlockX() - mineMin.getBlockX()) + 1;
        final int frameCount = width + 2;
        final Location northWestCorner = mineMax.clone().add(-1, 1, -1);
        final Location southWestCorner = northWestCorner.clone().add(0, 0, width + 1);

        final Location southEastCorner = northWestCorner.clone().add(width + 1, 0, width + 1);
        final Location northEastCorner = northWestCorner.clone().add(width + 1, 0, 0);
        final int minX = southWestCorner.getBlockX();
        final int maxX = southEastCorner.getBlockX();
        final int z = southEastCorner.getBlockZ();
        final int y = northEastCorner.getBlockY();
        int speed = 10;

        BukkitRunnable openAnimation = new BukkitRunnable() {

            int counter;
            int frame;

            @Override
            public void run() {

                if(frame < (width + 3) && counter == speed){
                    for (int x = minX; x <= maxX; x++) {
                        Block block = world.getBlockAt(x, y, (z - frame));
                        block.setType(Material.AIR);
                    }
                    frame++;
                    counter = 0;
                }else if (frame == width + 2){
                    this.cancel();
                    if(countdown != null){
                        countdown.setRegenerating(false);
                        countdown.setPaused(false);
                    }
                }

                counter++;
            }
        };

        Runnable onClosed = () -> {
            regen();
            new BukkitRunnable() {
                @Override
                public void run() {
                    openAnimation.runTaskTimer(PrisonMines.get(), 0, 1);
                }
            }.runTaskLater(PrisonMines.get(), 20 * 5);
        };

        BukkitTask closeAnimation = new BukkitRunnable() {

            int animation = 1;
            int counter;
            int frame;

            @Override
            public void run() {
                switch(animation){
                    case 1:

                        //-----------------//
                        // First Animation
                        //-----------------//

                        if(frame < frameCount && counter == speed){
                            Block block1 = world.getBlockAt(northWestCorner.clone().add(0,0, frame));
                            block1.setType(material.getMaterial());
                            block1.setData(material.getData());
                            Block block2 = world.getBlockAt(southEastCorner.clone().add(0,0, -frame));
                            block2.setType(material.getMaterial());
                            block2.setData(material.getData());
                            frame++;
                            counter = 0;
                        }else if (frame == frameCount){
                            animation = 2;
                            frame = 0;
                            counter = speed - 1;
                        }
                        break;
                    case 2:

                        //-----------------//
                        // Second Animation
                        //-----------------//
                        if(frame < frameCount && counter == speed){
                            Block block1 = world.getBlockAt(southWestCorner.clone().add(frame,0, 0));
                            block1.setType(material.getMaterial());
                            block1.setData(material.getData());
                            Block block2 = world.getBlockAt(northEastCorner.clone().add(-frame,0, 0));
                            block2.setType(material.getMaterial());
                            block2.setData(material.getData());
                            frame++;
                            counter = 0;
                        }else if (frame == frameCount){
                            animation = 3;
                            frame = 0;
                            counter = speed - 1;
                        }
                        break;
                    case 3:
                        //-----------------//
                        // Third Animation
                        //-----------------//
                        if(frame < (width + 2) && counter == speed){
                            for (int x = minX; x <= maxX; x++) {
                                Block block = world.getBlockAt(x, y, (z - (frame + 1)));
                                block.setType(material.getMaterial());
                                block.setData(material.getData());
                            }
                            frame++;
                            counter = 0;
                        }else if (frame == width + 1){
                            onClosed.run();
                            this.cancel();
                        }
                        break;
                }
                counter++;
            }
        }.runTaskTimer(PrisonMines.get(), 0, 1);
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
        this.generateMobilityArea();
        int heightDifference = h - getHeight();
        this.setMineMin(getMineMin().clone().add(0, -heightDifference, 0));
        this.setHeightVar(h);
        this.regen();
        this.pauseRegenCountdown(false);
        Bukkit.getServer().getPluginManager().callEvent(new EventMineChanged(this));
    }

    /**
     * Sets the width of this mine, updating the values stored in the regeneration timer.
     * Regenerates the unbreakable border around the mine.
     * @param w The width of the mine in blocks.
     */
    public void setWidth(int w, Runnable onFinish) {

        this.despawnNPCs();
        this.pauseRegenCountdown(true);
        this.clearBorder();
        this.clearMine();
        if(!autoRegenEnabled) removeLever();

        pasteFloor(getPathID(), w, () -> {
            regen();
            spawnNPCs();
            if(!autoRegenEnabled) placeLever();
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
            this.clearBuildings();
            this.setLocations(floor);
            this.pasteFloor(floorID, getWidth(), () -> {
                regen();
                spawnArchitectNPC();
                spawnResearcherNPC();
                spawnCollectorNPC();
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
                if(done == notDone) {
                    onFinish.run();
                    this.cancel();
                }
            }
        }.runTaskTimer(PrisonMines.get(), 0, 10);

    }

    /**
     * Pastes the floor schematic for the currently set FloorID given a new width.
     * Unsafe for NPCs.
     */
    private void pasteFloor(int floorID, int width, Runnable onFinish){
        FAWETracker clearTracker = MiscUtil.pasteSchematic(LayoutConf.get().getPath(getPathID()).getSchematic(getWidth()), new Vector(origin.getLocationX(), origin.getLocationY(), origin.getLocationZ()), true);
        Mine mine = this;
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
                                generateMobilityArea();
                                buildBuildings();
                                Location newMax = getMineCenter().add(-(width - 2), 0, -(width - 2));
                                Location newMin = getMineCenter().add(width - 2, -(getHeight() - 1), width - 2);
                                setMineMax(newMax);
                                setMineMin(newMin);
                                onFinish.run();
                                this.cancel();
                                Bukkit.getServer().getPluginManager().callEvent(new EventMineChanged(mine));
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

    public void setLocations(Floor floor){
        //TODO: Set building locations
        this.setSpawnPoint(floor.getSpawn().get(getOrigin()));
        this.setArchitectLocation(floor.getArchitectNPC().get(getOrigin()));
        this.setResearcherLocation(floor.getResearcherNPC().get(getOrigin()));
        this.setCollectorLocation(floor.getCollectorNPC().get(getOrigin()));
        this.setEnchantTableLocation(floor.getEnchantTable().get(getOrigin()));
        this.setBeaconLocation(floor.getBeacon().get(getOrigin()));
        this.setChestLocation(floor.getChest().get(getOrigin()));
        this.setPortalMaxLocation(floor.getPortalMax().get(getOrigin()));
        this.setPortalMinLocation(floor.getPortalMin().get(getOrigin()));
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

    public void generateMobilityArea(){

        int w = getWidth();
        int h = getHeight();
        Location mineCenter = getMineCenter();

        int widthV = (w-1);
        int heightV = (h-1);

        Location westTop = mineCenter.clone().add(-widthV, 0, -widthV);
        Location westBottom = westTop.clone().add(0, -heightV, widthV * 2);
        Location northTop = westBottom.clone().add(0, heightV, 0);
        Location northBottom = northTop.clone().add(widthV * 2, -heightV, 0);
        Location eastTop = northBottom.clone().add(0, heightV, 0);
        Location eastBottom = eastTop.clone().add(0, -heightV, -widthV * 2);
        Location southTop = eastBottom.clone().add(0, heightV, 0);
        Location southBottom = southTop.clone().add(-widthV * 2, -heightV, 0);

        BlockMaterial air = new BlockMaterial(Material.AIR);
        MiscUtil.blockFill(westBottom, westTop, air);
        MiscUtil.blockFill(northBottom, northTop, air);
        MiscUtil.blockFill(eastBottom, eastTop, air);
        MiscUtil.blockFill(southBottom, southTop, air);

        switch(selectedMobility){
            case LADDER_1:
                MiscUtil.blockFill(mineCenter.clone().add(-widthV, -heightV, 0), mineCenter.clone().add(-widthV, 0, 0), MiscUtil.getLadder(Direction.WEST));
                return;
            case LADDER_2:
                MiscUtil.blockFill(mineCenter.clone().add(0, -heightV, widthV), mineCenter.clone().add(0, 0, widthV), MiscUtil.getLadder(Direction.SOUTH));
                MiscUtil.blockFill(mineCenter.clone().add(0, -heightV, -widthV), mineCenter.clone().add(0, 0, -widthV), MiscUtil.getLadder(Direction.NORTH));
                MiscUtil.blockFill(mineCenter.clone().add(widthV, -heightV, 0), mineCenter.clone().add(widthV, 0, 0), MiscUtil.getLadder(Direction.EAST));
                MiscUtil.blockFill(mineCenter.clone().add(-widthV, -heightV, 0), mineCenter.clone().add(-widthV, 0, 0), MiscUtil.getLadder(Direction.WEST));
                return;
            case FULL_LADDER:
                MiscUtil.blockFill(westBottom, westTop, MiscUtil.getLadder(Direction.WEST));
                MiscUtil.blockFill(northBottom, northTop, MiscUtil.getLadder(Direction.NORTH));
                MiscUtil.blockFill(eastBottom, eastTop, MiscUtil.getLadder(Direction.EAST));
                MiscUtil.blockFill(southBottom, southTop, MiscUtil.getLadder(Direction.SOUTH));
                return;
            case JUMP_PAD:
                BlockMaterial plate = new BlockMaterial(Material.GOLD_PLATE);
                MiscUtil.blockFill(southBottom, southTop.clone().add(0, -heightV, 0), plate);
                MiscUtil.blockFill(westBottom, westTop.clone().add(0, -heightV, 0), plate);
                MiscUtil.blockFill(eastBottom, eastTop.clone().add(0, -heightV, 0), plate);
                MiscUtil.blockFill(northBottom, northTop.clone().add(0, -heightV, 0), plate);
                return;
        }

    }

    public void clearBuildings(){
        buildBeacon(true);
        buildChest(true);
        buildEnchantTable(true);
        buildPortal(true);
    }

    public void buildBuildings(){
        buildBeacon(false);
        buildChest(false);
        buildEnchantTable(false);
        buildPortal(false);
    }

    public boolean hasBuilding(BuildingType buildingType){
        return getUnlockedBuildings().contains(buildingType);
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

    public Floor getFloor(){
        return LayoutConf.get().getPath(getPathID());
    }

    public void buildEnchantTable(boolean destroy){
        if(!hasBuilding(BuildingType.ENCHANT_TABLE)) return;
        setBlock(getFloor().getEnchantTableBlock(), getEnchantTableLocation(), destroy);
    }

    public void buildBeacon(boolean destroy){
        if(!hasBuilding(BuildingType.BEACON)) return;
        setBlock(getFloor().getBeaconBlock(), getBeaconLocation(), destroy);
    }

    public void buildChest(boolean destroy){
        if(!hasBuilding(BuildingType.CHEST)) return;
        setBlock(getFloor().getChestBlock(), getChestLocation(), destroy);
    }

    public void buildPortal(boolean destroy){
        if(!hasBuilding(BuildingType.PORTAL)) return;
        BlockMaterial blockMaterial = getFloor().getPortalBlock();
        if(destroy) blockMaterial = new BlockMaterial(Material.AIR);
        MiscUtil.blockFill(getPortalMinLocation(), getPortalMaxLocation(), blockMaterial);
    }

    private void setBlock(BlockMaterial material, Location location, boolean destroy){
        Block block = location.getBlock();
        if(destroy) material = new BlockMaterial(Material.AIR);
        block.setType(material.getMaterial(), false);
        block.setData(material.getData(), false);
    }


    // -------------------------------------------- //
    //  LEVEL & UPGRADES
    // -------------------------------------------- //

    public void unlockUpgrade(String upgradeID, boolean purchased, boolean active){
        this.upgrades.put(upgradeID, new UpgradeStatus(purchased, active));
        this.changed();
    }

    public void purchaseUpgrade(String upgradeID, boolean active){
        upgrades.put(upgradeID, new UpgradeStatus(true, active));
        this.changed();
    }

    public void activateUpgrade(String upgradeID){
        upgrades.put(upgradeID, new UpgradeStatus(true, true));
        this.changed();
    }

    public void deactivateUpgrade(String upgradeID){
        upgrades.put(upgradeID, new UpgradeStatus(true, false));
        this.changed();
    }

    public void lockUpgrade(String upgradeID) {
        this.upgrades.remove(upgradeID);
        this.changed();
    }

    public boolean isUpgradePurchased(String upgrade){
        if(!isUpgradeUnlocked(upgrade)) return false;
        return upgrades.get(upgrade).isPurchased();
    }

    public boolean isUpgradeActive(String upgrade){
        if(!isUpgradeUnlocked(upgrade)) return false;
        return upgrades.get(upgrade).isActive();
    }

    public boolean isUpgradeUnlocked(String upgrade){
        return this.upgrades.containsKey(upgrade);
    }

    public Map<String, UpgradeStatus> getUpgrades() {
        return upgrades;
    }

    public void setUpgrades(Map<String, UpgradeStatus> upgrades) {
        this.upgrades = upgrades;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
        this.changed();
    }

    public void incrementLevel(){
        this.level++;
        this.changed();
    }

    public void decrementLevel(){
        this.level--;
        this.changed();
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

    public void setCollectorID(int collectorID) {
        this.collectorID = collectorID;
        this.changed();
    }

    public int getCollectorID() {
        return collectorID;
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

    public Location getCollectorLocation() {
        return new Location(MinesWorldManager.get().getWorld(), collectorLocation.getLocationX(), collectorLocation.getLocationY(), collectorLocation.getLocationZ(), collectorLocation.getYaw(), collectorLocation.getPitch());
    }

    public void setCollectorLocation(Location collectorLocation) {
        this.setCollectorLocation(PS.valueOf(collectorLocation));
    }

    public void setCollectorLocation(PS collectorLocation) {
        this.collectorLocation = collectorLocation;
        this.changed();
    }


    public void setBeaconLocation(PS beaconLocation) {
        this.beaconLocation = beaconLocation;
        this.changed();
    }

    public void setChestLocation(PS chestLocation) {
        this.chestLocation = chestLocation;
        this.changed();
    }

    public void setEnchantTableLocation(PS enchantTableLocation) {
        this.enchantTableLocation = enchantTableLocation;
        this.changed();
    }

    public void setPortalMaxLocation(PS portalMaxLocation) {
        this.portalMaxLocation = portalMaxLocation;
        this.changed();
    }

    public void setPortalMinLocation(PS portalMinLocation) {
        this.portalMinLocation = portalMinLocation;
        this.changed();
    }

    public void setPortalMaxLocation(Location portalMaxLocation) {
        this.setPortalMaxLocation(PS.valueOf(portalMaxLocation));
    }

    public void setPortalMinLocation(Location portalMinLocation) {
        this.setPortalMinLocation(PS.valueOf(portalMinLocation));
    }

    public void setBeaconLocation(Location beaconLocation) {
        this.setBeaconLocation(PS.valueOf(beaconLocation));
    }

    public void setChestLocation(Location chestLocation) {
        this.setChestLocation(PS.valueOf(chestLocation));
    }

    public void setEnchantTableLocation(Location enchantTableLocation) {
        this.setEnchantTableLocation(PS.valueOf(enchantTableLocation));
    }

    public Location getBeaconLocation() {
        return new Location(MinesWorldManager.get().getWorld(), beaconLocation.getLocationX(), beaconLocation.getLocationY(), beaconLocation.getLocationZ());
    }

    public Location getChestLocation() {
        return new Location(MinesWorldManager.get().getWorld(), chestLocation.getLocationX(), chestLocation.getLocationY(), chestLocation.getLocationZ());
    }

    public Location getEnchantTableLocation() {
        return new Location(MinesWorldManager.get().getWorld(), enchantTableLocation.getLocationX(), enchantTableLocation.getLocationY(), enchantTableLocation.getLocationZ());
    }

    public Location getPortalMaxLocation() {
        return new Location(MinesWorldManager.get().getWorld(), portalMaxLocation.getLocationX(), portalMaxLocation.getLocationY(), portalMaxLocation.getLocationZ());
    }
    public Location getPortalMinLocation() {
        return new Location(MinesWorldManager.get().getWorld(), portalMinLocation.getLocationX(), portalMinLocation.getLocationY(), portalMinLocation.getLocationZ());
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
        despawnArchitectNPC();
        NPC npc = new NPCArchitect().spawn("Archie-" + this.getId(),  getArchitectLocation(), 1);
        setArchitectID(npc.getId());
    }

    @SuppressWarnings("unused")
    public void spawnResearcherNPC(){
        despawnResearcherNPC();
        NPC npc = new NPCWarren().spawn("Warden-" + this.getId(), getResearcherLocation(), 1);
        setResearcherID(npc.getId());
    }

    @SuppressWarnings("unused")
    public void spawnCollectorNPC(){
        despawnCollectorNPC();
        NPC npc = new NPCCoinCollector().spawn("Collector-" + this.getId(), getCollectorLocation(), 1);
        setCollectorID(npc.getId());
    }

    @SuppressWarnings("unused")
    public void despawnArchitectNPC(){
        despawnNPC(getArchitectID());
    }

    @SuppressWarnings("unused")
    public void despawnResearcherNPC(){
        despawnNPC(getResearcherID());
    }

    @SuppressWarnings("unused")
    public void despawnCollectorNPC(){
        despawnNPC(getCollectorID());
    }

    private void despawnNPC(int id){
        NPCRegistry npcRegistry = PrisonNPCs.getRegistry();
        NPC npc = npcRegistry.getById(id);
        if(npc != null){
            npc.destroy();
        }
    }

    public void spawnNPCs(){
        this.spawnResearcherNPC();
        this.spawnArchitectNPC();
        this.spawnCollectorNPC();
    }

    public void despawnNPCs(){
        this.despawnResearcherNPC();
        this.despawnArchitectNPC();
        this.despawnCollectorNPC();
    }

    public boolean npcsSpawned(){
        NPCRegistry registry = PrisonNPCs.getRegistry();
        NPC architect = registry.getById(getArchitectID());
        if(architect == null) return false;
        NPC researcher = registry.getById(getArchitectID());
        if(researcher == null) return false;
        NPC collector = registry.getById(getArchitectID());
        if(collector == null) return false;
        NPC lever = registry.getById(getArchitectID());
        return lever != null;
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
        return this.countdown != null;
    }

    public void setAutoRegenEnabled(boolean autoRegenEnabled) {
        this.autoRegenEnabled = autoRegenEnabled;
    }

    public Location getLeverLocation(){
        return getMineMax().clone().add(-2, 2, -2);
    }

    public void placeLever(){
        World world = MinesWorldManager.get().getWorld();

        Block tempStand = world.getBlockAt(getLeverLocation().clone().add(0, -1,0));
        tempStand.setType(Material.DOUBLE_STONE_SLAB2);
        tempStand.setData((byte) 8);

        Block lever = world.getBlockAt(getLeverLocation());
        lever.setType(Material.LEVER);
        lever.setData((byte) 5);

        createRegenHologram();
    }

    public void createRegenHologram(){
        Hologram hologram = HologramsAPI.createHologram(PrisonMines.get(), getLeverLocation().clone().add(0.5, 1.5, 0.5));
        hologram.setAllowPlaceholders(true);
        hologram.appendTextLine("&a&lREGEN MINE");
        hologram.appendTextLine("{fast}&a%countdown_" + this.getId() + "% &r");
        setRegenHologram(hologram);
    }

    public void removeRegenHologram(){
        Hologram hologram = getRegenHologram();
        if(hologram != null) hologram.delete();
        this.regenHologram = null;
    }

    public boolean hologramExists(){
        return this.regenHologram != null;
    }

    public void removeLever(){
        World world = MinesWorldManager.get().getWorld();
        world.getBlockAt(getLeverLocation().clone().add(0, -1,0)).setType(Material.AIR);
        world.getBlockAt(getLeverLocation()).setType(Material.AIR);


        Hologram hologram = getRegenHologram();
        if(hologram != null) hologram.delete();
    }

    public boolean isAutoRegenEnabled() {
        return autoRegenEnabled;
    }

    public void toggleAutoRegenEnabled(){
        if(autoRegenEnabled){
            removeLever();
            setAutoRegenEnabled(false);
        }else{
            placeLever();
            setAutoRegenEnabled(true);
        }
    }

    public boolean tryManualRegen(){
        if(!isRegenCountdownActive()){
            Bukkit.broadcastMessage("Regen countdown not active");
            return false;
        }
        if(autoRegenEnabled) return false;
        countdown.setRegenerating(true);
        regenAnimation();
        return true;
    }

    public void setRegenHologram(Hologram regenHologram){
        this.regenHologram = regenHologram;
    }

    public Hologram getRegenHologram() {
        return regenHologram;
    }

    public TypeMobility getSelectedMobility() {
        return selectedMobility;
    }

    public void setSelectedMobility(TypeMobility selectedMobility) {
        this.selectedMobility = selectedMobility;
        this.changed();
    }

    public void setUnlockedBuildings(List<BuildingType> unlockedBuildings) {
        this.unlockedBuildings = unlockedBuildings;
        this.changed();
    }

    public void addUnlockedBuilding(BuildingType type){
        this.unlockedBuildings.add(type);
        this.changed();
    }

    public List<BuildingType> getUnlockedBuildings() {
        return unlockedBuildings;
    }
}

class UpgradeStatus{

    private boolean purchased;
    private boolean active;

    public UpgradeStatus(boolean purchased, boolean active) {
        this.purchased = purchased;
        this.active = active;
    }

    public boolean isActive() {
        return active;
    }

    public boolean isPurchased() {
        return purchased;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void setPurchased(boolean purchased) {
        this.purchased = purchased;
    }
}
