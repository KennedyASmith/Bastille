package com.kennedysmithjava.prisoncore.entity.player;

import com.kennedysmithjava.prisoncore.quest.Quest;
import com.kennedysmithjava.prisoncore.quest.QuestPath;
import com.kennedysmithjava.prisoncore.quest.QuestPathRegistry;
import com.massivecraft.massivecore.store.Entity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class QuestProfile extends Entity<QuestProfile> {

    // -------------------------------------------- //
    // CONSTANTS
    // -------------------------------------------- //
    public int totalCompletedQuests = 0;

    public String activeQuestPathData = "";
    public int activeQuestPathProgress = 0;
    public int activeQuestProgress = 0;
    public List<String> currentQuestsData =  new ArrayList<>();
    public List<String> unlockedQuestsData = new ArrayList<>();
    public List<String> completedQuestsData = new ArrayList<>();

    public transient List<QuestPath> currentQuests = new ArrayList<>();

    public transient List<QuestPath> unlockedQuests = new ArrayList<>();
    public transient QuestPath activeQuestPath;
    public transient Quest activeQuest;

    public transient List<Quest> pathQuestList;


    public static QuestProfile get(UUID playerUUID) {
        return QuestProfileColl.get().getByUUID(playerUUID);
    }
    @Override
    public QuestProfile load(QuestProfile that) {
        this.setTotalCompletedQuests(that.totalCompletedQuests);
        this.setCurrentQuestsData(that.currentQuestsData);
        this.setCompletedQuestsData(that.completedQuestsData);
        this.setUnlockedQuestsData(that.unlockedQuestsData);
        this.setActiveQuestPathData(that.activeQuestPathData);
        for (String currentQuestsDatum : currentQuestsData) {
            currentQuests.add(QuestPathRegistry.get().getQuest(currentQuestsDatum));
        }
        for (String unlockedQuestsDatum : unlockedQuestsData) {
            unlockedQuests.add(QuestPathRegistry.get().getQuest(unlockedQuestsDatum));
        }
        setActiveQuestPath(QuestPathRegistry.get().getQuest(that.activeQuestPathData));
        return this;
    }

    public List<QuestPath> getCurrentQuests() {
        return currentQuests;
    }

    public void setCurrentQuestsData(List<String> currentQuestsData) {
        this.currentQuestsData = currentQuestsData;
        this.changed();
    }

    public int getTotalCompletedQuests() {
        return totalCompletedQuests;
    }

    public void setTotalCompletedQuests(int totalCompletedQuests) {
        this.totalCompletedQuests = totalCompletedQuests;
        this.changed();
    }

    public void setCompletedQuestsData(List<String> completedQuestsData) {
        this.completedQuestsData = completedQuestsData;
        this.changed();
    }

    public List<String> getCompletedQuestsData() {
        return completedQuestsData;
    }


    public void setUnlockedQuestsData(List<String> unlockedQuestsData) {
        this.unlockedQuestsData = unlockedQuestsData;
    }

    public List<String> getUnlockedQuestsData() {
        return unlockedQuestsData;
    }


    public List<QuestPath> getUnlockedQuests() {
        return unlockedQuests;
    }

    public void removeCurrentQuest(String questName, QuestPath path){
        this.currentQuestsData.remove(questName);
        this.currentQuests.remove(path);
        this.changed();
    }

    public void removeUnlockedQuest(String questName, QuestPath path){
        this.unlockedQuestsData.remove(questName);
        this.unlockedQuests.remove(path);
        this.changed();
    }

    public void setActiveQuestPathData(String questPathData){
        this.activeQuestPathData = questPathData;
        this.changed();
    }

    public void setActiveQuestPath(QuestPath activeQuestPath) {
        if(activeQuestPath == null) return;
        setActiveQuestPathData(activeQuestPath.getClass().getSimpleName());
        this.activeQuestPath = activeQuestPath;
        removeCurrentQuest(activeQuestPathData, activeQuestPath);
        removeUnlockedQuest(activeQuestPathData, activeQuestPath);
        MPlayer player = MPlayerColl.get().getByUUID(this.getId());
        setPathQuestList(activeQuestPath.getInitializedQuests(player));
        activeQuestPath.activateCurrentQuest(player, activeQuestPathProgress);
    }

    private void setPathQuestList(List<Quest> pathQuestList) {
        this.pathQuestList = pathQuestList;
    }

    public QuestPath getActiveQuestPath() {
        if(!activeQuestPathData.equals("") && activeQuestPath == null)
                setActiveQuestPath(QuestPathRegistry.get()
                        .getQuest(activeQuestPathData));
        return activeQuestPath;
    }

    private String getActiveQuestPathData() {
        return activeQuestPathData;
    }

    public void finalizeQuestPathCompletion(QuestPath questPath){
        if(activeQuestPath.equals(questPath)){
            this.activeQuestPathData = "";
            this.activeQuestPath = null;
            this.activeQuestPathProgress = 0;
        } else removeUnlockedQuest(questPath.getClass().getSimpleName(), questPath);
        completedQuestsData.add(questPath.getQuestPathDisplayName());
        this.changed();
    }

    public void incrementCurrentPathProgress(){
        this.activeQuestPathProgress++;
        this.changed();
    }

    public void incrementCurrentQuestProgress(){
        this.activeQuestProgress++;
        this.changed();
    }

    public void resetCurrentQuestProgress(){
        this.activeQuestProgress = 0;
        this.changed();
    }

    public int getActiveQuestProgress() {
        return activeQuestProgress;
    }

    public void setActiveQuest(Quest activeQuest) {
        this.activeQuest = activeQuest;
    }

    public Quest getActiveQuest() {
        if(activeQuestPathData != null && activeQuest == null)
            setActiveQuestPath(QuestPathRegistry.get()
                            .getQuest(activeQuestPathData));
        return activeQuest;
    }

    public List<Quest> getPathQuestList(MPlayer player) {
        if(pathQuestList == null) pathQuestList = getActiveQuestPath().getInitializedQuests(player);
        return pathQuestList;
    }

    public void addUnlockedQuestPath(QuestPath path){
        unlockedQuestsData.add(path.getClass().getSimpleName());
        unlockedQuests.add(path);
        this.changed();
    }
}

