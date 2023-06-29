package com.kennedysmithjava.prisoncore.quest.paths;

import com.kennedysmithjava.prisoncore.crafting.objects.PrisonMetal;
import com.kennedysmithjava.prisoncore.crafting.objects.type.MetalType;
import com.kennedysmithjava.prisoncore.eco.CurrencyType;
import com.kennedysmithjava.prisoncore.entity.mines.WarrenConf;
import com.kennedysmithjava.prisoncore.entity.player.MPlayer;
import com.kennedysmithjava.prisoncore.entity.tools.PickaxeType;
import com.kennedysmithjava.prisoncore.quest.Quest;
import com.kennedysmithjava.prisoncore.quest.QuestDifficulty;
import com.kennedysmithjava.prisoncore.quest.QuestPath;
import com.kennedysmithjava.prisoncore.quest.reward.QuestReward;
import com.kennedysmithjava.prisoncore.quest.quests.QuestBreakBlock;
import com.kennedysmithjava.prisoncore.quest.quests.QuestInteractBlock;
import com.kennedysmithjava.prisoncore.quest.quests.QuestPromptPickupItem;
import com.kennedysmithjava.prisoncore.quest.quests.QuestWalkAndTalk;
import com.kennedysmithjava.prisoncore.quest.reward.QuestRewardPrisonObject;
import com.kennedysmithjava.prisoncore.util.ItemBuilder;
import com.kennedysmithjava.prisoncore.regions.Offset;
import com.massivecraft.massivecore.util.MUtil;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.awt.*;
import java.util.List;
public class PathIntroduction extends QuestPath {

    /**
     * Remember: Register all QuestPath objects with PrisonCore plugin.
     */

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
        return MUtil.list(
                new QuestRewardPrisonObject(new PrisonMetal(MetalType.ADAMANTINE_INGOT), 64),
                new QuestRewardPrisonObject(new PrisonMetal(MetalType.IRON_INGOT), 64)
        );
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
                        MUtil.list(
                                "&7&l ???",
                                " &7\"Hey you!",
                                " &7Where do you think you're wandering off to?\""
                        ),
                        MUtil.list(
                                "&6&lWarren the Warden",
                                " &7\"I'm &eWarren&7, the Warden around here.",
                                " &7You'll be working for me, for the time being.",
                                " &7I'll walk you through things to get started.\""
                        ),
                        MUtil.list(
                                "&6&lWarren the Warden",
                                " &7\"This pit down here is your &eMine.",
                                " &7You can exchange these blocks &7for &a" + CurrencyType.CASH.getSymbol() + " " + CurrencyType.CASH.getDisplayName(),
                                " &7if you speak with &eCaleb the Collector&7.\""
                        ),
                        MUtil.list(
                                "&6&lWarren the Warden",
                                "&7\"Here, grab this pickaxe and mine a few blocks.",
                                " &7&eMetal &7can also be found while mining, which",
                                " &7can be used to forge new &e Pickaxes&7!\"",
                                " &a- New Task: &7Pick up the &ePickaxe",
                                " &a- New Task: &7Mine &e10 &7blocks."
                        )
                ),
                false,
                6*20,
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

                        MUtil.list(
                                "&6&lWarren the Warden",
                                " &7\"Nice work.",
                                " &7Those &eBlocks &7aren't useful on their own,",
                                " &7But you can sell them &7for &aCash",
                                " &7if you speak with &eColin the Collector&7!\""
                        ),
                        MUtil.list(
                                "&6&lWarren the Warden",
                                " &7\"&a" + CurrencyType.CASH.getSymbol() + " " + CurrencyType.CASH.getDisplayName() + " &7 is very useful for spending on",
                                " &7Enchantments, Auto Miners&7,",
                                " &7Mine Upgrades & Decorations&7, &7and your",
                                " &7&ePlayer Level&7 if you speak with me later!\""
                        ),
                        MUtil.list(
                                "&6&lWarren the Warden",
                                " &7\"Anyways, it looks like your mine is",
                                " &7almost out of blocks. Flip this lever to",
                                " &7regenerate the blocks in your mine!\"",
                                " &r",
                                " &a- New Task: &7Flip the Regen Lever"
                        )
                ),
                false,
                6*20,
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
                        MUtil.list(
                                "&6&lWarren the Warden",
                                " &7\"You seem like you'd get lost easily.",
                                " &7I'll give you a map &6&lMap&7 in a moment.",
                                " &7You can use it to track Quests&7,",
                                " &7view nearby NPCs, find shops, and more!\""
                        ),
                        MUtil.list(
                                "&6&lWarren the Warden",
                                " &7\"I'll be over here if you need me.",
                                " &7&eArchie &7 will be on my left to help you ",
                                " &7edit your &eMine. &eColin &7will be on ",
                                " &7my right if you need to sell &eblocks.",
                                " &r",
                                " &7Now get working!\""
                        )
                ),
                true,
                6*20,
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
    public QuestDifficulty getDifficulty() {
        return QuestDifficulty.TUTORIAL;
    }

    @Override
    public void onCompleteQuest(int questPart, MPlayer player) {
        if(questPart == 5){
            //TODO: Give player a map here
            player.getQuestProfile().addUnlockedQuestPath(PathStarterGateway.get());
        }
    }

}
