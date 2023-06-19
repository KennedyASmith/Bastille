package com.kennedysmithjava.prisoncore.entity.mines.upgrades.actions;

import com.kennedysmithjava.prisoncore.eco.Cost;
import com.kennedysmithjava.prisoncore.entity.player.MPlayer;

import java.util.List;

public class ActionAttemptTransaction extends AbstractAction{

    List<Cost> costs;

    public ActionAttemptTransaction(List<Cost> costs) {
         this.costs = costs;
    }

    @Override
    public void apply(MPlayer player) {

    }
}
