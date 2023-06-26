package com.kennedysmithjava.prisoncore.engine;

import com.kennedysmithjava.prisoncore.PrisonCore;
import com.kennedysmithjava.prisoncore.entity.MConf;
import com.kennedysmithjava.prisoncore.npc.NPCLimboTrait;
import com.kennedysmithjava.prisoncore.util.Color;
import com.massivecraft.massivecore.Engine;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.List;

public class EngineLimbo extends Engine {

    private static final EngineLimbo i = new EngineLimbo();
    public static HashMap<Player, Long> limbo = new HashMap<>();
    private boolean active;
    private static final List<String> limboMessage = MConf.get().limboMessage;

    private long limbo_mesassage_period = 200;     // Delay for how often the limbo message should be sent

    public static EngineLimbo get() {
        return i;
    }

    EngineLimbo(){

    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {

    }

    public static HashMap<Player, Long> getPlayersInLimbo() {
        return limbo;
    }

    public void open() {
        active = true;

        new BukkitRunnable() {
            @Override
            public void run() {
                if (!active) {
                    this.cancel();
                    return;
                }
                limbo.forEach((player, timeJoined) -> {
                    if (player.getPlayer() == null) return;
                    if (NPCLimboTrait.getActiveUsers().contains(player.getPlayer())) return;

                    limboMessage.forEach(s -> {
                        player.sendMessage(Color.get(s.replaceAll("%player%", player.getName())));
                    });
                });
            }
        }.runTaskTimer(PrisonCore.get(), 0L, limbo_mesassage_period);
    }

    public void addToLimbo(Player player) {
        if (player == null) return;
        if (limbo.isEmpty() && !active) open();
        limbo.putIfAbsent(player, System.currentTimeMillis());
        Player p = player.getPlayer();
        if (p == null) return;

        //Teleport this player to the limbo spawn location
        p.teleport(MConf.get().getLimboLocation());
    }

    public void removeFromLimbo(Player player) {
        limbo.remove(player);
        if (limbo.isEmpty() && active) active = false;
    }


}
