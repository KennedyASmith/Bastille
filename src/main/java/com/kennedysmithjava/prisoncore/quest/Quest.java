package com.kennedysmithjava.prisoncore.quest;

import com.kennedysmithjava.prisoncore.entity.player.MPlayer;
import com.kennedysmithjava.prisoncore.entity.player.QuestProfile;
import com.kennedysmithjava.prisoncore.quest.region.QuestRegion;

import java.util.ArrayList;
import java.util.List;

/**

 This abstract class provides a template for Quests.
 Quests have a name, description, progress, and completion status.

 <p>Author: KennedyASmith</p>
 */

public abstract class Quest {

    public final List<QuestReward> rewards;
    public final MPlayer player;

    /**
     * Constructs a new Quest with the specified progress and completion status.
     *
     * @param rewards the rewards given for completing the quest part
     */
    public Quest(MPlayer player, List<QuestReward> rewards) {
        this.rewards = rewards;
        this.player = player;
    }

    public Quest(MPlayer player) {
        this.rewards = new ArrayList<>();
        this.player = player;
    }

    /**
     * Retrieves the name of the quest.
     *
     * @return the name of the quest
     */
    public abstract String getQuestName();

    /**
     * Retrieves the description of the quest.
     *
     * @return the description of the quest
     */
    public abstract String getQuestDescription();

    /**
     * Continues the quest for the specified player within the given quest path.
     *
     * @param player the player continuing the quest
     * @param thisPath the quest path associated with the quest
     */
    public abstract void continueQuest(int questProgress, QuestPath thisPath);


    /**
     * Handles logic for pausing the quest for the specified player.
     *
     * @param player the player pausing the quest
     */
    public abstract void onDeactivateQuest(int questProgress);


    /**
     * Handles logic for completing the quest for the specified player.
     * Runs before rewards are given.
     *
     * @param player the player pausing the quest
     */
    public abstract void onComplete();


    /**
     * Completes the quest for the specified player within the given quest path.
     *
     * @param player the player completing the quest
     */
    public void completeThisQuest(){
        onComplete();
        QuestPath path = player.getQuestProfile().getActiveQuestPath();
        path.innerCompleteQuest(player, player.getQuestProfile().activeQuestPathProgress);
        for (QuestReward reward : getRewards()) {
            reward.give(player);
        }
    }

    public List<QuestReward> getRewards() {
        return rewards;
    }

    public abstract QuestRegion getRegion(int questProgress);

    public boolean hasRegion(int questProgress) {
        return getRegion(questProgress) != null;
    }

    public abstract void onEnterRegion();

    public void incrementProgress(){
        QuestProfile profile = player.getQuestProfile();
        profile.incrementCurrentQuestProgress();
    }

    public int getProgress(){
        QuestProfile profile = player.getQuestProfile();
        return profile.getActiveQuestProgress();
    }

}

