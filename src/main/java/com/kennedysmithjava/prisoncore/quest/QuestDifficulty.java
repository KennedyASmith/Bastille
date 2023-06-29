package com.kennedysmithjava.prisoncore.quest;

public enum QuestDifficulty {
    EASY("&aEasy"),
    TUTORIAL("&aTutorial"),
    MEDIUM("&eMedium"),
    HARD("&cHard"),
    IMPOSSIBLE("&4Impossible");

    private final String difficultyString;
    QuestDifficulty(String difficultyString){
        this.difficultyString = difficultyString;
    }

    public String getDifficultyString() {
        return difficultyString;
    }
}
