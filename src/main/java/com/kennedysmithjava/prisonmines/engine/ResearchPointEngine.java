package com.kennedysmithjava.prisonmines.engine;

import com.kennedysmithjava.prisonmines.PrisonMines;
import com.kennedysmithjava.prisonmines.util.Color;
import com.massivecraft.massivecore.Engine;
import com.mcrivals.prisoncore.eco.CurrencyType;
import com.mcrivals.prisoncore.entity.MPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class ResearchPointEngine extends Engine {

    // -------------------------------------------- //
    // INSTANCE & CONSTRUCT
    // -------------------------------------------- //


    private static final ResearchPointEngine i = new ResearchPointEngine();

    public static ResearchPointEngine get() {
        return i;
    }

    private static final Map<Player, Integer> oweList = new HashMap<>();
    private static final long delay = 10*20L;
    public static Random random = new java.util.Random();
    public static int globalMultiplier = 1;

    public ResearchPointEngine() {
        BukkitScheduler scheduler = PrisonMines.get().getServer().getScheduler();
        scheduler.scheduleSyncRepeatingTask(PrisonMines.get(), () -> {
            oweList.forEach((player, count) -> {
                int total = 0;
                for (int i = 0; i < count; i++) {
                    double r = random.nextDouble();
                    double chance = 0.25;
                    if(r < chance){
                        total += globalMultiplier /* x localMultiplier */;
                    }
                }
                if(total > 0){
                    MPlayer mPlayer = MPlayer.get(player);
                    mPlayer.addBalance(CurrencyType.RESEARCH, (double) total);
                    player.sendMessage(Color.get("&7[&bServer&7] &7You have gained &b✪" + total + " &7for breaking &e" + count + " &7blocks."));
                }
            });
            oweList.clear();
        }, 20*60L, delay);
    }

    public void addBlockCount(Player player){
        int v = oweList.getOrDefault(player, 0);
        oweList.put(player, v + 1);
    }
}