package com.kennedysmithjava.prisoncore.quest;

import com.kennedysmithjava.prisoncore.quest.eventGroup.IntroductionTutorial;

import java.util.HashMap;
import java.util.Map;

public class QuestManager {

    public Map<String, QuestPhaseGroup> groupMap = new HashMap<>();

    public QuestManager(){
        registerQuestGroup(IntroductionTutorial.get());
    }

    private void registerQuestGroup(QuestPhaseGroup phaseGroup){
        groupMap.put(phaseGroup.getName(), phaseGroup);
    }

    public QuestPhaseGroup get(String name){
        return groupMap.get(name);
    }

}
