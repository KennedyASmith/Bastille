package com.kennedysmithjava.prisoncore.quest;

import com.kennedysmithjava.prisoncore.PrisonCore;

import java.lang.reflect.Field;
import java.util.*;

public class QuestRegistry {


    public static QuestRegistry i = new QuestRegistry();
    public static QuestRegistry get() {
        return i;
    }

    private final Map<String, Class<? extends Quest>> questClassMap;

    public QuestRegistry() {
        questClassMap = new HashMap<>();
    }

    public void registerQuestClass(Class<? extends Quest> questClass) {
        questClassMap.put(questClass.getSimpleName(), questClass);
    }

    public Class<? extends Quest> getQuestClass(String questName) {
        return questClassMap.get(questName);
    }


}
