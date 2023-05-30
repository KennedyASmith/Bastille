package com.kennedysmithjava.prisoncore.quest.quests;

import com.kennedysmithjava.prisoncore.PrisonCore;
import com.kennedysmithjava.prisoncore.entity.player.MPlayer;
import com.kennedysmithjava.prisoncore.npc.Skin;
import com.kennedysmithjava.prisoncore.quest.Quest;
import com.kennedysmithjava.prisoncore.quest.QuestPath;
import com.kennedysmithjava.prisoncore.quest.region.QuestCylindricalRegion;
import com.kennedysmithjava.prisoncore.quest.region.QuestRegion;
import com.kennedysmithjava.prisoncore.util.regions.Offset;
import com.massivecraft.massivecore.ps.PS;
import net.citizensnpcs.api.ai.Navigator;
import net.citizensnpcs.api.event.SpawnReason;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.npc.NPCRegistry;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class QuestWalkAndTalk extends Quest {

    private final PS origin;
    private final List<Offset> locations;
    private final List<String> dialogue;
    private final long messageDelay;
    private final String npcName;
    private final Skin npcSkin;
    private boolean deactivated;

    private int npcID;


    public QuestWalkAndTalk(PS origin,
                            List<Offset> locations,
                            List<String> dialogue,
                            long messageDelay,
                            String npcName,
                            Skin npcSkin,
                            int npcID
                     ){
        setProgress(0);
        this.origin = origin;
        this.locations = locations;
        this.dialogue = dialogue;
        this.messageDelay = messageDelay;
        this.npcName = npcName;
        this.npcSkin = npcSkin;
        this.deactivated = false;
        this.npcID = npcID;
    }

    public QuestWalkAndTalk(PS origin,
                            List<Offset> locations,
                            List<String> dialogue,
                            long messageDelay,
                            String npcName,
                            Skin npcSkin
    ){
        this(origin, locations, dialogue, messageDelay, npcName, npcSkin, getUniqueID());
    }

    @Override
    public String getQuestName() {
        return "&6&lFollow " + npcName;
    }

    @Override
    public String getQuestDescription() {
        return "&7&r" + npcName + "'s location is marked on your map.";
    }

    @Override
    public void continueQuest(MPlayer player, QuestPath thisPath) {
        deactivated = false;
        int progress = getProgress();
        if(progress == locations.size()){
            completeQuest(player);
            return;
        }
        NPC npc;

        NPCRegistry registry = PrisonCore.getNonPersistNPCRegistry();

        if(progress != 0){
            npc = registry.getById(npcID);
            if(npc == null) npc = registry.createNPC(EntityType.PLAYER, npcName + "-" + player.getName());
        }else{
            npc = registry.createNPC(EntityType.PLAYER, npcName + "-" + player.getName());
        }

        npcID = npc.getId();

        if(!npc.isSpawned()) spawnNPC(progress, npc);

        runDialogue(thisPath, player, PS.asBukkitLocation(origin), npc, progress);
    }

    private void spawnNPC(int progress, NPC npc){
        Location origin = PS.asBukkitLocation(this.origin);
        Location spawn = locations.get(progress).getFrom(origin);
        Location cloned = spawn.clone().add(0.5, 0, 0.5);
        Bukkit.broadcastMessage("Spawning: " + cloned);
        npc.spawn(cloned, SpawnReason.CREATE);
        npc.setProtected(true);
        //SkinManager.skin(npc, npcName, npcSkin, 10);
        Bukkit.broadcastMessage("Spawned NPC");
    }
    private void runDialogue(QuestPath path, MPlayer player, Location origin, NPC npc, int progress){
        Location target = locations.get(progress).getFrom(origin);
        final AtomicBoolean notBegunNavigating = new AtomicBoolean(true);
        AtomicReference<Navigator> navigator = new AtomicReference<>();
        new BukkitRunnable() {
            @Override
            public void run() {
                if(notBegunNavigating.get()){
                    if(npc.isSpawned()){
                        navigator.set(npc.getNavigator());
                        navigator.get().setTarget(target);
                        notBegunNavigating.set(false);
                    }else{
                        Bukkit.broadcastMessage("NPC Hasn't Spawned Yet");
                    }
                    return;
                }
                if(navigator.get().isNavigating()) return;
                if(deactivated) {
                    this.cancel();
                    return;
                }
                // Otherwise, it has reached its target
                player.msg(dialogue.get(progress));
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        incrementProgress();
                        continueQuest(player, path);
                    }
                }.runTaskLater(PrisonCore.get(), messageDelay);

                this.cancel();
            }
        }.runTaskTimer(PrisonCore.get(), 25L, 10L);
    }

    @Override
    public void onDeactivateQuest(MPlayer player) {
        NPCRegistry registry = PrisonCore.getNonPersistNPCRegistry();
        NPC npc = registry.getById(npcID);
        if(npc == null) return;
        if(!npc.isSpawned()) return;
        npc.despawn();
        registry.deregister(npc);
        deactivated = true;
    }

    @Override
    public void onComplete(MPlayer player) {
        NPCRegistry registry = PrisonCore.getNonPersistNPCRegistry();
        NPC npc = registry.getById(npcID);
        if(npc == null) return;
        if(!npc.isSpawned()) return;
        npc.despawn();
        registry.deregister(npc);
    }

    @Override
    public QuestRegion getRegion() {
        Offset offset = locations.get(getProgress());
        QuestCylindricalRegion region = null;
        if(offset != null){
            region = new QuestCylindricalRegion(
                    offset.getFrom(PS.asBukkitLocation(origin)),
                    3.0,
                    2
            );
        }
        return region;
    }

    @Override
    public void onEnterRegion(MPlayer player) {

    }

    private static int getUniqueID(){
        AtomicInteger count = new AtomicInteger();
        PrisonCore.getNonPersistNPCRegistry().sorted().forEach(npc -> count.getAndIncrement());
        return count.get() + 1;
    }
}

