package com.kennedysmithjava.prisoncore.quest;

import com.kennedysmithjava.prisoncore.entity.player.MPlayer;
import com.kennedysmithjava.prisoncore.entity.player.QuestProfile;
import com.kennedysmithjava.prisoncore.gui.QuestRewardsGui;
import com.kennedysmithjava.prisoncore.quest.reward.QuestReward;
import com.kennedysmithjava.prisoncore.util.Color;
import com.kennedysmithjava.prisoncore.util.ItemBuilder;
import com.massivecraft.massivecore.chestgui.ChestAction;
import com.massivecraft.massivecore.chestgui.ChestGui;
import com.massivecraft.massivecore.util.MUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
     This class creates a GUI for displaying a list of quests to a player.
     The GUI allows the player to view and navigate through their active, unlocked, and completed quests.
     Author: KennedyASmith
     @see com.kennedysmithjava.prisoncore.quest.QuestPath
 */

public class QuestListGUI {

    private final MPlayer player;

    private final ChestGui firstGuiMenu;

    /**
     * Constructs a new QuestListGUI object for the specified player,
     * with the given lists of unlocked quest paths, active quest paths,
     * and completed quests.
     *
     * @param player             the player for whom the GUI is created
     * @param unlockedQuestPaths the list of unlocked quest paths
     * @param activeQuestPath    the active quest path
     * @param completedQuests    the list of completed quests
     */
    public QuestListGUI(MPlayer player, List<QuestPath> unlockedQuestPaths, QuestPath activeQuestPath, List<String> completedQuests) {
        this.player = player;
        firstGuiMenu = getQuestInventory(activeQuestPath, unlockedQuestPaths, completedQuests.stream().sorted().toList(), null);
    }

    /**
     * Opens the quest list GUI for the player.
     */
    public void open(){
        if(player.getPlayer() == null) return;
        player.getPlayer().openInventory(firstGuiMenu.getInventory());
    }

    /**
     * Creates and returns the quest inventory GUI based on the given lists of active quest paths,
     * unlocked quest paths, completed quests, and the last page GUI.
     *
     * @param activeQuestPath   the active quest path (can be null)
     * @param unlockedQuestPaths the list of unlocked quest paths
     * @param completedQuests    the list of completed quests
     * @param lastPage           the last page GUI
     * @return the quest inventory GUI
     */
    @SuppressWarnings("DataFlowIssue")
    private ChestGui getQuestInventory(QuestPath activeQuestPath, List<QuestPath> unlockedQuestPaths, List<String> completedQuests, ChestGui lastPage){
        Inventory inventory = Bukkit.createInventory(null, 6*9, Color.get("&8&lPick a Quest"));
        ChestGui gui = ChestGui.getCreative(inventory);
        gui.setAutoremoving(true);
        gui.setBottomInventoryAllow(false);
        gui.setAutoclosing(true);

        ItemStack nextPageItem = new ItemBuilder(Material.ARROW)
                .name("&aNext Page")
                .lore(MUtil.list("&7Click to view the next page of quests!"))
                .build();

        ItemStack lastPageItem = new ItemBuilder(Material.ARROW)
                .name("&aLast Page")
                .lore(MUtil.list("&7Click to view the last page of quests!"))
                .build();

        for (int i = 0; i < 9; i++) {
            int slot = 45 + i;
            inventory.setItem(slot, new ItemBuilder(Material.LIGHT_BLUE_STAINED_GLASS_PANE)
                    .name("&r ")
                    .lore(MUtil.list(""))
                    .build());
        }

        int totalUnclaimed = player.getQuestProfile().getTotalUnclaimedRewards();
        ItemStack unclaimedQuestRewards = new ItemBuilder(Material.CHEST)
                .name("&a&lQuest Rewards")
                .lore(MUtil.list("&r", "&7Click to access unclaimed quest rewards!",
                        "&aTotal Unclaimed: &e" + totalUnclaimed))
                .build();
        inventory.setItem(49, unclaimedQuestRewards);
        gui.setAction(49, inventoryClickEvent -> {
            QuestProfile profile = player.getQuestProfile();
            if(profile.getUnclaimedQuestData().size() > 0){
                Map<QuestPath, List<QuestReward>> rewards = profile.getUnclaimedQuestRewards();
                QuestRewardsGui gui1 = new QuestRewardsGui(player.getPlayer(), rewards);
                gui1.open();
            }else {
                player.msg("&7[&bQuests&7] You have no unclaimed rewards!");
            }
            return false;
        });

        if(lastPage != null){
            inventory.setItem(51, lastPageItem);
            gui.setAction(51, inventoryClickEvent -> {
                player.getPlayer().openInventory(lastPage.getInventory());
                return false;
            });
        }

        if(activeQuestPath != null) {
            int firstSlot = inventory.firstEmpty();
            ItemStack pathIcon = activeQuestPath.getPathIcon();
            ItemBuilder builder = new ItemBuilder(pathIcon).addGlow();
            inventory.setItem(firstSlot, builder.build());
        }

        for (int i = 0; i < unlockedQuestPaths.size(); i++) {
            int firstSlot = inventory.firstEmpty();
            if(firstSlot == -1){ // The inv is full
                ChestGui nextPage = getQuestInventory(
                        null,
                        unlockedQuestPaths.subList(i, unlockedQuestPaths.size()),
                        completedQuests,
                        gui);
                inventory.setItem(51, nextPageItem);
                nextPage.setAction(51, inventoryClickEvent -> {
                    player.getPlayer().openInventory(nextPage.getInventory());
                    return false;
                });
                return gui;
            }
            QuestPath path = unlockedQuestPaths.get(i);

            ItemStack stockItem = path.getPathIcon();
            List<String> lore = stockItem.getItemMeta().getLore();
            lore.add(0, "&r");
            lore.add(1, "&7Difficulty: " + path.getDifficulty().getDifficultyString());
            lore.add(2, "&eClick to begin this quest!");
            lore.add(3, "&r");
            lore.add(4, "&7Description:");
            ItemBuilder pathBuilder = new ItemBuilder(stockItem).lore(lore);
            inventory.setItem(firstSlot, pathBuilder.build());
            gui.setAction(firstSlot, new ChestAction() {
                @Override
                public boolean onClick(InventoryClickEvent inventoryClickEvent) {
                    player.getQuestProfile().setActiveQuestPath(path);
                    player.getPlayer().closeInventory();
                    return false;
                }
            });
        }

        for (int i = 0; i < completedQuests.size(); i++) {
            int firstSlot = inventory.firstEmpty();
            if(firstSlot == -1) { // The inv is full
                ChestGui nextPage = getQuestInventory(
                        null,
                        new ArrayList<>(),
                        completedQuests.subList(i, completedQuests.size()),
                        gui);
                inventory.setItem(51, nextPageItem);
                nextPage.setAction(51, inventoryClickEvent -> {
                    player.getPlayer().openInventory(nextPage.getInventory());
                    return false;
                });
                return gui;
            }

            ItemStack completedQuestItem = new ItemBuilder(Material.GRAY_WOOL)
                    .name("&a" + completedQuests.get(i))
                    .lore(MUtil.list(" &r", "&7You've already completed this quest.", "&7Great work!"))
                    .build();

            inventory.setItem(firstSlot, completedQuestItem);
        }

        return gui;
    }
}
