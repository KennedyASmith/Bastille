package com.kennedysmithjava.prisoncore.quest.eventGroup;

import com.kennedysmithjava.prisoncore.PrisonCore;
import com.kennedysmithjava.prisoncore.entity.MConf;
import com.kennedysmithjava.prisoncore.entity.player.MPlayer;
import com.kennedysmithjava.prisoncore.entity.mines.Mine;
import com.kennedysmithjava.prisoncore.quest.QuestPhase;
import com.kennedysmithjava.prisoncore.quest.QuestPhaseGroup;
import com.kennedysmithjava.prisoncore.util.Color;
import com.kennedysmithjava.prisoncore.util.regions.Offset;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.ai.Navigator;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class IntroductionTutorial extends QuestPhaseGroup {

    public static IntroductionTutorial i = new IntroductionTutorial();

    IntroductionTutorial() {

        addEvent(new QuestPhase(this) {

            Map<Offset, List<String>> tour;
            Iterator<Offset> iterator;
            boolean pickaxeGiven;
            boolean spawned;
            NPC npc;
            MPlayer mPlayer;
            Player player;
            Mine mine;
            World world;
            Location origin;
            Navigator navigator;

            @Override
            public void initialize() {
                this.mPlayer = getPlayer();
                this.player = mPlayer.getPlayer();
                this.mine = mPlayer.getMine();
                tour = MConf.get().introductionTutorialTour;
                iterator = tour.keySet().iterator();
                world = mine.getWorld();
                origin = mine.getOrigin();
                pickaxeGiven = false;
                spawned = false;

                Location spawnLocation = getLocation(new Offset(-51, 51, -66, 0, 180));

                npc = CitizensAPI.getNPCRegistry().createNPC(EntityType.VILLAGER, "");

                npc.spawn(spawnLocation);
                Villager villager = (Villager) npc.getEntity();
                villager.addPotionEffect(PotionEffectType.SLOW.createEffect(999999, 1));

                this.navigator = npc.getNavigator();

                player.sendMessage(Color.get((MConf.get().introductionTutorialTourMessage)));

                mine.spawnNPCs();

                /*
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        startDialogue(iterator.next());
                    }
                }.runTaskLater(PrisonCore.get(), 5L*20);
                */

            }

            @Override
            public void interrupted() {
                npc.destroy();
            }

            public void startDialogue(Offset offset) {

                List<String> messages = tour.get(offset);
                Location target = getLocation(offset);
                navigator.setTarget(target);

                new BukkitRunnable() {

                    int finishedMessages = 0;
                    boolean npcSpeaking = false;
                    boolean npcReachedDestination = false;
                    boolean finished = false;

                    @Override
                    public void run() {
                        if (npcSpeaking) return;
                        if (navigator.isNavigating()) return;
                        if (player == null) {
                            interrupted();
                            this.cancel();
                            return;
                        }

                        if (!npcReachedDestination && npc.getStoredLocation() == target) npcReachedDestination = true;

                        if (finished) {
                            if (iterator.hasNext()) {
                                startDialogue(iterator.next());
                            }else{
                                Location location = npc.getEntity().getLocation();
                                Bukkit.broadcastMessage("Todo: Explosion here");
                                npc.destroy();
                                mine.spawnResearcherNPC();
                                completed(true);
                            }
                            this.cancel();
                            return;
                        }

                        String firstMessage = messages.get(0);
                        sendMessage(firstMessage, player);

                        for (String message : messages) {
                            if (message.equals(firstMessage)) continue;
                            npcSpeaking = true;
                            new BukkitRunnable() {
                                @Override
                                public void run() {
                                    if (player == null) this.cancel();
                                    sendMessage(message, player);
                                    finishedMessages = finishedMessages + 1;
                                    if (finishedMessages == messages.size() - 1) {
                                        npcSpeaking = false;
                                        finished = true;
                                    }
                                }
                            }.runTaskLater(PrisonCore.get(), (100 * messages.indexOf(message)) + (20L * 5L));
                        }
                    }
                }.runTaskTimer(PrisonCore.get(), 0L, 10L);
            }

            public Location getLocation(Offset o) {
                return new Location(origin.getWorld(), origin.getBlockX() + o.getX(), origin.getBlockY() + o.getY(), origin.getBlockZ() + o.getZ(), o.getYaw(), o.getPitch());
            }

            public void sendMessage(String message, Player player){
                String m = message.replaceAll("\n", " &r\n");
                player.sendMessage(Color.get((m)));
            }

        });

        addEvent(new QuestPhase(this) {
            @Override
            public void initialize() {

            }

            @Override
            public void interrupted() {

            }
        });

    }

    public static IntroductionTutorial get() {
        return i;
    }

    @Override
    public String getName() {
        return "IntroductionTutorial";
    }

    @Override
    public QuestPhaseGroup getNextTutorialGroup() {
        return null;
    }

}
