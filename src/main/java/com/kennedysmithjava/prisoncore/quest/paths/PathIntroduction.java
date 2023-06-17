package com.kennedysmithjava.prisoncore.quest.paths;

import com.kennedysmithjava.prisoncore.entity.mines.WarrenConf;
import com.kennedysmithjava.prisoncore.entity.player.MPlayer;
import com.kennedysmithjava.prisoncore.entity.tools.PickaxeType;
import com.kennedysmithjava.prisoncore.quest.Quest;
import com.kennedysmithjava.prisoncore.quest.QuestPath;
import com.kennedysmithjava.prisoncore.quest.QuestReward;
import com.kennedysmithjava.prisoncore.quest.quests.QuestBreakBlock;
import com.kennedysmithjava.prisoncore.quest.quests.QuestInteractBlock;
import com.kennedysmithjava.prisoncore.quest.quests.QuestPromptPickupItem;
import com.kennedysmithjava.prisoncore.quest.quests.QuestWalkAndTalk;
import com.kennedysmithjava.prisoncore.util.ItemBuilder;
import com.kennedysmithjava.prisoncore.util.regions.Offset;
import com.massivecraft.massivecore.util.MUtil;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.awt.*;
import java.util.List;
public class PathIntroduction extends QuestPath {

    private static PathIntroduction i = new PathIntroduction();
    public static PathIntroduction get() {
        return i;
    }
    private static final Offset PART_1_POS_1_OFFSET = new Offset(-51, 50, -56, 1F, 180F); //Spawn
    private static final Offset PART_1_POS_2_OFFSET = new Offset(-51, 50, -63, 1F, 180F); //Walk up to player
    private static final Offset PART_1_POS_3_OFFSET = new Offset(-49, 51, -52, 47F,46F ); // Looking into mine
    private static final Offset PART_1_POS_4_OFFSET = new Offset(-49, 51, -52, 47F,-130F ); // Looking at pickaxe
    private static final Offset PICKAXE_OFFSET = new Offset(-48, 50, -53); // Pickaxe
    private static final Offset PART_2_POS_1_OFFSET = new Offset(-54, 51, -54, 12F, -89F); //Stand in front of lever
    private static final Offset PART_2_POS_2_OFFSET = new Offset(-54, 51, -54, 23F, -35F); //Looking at lever
    private static final Offset PART_3_POS_1_OFFSET = new Offset(-51, 50, -29, 9F, -179F); //In front of Stands

    @Override
    public List<QuestReward> getPathRewards() {
        return null;
    }

    @Override
    public String getQuestPathDisplayName() {
        return "&6Introduction";
    }


    @Override
    public ItemStack getPathIcon() {
        return new ItemBuilder(Material.COMPASS).name(getQuestPathDisplayName()).build();
    }

    @Override
    public void onActivate(MPlayer player) {
        Bukkit.broadcastMessage("You've activated the QuestPath!");
    }

    public List<Quest> getInitializedQuests(MPlayer player){
        Location origin = player.getMine().getOrigin();
        Location leverLocation = player.getMine().getLeverLocation();

        /* Part 1: Introduce Warren */
        QuestWalkAndTalk part_1 = new QuestWalkAndTalk(player, origin,
                MUtil.list(
                        PART_1_POS_1_OFFSET,
                        PART_1_POS_2_OFFSET,
                        PART_1_POS_3_OFFSET,
                        PART_1_POS_4_OFFSET),
                MUtil.list(
                        "Hey you!",
                        "I'm Warren, the Warden around here.",
                        "This here is your mine",
                        "Take this pickaxe and mine 10 blocks."
                ),
                false,
                3*20,
                "Warren",
                WarrenConf.get().warrenSkin);

        /* Part 2: Pick up the pickaxe */
        PickaxeType type = PickaxeType.get("novice_pick");
        ItemStack pickaxe = type.getItemStack();
        QuestPromptPickupItem part_2 = new QuestPromptPickupItem(player, origin, PICKAXE_OFFSET, pickaxe, "&fNovice Pickaxe", Color.GREEN);

        /* Part 4: Break 10 blocks of any type */
        QuestBreakBlock part_3 = new QuestBreakBlock(player, 10, true);

        /* Part 4: Warren talks about running out of blocks*/
        NPC npc = part_1.getNpc(); //Get the NPC from part 1
        QuestWalkAndTalk part_4 = new QuestWalkAndTalk(player, origin,
                MUtil.list(
                        PART_1_POS_4_OFFSET,
                        PART_2_POS_1_OFFSET,
                        PART_2_POS_2_OFFSET),
                MUtil.list(
                        "Good work.",
                        "Your mine is almost out of blocks!",
                        "Press this lever to regen your mine!"
                ),
                false,
                3*20,
                "Warren",
                WarrenConf.get().warrenSkin,
                npc);

        /* Part 5: Flip the lever */
        QuestInteractBlock part_5 = new QuestInteractBlock(player, leverLocation, true, "the lever.");

        /* Part 5: Walk over to NPC stands */
        QuestWalkAndTalk part_6 = new QuestWalkAndTalk(player, origin,
                MUtil.list(PART_2_POS_2_OFFSET,
                        PART_3_POS_1_OFFSET),
                MUtil.list(
                        "Great work. ",
                        "I'll be over here if you need me."
                ),
                true,
                3*20,
                "Warren",
                WarrenConf.get().warrenSkin,
                npc);

        /* Submit all quest parts */
        return MUtil.list(
                part_1,
                part_2,
                part_3,
                part_4,
                part_5,
                part_6
        );
    }

    @Override
    public void onCompleteQuest(int questPart) {
        switch (questPart) {
            case 0 -> Bukkit.broadcastMessage("You finished part 1.");
            case 1 -> Bukkit.broadcastMessage("You finished part 2.");
        }
    }

}
