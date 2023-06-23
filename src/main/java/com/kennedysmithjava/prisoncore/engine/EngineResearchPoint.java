package com.kennedysmithjava.prisoncore.engine;

import com.kennedysmithjava.prisoncore.PrisonCore;
import com.kennedysmithjava.prisoncore.eco.CurrencyType;
import com.kennedysmithjava.prisoncore.entity.player.MPlayer;
import com.kennedysmithjava.prisoncore.util.Color;
import com.massivecraft.massivecore.Engine;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class EngineResearchPoint extends Engine {

    // -------------------------------------------- //
    // INSTANCE & CONSTRUCT
    // -------------------------------------------- //


    private static final EngineResearchPoint i = new EngineResearchPoint();

    public static EngineResearchPoint get() {
        return i;
    }

    private static final Map<Player, Integer> oweList = new HashMap<>();
    private static final long delay = 10*20L;
    public static Random random = ThreadLocalRandom.current();
    public static int globalMultiplier = 1;

    public EngineResearchPoint() {
        BukkitScheduler scheduler = PrisonCore.get().getServer().getScheduler();
        scheduler.scheduleSyncRepeatingTask(PrisonCore.get(), () -> {
            oweList.forEach((player, count) -> {
                int total = 0;
                for (int i = 0; i < count; i++) {
                    double pRandom = random.nextDouble();
                    double pointChance = 0.25;
                    if(pRandom < pointChance){
                        total += globalMultiplier /* x localMultiplier */;
                    }

                }
                if(total > 0){
                    MPlayer mPlayer = MPlayer.get(player);
                    mPlayer.addBalance(CurrencyType.RESEARCH, (double) total);
                    player.sendMessage(Color.get("&7[&b&l⛏&7] &7You have gained &b✪" + total + " &7for breaking &e" + count + " &7blocks."));
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
