package com.kennedysmithjava.prisoncore.entity.player;

import com.kennedysmithjava.prisoncore.quest.Quest;
import com.kennedysmithjava.prisoncore.quest.QuestPath;
import com.massivecraft.massivecore.collections.MassiveList;
import com.massivecraft.massivecore.store.Entity;
import org.bukkit.Bukkit;


import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class QuestProfile extends Entity<QuestProfile> {

    // -------------------------------------------- //
    // CONSTANTS
    // -------------------------------------------- //

    public String uuid;
    public int totalCompletedQuests = 0;
    public QuestPath activeQuestPath;
    public List<QuestPath> currentQuests =  new ArrayList<>();
    public List<QuestPath> unlockedQuests = new ArrayList<>();
    public List<String> completedQuests = new ArrayList<>();
    public static QuestProfile get(UUID playerUUID) {
        return QuestProfileColl.get().getByUUID(playerUUID);
    }

    public QuestProfile(String uuid){
        this.uuid = uuid;
    }
    @Override
    public QuestProfile load(QuestProfile that) {
        this.setUuid(that.uuid);
        this.setTotalCompletedQuests(that.totalCompletedQuests);
        this.setCurrentQuests(that.currentQuests);
        this.setCompletedQuests(that.completedQuests);
        this.setUnlockedQuests(that.unlockedQuests);
        this.setActiveQuestPath(that.activeQuestPath);
        return this;
    }
    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public List<QuestPath> getCurrentQuests() {
        return currentQuests;
    }

    public void setCurrentQuests(List<QuestPath> currentQuests) {
        this.currentQuests = currentQuests;
        this.changed();
    }

    public int getTotalCompletedQuests() {
        return totalCompletedQuests;
    }

    public void setTotalCompletedQuests(int totalCompletedQuests) {
        this.totalCompletedQuests = totalCompletedQuests;
        this.changed();
    }

    public void setCompletedQuests(List<String> completedQuests) {
        this.completedQuests = completedQuests;
        this.changed();
    }

    public List<String> getCompletedQuests() {
        return completedQuests;
    }


    public void setUnlockedQuests(List<QuestPath> unlockedQuests) {
        this.unlockedQuests = unlockedQuests;
    }

    public List<QuestPath> getUnlockedQuests() {
        return unlockedQuests;
    }

    public void setActiveQuestPath(QuestPath activeQuestPath) {
        this.activeQuestPath = activeQuestPath;
        currentQuests.remove(activeQuestPath);
        unlockedQuests.remove(activeQuestPath);
        this.changed();
        activeQuestPath.activate(MPlayerColl.get().getByUUID(getUuid()));
    }

    public QuestPath getActiveQuestPath() {
        return activeQuestPath;
    }

    public void finalizeQuestPathCompletion(QuestPath questPath){
        if(activeQuestPath.equals(questPath)){
            activeQuestPath = null;
            Bukkit.broadcastMessage("You've completed a quest path. Activate a new one!");
        } else unlockedQuests.remove(questPath);
        completedQuests.add(questPath.getQuestPathDisplayName());
        this.changed();
    }
}

