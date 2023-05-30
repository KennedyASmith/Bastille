package com.kennedysmithjava.prisoncore.quest.quests;

import com.kennedysmithjava.prisoncore.entity.player.MPlayer;
import com.kennedysmithjava.prisoncore.quest.Quest;
import com.kennedysmithjava.prisoncore.quest.QuestPath;
import com.kennedysmithjava.prisoncore.quest.QuestReward;
import com.kennedysmithjava.prisoncore.quest.region.QuestRegion;

import java.util.List;
import java.util.Set;

public class QuestNavigateArea extends Quest {

    QuestRegion region;

    /**
     * Constructs a new Quest with the specified progress and completion status.
     *
     * @param progress       the progress of the quest
     * @param questCompleted the completion status of the quest
     */
    public QuestNavigateArea(QuestRegion region, List<QuestReward> rewards, int progress) {
        super(progress, rewards);
        this.region = region;
    }

    public QuestNavigateArea(QuestRegion region) {
        this.region = region;
    }

    @Override
    public String getQuestName() {
        return "Objective:";
    }

    @Override
    public String getQuestDescription() {
        return "Navigate to the marked area on your map!";
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
        return region;
    }

    @Override
    public void onEnterRegion(MPlayer player) {
        this.completeQuest(player);
    }

}
