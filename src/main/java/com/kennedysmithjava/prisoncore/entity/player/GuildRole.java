package com.kennedysmithjava.prisoncore.entity.player;

import com.massivecraft.massivecore.collections.MassiveSet;

import java.util.Collections;
import java.util.Set;

public enum GuildRole {

    RECRUIT("Recruit in the gang", "Slave", "Pleb", "Recruit"),
    CO_LEADER("CoLeader of the gang", "Co-Leader", "Admin"),
    LEADER("Leader Of the gang", "Leader", "Master"),

    //End....
    ;

    private final String description;
    private final Set<String> names;

    GuildRole(String description, String... names) {
        this.description = description;
        this.names = Collections.unmodifiableSet(new MassiveSet<>(names));
    }

    public int getValue() {
        return this.ordinal();
    }

    public Set<String> getNames() {
        return this.names;
    }

    public String getDescription() {
        return this.description;
    }

    public boolean isAtLeast(GuildRole role) {
        return this.getValue() >= role.getValue();
    }

    public boolean isAtMost(GuildRole role) {
        return this.getValue() <= role.getValue();
    }

    public boolean isLessThan(GuildRole role) {
        return this.getValue() < role.getValue();
    }

    public boolean isMoreThan(GuildRole role) {
        return this.getValue() > role.getValue();
    }

    public boolean isRank() {
        return this.isAtLeast(GuildRole.RECRUIT);
    }

}
