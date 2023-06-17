package com.kennedysmithjava.prisoncore.quest.quests;

import com.kennedysmithjava.prisoncore.PrisonCore;
import com.kennedysmithjava.prisoncore.entity.player.MPlayer;
import com.kennedysmithjava.prisoncore.npc.Skin;
import com.kennedysmithjava.prisoncore.npc.SkinManager;
import com.kennedysmithjava.prisoncore.quest.Quest;
import com.kennedysmithjava.prisoncore.quest.QuestPath;
import com.kennedysmithjava.prisoncore.quest.region.QuestExactRegion;
import com.kennedysmithjava.prisoncore.quest.region.QuestRegion;
import com.kennedysmithjava.prisoncore.util.regions.Offset;
import net.citizensnpcs.api.ai.Navigator;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class QuestWalkAndTalk extends Quest {

    private final Location origin;
    private final List<Offset> locations;
    private final List<String> dialogue;
    private final long messageDelay;
    private final String npcName;
    private final Skin npcSkin;
    private boolean deactivated;

    private NPC npc;

    private Entity entity;

    private final boolean despawnAfterDone;


    public QuestWalkAndTalk(MPlayer player, Location origin,
                            List<Offset> locations,
                            List<String> dialogue,
                            boolean despawnAfterDone,
                            long messageDelay,
                            String npcName,
                            Skin npcSkin
                     ){
        super(player);
        this.origin = origin.clone();
        this.locations = locations;
        this.dialogue = dialogue;
        this.despawnAfterDone = despawnAfterDone;
        this.messageDelay = messageDelay;
        this.npcName = npcName;
        this.npcSkin = npcSkin;
        this.deactivated = false;
        this.npc = PrisonCore.getNonPersistNPCRegistry().createNPC(EntityType.PLAYER, npcName);
    }

    public QuestWalkAndTalk(MPlayer player, Location origin,
                            List<Offset> locations,
                            List<String> dialogue,
                            boolean despawnAfterDone,
                            long messageDelay,
                            String npcName,
                            Skin npcSkin,
                            NPC npc
    ){
        super(player);
        this.origin = origin.clone();
        this.locations = locations;
        this.dialogue = dialogue;
        this.despawnAfterDone = despawnAfterDone;
        this.messageDelay = messageDelay;
        this.npcName = npcName;
        this.npcSkin = npcSkin;
        this.deactivated = false;
        this.npc = npc;
        this.entity = npc.getEntity();
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
    public void continueQuest(int progress, QuestPath path) {
        if(npc == null) Bukkit.broadcastMessage("NPC IS NULL");
        deactivated = false;
        if(progress == locations.size()){
            completeThisQuest();
            return;
        }

        if(entity == null){
            spawnNPC(progress);
        }
        runDialogue(path, origin, progress);
    }

    private void spawnNPC(int progress){
        Location spawn = locations.get(progress).getFrom(origin);
        Location cloned = spawn.clone().add(0.5, 0, 0.5);
        npc.spawn(cloned);
        npc.setProtected(true);
        SkinManager.skin(npc, npcName, npcSkin, 10);
        entity = npc.getEntity();
    }
    private void runDialogue(QuestPath path, Location origin, int progress){
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
                        if(player.getPlayer() != null){
                            incrementProgress();
                            continueQuest( progress + 1, path);
                        }else{
                            deactivated = true;
                            this.cancel();
                        }
                    }
                }.runTaskLater(PrisonCore.get(), messageDelay);

                this.cancel();
            }
        }.runTaskTimer(PrisonCore.get(), 25L, 10L);
    }

    @Override
    public void onDeactivateQuest(int questProgress) {
        if(npc == null) return;
        if(npc.isSpawned()) npc.despawn();
        npc.destroy();
        deactivated = true;
        Bukkit.broadcastMessage("Deactivated quest");
    }

    @Override
    public void onComplete() {
        if(despawnAfterDone){
            if(npc == null) return;
            if(npc.isSpawned()) npc.despawn();
            npc.destroy();
        }
        deactivated = true;
    }

    @Override
    public QuestRegion getRegion(int progress) {
        if(progress > locations.size() - 1) return null;
        Offset offset = locations.get(progress);
        QuestExactRegion region = null;
        if(offset != null){
            region = new QuestExactRegion(offset.getFrom(origin));
        }
        return region;
    }

    @Override
    public void onEnterRegion() {

    }

    public NPC getNpc() {
        return npc;
    }

}

