package com.kennedysmithjava.prisonmines;

import com.kennedysmithjava.prisonmines.entity.Mine;
import com.kennedysmithjava.prisonmines.util.TimeUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.Collection;

public class MineRegenCountdown {

    Mine mine;
    long delay;
    long counter;
    BukkitTask task;

    public static Collection<MineRegenCountdown> countdowns;

    public MineRegenCountdown(Mine mine, int when){
        this.mine = mine;
        if(mine == null) return;

        delay = mine.getRegenTimer();
        counter = 0;

        task = new BukkitRunnable(){
            @Override
            public void run() {
                if(counter == delay){
                    //Bukkit.broadcastMessage("Mine " + mine.getName() + " has just reset.");
                    sendPlayersToMineSpawn();
                    mine.regen();
                    counter = 0L;
                    return;
                }
                counter++;
            }
        }.runTaskTimer(PrisonMines.get(), when, 20L);
    }

    public void forceReset(){
        counter = 0;
    }

    public long getTimeLeft(){
        return delay - counter;
    }

    public String getTimeLeftFormatted(){
        return TimeUtil.formatPlayTime(counter * 1000);
    }

    public Mine getMine() {
        return mine;
    }

    public void cancel() {
        this.task.cancel();
    }

    public void sendPlayersToMineSpawn(){
        Location min = mine.getMineMin();
        Location max = mine.getMineMax();

        Bukkit.getServer().getOnlinePlayers().forEach(player ->
                {
                    if(contains(player.getLocation(), min, max)){
                        player.teleport(mine.getSpawnPointLoc());
                    }
                });
    }

    public boolean contains(Location loc, Location min, Location max) {
        int x = loc.getBlockX(); int y = loc.getBlockY(); int z = loc.getBlockZ();

        double x1 = min.getBlockX();
        double y1 = min.getBlockY();
        double z1 = min.getBlockZ();
        double x2 = max.getBlockX();
        double y2 = max.getBlockY();
        double z2 = max.getBlockZ();

        x2 = Math.max(x1, x2);
        y1 = Math.min(y1, y2);
        y2 = Math.max(y1, y2);
        z1 = Math.min(z1, z2);
        z2 = Math.max(z1, z2);

        return x >= x1 && x <= x2 && y >= y1 && y <= y2 && z >= z1 && z <= z2;
    }

    public void setMine(Mine mine) {
        this.mine = mine;
    }
}