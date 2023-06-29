package com.kennedysmithjava.prisoncore.engine;

import com.kennedysmithjava.prisoncore.PrisonCore;
import com.kennedysmithjava.prisoncore.util.CooldownReason;
import com.massivecraft.massivecore.util.MUtil;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class EngineCooldown {

    public static Map<UUID, Map<CooldownReason, Long>> cooldownList = new HashMap<>();

    public static void add(UUID uuid, int ticks, CooldownReason reason){
        long m = (long) ((ticks / 20.0) * 1000.0);
        long currentTime = System.currentTimeMillis();
        if(cooldownList.containsKey(uuid)){
            Map<CooldownReason, Long> reasons = cooldownList.get(uuid);
            reasons.put(reason, currentTime + m);
            cooldownList.put(uuid, reasons);
        }else{
            cooldownList.put(uuid, MUtil.map(reason, currentTime + m));
        }

        new BukkitRunnable() {
            @Override
            public void run() {
                cooldownList.remove(uuid);
            }
        }.runTaskLater(PrisonCore.get(), ticks);
    }

    public static boolean inCooldown(UUID uuid, CooldownReason reason){
        if(cooldownList.containsKey(uuid)){
            return cooldownList.get(uuid).containsKey(reason);
        }else{
            return false;
        }
    }

    public static int getTime(UUID uuid, CooldownReason reason){
        Map<CooldownReason, Long> reasons = cooldownList.get(uuid);
        long end = reasons.get(reason);
        long left = end - System.currentTimeMillis();
        return Math.round(Math.floorDiv(left, 1000));
    }


}

