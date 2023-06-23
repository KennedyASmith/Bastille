package com.kennedysmithjava.prisoncore.entity.player;

import com.kennedysmithjava.prisoncore.skill.SkillType;
import com.massivecraft.massivecore.store.EntityInternal;

import java.util.HashMap;
import java.util.Map;

public class Skill extends EntityInternal<SkillProfile> {

    // Name of type of tree
    private SkillType type;

    private int currentLevel;
    private int currentXP;

    private final int maxLevel;

    public transient static Map<SkillType, Map<Integer, Integer>> xpNeeded = new HashMap<>();

    public Skill(SkillType type, int currentLevel, int currentXP) {
        this.type = type;
        this.currentLevel = currentLevel;
        this.currentXP = currentXP;
        this.maxLevel = SkillsConf.get().maxLevels.get(type);
    }

    public int getCurrentLevel() {
        return currentLevel;
    }

    public int getCurrentXP() {
        return currentXP;
    }

    public SkillType getType() {
        return type;
    }

    /**
     *
     * @param xp
     * @return true if this levels up the player
     */
    public boolean addXP(int xp){
        if(maxLevelReached()) return false;
        int xpRequired = SkillsConf.getXpRequired(type, currentLevel);
        if(currentXP + xp >= xpRequired){
            this.setLevel(currentLevel + 1, currentXP - xp);
            return true;
        }else{
            this.currentXP += xp;
            this.changed();
            return false;
        }
    }

    public void setLevel(int level, int xp){
        this.currentXP = xp;
        this.currentLevel = level;
        this.changed();
    }


    public String getXPBar(int neededXP){
        StringBuilder builder = new StringBuilder("‖‖‖‖‖‖‖‖‖‖‖‖‖‖‖‖‖‖‖‖‖‖‖‖‖‖‖‖‖‖‖‖‖‖‖‖‖‖‖‖");
        int length = builder.length();
        double percentage = (double) currentXP / (double) neededXP;
        int numberOfGreen = (int) (percentage * (double) length);
        builder.insert(Math.max(0, Math.min(numberOfGreen, length - 1)), "&7");
        builder.insert(0, "&a");
        return builder.toString();
    }

    public String getXPBar(){
        return getXPBar(getXPRequired());
    }

    public int getXPRequired(){
        if(maxLevelReached()) return -1;
        return SkillsConf.getXpRequired(type, currentLevel);
    }

    public boolean maxLevelReached(){
        return maxLevel <= currentLevel;
    }

    public int getMaxLevel(){
        return SkillsConf.get().getMaxLevels().get(type);
    }


}
