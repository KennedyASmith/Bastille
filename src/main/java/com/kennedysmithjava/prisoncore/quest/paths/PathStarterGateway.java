package com.kennedysmithjava.prisoncore.quest.paths;

import com.kennedysmithjava.prisoncore.entity.mines.WarrenConf;
import com.kennedysmithjava.prisoncore.entity.npcs.SkinsConf;
import com.kennedysmithjava.prisoncore.entity.player.MPlayer;
import com.kennedysmithjava.prisoncore.npc.Skin;
import com.kennedysmithjava.prisoncore.quest.Quest;
import com.kennedysmithjava.prisoncore.quest.QuestMessage;
import com.kennedysmithjava.prisoncore.quest.QuestPath;
import com.kennedysmithjava.prisoncore.quest.QuestReward;
import com.kennedysmithjava.prisoncore.quest.quests.QuestMineBuildBuilding;
import com.kennedysmithjava.prisoncore.util.BuildingType;
import com.kennedysmithjava.prisoncore.util.ItemBuilder;
import com.massivecraft.massivecore.util.MUtil;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class PathStarterGateway extends QuestPath {

    /**
     * Remember: Register all QuestPath objects with PrisonCore plugin.
     */

    private static PathStarterGateway i = new PathStarterGateway();
    public static PathStarterGateway get() {
        return i;
    }

    @Override
    public List<QuestReward> getPathRewards() {
        return null;
    }

    @Override
    public String getQuestPathDisplayName() {
        return "&6Gateway To Freedom";
    }


    @Override
    public ItemStack getPathIcon() {
        return new ItemBuilder(Material.FEATHER).name(getQuestPathDisplayName()).build();
    }

    @Override
    public void onActivate(MPlayer player) {
        String warrenName = WarrenConf.get().researcherName;
        Skin warrenSkin = SkinsConf.get().getSkin(warrenName);
        List<String> message = MUtil.list(
                "&6&lWarren the Warden &7(Quest)",
                "\"&7You've been doing well. I'll do you",
                " &7a little favor. I'll let you travel into",
                " &eTown &7periodically if you get me a few things.\"",
                " &r",
                " &a- New Task: &7Purchase & build a &bPortal!",
                " &8&oHint: Reach Player Level 2 & speak to Warren"
        );
        QuestMessage questMessage = new QuestMessage(warrenName, warrenSkin, message);
        questMessage.sendMessage(player.getPlayer());
    }

    public List<Quest> getInitializedQuests(MPlayer player){
        QuestMineBuildBuilding part_1 = new QuestMineBuildBuilding(player, BuildingType.PORTAL);

        /* Submit all quest parts */
        return MUtil.list(
                part_1
        );
    }

    @Override
    public void onCompleteQuest(int questPart, MPlayer player) {

    }

}
