package com.kennedysmithjava.prisoncore.cmd;

import com.kennedysmithjava.prisoncore.entity.player.MPlayer;
import com.kennedysmithjava.prisoncore.entity.player.QuestProfile;
import com.kennedysmithjava.prisoncore.quest.QuestListGUI;
import com.kennedysmithjava.prisoncore.quest.QuestPath;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.util.MUtil;
import org.bukkit.entity.Player;

import java.util.List;

public class CmdQuest extends CoreCommand {
    // -------------------------------------------- //
    // INSTANCE
    // -------------------------------------------- //

    private static final CmdQuest i = new CmdQuest();

    // -------------------------------------------- //
    // FIELDS
    // -------------------------------------------- //
    public CmdQuestHelp cmdQuestHelp = new CmdQuestHelp();

    // -------------------------------------------- //
    // CONSTRUCT
    // -------------------------------------------- //
    public CmdQuest() {

    }

    public static CmdQuest get() {
        return i;
    }

    // -------------------------------------------- //
    // OVERRIDE
    // -------------------------------------------- //

    @Override
    public List<String> getAliases() {
        return MUtil.list("quest", "quests", "q");
    }

    @Override
    public void perform() throws MassiveException {
        if (!(sender instanceof Player)) return;
        MPlayer player = MPlayer.get(sender);
        QuestProfile questProfile = player.getQuestProfile();
        List<QuestPath> unlockedQuests = questProfile.getUnlockedQuests();
        QuestPath activeQuestPath = questProfile.getActiveQuestPath();
        List<String> completedQuests = questProfile.getCompletedQuestsData();
        QuestListGUI questListGUI = new QuestListGUI(player, unlockedQuests, activeQuestPath, completedQuests);
        questListGUI.open();
    }
}
