package com.kennedysmithjava.prisoncore.entity.player;

import com.kennedysmithjava.prisoncore.skill.SkillType;
import com.massivecraft.massivecore.store.EntityInternal;

public class Skill extends EntityInternal<SkillProfile> {

    // Name of type of tree
    private final SkillType type;

    private int currentLevel;
    private int currentXP;

    private double xpMultiplier;


    public Skill(SkillType type, int currentLevel, int currentXP) {
        this.type = type;
        this.currentLevel = currentLevel;
        this.currentXP = currentXP;
        this.xpMultiplier = 1.0;
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
        if(xp == 0) return false;
        if(maxLevelReached()) return false;
        xp = (int) (xp * xpMultiplier);
        int xpRequired = SkillsConf.getXpRequired(type, currentLevel);
        int xpActualNeed = xpRequired - currentXP;
        if(xp >= xpActualNeed){
            this.setLevel(currentLevel + 1, 0);
            this.addXP(xp - xpActualNeed);
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

    public boolean addLevel(int level){
        int maxLevel = getMaxLevel();
        if(currentLevel + level > getMaxLevel()){
            this.currentLevel = maxLevel;
            this.changed();
            return false;
        }else {
            this.currentLevel += level;
            this.changed();
            return true;
        }
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
        return getMaxLevel() <= currentLevel;
    }

    public int getMaxLevel(){
        return SkillsConf.get().getMaxLevels().get(type);
    }

    public void addXpMultiplier(double xpMultiplier){
        this.xpMultiplier += xpMultiplier;
        this.changed();
    }

}
