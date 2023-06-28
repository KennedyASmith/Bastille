package com.kennedysmithjava.prisoncore.quest.quests;

import com.kennedysmithjava.prisoncore.PrisonCore;
import com.kennedysmithjava.prisoncore.entity.player.MPlayer;
import com.kennedysmithjava.prisoncore.quest.Quest;
import com.kennedysmithjava.prisoncore.quest.QuestPath;
import com.kennedysmithjava.prisoncore.regions.RegionExact;
import com.kennedysmithjava.prisoncore.regions.Region;
import com.kennedysmithjava.prisoncore.util.MiscUtil;
import com.kennedysmithjava.prisoncore.regions.Offset;
import com.kennedysmithjava.prisoncore.util.vfx.CircleSequence;
import com.kennedysmithjava.prisoncore.util.vfx.ParticleFn;
import me.filoghost.holographicdisplays.api.beta.HolographicDisplaysAPI;
import me.filoghost.holographicdisplays.api.beta.hologram.Hologram;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import xyz.xenondevs.particle.ParticleEffect;

import java.awt.*;

public class QuestPromptPickupItem extends Quest {

    private final ParticleFn PARTICLE_FN;
    private final CircleSequence PARTICLE_SEQUENCE;

    private final Location origin;
    private final Location location;
    private final HolographicDisplaysAPI api;
    private final String itemName;
    private final ItemStack item;

    private BukkitRunnable particles;

    private Hologram hologram;

    public QuestPromptPickupItem(MPlayer player, Location origin,
                                 Offset offset,
                                 ItemStack item,
                                 String itemName,
                                 Color particleColor
                     ){
        super(player);
        this.origin = origin.clone();
        this.location = offset.getFrom(origin).add(0.5, 0, 0.5);
        this.itemName = itemName;
        this.api = HolographicDisplaysAPI.get(PrisonCore.get());
        this.item = item;
        this.PARTICLE_FN = (location, p) -> ParticleEffect.REDSTONE.display(location, particleColor, p);
        this.PARTICLE_SEQUENCE = new CircleSequence(1, 8);
        this.PARTICLE_SEQUENCE.setParticleFn(PARTICLE_FN);
    }

    @Override
    public String getQuestName() {
        return "&6&lPickup Item: " + itemName;
    }

    @Override
    public String getQuestDescription() {
        return "&7&rItem location is marked on your map.";
    }

    @Override
    public void continueQuest(int progress, QuestPath path) {
        hologram = api.createHologram(location.clone().add(0, 1.5, 0));
        hologram.getLines().appendText(com.kennedysmithjava.prisoncore.util.Color.get("&a&lPickup Item:"));
        hologram.getLines().appendText(com.kennedysmithjava.prisoncore.util.Color.get("&f" + itemName));
        hologram.getLines().appendItem(item);
        particles = new BukkitRunnable() {
            @Override
            public void run() {
                PARTICLE_SEQUENCE.draw(location);
            }
        };

        particles.runTaskTimer(PrisonCore.get(), 0, 10);
    }

    @Override
    public void onDeactivateQuest(int questProgress) {
        if(particles != null) particles.cancel();
        if(hologram == null) return;
        if(hologram.isDeleted()) return;
        hologram.delete();
    }

    @Override
    public void onComplete() {
        if(particles != null) particles.cancel();
        if(hologram == null) return;
        if(hologram.isDeleted()) return;
        hologram.delete();
    }

    @Override
    public Region getRegion(int progress) {
        return new RegionExact(location);
    }

    @Override
    public void onEnterRegion() {
        Player p = player.getPlayer();
        MiscUtil.givePlayerItem(p, item, 1);
        this.completeThisQuest();
    }

    @Override
    public String getShortProgressString() {
        return "&ePickup &aitem (" + getProgress() + "/1)";
    }
}

