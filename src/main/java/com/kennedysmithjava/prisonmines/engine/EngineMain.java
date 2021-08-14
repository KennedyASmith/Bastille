package com.kennedysmithjava.prisonmines.engine;

import com.kennedysmithjava.prisonmines.entity.Mine;
import com.massivecraft.massivecore.Engine;
import com.mcrivals.prisoncore.engine.Cooldown;
import com.mcrivals.prisoncore.engine.CooldownReason;
import com.mcrivals.prisoncore.entity.MPlayer;
import com.mcrivals.prisoncore.entity.MPlayerColl;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class EngineMain extends Engine {

    private static final EngineMain i = new EngineMain();

    public static EngineMain get() {
        return i;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerDisconnect(PlayerQuitEvent event) {

    }


    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerKick(PlayerKickEvent event) {

    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerJoin(PlayerJoinEvent event) {

    }


    /*public void leaveProtocol(Player player){
        if(Cooldown.inCooldown(player, CooldownReason.LOG_OFF)) return;
        MPlayer mPlayer = MPlayerColl.get().getByPlayer(player);
        if(mPlayer.hasMine()){
            Mine mine = mPlayer.getMine();
            mine.despawnNPCs();
        }
        Cooldown.add(player.getPlayer(), 20*10, CooldownReason.LOG_OFF);
    }

    public void joinProtocol(Player player){
        MPlayer mPlayer = MPlayerColl.get().getByPlayer(player);
        if(mPlayer == null) return;
        if(mPlayer.hasMine()){
            Mine mine = mPlayer.getMine();
            if(!mine.npcsSpawned()){
                mine.spawnNPCs();
            }
        }
    }*/

}
