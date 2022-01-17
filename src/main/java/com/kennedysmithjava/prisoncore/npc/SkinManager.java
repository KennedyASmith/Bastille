package com.kennedysmithjava.prisoncore.npc;

import com.kennedysmithjava.prisoncore.PrisonCore;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.trait.SkinTrait;
import org.bukkit.scheduler.BukkitRunnable;

public class SkinManager {

    public static void skin(NPC npc, String name, Skin skin, int delay){
        skin(npc, name, skin.getTexture(), skin.getSignature(), delay);
    }

    public static void skin(NPC npc, String name, int delay){
        SkinTrait trait = npc.getTrait(SkinTrait.class);
        new BukkitRunnable() {
            @Override
            public void run() {
                trait.setSkinName(name,  true);
            }
        }.runTaskLater(PrisonCore.get(), delay);
    }

    public static void skin(NPC npc, String name, String texture, String signature, int delay){
        SkinTrait trait = npc.getTrait(SkinTrait.class);
        new BukkitRunnable() {
            @Override
            public void run() {
                trait.setShouldUpdateSkins(true);
                trait.setSkinPersistent(name, signature, texture);
            }
        }.runTaskLater(PrisonCore.get(), delay);
    }
}
