package com.kennedysmithjava.prisoncore.quest;

import com.kennedysmithjava.prisoncore.PrisonCore;
import com.kennedysmithjava.prisoncore.entity.player.MPlayer;
import com.kennedysmithjava.prisoncore.event.EventTutorialGroupComplete;
import org.bukkit.Bukkit;

import java.util.HashMap;
import java.util.Map;

public class QuestProfile {

    MPlayer player;
    Map<QuestPhaseGroup, QuestPhase> activeQuests = new HashMap<>();

    public QuestProfile(MPlayer player) {
        this.player = player;
    }

    public void startQuest(QuestPhaseGroup phaseGroup) {
        startQuest(phaseGroup, 0);
    }

    public void startQuest(QuestPhaseGroup eventGroup, int phaseIndex) {
        //Save quest to the player's data file
        PrisonCore.activeTutorials.put(player, this);
        player.setActiveQuest(eventGroup.getName(), phaseIndex);

        //Get the phase from the event group based on the index given.
        QuestPhase phase = eventGroup.getPhases().get(phaseIndex);
        phase.startThisPhase(this);

        //Store this information in the player's active QuestProfile
        activeQuests.put(eventGroup, phase);
    }

    public void rejoinQuest(QuestPhaseGroup eventGroup, int phaseIndex) {
        //Get the phase from the event group based on the index given.
        /*
        QuestPhase phase = eventGroup.getPhases().get(phaseIndex);
        phase.startThisPhase(this);

        //Store this information in the player's active QuestProfile
        activeQuests.put(eventGroup, phase);
         */
    }

    public MPlayer getPlayer() {
        return player;
    }

    public void nextPhase(QuestPhaseGroup group, QuestPhase currentPhase) {
        if(group.hasNextPhase(currentPhase)){
            QuestPhase nextPhase = group.nextPhase(currentPhase);
            activeQuests.put(group, nextPhase);
            nextPhase.startThisPhase(this);
            player.setActiveQuest(group.getName(), group.indexOf(nextPhase));
        }else{
            Bukkit.getServer().getPluginManager().callEvent(new EventTutorialGroupComplete(player, group));
            removeQuest(group);
        }
    }

    public Map<QuestPhaseGroup, QuestPhase> getActiveQuests() {
        return activeQuests;
    }

    public void removeQuest(QuestPhaseGroup group){
        activeQuests.remove(group);
        player.removeQuest(group.getName());
    }

}
