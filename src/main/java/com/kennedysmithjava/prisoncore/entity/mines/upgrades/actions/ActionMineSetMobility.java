package com.kennedysmithjava.prisoncore.entity.mines.upgrades.actions;

import com.kennedysmithjava.prisoncore.cmd.type.TypeMobility;
import com.kennedysmithjava.prisoncore.entity.mines.Mine;
import com.kennedysmithjava.prisoncore.entity.player.MPlayer;

public class ActionMineSetMobility extends AbstractAction {

    TypeMobility mobility;

    public ActionMineSetMobility(TypeMobility mobility) {
        this.mobility = mobility;
    }

    @Override
    public void apply(MPlayer player) {
        Mine mine = player.getMine();
        mine.setSelectedMobility(mobility);
        mine.generateMobilityArea(mine.getWidth(), mine.getHeight());
    }
}
