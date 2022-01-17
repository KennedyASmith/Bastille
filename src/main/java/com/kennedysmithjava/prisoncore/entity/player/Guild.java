package com.kennedysmithjava.prisoncore.entity.player;

import com.kennedysmithjava.prisoncore.PrisonParticipator;
import com.kennedysmithjava.prisoncore.util.MiscUtil;
import com.massivecraft.massivecore.collections.MassiveMapDef;
import com.massivecraft.massivecore.store.Entity;

public class Guild extends Entity<Guild> implements PrisonParticipator {

    private final String desc = "This guild has no description";
    private final MassiveMapDef<MPlayer, GuildRole> members = new MassiveMapDef<>();
    private String name = null;

    public static Guild get(Object oid) {
        return GuildColl.get().get(oid);
    }

    public void update() {
        this.changed();
    }

    @Override
    public Guild load(Guild that) {
        setName(that.name);
        return this;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public MassiveMapDef<MPlayer, GuildRole> getMembers() {
        return members;
    }

    public void setMember(MPlayer mPlayer, GuildRole guildRole) {
        members.put(mPlayer, guildRole);
    }

    public void removeMember(MPlayer mPlayer) {
        members.remove(mPlayer);
    }

    public void addMember(MPlayer mPlayer) {
        setMember(mPlayer, GuildRole.RECRUIT);
    }

    public void promoteMember(MPlayer mPlayer) {
        setMember(mPlayer, GuildRole.LEADER);
    }

    public void demoteMember(MPlayer mPlayer) {
        members.remove(mPlayer);
    }

    public String getComparisonName() {
        return MiscUtil.getComparisonString(this.getName());
    }

}
