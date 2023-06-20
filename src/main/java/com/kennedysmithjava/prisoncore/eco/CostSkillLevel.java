package com.kennedysmithjava.prisoncore.eco;

import com.kennedysmithjava.prisoncore.entity.player.MPlayer;
import com.kennedysmithjava.prisoncore.entity.player.SkillProfile;
import com.kennedysmithjava.prisoncore.skill.SkillType;

public class CostSkillLevel extends Cost{

    private final SkillType skillType;
    private final int level;

    public CostSkillLevel(SkillType skillType, int level){
        this.skillType = skillType;
        this.level = level;
    }

    @Override
    public boolean hasCost(MPlayer player) {
        SkillProfile profile = player.getSkillProfile();
        return profile.getSkill(skillType).getCurrentLevel() >= level;
    }

    @Override
    public boolean transaction(MPlayer player) {
        return hasCost(player);
    }


    @Override
    public String getName() {
        return skillType.getDisplayName();
    }

    @Override
    public String getPriceline() {
        return skillType.getDisplayName() + " &7level &e" + level + " &7required.";
    }

    @Override
    public String getInsufficientLine(MPlayer player) {
        return skillType.getDisplayName() +
                " &clevel &e" + level +
                " &crequired. Your level: &e" +
                player.getSkillProfile().getSkill(skillType).getCurrentLevel();
    }

    @Override
    public Cost combine(Cost similarCost) {
        return new CostSkillLevel(skillType, Math.max(this.getLevel(), ((CostSkillLevel) similarCost).getLevel()));
    }

    public int getLevel() {
        return level;
    }
}
