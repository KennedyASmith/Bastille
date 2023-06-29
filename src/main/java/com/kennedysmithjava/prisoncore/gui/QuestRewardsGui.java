package com.kennedysmithjava.prisoncore.gui;

import com.kennedysmithjava.prisoncore.entity.player.MPlayer;
import com.kennedysmithjava.prisoncore.quest.QuestPath;
import com.kennedysmithjava.prisoncore.quest.reward.QuestReward;
import com.massivecraft.massivecore.chestgui.ChestGui;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Map;

public class QuestRewardsGui extends BaseGui{


    private final Map<QuestPath, List<QuestReward>> questRewards;
    public QuestRewardsGui(Player player, Map<QuestPath, List<QuestReward>> questRewards) {
        super(player, "&8&lClaim Quest Rewards", 5, false, false);
        this.questRewards = questRewards;
    }

    @Override
    public void onBuild(Player player, ChestGui gui, Inventory inventory) {
        MPlayer mPlayer = MPlayer.get(player);
        for (Map.Entry<QuestPath, List<QuestReward>> set : questRewards.entrySet()) {
            String pathDisplayName = set.getKey().getQuestPathDisplayName();
            List<QuestReward> pathRewards = set.getValue();
            for (int i = 0; i < pathRewards.size(); i++) {
                int firstEmpty = inventory.firstEmpty();
                if(firstEmpty == -1) return;
                QuestReward reward = pathRewards.get(i);
                ItemStack item = reward.getItem(pathDisplayName);
                setItem(firstEmpty, item);
                setAction(firstEmpty, reward.getAction(mPlayer, this, set.getKey().getClass().getSimpleName(), i));
            }
        }
    }

}
