package com.kennedysmithjava.prisoncore.upgrades.actions;

import com.kennedysmithjava.prisoncore.PrisonCore;
import com.kennedysmithjava.prisoncore.engine.EngineLoadingScreen;
import com.kennedysmithjava.prisoncore.entity.player.MPlayer;
import com.kennedysmithjava.prisoncore.entity.mines.Mine;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class ActionMineSize extends AbstractAction {

    int newHeight;
    int newWidth;
    int constructionTimeTicks;

    public ActionMineSize(int newWidth, int newHeight){
        this.newHeight = newHeight;
        this.newWidth = newWidth;
        this.constructionTimeTicks = 0;
    }

    @Override
    public void apply(MPlayer player) {
        Mine mine = player.getMine();
        int oldWidth = mine.getWidth();
        int oldHeight = mine.getHeight();

        Player p = player.getPlayer();

        if(oldHeight != newHeight) mine.setHeight(newHeight);

        if(oldWidth != newWidth){
            EngineLoadingScreen.addLoadingScreen(p, mine.getMineCenter().clone().add(0, 500, 0));
            new BukkitRunnable() {
                @Override
                public void run() {
                    mine.setWidth(newWidth, () -> {
                        returnPlayerToMine(player);
                    });
                }
            }.runTaskLater(PrisonCore.get(), 20*3);
        }
    }

    public void returnPlayerToMine(MPlayer player){
        Player p = player.getPlayer();
        new BukkitRunnable() {
            @Override
            public void run() {
                if(p == null) return;
                p.teleport(player.getMine().getSpawnPoint());
                EngineLoadingScreen.removeLoadingScreen(p, 0);
            }
        }.runTaskLater(PrisonCore.get(), constructionTimeTicks);
    }

}
