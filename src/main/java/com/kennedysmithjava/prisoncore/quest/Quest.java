package com.kennedysmithjava.prisoncore.quest;

import com.kennedysmithjava.prisoncore.entity.player.MPlayer;
import com.kennedysmithjava.prisoncore.quest.region.QuestRegion;
import com.massivecraft.massivecore.store.EntityInternal;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

/**

 This abstract class provides a template for Quests.
 Quests have a name, description, progress, and completion status.

 <p>Author: KennedyASmith</p>
 */

public abstract class Quest extends EntityInternal<Quest> {

    private int progress;
    private final List<QuestReward> rewards;


    /**
     * Constructs a new Quest with the specified progress and completion status.
     *
     * @param progress the progress of the quest
     * @param rewards the rewards given for completing the quest part
     */
    public Quest(int progress, List<QuestReward> rewards) {
        this.progress = progress;
        this.rewards = rewards;
    }

    public Quest(List<QuestReward> rewards) {
        this.progress = 0;
        this.rewards = rewards;
    }

    public Quest() {
        this.progress = 0;
        this.rewards = new ArrayList<>();
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
    public abstract void continueQuest(MPlayer player, QuestPath thisPath);


    /**
     * Handles logic for pausing the quest for the specified player.
     *
     * @param player the player pausing the quest
     * @param thisPath the quest path associated with the quest
     */
    public abstract void onDeactivateQuest(MPlayer player);


    /**
     * Handles logic for completing the quest for the specified player.
     *
     * @param player the player pausing the quest
     * @param thisPath the quest path associated with the quest
     */
    public abstract void onComplete(MPlayer player);


    /**
     * Completes the quest for the specified player within the given quest path.
     *
     * @param player the player completing the quest
     */
    public void completeQuest(MPlayer player){
        QuestPath path = player.getQuestProfile().getActiveQuestPath();
        path.completeCurrentQuest(player);
        for (QuestReward reward : getRewards()) {
            reward.give(player);
        }
    }

    /**
     * Retrieves the progress of the quest.
     *
     * @return the progress of the quest
     */
    public int getProgress() {
        return progress;
    }

    public List<QuestReward> getRewards() {
        return rewards;
    }

    /**
     * Sets the progress of the quest.
     *
     * @param progress the progress of the quest
     */
    public void setProgress(int progress) {
        this.progress = progress;
    }


    public void incrementProgress() {
        this.progress = progress + 1;
    }

    public abstract QuestRegion getRegion();

    public boolean hasRegion() {
        return getRegion() != null;
    }

    public abstract void onEnterRegion(MPlayer player);
}

