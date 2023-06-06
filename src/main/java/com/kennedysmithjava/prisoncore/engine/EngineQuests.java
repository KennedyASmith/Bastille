package com.kennedysmithjava.prisoncore.engine;

import com.kennedysmithjava.prisoncore.entity.player.MPlayer;
import com.kennedysmithjava.prisoncore.entity.player.MPlayerColl;
import com.kennedysmithjava.prisoncore.event.EventNewMine;
import com.kennedysmithjava.prisoncore.event.EventNewPlayerJoin;
import com.kennedysmithjava.prisoncore.quest.quests.QuestBreakBlock;
import com.kennedysmithjava.prisoncore.quest.quests.QuestInteractBlock;
import com.massivecraft.massivecore.Engine;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;
import java.util.UUID;

public class EngineQuests extends Engine {

    private static final EngineQuests i = new EngineQuests();

    private static final HashMap<UUID, QuestInteractBlock> blockInteractWaitlist = new HashMap<>();
    private static final HashMap<UUID, QuestBreakBlock> blockBreakWaitlist = new HashMap<>();

    public static EngineQuests get() {
        return i;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onNewMine(EventNewMine event) {
        MPlayer mPlayer = event.getPlayer();
        if(mPlayer == null) return;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onNewPlayerJoin(EventNewPlayerJoin event) {

    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerJoin(PlayerJoinEvent event) {
    }


    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerQuit(PlayerQuitEvent event) {
        MPlayer player = MPlayerColl.get().getByPlayer(event.getPlayer());
        player.interruptAnyActiveQuest();
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerKick(PlayerKickEvent event) {
        MPlayer player = MPlayerColl.get().getByPlayer(event.getPlayer());
        player.interruptAnyActiveQuest();
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerInteract(PlayerInteractEvent event){
        Player player = event.getPlayer();
        // Check if the player right-clicked a block
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if(!blockInteractWaitlist.containsKey(player.getUniqueId())) return;
            Block clickedBlock = event.getClickedBlock();
            if(clickedBlock == null) return;
            QuestInteractBlock quest = blockInteractWaitlist.get(player.getUniqueId());
            if(!clickedBlock.getLocation().equals(quest.getLocation())) return;
            quest.completeThisQuest();
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onBlockBreak(BlockBreakEvent event){
        Player player = event.getPlayer();
        if(!blockBreakWaitlist.containsKey(player.getUniqueId())) return;
        QuestBreakBlock quest = blockBreakWaitlist.get(player.getUniqueId());
        if(quest.materialMatters()){
            if(quest.getMaterial() != event.getBlock().getType()) return;
            quest.blockBroken();
        }else{
            quest.blockBroken();
        }
    }

    public static void addBlockInteractListener(UUID uuid, QuestInteractBlock questInteractBlock){
        blockInteractWaitlist.put(uuid, questInteractBlock);
    }

    public static void removeBlockInteractListener(UUID uuid){
        blockInteractWaitlist.remove(uuid);
    }

    public static void addBlockBreakListener(UUID uuid, QuestBreakBlock questBreakBlock){
        blockBreakWaitlist.put(uuid, questBreakBlock);
    }

    public static void removeBlockBreakListener(UUID uuid){
        blockBreakWaitlist.remove(uuid);
    }

}
