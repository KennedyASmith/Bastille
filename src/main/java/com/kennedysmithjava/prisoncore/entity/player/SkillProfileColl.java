package com.kennedysmithjava.prisoncore.entity.player;

import com.massivecraft.massivecore.store.Coll;

import java.util.UUID;

public class SkillProfileColl extends Coll<SkillProfile> {
    // -------------------------------------------- //
    // INSTANCE & CONSTRUCT
    // -------------------------------------------- //

    private static final SkillProfileColl i = new SkillProfileColl();

    public static SkillProfileColl get() {
        return i;
    }

    // -------------------------------------------- //
    // STACK TRACEABILITY
    // -------------------------------------------- //

    public SkillProfile getByUUID(UUID uuid){
        for (SkillProfile profile : this.getAll()) {
            if (profile.getId().equals(uuid.toString())) {
                return profile;
            }
        }
        SkillProfile skillProfile = new SkillProfile();
        SkillProfileColl.get().attach(skillProfile, uuid.toString());
        return skillProfile;

    }
}
