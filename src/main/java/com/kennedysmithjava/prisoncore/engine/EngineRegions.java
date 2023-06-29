package com.kennedysmithjava.prisoncore.engine;

import com.kennedysmithjava.prisoncore.PrisonCore;
import com.kennedysmithjava.prisoncore.entity.player.MPlayer;
import com.kennedysmithjava.prisoncore.event.PlayerEnterRegionEvent;
import com.kennedysmithjava.prisoncore.maps.MapUtil;
import com.kennedysmithjava.prisoncore.maps.PrisonMapRenderer;
import com.kennedysmithjava.prisoncore.quest.Quest;
import com.kennedysmithjava.prisoncore.regions.Region;
import com.kennedysmithjava.prisoncore.regions.RegionType;
import com.kennedysmithjava.prisoncore.regions.RegionWorld;
import com.kennedysmithjava.prisoncore.regions.RegionWrapper;
import com.kennedysmithjava.prisoncore.util.CooldownReason;
import com.kennedysmithjava.prisoncore.util.Pair;
import com.massivecraft.massivecore.Engine;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class EngineRegions extends Engine {

    // -------------------------------------------- //
    // INSTANCE & CONSTRUCT
    // -------------------------------------------- //
    private static final EngineRegions i = new EngineRegions();
    public static EngineRegions get() {
        return i;
    }

    /* A map of players who have active quest regions */
    private final Map<UUID, Region> activeQuestRegions = new HashMap<>();

    /* A set of players known to be standing in their quest region */
    private final Set<UUID> inTheirQuestRegion = new HashSet<>();

    /* The last known regions of each player */
    private static final HashMap<UUID, RegionWrapper> playerLocations = new HashMap<>();

    public static final HashMap<UUID, String> lastKnownWorldName = new HashMap<>();
    private static final HashMap<String, RegionWrapper> activeRegions = new HashMap<>();

    public static final Set<Player> recentlyRegionAlteredPlayers = new HashSet<>();
    EngineRegions(){
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player player : recentlyRegionAlteredPlayers) {
                    if(player == null) continue;
                    boolean updated = updateKnownRegion(player);
                    if(updated){
                        UUID uuid = player.getUniqueId();
                        if(!PrisonMapRenderer.shouldRenderPlayers.contains(uuid)) continue;
                        MapUtil.refreshMapRegion(player, playerLocations.get(uuid).name());
                    }
                }
                //Empty the list for next iteration
                recentlyRegionAlteredPlayers.clear();
            }
        }.runTaskTimer(PrisonCore.get(), 20, 10);

        // Maybe set up a system for region priority to handle overlapping regions?
        /*new BukkitRunnable() {
            @Override
            public void run() {
                //Empty the cache of player locations for next iteration every 4 seconds
                playerLocations.clear();
            }
        }.runTaskTimerAsynchronously(PrisonCore.get(), 20, 60);*/
    }


    /**
     * Handles all situations that require detecting player location changes,
     * including maps
     */
    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerMove(PlayerMoveEvent event){
        Location getTo = event.getTo();
        if(getTo  == null) return;
        Location getFrom = event.getFrom();
        if (getFrom.getBlockX() == getTo.getBlockX() && getFrom.getBlockZ() == getTo.getBlockZ())
            return; //The player hasn't moved an entire block, end here
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();

        //Handle map updating

        if(PrisonMapRenderer.shouldRenderPlayers.contains(uuid) && !EngineCooldown.inCooldown(uuid, CooldownReason.REGION_UPDATE)){
            PrisonMapRenderer.shouldUpdateRenderPlayers.add(uuid);
            EngineCooldown.add(uuid, 20, CooldownReason.REGION_UPDATE);
        }

        recentlyRegionAlteredPlayers.add(player); //This may need to change locations in future

        Region region = activeQuestRegions.get(uuid);
        if(region == null) return;
        if(region.has(getTo)) {
            if(inTheirQuestRegion.contains(uuid)) return;
            MPlayer p = MPlayer.get(player);
            Quest q = p.getQuestProfile().getActiveQuest();
            if(q == null){
                inTheirQuestRegion.remove(uuid);
                return;
            }
            q.onEnterRegion();
            PlayerEnterRegionEvent questRegionEvent = new PlayerEnterRegionEvent(player, RegionType.QUEST, "Quest Area");
            Bukkit.getServer().getPluginManager().callEvent(questRegionEvent);
        }else {
            inTheirQuestRegion.remove(uuid);
        }

    }

    @EventHandler
    public void onPlayerTeleport(PlayerTeleportEvent event){
        Player player = event.getPlayer();
        MapUtil.removeMaps(player);
        recentlyRegionAlteredPlayers.add(player);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event){
        Player player = event.getPlayer();
        lastKnownWorldName.put(player.getUniqueId(), player.getWorld().getName());
        recentlyRegionAlteredPlayers.add(event.getPlayer());
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event){
        Player player = event.getPlayer();
        lastKnownWorldName.remove(player.getUniqueId());
        PrisonMapRenderer.shouldRenderPlayers.remove(player.getUniqueId());
    }

    public void addToRegionTracker(UUID player, Region region){
        activeQuestRegions.put(player, region);
    }

    public void removeFromRegionTracker(UUID player){
        activeQuestRegions.remove(player);
        inTheirQuestRegion.remove(player);
    }

    public static Pair<String, RegionWrapper> getRegion(Player player, Location location){
        int x = location.getBlockX();
        int z = location.getBlockZ();

        for (String s : activeRegions.keySet()) {
            RegionWrapper regionWrapper = activeRegions.get(s);
            Region region = regionWrapper.region();
            if(!region.has(x, z)) continue;
            return new Pair<>(s, regionWrapper);
        }

        String worldName = player.getWorld().getName();
        RegionWrapper wrapper = new RegionWrapper(new RegionWorld(player.getUniqueId(), worldName), RegionType.WORLD, worldName);
        return new Pair<>(worldName, wrapper);
    }

    /**
     * Returns true if updated
     */
    public static boolean updateKnownRegion(Player player){
        UUID uuid = player.getUniqueId();
        //Could skip this
        //Could skip this
        if(playerLocations.containsKey(uuid)){
            RegionWrapper knownRegion = playerLocations.get(uuid);
            if(knownRegion.type() == RegionType.WORLD // If it's a world, we want to invalidate always
                    || !knownRegion.region().has(player.getLocation())){ // If it's a regular region & player not inside, invalidate
                Bukkit.broadcastMessage("Player is in world or not in known region: " + knownRegion.type());
                playerLocations.remove(uuid); //Invalidate and try again recursively
                return updateKnownRegion(player);
            }else {
                Bukkit.broadcastMessage("Player is in their region: " + knownRegion.type() + " " + knownRegion.name());
                return false;
            }
        }

        Pair<String, RegionWrapper> pair = getRegion(player, player.getLocation());
        Bukkit.broadcastMessage("Player entered new region: " + pair.getLeft());
        playerLocations.put(uuid, pair.getRight()); //Update the value;
        return true;
    }

    public static boolean inWorld(UUID uuid, String worldName){
        String lastKnownWorld = lastKnownWorldName.get(uuid);
        return lastKnownWorld != null && lastKnownWorld.equals(worldName);
    }

    public static void addActiveRegion(String name, RegionWrapper wrapper){
        activeRegions.put(name, wrapper);
    }
    public static void removeActiveRegion(String name){
        activeRegions.remove(name);
    }

    public static HashMap<String, RegionWrapper> getActiveRegions() {
        return activeRegions;
    }
}
