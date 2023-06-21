package com.kennedysmithjava.prisoncore.quest.quests;

import com.kennedysmithjava.prisoncore.entity.player.MPlayer;
import com.kennedysmithjava.prisoncore.quest.Quest;
import com.kennedysmithjava.prisoncore.quest.QuestPath;
import com.kennedysmithjava.prisoncore.quest.region.QuestExactRegion;
import com.kennedysmithjava.prisoncore.quest.region.QuestRegion;
import com.kennedysmithjava.prisoncore.util.BuildingType;

public class QuestMineBuildBuilding extends Quest {

    private final BuildingType buildingType;

    private final QuestExactRegion warrenLocation;

    public QuestMineBuildBuilding(MPlayer player, BuildingType buildingType){
        super(player);
        this.buildingType = buildingType;
        this.warrenLocation = new QuestExactRegion(player.getMine().getResearcherLocation());
    }

    @Override
    public String getQuestName() {
        return "&6&lBuild " + buildingType.getName();
    }

    @Override
    public String getQuestDescription() {
        return "&7Speak with &eWarren &7to build a " + buildingType.getName() + "!";
    }

    @Override
    public void continueQuest(int progress, QuestPath path) {
        if(player.getMine().hasBuilding(BuildingType.PORTAL)){
            this.completeThisQuest();
        }
        /*
            ALL LOGIC FOR THIS QUEST IS HANDLED BY: ActionBuildingUnlock.class
        */
    }

    @Override
    public void onDeactivateQuest(int questProgress) {

    }

    @Override
    public void onComplete() {

    }

    @Override
    public QuestRegion getRegion(int progress) {
        return warrenLocation;
    }

    @Override
    public void onEnterRegion() {

    }

    @Override
    public String getShortProgressString() {
        return "&aBuild a " + buildingType.getName()  + " &a(0/1)";
    }

    public BuildingType getBuildingType() {
        return buildingType;
    }
}

