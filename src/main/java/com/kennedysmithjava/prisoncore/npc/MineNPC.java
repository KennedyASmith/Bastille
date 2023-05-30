package com.kennedysmithjava.prisoncore.npc;

import com.kennedysmithjava.prisoncore.PrisonCore;
import com.kennedysmithjava.prisoncore.npc.Skin;
import com.kennedysmithjava.prisoncore.npc.SkinManager;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.trait.Trait;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;

import java.util.List;

public abstract class MineNPC {

    public NPC spawn(String name, Location location){
        return spawn(name, location, 0);
    }

    public NPC spawn(String name, Location location, int skinDelay){

        NPC npc = PrisonCore.getNonPersistNPCRegistry().createNPC(EntityType.PLAYER,name);

        if(npc.getId() == 0){
            npc = PrisonCore.getNonPersistNPCRegistry().createNPC(EntityType.PLAYER,name);
        }

        npc.setProtected(true);
        getTraits().forEach(npc::addTrait);
        npc.spawn(location.clone().add(0.5, 0, 0.5));

        SkinManager.skin(npc, name, getSkin(), skinDelay);
        onSpawn(npc);
        return npc;
    }

    public abstract void onSpawn(NPC npc);

    public abstract Skin getSkin();

    public abstract List<Trait> getTraits();

}
