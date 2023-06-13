package com.kennedysmithjava.prisoncore.engine;

import com.kennedysmithjava.prisoncore.util.CooldownReason;
import com.kennedysmithjava.prisoncore.PrisonCore;
import com.massivecraft.massivecore.util.MUtil;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;

public class EngineCooldown {

    public static Map<String, Map<CooldownReason, Long>> cooldownList = new HashMap<>();

    public static void add(Player player, int ticks, CooldownReason reason){
        long m = (long) ((ticks / 20.0) * 1000.0);
        long currentTime = System.currentTimeMillis();
        String uuid = player.getUniqueId().toString();
        if(cooldownList.containsKey(uuid)){
            Map<CooldownReason, Long> reasons = cooldownList.get(uuid);
            reasons.put(reason, currentTime + m);
            cooldownList.put(uuid, reasons);
        }else{
            cooldownList.put(player.getUniqueId().toString(), MUtil.map(reason, currentTime + m));
        }

        new BukkitRunnable() {
            @Override
            public void run() {
                cooldownList.remove(player.getUniqueId().toString());
            }
        }.runTaskLater(PrisonCore.get(), ticks);
    }

    public static boolean inCooldown(Player player, CooldownReason reason){
        String uuid = player.getUniqueId().toString();
        if(cooldownList.containsKey(uuid)){
            return cooldownList.get(uuid).containsKey(reason);
        }else{
            return false;
        }
    }

    public static int getTime(Player player, CooldownReason reason){
        Map<CooldownReason, Long> reasons = cooldownList.get(player.getUniqueId().toString());
        long end = reasons.get(reason);
        long left = end - System.currentTimeMillis();
        return Math.round(Math.floorDiv(left, 1000));
    }


}

