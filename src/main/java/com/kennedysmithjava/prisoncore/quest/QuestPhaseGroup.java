package com.kennedysmithjava.prisoncore.quest;

import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.List;


public abstract class QuestPhaseGroup {

    List<QuestPhase> phases = new ArrayList<>();

    public QuestPhaseGroup() {
    }

    public void addEvent(QuestPhase event) {
        phases.add(event);
    }

    public List<QuestPhase> getPhases() {
        return phases;
    }

    public abstract String getName();

    public abstract QuestPhaseGroup getNextTutorialGroup();

    public boolean hasNextPhase(QuestPhase currentPhase){
        boolean value = phases.get(phases.indexOf(currentPhase) + 1) != null;;
        if(!value) Bukkit.broadcastMessage("There's not a next phase for this quest group.");
        return value;
    }

    public QuestPhase nextPhase(QuestPhase currentPhase){
        return phases.get(phases.indexOf(currentPhase) + 1);
    }

    public int indexOf(QuestPhase phase){
        return phases.indexOf(phase);
    }
}

