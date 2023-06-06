package com.kennedysmithjava.prisoncore.quest;

import com.kennedysmithjava.prisoncore.PrisonCore;

import java.util.*;

public class QuestPathRegistry {


    public static QuestPathRegistry i = new QuestPathRegistry();
    public static QuestPathRegistry get() {
        return i;
    }

    private final Map<String, QuestPath> questClassMap;

    public QuestPathRegistry() {
        questClassMap = new HashMap<>();
    }

    public void registerQuestClass(QuestPath questClass) {
        questClassMap.put(questClass.getClass().getSimpleName(), questClass);
    }

    public QuestPath getQuest(String questName) {
        return questClassMap.get(questName);
    }

}
