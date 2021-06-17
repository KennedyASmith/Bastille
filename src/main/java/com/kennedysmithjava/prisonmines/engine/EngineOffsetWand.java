package com.kennedysmithjava.prisonmines.engine;

import com.boydti.fawe.FaweAPI;
import com.kennedysmithjava.prisonmines.MinesWorldManager;
import com.kennedysmithjava.prisonmines.PrisonMines;
import com.kennedysmithjava.prisonmines.cmd.CmdOffset;
import com.kennedysmithjava.prisonmines.entity.LayoutConf;
import com.kennedysmithjava.prisonmines.entity.MConf;
import com.kennedysmithjava.prisonmines.entity.Mine;
import com.kennedysmithjava.prisonmines.entity.MineColl;
import com.kennedysmithjava.prisonmines.event.EventNewMine;
import com.kennedysmithjava.prisonmines.event.EventPrisonPlayerJoin;
import com.kennedysmithjava.prisonmines.traits.ArchitectTrait;
import com.kennedysmithjava.prisonmines.util.LocSerializer;
import com.massivecraft.massivecore.Engine;
import com.massivecraft.massivecore.store.MStore;
import com.mcrivals.prisoncore.entity.MPlayer;
import com.mcrivals.prisoncore.entity.MPlayerColl;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.util.UUID;

public class EngineOffsetWand extends Engine {

    private static final EngineOffsetWand i = new EngineOffsetWand();

    public static EngineOffsetWand get() {
        return i;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onBlockClick(PlayerInteractEvent event) {

        Player player = event.getPlayer();
        if(player.getItemInHand().getType() != Material.STONE_AXE) return;
        if(!player.hasPermission("offsetWand")) return;

        if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Location loc1 = CmdOffset.posOneCache.get(player.getUniqueId().toString());
            if(loc1 == null) {
                player.sendMessage("You must select a first position with /offset pos1");
                return;
            }

            CmdOffset.sendOffsetInfo(player, loc1, event.getClickedBlock().getLocation());

        }
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        if(player.getItemInHand().getType() != Material.STONE_AXE) return;
        if(!player.hasPermission("offsetWand")) return;

        event.setCancelled(true);

        CmdOffset.addToPosOneCache(player.getUniqueId().toString(), event.getBlock().getLocation());
        player.sendMessage("Position one saved.");

    }

    }
