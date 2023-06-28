package com.kennedysmithjava.prisoncore.quest;

import com.kennedysmithjava.prisoncore.engine.EngineRegions;
import com.kennedysmithjava.prisoncore.entity.player.MPlayer;
import com.kennedysmithjava.prisoncore.entity.player.QuestProfile;
import com.kennedysmithjava.prisoncore.maps.PrisonMapRenderer;
import com.kennedysmithjava.prisoncore.regions.Region;
import org.bukkit.inventory.ItemStack;

import java.util.*;

/**
    This class represents a quest path. A quest path contains a series of quests
    and tracks the player's progress through the path.

    Author: KennedyASmith
    @see com.kennedysmithjava.prisoncore.engine.EngineQuests
 */

public abstract class QuestPath {

    /**
     * Returns the initialized list of quests in the quest path.
     *
     * @return the list of quests
     */
    public abstract List<Quest> getInitializedQuests(MPlayer player);

    /**
     * Returns the rewards associated with the quest path.
     * This method should be implemented by subclasses.
     *
     * @return the list of quest rewards
     */
    public abstract List<QuestReward> getPathRewards();

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

    public abstract void onActivate(MPlayer player);

    public boolean hasNext(MPlayer player, List<Quest> questList, int currentProgress){
        if(questList == null) questList = this.getInitializedQuests(player);
        return currentProgress < (questList.size() - 1);
    }

    public void activateCurrentQuest(MPlayer player, int currentPathProgress){
        QuestProfile profile = player.getQuestProfile();
        List<Quest> questList = profile.getPathQuestList(player);
        Quest quest = questList.get(currentPathProgress);
        int questProgress = player.getQuestProfile().getActiveQuestProgress();
        quest.continueQuest(questProgress, this);
        player.getQuestProfile().setActiveQuest(quest);
        if(quest.hasRegion(questProgress)){
            Region region = quest.getRegion(questProgress);
            EngineRegions.get().addToRegionTracker(player.getUuid(), region);
            if(PrisonMapRenderer.mapRenderers.containsKey(player.getUuid())){
                PrisonMapRenderer.reRenderQuest.add(player.getUuid());
            }
        }
        onActivate(player);
    }
    public abstract void onCompleteQuest(int currentPathProgress, MPlayer player);

    public void deactivate(MPlayer player){
        QuestProfile profile = player.getQuestProfile();
        Quest quest = profile.getActiveQuest();
        if(quest == null) return;
        quest.onDeactivateQuest(profile.getActiveQuestProgress());

    }

    public void innerCompleteQuest(MPlayer player, int currentPathProgress){
        QuestProfile profile = player.getQuestProfile();
        Quest quest = profile.getActiveQuest();
        if(quest != null){
            EngineRegions.get().removeFromRegionTracker(player.getUuid());
            PrisonMapRenderer.reRenderQuest.remove(player.getUuid());
        }
        profile.resetCurrentQuestProgress();
        onCompleteQuest(currentPathProgress, player);
        profile.setActiveQuest(null);
        if(hasNext(player, profile.getPathQuestList(player), currentPathProgress)){
            profile.incrementCurrentPathProgress();
            activateCurrentQuest(player, currentPathProgress + 1);
        } else {
            if(getPathRewards() != null){
                for (QuestReward pathReward : getPathRewards()) {
                    pathReward.give(player);
                }
            }
            player.getQuestProfile().finalizeQuestPathCompletion(this);
        }
    }

    public static void register(QuestPath quest){
        QuestPathRegistry.get().registerQuestClass(quest);
    }

}
