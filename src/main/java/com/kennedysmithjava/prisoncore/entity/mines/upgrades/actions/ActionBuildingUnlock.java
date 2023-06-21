package com.kennedysmithjava.prisoncore.entity.mines.upgrades.actions;

import com.kennedysmithjava.prisoncore.entity.player.MPlayer;
import com.kennedysmithjava.prisoncore.quest.Quest;
import com.kennedysmithjava.prisoncore.quest.quests.QuestMineBuildBuilding;
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
        Quest quest = player.getQuestProfile().getActiveQuest();
        if(quest instanceof QuestMineBuildBuilding mbQuest){
            if(mbQuest.getBuildingType() != type) return;
            quest.completeThisQuest();
        }
    }
}
