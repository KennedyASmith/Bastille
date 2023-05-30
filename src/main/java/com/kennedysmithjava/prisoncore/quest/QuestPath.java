package com.kennedysmithjava.prisoncore.quest;

import com.kennedysmithjava.prisoncore.entity.player.MPlayer;
import com.massivecraft.massivecore.collections.MassiveList;
import com.massivecraft.massivecore.store.EntityInternal;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.function.Consumer;

/**
    This class represents a quest path. A quest path contains a series of quests
    and tracks the player's progress through the path.

    <p>This class extends the EntityInternal class for storability.</p>
    Author: KennedyASmith
    @see com.massivecraft.massivecore.store.EntityInternal
 */

public abstract class QuestPath extends EntityInternal<QuestPath> {

    private final List<Quest> quests;

    private int currentProgress;

    /**
     * Constructs a new QuestPath object with the specified list of quests and current progress.
     *
     * @param quests          the list of quests in the path
     * @param currentProgress the current progress of the player in the path
     */
    public QuestPath(List<Quest> quests, int currentProgress) {
        this.quests = quests;
        this.currentProgress = currentProgress;
    }

    /**
     * Returns the list of quests in the quest path.
     *
     * @return the list of quests
     */
    public List<Quest> getQuests() {
        return quests;
    }

    /**
     * Returns the rewards associated with the quest path.
     * This method should be implemented by subclasses.
     *
     * @return the list of quest rewards
     */
    public abstract List<QuestReward> getPathRewards();

    /**
     * Returns the ID of the quest path.
     * This method should be implemented by subclasses.
     *
     * @return the quest path ID
     */
    public abstract String getQuestPathID();

    /**
     * Returns the display name of the quest path.
     * This method should be implemented by subclasses.
     *
     * @return the quest path display name
     */
    public abstract String getQuestPathDisplayName();

    /**
     * Returns the icon representing the quest path.
     * This method should be implemented by subclasses.
     *
     * @return the quest path icon
     */
    public abstract ItemStack getPathIcon();

    /**
     * Returns the current progress of the player in the quest path.
     *
     * @return the current progress
     */
    public int getCurrentProgress() {
        return currentProgress;
    }

    /**
     * Sets the current progress of the player in the quest path.
     *
     * @param currentProgress the current progress to set
     */
    public void setCurrentProgress(int currentProgress) {
        this.currentProgress = currentProgress;
    }

    /**
     * Increments the current progress of the player in the quest path by 1.
     */
    public void incrementCurrentProgress(){
        setCurrentProgress(this.currentProgress + 1);
    }


    public abstract Map<Quest, List<Consumer<MPlayer>>> getQuestCompletionLogic();

    public abstract void onActivate(MPlayer player);

    public List<Quest> getQuestParts(){
        return quests;
    }
    public boolean hasQuest(Quest quest){
        return getQuestParts().contains(quest);
    }

    public boolean hasNext(){
        return currentProgress < (quests.size() - 1);
    }

    public void activate(MPlayer player){
        Quest quest = getQuestParts().get(currentProgress);
        quest.continueQuest(player, this);
        onActivate(player);
    }

    public abstract void onCompleteQuest(int questIndex);

    public void deactivate(MPlayer player){
        Quest quest = getQuestParts().get(currentProgress);
        quest.onDeactivateQuest(player);
    }

    public void completeCurrentQuest(MPlayer player){
        onCompleteQuest(currentProgress);
        if(hasNext()){
            incrementCurrentProgress();
            activate(player);
        } else {
            if(getPathRewards() != null){
                for (QuestReward pathReward : getPathRewards()) {
                    pathReward.give(player);
                }
            }

            player.getQuestProfile().finalizeQuestPathCompletion(this);
        }
    }

}
