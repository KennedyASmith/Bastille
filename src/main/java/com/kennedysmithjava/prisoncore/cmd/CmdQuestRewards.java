package com.kennedysmithjava.prisoncore.cmd;

import com.kennedysmithjava.prisoncore.entity.player.MPlayer;
import com.kennedysmithjava.prisoncore.entity.player.QuestProfile;
import com.kennedysmithjava.prisoncore.gui.QuestRewardsGui;
import com.kennedysmithjava.prisoncore.quest.QuestPath;
import com.kennedysmithjava.prisoncore.quest.reward.QuestReward;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.requirement.RequirementIsPlayer;

import java.util.List;
import java.util.Map;

public class CmdQuestRewards extends CoreCommand {

    public CmdQuestRewards() {

        // Aliases
        this.addRequirements(RequirementIsPlayer.get());
        this.addAliases("rewards");
        this.setSetupPermBaseClassName("QUEST");

    }

    @Override
    public void perform() throws MassiveException {
        MPlayer player = MPlayer.get(me);
        QuestProfile profile = player.getQuestProfile();
        if(profile.getUnclaimedQuestData().size() > 0){
            Map<QuestPath, List<QuestReward>> rewards = profile.getUnclaimedQuestRewards();
            QuestRewardsGui gui = new QuestRewardsGui(me, rewards);
            gui.open();
        }else {
            player.msg("&7[&bQuests&7] You have no unclaimed rewards!");
        }
    }

}
