package com.kennedysmithjava.prisonmines.upgrades;

import com.kennedysmithjava.prisonmines.entity.Mine;
import com.mcrivals.prisoncore.entity.MPlayer;

public class MainMineUpgrade extends AbstractUpgrade{

    int level;

    MainMineUpgrade(int level){
        this.level = level;
    }

    @Override
    public void apply(MPlayer player) {
        Mine.get(player.getMineID()).setLevel(level);
    }
}
