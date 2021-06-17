package com.kennedysmithjava.prisonmines.engine;

import com.boydti.fawe.FaweAPI;
import com.kennedysmithjava.prisonmines.MinesWorldManager;
import com.kennedysmithjava.prisonmines.PrisonMines;
import com.kennedysmithjava.prisonmines.entity.*;
import com.kennedysmithjava.prisonmines.event.EventNewMine;
import com.kennedysmithjava.prisonmines.event.EventPrisonPlayerJoin;
import com.kennedysmithjava.prisonmines.traits.ArchitectTrait;
import com.kennedysmithjava.prisonmines.traits.ResearcherTrait;
import com.kennedysmithjava.prisonmines.util.Offset;
import com.massivecraft.massivecore.Engine;
import com.massivecraft.massivecore.store.MStore;
import com.mcrivals.prisoncore.entity.MPlayer;
import com.mcrivals.prisoncore.entity.MPlayerColl;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.util.UUID;

public class EnginePersonalMines extends Engine {

    private static final EnginePersonalMines i = new EnginePersonalMines();

    public static EnginePersonalMines get() {
        return i;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        MPlayer mPlayer = MPlayerColl.get().getByPlayer(player);
        if(mPlayer == null) return;

        Bukkit.broadcastMessage("Gotten MineID: " + mPlayer.getMineID());
        if (mPlayer.getMineID().equals("none")) {
            //A mine doesn't exist for this player, create it
            createMine(mPlayer);
            Bukkit.getServer().getPluginManager().callEvent(new EventNewMine(mPlayer, Mine.get(mPlayer.getMineID())));
        }

        Bukkit.getServer().getPluginManager().callEvent(new EventPrisonPlayerJoin(mPlayer));
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPrisonPlayerJoin(EventPrisonPlayerJoin event){
        Mine mine = event.getMine();

        //Set regeneration countdown to active if one doesn't already exist
        if(!MineColl.get().countdownPresent(mine)) MineColl.get().addCountdown(mine);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        MPlayer mPlayer = MPlayer.get(player);
        if (mPlayer.getMineID() == null) return;
        new BukkitRunnable() {
            @Override
            public void run() {
                if(player == null){
                    MineColl.get().removeCountdown(MineColl.get().get(mPlayer.getMineID()));
                    Mine mine = Mine.get(mPlayer.getMineID());
                    NPC npc = CitizensAPI.getNPCRegistry().getByUniqueId(UUID.fromString(mine.getArchitectUUID()));
                    if(npc != null){
                        npc.destroy();
                    }
                }
            }
        }.runTaskLater(PrisonMines.get(), 300);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerKick(PlayerKickEvent event) {
        Player player = event.getPlayer();
        MPlayer mPlayer = MPlayer.get(player);
        if (mPlayer.getMineID().equals("none")) return;
        new BukkitRunnable() {
            @Override
            public void run() {
                if(player == null){
                    MineColl.get().removeCountdown(MineColl.get().get(mPlayer.getMineID()));
                    Mine mine = Mine.get(mPlayer.getMineID());
                    NPC npc = CitizensAPI.getNPCRegistry().getByUniqueId(UUID.fromString(mine.getArchitectUUID()));
                    if(npc != null){
                        npc.destroy();
                    }
                }
            }
        }.runTaskLater(PrisonMines.get(), 300);
    }

    private void createMine(MPlayer player) {

        // Misc
        int width = 3;
        int height = 3;

        MinesWorldManager worldManager = MinesWorldManager.get();
        Vector minCorner = worldManager.getUniqueLocation();
        Location origin = new Location(worldManager.getWorld(), minCorner.getBlockX(), minCorner.getBlockY(), minCorner.getBlockZ());

        // Load layouts
        LayoutConf layoutConf = LayoutConf.get();
        Floor floor = layoutConf.getPath(layoutConf.defaultPathSchem);
        Wall wall = layoutConf.getWall(layoutConf.defaultWallSchem);

        // Set up primary locations
        Vector maxCorner = minCorner.add(100, 256 - minCorner.getY(), 100);
        Vector middle = new Vector(((maxCorner.getX() + minCorner.getX())/2), ((maxCorner.getY() + minCorner.getY())/2),((maxCorner.getZ() + minCorner.getZ())/2));
        Location mineCenter = new Location(worldManager.getWorld(), origin.getBlockX() + floor.getMineCenter().getX(), origin.getBlockY() + floor.getMineCenter().getY(), origin.getBlockZ() + floor.getMineCenter().getZ());

        // Paste base schematics
        pasteSchematic(floor.getSchematic(1), minCorner);
        pasteSchematic(wall.getSchematic(), minCorner);

        // Set up spawn location, teleport player there
        Offset spawnOffset = floor.getSpawn();
        Location spawn = new Location(origin.getWorld(), origin.getBlockX() + spawnOffset.getX(), origin.getBlockY() + spawnOffset.getY(), origin.getBlockZ() + spawnOffset.getZ(), spawnOffset.getYaw(), spawnOffset.getPitch());
        player.getPlayer().teleport(spawn.add(0.5, 0, 0.5));

        /*org.bukkit.util.Vector dir = new Location(worldManager.getWorld(), middle.getX(), 104, middle.getZ()).subtract(player.getPlayer().getEyeLocation()).toVector();
        Location loc = player.getPlayer().getLocation().setDirection(dir);
        player.getPlayer().teleport(loc);*/

        //Setting up Mine entity
        Location maxMine = mineCenter.add(-(width - 2), 0, -(width - 2));
        Location minMine = maxMine.clone().add(width-1, -(height - 1), width-1);

        String id = player.getPlayer().getUniqueId().toString();
        Mine mine = MineColl.get().create(id);
        mine.setName(player.getPlayer().getDisplayName() + "'s Mine");
        mine.setRegenTimer(MConf.get().getDefaultResetTimer());
        mine.setMin(minMine);
        mine.setMax(maxMine);
        mine.setBlockDistribution(DistributionConf.get().distribution.get(1).getRates());
        mine.setSpawnPoint(spawn);
        mine.setArchitectUUID(MStore.createId());
        mine.setOrigin(origin);
        mine.setPathLVL(1);
        mine.setPathID(1);
        mine.setWallID(1);

        player.setMineID(id);
        mine.regen();

        //////////////////////////////////////////
        //SETTING UP THE NPCS

        Location aL = origin.add(76, 50, 50);
        org.bukkit.util.Vector direc = new Location(worldManager.getWorld(), middle.getX(), 104, middle.getZ()).subtract(aL).toVector();
        Location architectLoc = aL.clone().setDirection(direc);

        mine.setArchitectLocation(architectLoc);

        NPC npc = spawnResearcher(player, UUID.fromString(mine.getArchitectUUID()), architectLoc);
        NPC arch = spawnArchitect(player, UUID.randomUUID(), architectLoc.add(-1, 0,0));
    }

    private void pasteSchematic(String filepath, Vector location) {
        try {
            Bukkit.broadcastMessage("FilePath: " + filepath);
            MinesWorldManager worldManager = MinesWorldManager.get();
            FaweAPI.load(new File(PrisonMines.get().getDataFolder() + File.separator + filepath)).paste(new BukkitWorld(worldManager.getWorld()), location, false);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    private NPC spawnResearcher(MPlayer player, UUID uuid, Location where){
        NPC npc = CitizensAPI.getNPCRegistry().createNPC(EntityType.VILLAGER, uuid, 0, "Architect");
        npc.setProtected(true);
        npc.addTrait(new ArchitectTrait());
        npc.spawn(where);
        return npc;
    }

    private NPC spawnArchitect(MPlayer player, UUID uuid, Location where){
        NPC npc = CitizensAPI.getNPCRegistry().createNPC(EntityType.VILLAGER, uuid, 0, "Architect");
        npc.setProtected(true);
        npc.addTrait(new ResearcherTrait());
        npc.spawn(where);
        return npc;
    }
}
