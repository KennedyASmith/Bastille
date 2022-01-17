package com.kennedysmithjava.prisoncore.upgrades.actions;

import com.kennedysmithjava.prisoncore.entity.mines.Mine;
import com.kennedysmithjava.prisoncore.entity.player.MPlayer;

public class ActionMineDistribution extends AbstractAction {

    int distribution;
    public ActionMineDistribution(int distribution){
        this.distribution = distribution;
    }

    @Override
    public void apply(MPlayer player) {
        Mine mine = player.getMine();
        mine.setBlockDistribution(distribution);
        mine.regen();
    }

}
