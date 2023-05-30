package com.kennedysmithjava.prisoncore.quest.quests;

import com.kennedysmithjava.prisoncore.entity.player.MPlayer;
import com.kennedysmithjava.prisoncore.quest.Quest;
import com.kennedysmithjava.prisoncore.quest.QuestPath;
import com.kennedysmithjava.prisoncore.quest.region.QuestRegion;
import org.bukkit.ChatColor;

import java.util.ArrayList;

public class QuestCollectItems extends Quest {


    String itemKey;
    String itemDisplayName;
    int count;

    public QuestCollectItems(int progress, boolean questCompleted,
                             String itemKey, String itemDisplayName, int count) {
        super(progress, new ArrayList<>());
        this.itemKey = itemKey;
        this.itemDisplayName = itemDisplayName;
        this.count = count;
    }

    @Override
    public String getQuestName() {
        return "&6Collect Item: " + ChatColor.stripColor(itemDisplayName);
    }

    @Override
    public String getQuestDescription() {
        return "&7Progress: (" + getProgress() + "\\" + count + ")";
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

