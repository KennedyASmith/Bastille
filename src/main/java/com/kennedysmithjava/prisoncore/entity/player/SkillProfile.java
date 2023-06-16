package com.kennedysmithjava.prisoncore.entity.player;

import com.kennedysmithjava.prisoncore.skill.SkillType;
import com.massivecraft.massivecore.store.Entity;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SkillProfile extends Entity<SkillProfile> {

    // -------------------------------------------- //
    // CONSTANTS
    // -------------------------------------------- //



    public static SkillProfile get(UUID playerUUID) {
        return SkillProfileColl.get().getByUUID(playerUUID);
    }


    public Map<SkillType, Skill> skills = new HashMap<>();
    public SkillType featuredSkill = SkillType.PLAYER;

    @Override
    public SkillProfile load(SkillProfile that) {
        this.setSkills(that.skills);
        this.setFeaturedSkill(that.featuredSkill);
        return this;
    }


    public void setSkills(Map<SkillType, Skill> skills) {
        this.skills = skills;
    }

    public Map<SkillType, Skill> getSkills() {
        if(skills.isEmpty()) buildProfile();
        return skills;
    }

    public Skill getSkill(SkillType type){
        if(skills.isEmpty()) buildProfile();
        return skills.get(type);
    }

    public void buildProfile(){
        for (SkillType value : SkillType.values()) {
            Skill skill = new Skill(value, 1, 0);
            skills.put(value, skill);
        }
    }

    public SkillType getFeaturedSkill() {
        return featuredSkill;
    }

    public void setFeaturedSkill(SkillType featuredSkill) {
        this.featuredSkill = featuredSkill;
        this.changed();
    }
}

