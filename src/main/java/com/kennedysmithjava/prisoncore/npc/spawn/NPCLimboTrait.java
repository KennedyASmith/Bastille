package com.kennedysmithjava.prisoncore.npc.spawn;

import com.kennedysmithjava.prisoncore.PrisonCore;
import com.kennedysmithjava.prisoncore.engine.EngineLimbo;
import com.kennedysmithjava.prisoncore.engine.EngineLoadingScreen;
import com.kennedysmithjava.prisoncore.entity.player.MPlayer;
import com.kennedysmithjava.prisoncore.entity.player.MPlayerColl;
import com.kennedysmithjava.prisoncore.quest.eventGroup.IntroductionTutorial;
import net.citizensnpcs.api.event.NPCLeftClickEvent;
import net.citizensnpcs.api.event.NPCRightClickEvent;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.trait.Trait;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

@SuppressWarnings("ConstantConditions")
public class NPCLimboTrait extends Trait {

    public static List<Player> activeUsers = new ArrayList<>();

    public NPCLimboTrait() {
        super("limbowarrentrait");
    }

    public static List<Player> getActiveUsers() {
        return activeUsers;
    }

    @EventHandler
    public void click(NPCRightClickEvent event) {
        //Handle a click on a NPC.
        //Be sure to check event.getNPC() == this.getNPC() so you only handle clicks on this NPC!
        if(event.getNPC() != this.getNPC()) return;
        if(!event.getNPC().hasTrait(NPCLimboTrait.class)) return;
        NPC npc = event.getNPC();
        Player player = event.getClicker();
        if (player == null) return;
        //Ensure that the clicker isn't already in the Limbo NPC's dialogue.
        if (activeUsers.contains(player)) return;
        activeUsers.add(player);

        MPlayer mPlayer = MPlayerColl.get().getByPlayer(player);
        if (mPlayer == null) return;

        if (!mPlayer.hasMine()) {

            EngineLimbo.get().removeFromLimbo(player);
            EngineLoadingScreen.addLoadingScreen(player, player.getLocation());
            AtomicBoolean mineFinished = new AtomicBoolean(false);

            //TODO: Queue system for this
            //A runnable to create the mine itself.
            new BukkitRunnable() {
                @Override
                public void run() {
                    //Create the mine, set mineFinished to true if completed pasting.
                    PrisonCore.createMine(mPlayer, () -> mineFinished.set(true));
                }
            }.runTaskLater(PrisonCore.get(), 20*2);

            //A runnable to remove the player from limbo when mine is finished building.
            new BukkitRunnable() {
                @Override
                public void run() {
                    if (mineFinished.get()) {
                        concludeLimbo(mPlayer);
                        this.cancel();
                    }
                }
            }.runTaskTimer(PrisonCore.get(), 0, 20 * 2);

        } else {
            //Somehow, if the player already has a mine, teleport them there
            player.sendMessage("Teleported you back to your prison cell.");
            player.teleport(mPlayer.getMine().getSpawnPoint());
            EngineLimbo.get().removeFromLimbo(player);
        }

        activeUsers.remove(player);
    }

    @EventHandler
    public void click(NPCLeftClickEvent event) {

    }


    // Called every tick
    @Override
    public void run() {
    }

    //Run code when your trait is attached to a NPC.
    @Override
    public void onAttach() {

    }

    // Run code when the NPC is despawned. This is called before the entity actually despawns so npc.getEntity() is still valid.
    @Override
    public void onDespawn() {

    }

    //Run code when the NPC is spawned. Note that npc.getEntity() will be null until this method is called.
    //This is called AFTER onAttach and AFTER Load when the server is started.
    @Override
    public void onSpawn() {

    }

    //run code when the NPC is removed. Use this to tear down any repeating tasks.
    @Override
    public void onRemove() {

    }

    public void concludeLimbo(MPlayer mPlayer){
        Player player = mPlayer.getPlayer();
        if (player == null) return;
        int tpDelay = 3 * 20;
        //Delayed teleport
        new BukkitRunnable() {
            @Override
            public void run() {
                if(player == null) return;
                player.teleport(mPlayer.getMine().getSpawnPoint());
                EngineLoadingScreen.removeLoadingScreen(player.getPlayer(), 0);
                mPlayer.getQuestProfile().startQuest(IntroductionTutorial.get());
            }
        }.runTaskLater(PrisonCore.get(), tpDelay);
    }

}
