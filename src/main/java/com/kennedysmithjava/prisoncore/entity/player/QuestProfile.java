package com.kennedysmithjava.prisoncore.entity.player;

import com.kennedysmithjava.prisoncore.quest.Quest;
import com.kennedysmithjava.prisoncore.quest.QuestPath;
import com.kennedysmithjava.prisoncore.quest.QuestPathRegistry;
import com.kennedysmithjava.prisoncore.quest.reward.QuestReward;
import com.massivecraft.massivecore.store.Entity;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class QuestProfile extends Entity<QuestProfile> {

    // -------------------------------------------- //
    // CONSTANTS
    // -------------------------------------------- //
    public int totalCompletedQuests = 0;

    public String activeQuestPathData = "";
    public int activeQuestPathProgress = 0;
    public int activeQuestProgress = 0;
    public List<String> unlockedQuestsData = new ArrayList<>();
    public List<String> completedQuestsData = new ArrayList<>();

    public Map<String, List<Integer>> unclaimedQuestRewards = new HashMap<>();
    public transient QuestPath activeQuestPath;
    public transient Quest activeQuest;

    public transient List<Quest> pathQuestList;



    public static QuestProfile get(UUID playerUUID) {
        return QuestProfileColl.get().getByUUID(playerUUID);
    }
    @Override
    public QuestProfile load(QuestProfile that) {
        this.setTotalCompletedQuests(that.totalCompletedQuests);
        this.setCompletedQuestsData(that.completedQuestsData);
        this.setUnlockedQuestsData(that.unlockedQuestsData);
        this.setActiveQuestPathData(that.activeQuestPathData);
        this.setUnclaimedQuestRewards(that.unclaimedQuestRewards);
        setActiveQuestPath(QuestPathRegistry.get().getQuest(that.activeQuestPathData));
        return this;
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

    public List<QuestPath> getUnlockedQuests() {
        List<QuestPath> unlockedQs = new ArrayList<>();
        for (String unlockedQuestsDatum : unlockedQuestsData) {
            QuestPath path = QuestPathRegistry.get().getQuest(unlockedQuestsDatum);
            if(path != null) unlockedQs.add(path);
        }
        return unlockedQs;
    }

    public void removeUnlockedQuest(String questName){
        this.unlockedQuestsData.remove(questName);
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
        removeUnlockedQuest(activeQuestPathData);
        MPlayer player = MPlayerColl.get().getByUUID(this.getId());
        if(player == null){
            this.detach();
            return;
        }
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

    public void finalizeQuestPathCompletion(QuestPath questPath){
        if(activeQuestPath.equals(questPath)){
            this.activeQuestPathData = "";
            this.activeQuestPath = null;
            this.activeQuestPathProgress = 0;
        } else removeUnlockedQuest(questPath.getClass().getSimpleName());
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
        this.changed();
    }

    public void setUnclaimedQuestRewards(Map<String, List<Integer>> unclaimedQuestRewards) {
        this.unclaimedQuestRewards = unclaimedQuestRewards;
        this.changed();
    }

    //Quest Path Index Key:
    // -1: Quest Path rewards
    // 0 to path.size(): Quest indexes
    public void addUnclaimedReward(String pathName, int rewardIndex){
        List<Integer> questRewards = unclaimedQuestRewards.getOrDefault(pathName, new ArrayList<>());
        questRewards.add(rewardIndex);
        unclaimedQuestRewards.put(pathName, questRewards);
        this.changed();
    }

    public void removeUnclaimedReward(String pathName, int rewardIndex){
        List<Integer> questRewards = unclaimedQuestRewards.getOrDefault(pathName, new ArrayList<>());
        questRewards.remove(rewardIndex);
        if(questRewards.size() == 0){
            unclaimedQuestRewards.remove(pathName);
        }else {
            unclaimedQuestRewards.put(pathName, questRewards);
        }
        this.changed();
    }

    public Map<QuestPath, List<QuestReward>> getUnclaimedQuestRewards(){
        Map<QuestPath, List<QuestReward>> unclaimedRewards = new HashMap<>();
        for (Map.Entry<String, List<Integer>> entry : unclaimedQuestRewards.entrySet()) {
                List<QuestReward> unclaimedPathRewards = new ArrayList<>();
                List<Integer> rewardIndexes = entry.getValue();
                QuestPath path = QuestPathRegistry.get().getQuest(entry.getKey());
                if(path == null) continue;
                List<QuestReward> pathRewards = path.getPathRewards();
                for (Integer rewardIndex : rewardIndexes) {
                    QuestReward reward = pathRewards.get(rewardIndex);
                    if(reward != null) unclaimedPathRewards.add(reward);
                }
                unclaimedRewards.put(path, unclaimedPathRewards);
        }
        return unclaimedRewards;
    }

    public Map<String, List<Integer>> getUnclaimedQuestData(){
        return this.unclaimedQuestRewards;
    }


    public int getTotalUnclaimedRewards(){
        AtomicInteger total = new AtomicInteger(0);
            unclaimedQuestRewards.forEach((s, integers) -> {
                for (Integer integer : integers) {
                    total.getAndIncrement();
                }
            });
        return total.get();
    }
}

