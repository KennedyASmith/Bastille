package com.kennedysmithjava.prisoncore.upgrades.actions;

import com.kennedysmithjava.prisoncore.entity.player.MPlayer;

public class ActionGiveItem extends AbstractAction {

    int distribution;

    public ActionGiveItem(int distribution){
        this.distribution = distribution;
    }

    @Override
    public void apply(MPlayer player) {
        player.getMine().setBlockDistribution(distribution);
    }
}
