package com.kennedysmithjava.prisoncore.engine;

import com.kennedysmithjava.prisoncore.entity.player.MPlayer;
import com.kennedysmithjava.prisoncore.entity.mines.Mine;
import com.kennedysmithjava.prisoncore.entity.mines.MineColl;
import com.kennedysmithjava.prisoncore.maps.MapUtil;
import com.kennedysmithjava.prisoncore.util.CooldownReason;
import com.kennedysmithjava.prisoncore.quest.QuestPath;
import com.massivecraft.massivecore.Engine;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class EnginePlayers extends Engine {

    private static final EnginePlayers i = new EnginePlayers();

    public static EnginePlayers get() {
        return i;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerDisconnect(PlayerQuitEvent event) {
        leaveProtocol(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerKick(PlayerKickEvent event) {
        leaveProtocol(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerJoin(PlayerJoinEvent event) {
        MPlayer mPlayer = MPlayer.get(event.getPlayer());
        if(mPlayer == null) return;
        joinProtocol(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerLeverClick(PlayerInteractEvent event) {
        if(!event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) return;
        Block block = event.getClickedBlock();
        if(block == null) return;
        if(!block.getType().equals(Material.LEVER)) return;
        Mine mine = MineColl.get().getByLocation(block);
        if(mine != null) {
            if(EngineCooldown.inCooldown(event.getPlayer(), CooldownReason.REGEN)) return;
            if(mine.tryManualRegen()) EngineCooldown.add(event.getPlayer(), 20*30, CooldownReason.REGEN);
        }
    }


    private void leaveProtocol(Player player){
        MPlayer mPlayer = MPlayer.get(player);
        if(mPlayer.hasMine()){
            if(!mPlayer.inCooldown(CooldownReason.LOG_OFF)) {
                EngineCooldown.add(player, 20 * 15, CooldownReason.LOG_OFF);
                Mine mine = mPlayer.getMine();

                mine.despawnNPCs();
                mine.removeCountdown();
                MineColl.get().removeMineFromCache(mine);
            }
            mPlayer.getQuestProfile().getActiveQuestPath().deactivate(mPlayer);
        }
    }

    private void joinProtocol(Player player){
        MPlayer mPlayer = MPlayer.get(player);
        if(mPlayer.hasMine()){
            Mine mine = mPlayer.getMine();
            MineColl.get().addMineToCache(mine);
            mine.createRegenCountdown();
            if(!mine.npcsSpawned()) mine.spawnNPCs();
            if(!mine.hologramExists()) mine.createRegenHologram();
            int progress = mPlayer.getQuestProfile().activeQuestPathProgress;

            // Get the quest path & assert it is non-null
            QuestPath questPath = mPlayer.getQuestProfile().getActiveQuestPath();

            // If a questPath exists for mplayer, then activate
            if(questPath != null) {
                questPath.activateCurrentQuest(mPlayer, progress);
            }

        } else{
            EngineLimbo.get().addToLimbo(mPlayer.getPlayer());
        }
        MapUtil.replaceAnyMaps(player, player.getLocation());
    }
}
