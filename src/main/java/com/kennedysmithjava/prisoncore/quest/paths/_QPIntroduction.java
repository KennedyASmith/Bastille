package com.kennedysmithjava.prisoncore.quest.paths;

import com.kennedysmithjava.prisoncore.entity.mines.Mine;
import com.kennedysmithjava.prisoncore.entity.mines.WarrenConf;
import com.kennedysmithjava.prisoncore.entity.player.MPlayer;
import com.kennedysmithjava.prisoncore.quest.Quest;
import com.kennedysmithjava.prisoncore.quest.QuestPath;
import com.kennedysmithjava.prisoncore.quest.QuestReward;
import com.kennedysmithjava.prisoncore.quest.quests.QuestWalkAndTalk;
import com.kennedysmithjava.prisoncore.util.ItemBuilder;
import com.kennedysmithjava.prisoncore.util.regions.Offset;
import com.massivecraft.massivecore.ps.PS;
import com.massivecraft.massivecore.util.MUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

public class _QPIntroduction extends QuestPath {

    private static transient final Offset POS_1_OFFSET = new Offset(-51, 50, -63);
    private static transient final Offset POS_2_OFFSET = new Offset(-43, 50, -57);
    private static transient final Offset POS_3_OFFSET = new Offset(-50, 51, -31);


    private _QPIntroduction(List<Quest> quests, int currentProgress) {
        super(quests, currentProgress);
    }

    public _QPIntroduction(PS mineOrigin) {
        super(MUtil.list(
                new QuestWalkAndTalk(mineOrigin,
                        MUtil.list(
                                POS_1_OFFSET,
                                POS_2_OFFSET,
                                POS_3_OFFSET),
                        MUtil.list(
                                "Welcome, this is me, Warden!",
                                        "Here you can mine",
                                        "I'll be over here"
                                ),
                        3*20,
                        "Warren",
                        WarrenConf.get().warrenSkin
                        )
        ), 0);
    }

    @Override
    public List<QuestReward> getPathRewards() {
        return null;
    }

    @Override
    public String getQuestPathID() {
        return "introduction";
    }

    @Override
    public String getQuestPathDisplayName() {
        return "&6Introduction";
    }


    @Override
    public Map<Quest, List<Consumer<MPlayer>>> getQuestCompletionLogic() {
        List<Quest> quests = getQuestParts();
        return MUtil.map(
                quests.get(0), MUtil.list( mPlayer -> {})
        );
    }

    @Override
    public ItemStack getPathIcon() {
        return new ItemBuilder(Material.COMPASS).name(getQuestPathDisplayName()).build();
    }

    @Override
    public void onActivate(MPlayer player) {
        Bukkit.broadcastMessage("You've activated the QuestPath!");
        Bukkit.broadcastMessage("You are on part: " + getCurrentProgress());
    }

    @Override
    public void onCompleteQuest(int questPart) {
        switch (questPart) {
            case 0 -> Bukkit.broadcastMessage("You finished part 1.");
            case 1 -> Bukkit.broadcastMessage("You finished part 2.");
        }
    }

}
