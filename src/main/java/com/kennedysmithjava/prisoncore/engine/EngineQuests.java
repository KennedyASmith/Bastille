package com.kennedysmithjava.prisoncore.engine;


import com.kennedysmithjava.prisoncore.entity.player.MPlayer;
import com.kennedysmithjava.prisoncore.entity.player.MPlayerColl;
import com.kennedysmithjava.prisoncore.event.EventNewMine;
import com.kennedysmithjava.prisoncore.event.EventNewPlayerJoin;
import com.kennedysmithjava.prisoncore.event.EventReturningPlayerJoin;
import com.kennedysmithjava.prisoncore.quest.QuestPath;
import com.kennedysmithjava.prisoncore.quest.paths._QPIntroduction;
import com.massivecraft.massivecore.Engine;
import com.massivecraft.massivecore.ps.PS;
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
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onNewMine(EventNewMine event) {
        MPlayer mPlayer = event.getPlayer();
        if(mPlayer == null) return;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onNewPlayerJoin(EventNewPlayerJoin event) {

    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onReturningPlayerJoin(EventReturningPlayerJoin event) {
        MPlayer player = event.getPlayer();

    }


    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerQuit(PlayerQuitEvent event) {
        MPlayer player = MPlayerColl.get().getByPlayer(event.getPlayer());
        player.interruptAnyActiveQuest();
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerKick(PlayerKickEvent event) {
        MPlayer player = MPlayerColl.get().getByPlayer(event.getPlayer());
        player.interruptAnyActiveQuest();
    }


}
