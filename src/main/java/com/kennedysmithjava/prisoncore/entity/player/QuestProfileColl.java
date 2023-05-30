package com.kennedysmithjava.prisoncore.entity.player;

import com.massivecraft.massivecore.store.Coll;

import java.util.UUID;

public class QuestProfileColl extends Coll<QuestProfile> {
    // -------------------------------------------- //
    // INSTANCE & CONSTRUCT
    // -------------------------------------------- //

    private static final QuestProfileColl i = new QuestProfileColl();

    public static QuestProfileColl get() {
        return i;
    }

    // -------------------------------------------- //
    // STACK TRACEABILITY
    // -------------------------------------------- //

    public QuestProfile getByUUID(UUID uuid){
        for (QuestProfile profile : this.getAll()) {
            if (profile.getId().equals(uuid.toString())) {
                return profile;
            }
        }
        QuestProfile questProfile = new QuestProfile(uuid.toString());
        QuestProfileColl.get().attach(questProfile, uuid.toString());
        return questProfile;

    }
}
