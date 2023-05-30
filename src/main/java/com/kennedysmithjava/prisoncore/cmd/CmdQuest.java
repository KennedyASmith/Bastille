package com.kennedysmithjava.prisoncore.cmd;

import com.kennedysmithjava.prisoncore.entity.player.MPlayer;
import com.kennedysmithjava.prisoncore.entity.player.QuestProfile;
import com.kennedysmithjava.prisoncore.quest.QuestListGUI;
import com.kennedysmithjava.prisoncore.quest.QuestPath;
import com.massivecraft.massivecore.util.MUtil;
import org.bukkit.command.CommandSender;

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
    public void execute(CommandSender sender, List<String> args) {
        super.execute(sender, args);
        if(senderIsConsole) return;
        if(sender == null) return;
        if(args.size() > 1) return;
        MPlayer player = MPlayer.get(sender);
        QuestProfile questProfile = player.getQuestProfile();
        List<QuestPath> unlockedQuests = questProfile.getUnlockedQuests();
        List<QuestPath> activeQuests = questProfile.getCurrentQuests();
        List<String> completedQuests = questProfile.getCompletedQuests();
        QuestListGUI questListGUI = new QuestListGUI(player, unlockedQuests, activeQuests, completedQuests);
        questListGUI.open();
    }
}
