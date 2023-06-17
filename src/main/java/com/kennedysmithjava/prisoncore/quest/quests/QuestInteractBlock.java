package com.kennedysmithjava.prisoncore.quest.quests;

import com.kennedysmithjava.prisoncore.PrisonCore;
import com.kennedysmithjava.prisoncore.engine.EngineQuests;
import com.kennedysmithjava.prisoncore.entity.player.MPlayer;
import com.kennedysmithjava.prisoncore.quest.Quest;
import com.kennedysmithjava.prisoncore.quest.QuestPath;
import com.kennedysmithjava.prisoncore.quest.region.QuestExactRegion;
import com.kennedysmithjava.prisoncore.quest.region.QuestRegion;
import com.kennedysmithjava.prisoncore.util.vfx.CircleSequence;
import com.kennedysmithjava.prisoncore.util.vfx.ParticleFn;
import org.bukkit.Location;
import org.bukkit.scheduler.BukkitRunnable;
import xyz.xenondevs.particle.ParticleEffect;

import java.awt.*;


/**
 * Block interaction handled in EngineQuests
 */
public class QuestInteractBlock extends Quest {

    private final Location location;

    private  final boolean hasParticles;

    private final ParticleFn PARTICLE_FN;

    private final CircleSequence PARTICLE_SEQUENCE;

    private BukkitRunnable particles;

    private String blockName;


    public QuestInteractBlock(MPlayer player, Location location,
                              boolean hasParticles, String blockName){
        super(player);
        this.location = location;
        this.hasParticles = hasParticles;
        this.PARTICLE_FN = (loc, p) -> ParticleEffect.REDSTONE.display(loc, Color.GREEN, p);
        this.PARTICLE_SEQUENCE = new CircleSequence(1, 8);
        this.PARTICLE_SEQUENCE.setParticleFn(PARTICLE_FN);
        this.blockName = blockName;
    }

    @Override
    public String getQuestName() {
        return "&6&lInteract with block";
    }

    @Override
    public String getQuestDescription() {
        return "&7&rBlock location is marked on your map.";
    }

    @Override
    public void continueQuest(int progress, QuestPath path) {
        EngineQuests.addBlockInteractListener(player.getUuid(), this);
        Location particleLocation = location.clone().add(0.5, 0, 0.5);
        particles = new BukkitRunnable() {
            @Override
            public void run() {
                PARTICLE_SEQUENCE.draw(particleLocation);
            }
        };

        particles.runTaskTimer(PrisonCore.get(), 0, 10);
    }

    @Override
    public void onDeactivateQuest(int questProgress) {
        if(particles != null) particles.cancel();
        EngineQuests.removeBlockInteractListener(player.getUuid());
    }

    @Override
    public void onComplete() {
        if(particles != null) particles.cancel();
        EngineQuests.removeBlockInteractListener(player.getUuid());
    }

    @Override
    public QuestRegion getRegion(int progress) {
        return new QuestExactRegion(location);
    }

    @Override
    public void onEnterRegion() {

    }

    @Override
    public String getShortProgressString() {
        return "&eClick &a" + blockName;
    }

    public Location getLocation() {
        return location;
    }
}

