package com.kennedysmithjava.prisoncore.gui;

import com.kennedysmithjava.prisoncore.crafting.objects.PrisonEnchantBook;
import com.kennedysmithjava.prisoncore.eco.Cost;
import com.kennedysmithjava.prisoncore.entity.player.MPlayer;
import com.kennedysmithjava.prisoncore.tools.Pickaxe;
import com.kennedysmithjava.prisoncore.tools.enchantment.Enchant;
import com.kennedysmithjava.prisoncore.util.Color;
import com.kennedysmithjava.prisoncore.util.ItemBuilder;
import com.massivecraft.massivecore.chestgui.ChestGui;
import com.massivecraft.massivecore.util.MUtil;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class EnchantApplyGui extends BaseGui{

    Pickaxe pickaxe;
    Map<Integer, PrisonEnchantBook> enchantBooks;
    Map<Integer, ItemStack> originalBookItems;

    public EnchantApplyGui(Player player, Pickaxe pickaxe, Map<Integer, PrisonEnchantBook> enchantBooks, Map<Integer, ItemStack> originalBookItems, BaseGui returnMenu) {
        super(player, "&8Confirm Enchants", 5, false, false, returnMenu);
        this.pickaxe = pickaxe;
        this.enchantBooks = enchantBooks;
        this.originalBookItems = originalBookItems;
    }

    @Override
    public void onBuild(Player player, ChestGui gui, Inventory inventory) {
        Map<Enchant<?>, Integer> currentEnchants = pickaxe.getEnchants();

        ItemStack clonedItem = pickaxe.getItem().clone();
        blockFill(Material.WHITE_STAINED_GLASS_PANE);
        setItem(13, clonedItem);

        Map<Integer, PrisonEnchantBook> applicableEnchants = new HashMap<>();
        Map<Integer, PrisonEnchantBook> nonApplicableEnchants = new HashMap<>();
        Map<Enchant<?>, Integer> currentLevelEnchants = new HashMap<>(currentEnchants);
        List<String> confirmLore = MUtil.list("&7To " + Color.strip(clonedItem.getItemMeta().getDisplayName()));

        for (Integer invSlot : enchantBooks.keySet()) {
            PrisonEnchantBook book = enchantBooks.get(invSlot);
            if(book == null) continue;
            Enchant<?> enchant = book.getEnchant();
            int currentLevel = 0;
            if(currentLevelEnchants.containsKey(enchant)) {
                currentLevel += currentLevelEnchants.get(enchant);
            }
            if(currentLevel + book.getLevel() >= enchant.getMaxLevel()){
                nonApplicableEnchants.put(invSlot, book);
            }else {
                currentLevelEnchants.put(enchant, currentLevel + book.getLevel());
                applicableEnchants.put(invSlot, book);
            }
        }

        List<Cost> duplicateCosts = new ArrayList<>();
        if(applicableEnchants.size() > 0){
            applicableEnchants.forEach((integer, book) ->{
                confirmLore.add("&7- &d" + book.getEnchant().getName() + ": &a+" + book.getLevel() + " &7level");
                duplicateCosts.addAll(book.getEnchant().getApplyCosts());
                    });
        }

        if(nonApplicableEnchants.size() > 0){
            confirmLore.add(" &r");
            confirmLore.add("&cCannot add Enchants:");
            nonApplicableEnchants.forEach((integer, book) ->
                    confirmLore.add("&c- " + book.getEnchant().getName() + "&c: Max level Reached"));
        }

        boolean canAfford = true;
        Collection<Cost> totalCosts = Cost.combineList(duplicateCosts);
        if(totalCosts.size() > 0){
            confirmLore.add(" &r");
            confirmLore.add("&7&lREQUIREMENTS");

            MPlayer mPlayer = MPlayer.get(player);
            for (Cost c : totalCosts) {
                if(c.hasCost(mPlayer)){
                    confirmLore.add("&a✔ &7" + c.getPriceline());
                }else{
                    confirmLore.add("&c❌ " + c.getInsufficientLine(mPlayer));
                    canAfford = false;
                }
            }
        }

        confirmLore.add(" &r");
        confirmLore.add("&7&oThis action will consume your enchant book(s).");

        ItemBuilder confirmButton = new ItemBuilder(Material.GREEN_WOOL).name("&a&lApply Enchants");
        boolean finalCanAfford = canAfford;
        setAction(32, inventoryClickEvent -> {
            if(applicableEnchants.size() > 0 && finalCanAfford){
                MPlayer mPlayer = MPlayer.get(player);
                for (Cost totalCost : totalCosts) {
                    totalCost.transaction(mPlayer);
                }
                removeAllEnchantBooks(player, enchantBooks);
                pickaxe.setEnchants(currentLevelEnchants);
                pickaxe.forceLoreUpdate();
                this.close();
            }
            return false;
        });
        setItem(32, confirmButton.lore(confirmLore).build());


        setItem(30, Material.RED_WOOL, "&cGo Back", new ArrayList<>(), false);
        setAction(30, inventoryClickEvent -> {
            returnToLastMenu();
            return false;
        });
    }

    public void removeAllEnchantBooks(Player player, Map<Integer, PrisonEnchantBook> books){
        for (Integer slot : books.keySet()) {
            ItemStack item = originalBookItems.get(slot);
            player.getInventory().remove(item);
        }
    }
}
