package com.kennedysmithjava.prisoncore.entity.mines.upgrades.actions;

import com.kennedysmithjava.prisoncore.entity.player.MPlayer;

public class ActionMineLevelIncrement extends AbstractAction {
    @Override
    public void apply(MPlayer player) {
        player.getMine().incrementLevel();
    }
}
