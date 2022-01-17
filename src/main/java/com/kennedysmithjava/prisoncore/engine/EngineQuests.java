package com.kennedysmithjava.prisoncore.engine;

import com.kennedysmithjava.prisoncore.entity.player.MPlayer;
import com.kennedysmithjava.prisoncore.entity.player.MPlayerColl;
import com.kennedysmithjava.prisoncore.event.EventNewMine;
import com.kennedysmithjava.prisoncore.event.NewPlayerJoinEvent;
import com.kennedysmithjava.prisoncore.event.ReturningPlayerJoinEvent;
import com.kennedysmithjava.prisoncore.event.TutorialEventGroupComplete;
import com.massivecraft.massivecore.Engine;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.ArrayList;
import java.util.List;

public class EngineQuests extends Engine {

    private static final EngineQuests i = new EngineQuests();

    public static EngineQuests get() {
        return i;
    }

    public static List<MPlayer> interruptQuestQueue = new ArrayList<>();

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onNewMine(EventNewMine event) {
        MPlayer mPlayer = event.getPlayer();
        Player player = mPlayer.getPlayer();
        if(player == null) return;

    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onNewPlayerJoin(NewPlayerJoinEvent event) {

    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onReturningPlayerJoin(ReturningPlayerJoinEvent event) {
        MPlayer player = event.getPlayer();

    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onTutorialComplete(TutorialEventGroupComplete event) {
        MPlayer player = event.getPlayer();


    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerQuit(PlayerQuitEvent event) {
        MPlayer player = MPlayerColl.get().getByPlayer(event.getPlayer());
        if(!interruptQuestQueue.contains(player)){
            interruptQuestQueue.add(player);
            interruptQuests();
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerKick(PlayerKickEvent event) {
        MPlayer player = MPlayerColl.get().getByPlayer(event.getPlayer());
        if(!interruptQuestQueue.contains(player)){
            interruptQuestQueue.add(player);
            interruptQuests();
        }
    }

    void interruptQuests(){
        interruptQuestQueue.forEach(player -> player.getQuestProfile().getActiveQuests().forEach((questPhaseGroup, questPhase) -> questPhase.interrupted()));
    }

}
