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

public class EngineXP extends Engine {

    // -------------------------------------------- //
    // INSTANCE & CONSTRUCT
    // -------------------------------------------- //


    /**
     *
     * Using:
     * XP = 10 * Math.pow(level, 0.2) * Math.pow(level, 2.0) + Math.pow(level - 1, 2.0)
     * yanfly.moe/tools/expcalculator/
     *
     */


    private static final EngineXP i = new EngineXP();

    public static EngineXP get() {
        return i;
    }

    private static final Map<Player, Integer> oweList = new HashMap<>();
    private static final long delay = 10*20L;
    public static Random random = new Random();
    public static int globalMultiplier = 1;

    public EngineXP() {
        BukkitScheduler scheduler = PrisonCore.get().getServer().getScheduler();
        scheduler.scheduleSyncRepeatingTask(PrisonCore.get(), () -> {
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
                    player.sendMessage(Color.get("&aYou have gained &a" + total + "xp."));
                }
            });
            oweList.clear();
        }, 20*60L, delay);
    }

    public static void setPlayerXPLevel(Player player, int level, int extra) {
        /*if(level % 1 == 0){

        }

        int currentLevel = player.getLevel();

        if (level > currentLevel) {
            for (int i = currentLevel; i < level; i++) {
                requiredXP += getRequiredXP(i);
            }
        } else if (level < currentLevel) {
            for (int i = currentLevel - 1; i >= level; i--) {
                requiredXP -= getRequiredXP(i);
            }
        }

        player.setLevel((int) level);
        player.setExp(0.0f);
        player.giveExp(requiredXP);*/
    }

    public static int getRequiredVanillaXP(double level) {
        if (level >= 30) {
            return (int) (112 + (level - 30) * 9);
        } else if (level >= 15) {
            return (int) (37 + (level - 15) * 5);
        } else {
            return (int) (7 + level * 2);
        }
    }

}
