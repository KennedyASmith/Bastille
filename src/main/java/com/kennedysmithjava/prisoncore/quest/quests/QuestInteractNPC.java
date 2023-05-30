package com.kennedysmithjava.prisoncore.quest.quests;

import com.kennedysmithjava.prisoncore.entity.player.MPlayer;
import com.kennedysmithjava.prisoncore.quest.Quest;
import com.kennedysmithjava.prisoncore.quest.QuestPath;
import com.kennedysmithjava.prisoncore.quest.region.QuestRegion;

public class QuestInteractNPC extends Quest {

    QuestInteractNPC(String npcID){

    }

    @Override
    public String getQuestName() {
        return null;
    }

    @Override
    public String getQuestDescription() {
        return null;
    }

    @Override
    public void continueQuest(MPlayer player, QuestPath thisPath) {

    }

    @Override
    public void onDeactivateQuest(MPlayer player) {

    }

    @Override
    public void onComplete(MPlayer player) {

    }

    @Override
    public QuestRegion getRegion() {
        return null;
    }

    @Override
    public void onEnterRegion(MPlayer player) {

    }
}
