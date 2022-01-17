package com.kennedysmithjava.prisoncore.util;

import com.kennedysmithjava.prisoncore.PrisonCore;
import com.kennedysmithjava.prisoncore.entity.mines.Mine;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class MineRegenCountdown {

    Mine mine;
    long delay;
    long counter;
    long timeLeft;
    BukkitTask task;
    boolean paused;
    boolean regenerating;

    public MineRegenCountdown(Mine mine, int when){
        this.mine = mine;
        if(mine == null) return;

        delay = mine.getRegenTimer();
        counter = 0;

        task = new BukkitRunnable(){
            @Override
            public void run() {
                if(paused) return;
                if(regenerating) return;
                if(counter == delay){
                    if(mine.isAutoRegenEnabled()){
                        mine.regen();
                    }else{
                        paused = true;
                        timeLeft = 0L;
                    }
                    counter = 0L;
                    return;
                }
                counter++;
                timeLeft = delay - counter;
            }
        }.runTaskTimer(PrisonCore.get(), when, 20L);
    }

    public void resetCounter(){
        counter = 0;
    }

    public long getTimeLeft(){
        return timeLeft;
    }

    public String getTimeLeftFormatted(){
        return TimeUtil.formatPlayTime(getTimeLeft() * 1000);
    }

    public Mine getMine() {
        return mine;
    }

    public void pause(boolean paused){
        this.paused = paused;
    }

    public boolean isPaused(){
        return paused;
    }

    public void stop() {
        this.task.cancel();
    }

    public void setMine(Mine mine) {
        this.mine = mine;
    }

    public void setPaused(boolean paused) {
        this.paused = paused;
    }

    public boolean isRegenerating() {
        return regenerating;
    }

    public void setRegenerating(boolean regenerating) {
        this.regenerating = regenerating;
    }
}