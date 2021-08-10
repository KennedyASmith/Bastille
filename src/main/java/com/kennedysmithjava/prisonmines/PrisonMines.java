package com.kennedysmithjava.prisonmines;

import com.kennedysmithjava.prisonmines.blockhandler.BlockBreakEngine;
import com.kennedysmithjava.prisonmines.engine.EngineOffsetWand;
import com.kennedysmithjava.prisonmines.entity.*;
import com.kennedysmithjava.prisonmines.entity.blocks.DistributionConfColl;
import com.kennedysmithjava.prisonmines.entity.mine.Mine;
import com.kennedysmithjava.prisonmines.entity.mine.MineColl;
import com.kennedysmithjava.prisonmines.entity.mine.MinesConf;
import com.kennedysmithjava.prisonmines.entity.mine.MinesConfColl;
import com.kennedysmithjava.prisonmines.event.EventNewMine;
import com.kennedysmithjava.prisonmines.util.*;
import com.massivecraft.massivecore.MassivePlugin;
import com.massivecraft.massivecore.collections.MassiveList;
import com.massivecraft.massivecore.util.MUtil;
import com.mcrivals.prisoncore.entity.MPlayer;
import com.sk89q.worldedit.Vector;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

public class PrisonMines extends MassivePlugin {

    // -------------------------------------------- //
    // INSTANCE & CONSTRUCT
    // -------------------------------------------- //
    private static PrisonMines i;

    public PrisonMines() {
        PrisonMines.i = this;
    }

    public static PrisonMines get() {
        return i;
    }


    // -------------------------------------------- //
    // OVERRIDE
    // -------------------------------------------- //

    @Override
    public void onEnableInner() {

        // ACTIVATE ENGINES/COLLECTORS
        this.activateAuto();

    }

    @Override
    public List<Class<?>> getClassesActiveColls() {
        // MConf should always be activated first for all plugins. It's simply a standard. The config should have no dependencies.
        // MFlag and MPerm are both dependency free.
        return new MassiveList<>(
                MinesConfColl.class,
                DistributionConfColl.class,
                MineColl.class,
                LayoutConfColl.class
        );
    }

    @Override
    public List<Class<?>> getClassesActiveEngines() {
        List<Class<?>> ret = super.getClassesActiveEngines();
        ret.add(EngineOffsetWand.class);
        ret.add(BlockBreakEngine.class);
        return ret;
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

    @Override
    public boolean isVersionSynchronized() {
        return false;
    }

    @Override
    public ChunkGenerator getDefaultWorldGenerator(String worldName, String id) {
        return new VoidGenerator();
    }

    // -------------------------------------------- //
    // STATIC METHODS
    // -------------------------------------------- //

    @SuppressWarnings("unused")
    public static void createMine(MPlayer player, Runnable onComplete) {

        //GENERATE A RANDOM ID FOR MINE & ASSIGN TO PLAYER
        String id = UUID.randomUUID().toString();
        Mine mine = MineColl.get().create(id);
        player.setMineID(id);


        // DEFINE ESSENTIAL LAYOUTS AND LOCATIONS
        MinesWorldManager worldManager = MinesWorldManager.get();
        World world = worldManager.getWorld();
        LayoutConf layoutConf = LayoutConf.get();
        Floor floor = layoutConf.getPath(MinesConf.get().mineDefaultWallID);
        Wall wall = layoutConf.getWall(MinesConf.get().mineDefaultWallID);
        int width = MinesConf.get().mineDefaultWidth;
        int height = MinesConf.get().mineDefaultHeight;
        Offset spawnOffset = floor.getSpawn();
        Offset mineCOffset = floor.getMineCenter();
        Offset archOffset = floor.getArchitectNPC();
        Offset resOffset = floor.getResearcherNPC();
        Vector minCorner = worldManager.getUniqueLocation();
        Location origin = new Location(world, minCorner.getBlockX(), minCorner.getBlockY(), minCorner.getBlockZ());
        Location mineCenter = new Location(world, mineCOffset.getX() + origin.getX(), mineCOffset.getY() + origin.getY(), mineCOffset.getZ() + origin.getZ());
        Location spawn = new Location(origin.getWorld(), origin.getBlockX() + spawnOffset.getX(), origin.getBlockY() + spawnOffset.getY(), origin.getBlockZ() + spawnOffset.getZ(), spawnOffset.getYaw(), spawnOffset.getPitch());
        Location maxMine = mineCenter.clone().add(-(width - 2), 0, -(width - 2));
        Location minMine = maxMine.clone().add(width - 1, -(height - 1), width - 1);
        Location architectLocation = new Location(world, origin.getBlockX() + archOffset.getX(), origin.getBlockY() + archOffset.getY(), origin.getBlockZ() + archOffset.getZ(), archOffset.getYaw(), archOffset.getPitch());
        Location researcherLocation = new Location(world, origin.getBlockX() + resOffset.getX(), origin.getBlockY() + resOffset.getY(), origin.getBlockZ() + resOffset.getZ(), resOffset.getYaw(), resOffset.getPitch());

        // SAVE ESSENTIAL MINE VALUES
        mine.setName(MinesConf.get().mineDefaultName.replaceAll("%player%", player.getPlayer().getName()));
        mine.setMineMin(minMine);
        mine.setMineMax(maxMine);
        mine.setSpawnPoint(spawn);
        mine.setArchitectLocation(architectLocation);
        mine.setArchitectUUID(UUID.randomUUID().toString());
        mine.setResearcherUUID(UUID.randomUUID().toString());
        mine.setResearcherLocation(researcherLocation);
        mine.setOrigin(origin);
        mine.setMineCenter(mineCenter);
        mine.setRegenTimer(MinesConf.get().defaultResetTimer);
        mine.setPathIDVar(MinesConf.get().mineDefaultPathID);
        mine.setWallIDVar(MinesConf.get().mineDefaultWallID);
        mine.setWidthVar(MinesConf.get().mineDefaultWidth);
        mine.setHeightVar(MinesConf.get().mineDefaultHeight);
        mine.setUnlockedDistributions(MUtil.list(1));
        mine.setBlockDistribution(1);

        //PASTE SCHEMATICS
        FAWETracker floorT = MiscUtil.pasteSchematic(floor.getSchematic(mine.getWidth()), minCorner);
        FAWETracker wallT = MiscUtil.pasteSchematic(wall.getSchematic(), minCorner);

        //GENERATE UNBREAKABLE BORDER & GENERATE MINE BLOCKS
        mine.generateBorder(mine.getWidth(), mine.getWidth(), MinesConf.get().minesBorderMaterial);

        //ENSURE THAT BOTH PASTES ARE FINISHED
        new BukkitRunnable() {
            @Override
            public void run() {
                if (floorT.isDone() && wallT.isDone()) {
                    onComplete.run();
                    mine.createRegenCountdown();
                    mine.regen();
                    Bukkit.getServer().getPluginManager().callEvent(new EventNewMine(player, mine));
                    this.cancel();
                }
            }
        }.runTaskTimer(PrisonMines.get(), 0L, 1L);
    }

}
