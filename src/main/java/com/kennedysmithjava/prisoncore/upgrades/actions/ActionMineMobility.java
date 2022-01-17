package com.kennedysmithjava.prisoncore.upgrades.actions;

import com.kennedysmithjava.prisoncore.cmd.type.TypeMobility;
import com.kennedysmithjava.prisoncore.entity.mines.Mine;
import com.kennedysmithjava.prisoncore.entity.player.MPlayer;

public class ActionMineMobility extends AbstractAction {

    TypeMobility mobility;

    public ActionMineMobility(TypeMobility mobility) {
        this.mobility = mobility;
    }

    @Override
    public void apply(MPlayer player) {
        Mine mine = player.getMine();
        mine.setSelectedMobility(mobility);
        mine.generateMobilityArea();
    }
}
