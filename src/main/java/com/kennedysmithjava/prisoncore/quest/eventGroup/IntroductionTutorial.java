package com.kennedysmithjava.prisoncore.quest.eventGroup;

import com.kennedysmithjava.prisoncore.PrisonCore;
import com.kennedysmithjava.prisoncore.entity.MConf;
import com.kennedysmithjava.prisoncore.entity.mines.ArchitectConf;
import com.kennedysmithjava.prisoncore.entity.mines.WarrenConf;
import com.kennedysmithjava.prisoncore.entity.player.MPlayer;
import com.kennedysmithjava.prisoncore.entity.mines.Mine;
import com.kennedysmithjava.prisoncore.npc.SkinManager;
import com.kennedysmithjava.prisoncore.quest.QuestPhase;
import com.kennedysmithjava.prisoncore.quest.QuestPhaseGroup;
import com.kennedysmithjava.prisoncore.util.Color;
import com.kennedysmithjava.prisoncore.util.regions.Offset;
import com.massivecraft.massivecore.util.MUtil;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.ai.Navigator;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.trait.HologramTrait;
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

    private static String prefix = " \n \n \n &7&m-------------------------------------------------- \n &r&7&o[NPC] &r&e&lWarren the Warden:&7\n";
    private static String suffix = "\n &7&m-------------------------------------------------- \n";

    private static final List<String> messages = MUtil.list(
            prefix + "&r&fWelcome to your new home. You do the crime, you do the time." + "\n" +
                    "&r&fFollow me, I'll show you around. " + suffix,
            ////////////////////////////////////////////////////////////////////////////////////////////////////////////
            "\n \n &r&7&o[NPC] &r&e&lWarren the Warden: &fThis area here is your &eMine&f!"  + "\n" +
            "You're expected to mine blocks here."  + "\n" +
            "You can give me those blocks for &a ⛁Cash&f!",
            ////////////////////////////////////////////////////////////////////////////////////////////////////////////
            "&r&7&o[NPC] &r&e&lWarren the Warden: &fIt's not much yet... but maybe for some &a⛁Cash &fI can fix your &eMine &fup." + "\n" +
            "Talk to me later, I can improve the &eWidth, Height&f, &fand the &eMaterials &f (and much more) of your &eMine&f." + "\n" +
            "With good behavior (and for a price), I can even improve your &eMine&f's &eScenery&f. So no sassing!",
            ////////////////////////////////////////////////////////////////////////////////////////////////////////////
            "&r&7&o[NPC] &r&e&lWarren the Warden: &fI'll be watching you from here. No funny business!" + "\n" +
            "Don't bother asking me if you have any questions... " + "\n" +
            "...but the command &e/help&f might be useful."  + "\n" +
            "You'll probably need a &ePickaxe&f. Here, I'll give you one." + "\n" +
            "Now get working! No funny business." + "\n \n" +
            "&7You've been given &e+1 &6Old Rusty Pickaxe&7!"
    );

    private static final List<Offset> locations = MUtil.list(
            new Offset(-51, 50, -60), //In front of the mine
            new Offset(-41, 50, -50), //To the west of the mine
            new Offset(-51, 50, -30) //In front of NPC stands
    );


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
                npc = PrisonCore.getNonPersistNPCRegistry().createNPC(EntityType.PLAYER,Color.get("&eWarren the Warden"));
                if(npc.getId() == 0) npc = PrisonCore.getNonPersistNPCRegistry().createNPC(EntityType.PLAYER,Color.get("&eWarren the Warden"));
                SkinManager.skin(npc, "WarrenTutorial-" + npc.hashCode() + "-" + Math.random(), WarrenConf.get().warrenSkin, 15);
                npc.setProtected(true);
                npc.spawn(spawnLocation);

                this.navigator = npc.getNavigator();
                player.sendMessage(Color.get(messages.get(0)));

                new BukkitRunnable() {
                    @Override
                    public void run() {
                        startDialogue();
                    }
                }.runTaskLater(PrisonCore.get(), 7L*20);

            }

            @Override
            public void interrupted() {
                npc.destroy();
            }

            public void startDialogue() {


                new BukkitRunnable() {

                    int finishedMessages = 0;
                    boolean npcSpeaking = false;
                    boolean npcReachedDestination = false;
                    boolean finished = false;

                    Location target = player.getLocation().add(0, -10, 0);

                    @Override
                    public void run() {

                        if (npcSpeaking) return;
                        if (navigator.isNavigating()) return;
                        if (player == null) {
                            interrupted();
                            this.cancel();
                            return;
                        }

                        if (!npcReachedDestination && locationsEqual(npc.getEntity().getLocation(), target)) npcReachedDestination = true;
                        finished = (messages.size() - 1 == finishedMessages) && npcReachedDestination;

                        if (finished) {
                            npc.destroy();
                            mine.spawnResearcherNPC();
                            completed(true);
                            this.cancel();
                        }else{
                            target = getLocation(locations.get(finishedMessages));
                            navigator.setTarget(target);
                            if(npcReachedDestination){
                                sendMessage(messages.get(finishedMessages), player);
                                npcSpeaking = true;
                                new BukkitRunnable() {
                                    @Override
                                    public void run() {
                                        npcSpeaking = false;
                                        npcReachedDestination = false;
                                        finishedMessages++;
                                    }
                                }.runTaskLater(PrisonCore.get(), 100L);
                            }
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

    }

    public static IntroductionTutorial get() {
        return i;
    }

    @Override
    public String getName() {
        return "IntroductionTutorial";
    }


    private boolean locationsEqual(Location loc1, Location loc2){
        return loc1.distance(loc2) < 2;
    }

    @Override
    public QuestPhaseGroup getNextTutorialGroup() {
        return null;
    }

}
