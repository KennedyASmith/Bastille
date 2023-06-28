package com.kennedysmithjava.prisoncore.quest.quests;

import com.kennedysmithjava.prisoncore.engine.EngineQuests;
import com.kennedysmithjava.prisoncore.entity.player.MPlayer;
import com.kennedysmithjava.prisoncore.quest.Quest;
import com.kennedysmithjava.prisoncore.quest.QuestPath;
import com.kennedysmithjava.prisoncore.regions.Region;
import org.bukkit.Material;


/**
 * Block interaction handled in EngineQuests
 */
public class QuestBreakBlock extends Quest {


    private final Material material;
    private final boolean materialMatters;
    private final int count;
    private final boolean showMessage;
    private String materialString;


    public QuestBreakBlock(MPlayer player, Material material, int count, boolean showMessage){
        super(player);
        this.materialMatters = true;
        this.material = material;
        this.count = count;
        this.showMessage = showMessage;

    }

    public QuestBreakBlock(MPlayer player, int count, boolean showMessage){
        super(player);
        this.materialMatters = false;
        this.count = count;
        this.material = null;
        this.showMessage = showMessage;
        this.materialString = "any block.";
    }

    @Override
    public String getQuestName() {
        return "&6&lBreak blocks";
    }

    @Override
    public String getQuestDescription() {
        return "&7&rBreak a certain number of blocks.";
    }

    @Override
    public void continueQuest(int progress, QuestPath path) {
        if(this.getProgress() == this.count){
            this.completeThisQuest();
            return;
        }
        EngineQuests.addBlockBreakListener(player.getUuid(), this);
    }

    @Override
    public void onDeactivateQuest(int questProgress) {
        EngineQuests.removeBlockBreakListener(player.getUuid());
    }

    @Override
    public void onComplete() {
        EngineQuests.removeBlockBreakListener(player.getUuid());
    }

    @Override
    public Region getRegion(int progress) {
        return null;
    }

    @Override
    public void onEnterRegion() {

    }

    public void blockBroken(){
        this.incrementProgress();
        if(showMessage){
            player.msg("&7[&fQuest&7] &aYou've broken &e" + getProgress()+"/"+count+" &ablocks needed.");
        }
        if(this.getProgress() == this.count){
            this.completeThisQuest();
        }
    }

    public boolean materialMatters(){
        return materialMatters;
    }

    public Material getMaterial(){
        return material;
    }

    @Override
    public String getShortProgressString() {
        return "&eBreak &a" + materialString + " &a(" + getProgress() + "/" + count + ")";
    }
}

