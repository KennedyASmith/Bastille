package com.kennedysmithjava.prisoncore.quest;

import com.kennedysmithjava.prisoncore.entity.player.MPlayer;
import com.kennedysmithjava.prisoncore.util.Color;
import com.kennedysmithjava.prisoncore.util.Glow;
import com.kennedysmithjava.prisoncore.util.ItemBuilder;
import com.massivecraft.massivecore.chestgui.ChestGui;
import com.massivecraft.massivecore.util.MUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

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
     * @param activeQuestPaths   the list of active quest paths
     * @param completedQuests    the list of completed quests
     */
    public QuestListGUI(MPlayer player, List<QuestPath> unlockedQuestPaths, List<QuestPath> activeQuestPaths, List<String> completedQuests) {
        this.player = player;
        firstGuiMenu = getQuestInventory(activeQuestPaths, unlockedQuestPaths, completedQuests.stream().sorted().toList(), null);
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
     * @param activeQuestPaths   the list of active quest paths
     * @param unlockedQuestPaths the list of unlocked quest paths
     * @param completedQuests    the list of completed quests
     * @param lastPage           the last page GUI
     * @return the quest inventory GUI
     */
    private ChestGui getQuestInventory(List<QuestPath> activeQuestPaths, List<QuestPath> unlockedQuestPaths, List<String> completedQuests, ChestGui lastPage){
        Inventory inventory = Bukkit.createInventory(null, 6*9, Color.get("&8&lPick a Quest"));
        ChestGui gui = ChestGui.getCreative(inventory);
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
            inventory.setItem(slot, new ItemBuilder(Material.WHITE_STAINED_GLASS_PANE)
                            .name("&r ")
                            .lore(MUtil.list(""))
                            .build());
        }

        if(lastPage != null){
            inventory.setItem(51, lastPageItem);
            gui.setAction(51, inventoryClickEvent -> {
                player.getPlayer().openInventory(lastPage.getInventory());
                return false;
            });
        }

        for (int i = 0; i < activeQuestPaths.size(); i++) {
            int firstSlot = inventory.firstEmpty();
            if(firstSlot == -1){ // The inv is full
                ChestGui nextPage = getQuestInventory(
                        activeQuestPaths.subList(i, activeQuestPaths.size()),
                        unlockedQuestPaths,
                        completedQuests,
                        gui);
                inventory.setItem(51, nextPageItem);
                nextPage.setAction(51, inventoryClickEvent -> {
                    player.getPlayer().openInventory(nextPage.getInventory());
                    return false;
                });
                return gui;
            }
            QuestPath path = activeQuestPaths.get(i);
            ItemStack pathIcon = path.getPathIcon();
            pathIcon.addEnchantment(Glow.getGlow(), 1);
            inventory.setItem(firstSlot, pathIcon);
        }

        for (int i = 0; i < unlockedQuestPaths.size(); i++) {
            int firstSlot = inventory.firstEmpty();
            if(firstSlot == -1){ // The inv is full
                ChestGui nextPage = getQuestInventory(
                        new ArrayList<>(),
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
            QuestPath path = activeQuestPaths.get(i);
            inventory.setItem(firstSlot, path.getPathIcon());
        }

        for (int i = 0; i < completedQuests.size(); i++) {
            int firstSlot = inventory.firstEmpty();
            if(firstSlot == -1) { // The inv is full
                ChestGui nextPage = getQuestInventory(
                        new ArrayList<>(),
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

            ItemStack completedQuestItem = new ItemBuilder(Material.ARROW)
                    .name("&a" + completedQuests.get(i))
                    .lore(MUtil.list("&7You've completed this quest."))
                    .build();

            inventory.setItem(firstSlot, completedQuestItem);
        }


        gui.setBottomInventoryAllow(false);
        return gui;
    }
}
