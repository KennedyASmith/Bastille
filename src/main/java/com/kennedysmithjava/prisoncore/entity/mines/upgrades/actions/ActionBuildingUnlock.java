package com.kennedysmithjava.prisoncore.entity.mines.upgrades.actions;

import com.kennedysmithjava.prisoncore.entity.player.MPlayer;
import com.kennedysmithjava.prisoncore.util.BuildingType;

public class ActionBuildingUnlock extends AbstractAction {

    BuildingType type;

    public ActionBuildingUnlock(BuildingType type) {
        this.type = type;
    }

    @Override
    public void apply(MPlayer player) {
        player.getMine().addUnlockedBuilding(type);
        player.getMine().buildBuildings();
    }
}
