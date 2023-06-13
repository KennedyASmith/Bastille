package com.kennedysmithjava.prisoncore.crates;

import com.kennedysmithjava.prisoncore.PrisonCore;
import com.kennedysmithjava.prisoncore.util.Color;
import com.kennedysmithjava.prisoncore.util.GuiCell;
import com.kennedysmithjava.prisoncore.util.ItemBuilder;
import com.kennedysmithjava.prisoncore.util.LoopIterator;
import com.massivecraft.massivecore.chestgui.ChestGui;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.kennedysmithjava.prisoncore.crates.PrizeRandomizer.*;

/**
 * The CrateRoulette class provides functionality for animating a crate roulette game.
 * Players can participate in the roulette by spinning a virtual wheel and winning prizes
 * based on their luck factor. The class handles the animation, prize selection, and rarity
 * adjustments.
 *
 * @author KennedyASmith
 */
public class CrateRoulette {

    /**
     * Animates the crate roulette for the given player with the provided list of prizes and luck factor.
     *
     * @param player  The player participating in the crate roulette.
     * @param prizes  The list of prizes available for the roulette.
     * @param luck    The luck factor for adjusting the rarity of prizes.
     */
    public static void animate(Player player, List<CratePrize> prizes, double luck) {
        int numPrizes = 9 * 8;
        List<CratePrize> prizePool = new ArrayList<>();

        // Adjust the rarity of prizes based on luck
        Map<Double, List<CratePrize>> similarRarityPrizes = getSimilarRarities(prizes);
        Map<Double, List<CratePrize>> adjustedRarities = adjustRarity(similarRarityPrizes, luck);

        // Select prizes for the prize pool
        for (int i = 0; i < numPrizes; i++) {
            CratePrize p = pickPrize(adjustedRarities);
            prizePool.add(p);
        }

        // Create inventory and chest GUI
        Inventory inventory = Bukkit.createInventory(null, 27, Color.get("&4&lCrafting Pickaxe"));
        ChestGui chestGui = ChestGui.getCreative(inventory);
        chestGui.setAutoclosing(false);
        chestGui.setAutoremoving(false);
        chestGui.setSoundOpen(null);
        chestGui.setSoundClose(null);
        player.openInventory(inventory);

        LoopIterator<CratePrize> prizeLoopIterator = new LoopIterator<>(prizePool);
        HashMap<GuiCell, CratePrize> currentlyDisplayed = new HashMap<>();
        List<GuiCell> currentlyDisplayedGlass = new ArrayList<>();
        ItemStack spinnerItem = new ItemBuilder(Material.HOPPER).name(Color.get("&7Spinner")).build();

        // Display initial prizes in the inventory
        for (int i = 0; i < 9; i++) {
            CratePrize p = prizeLoopIterator.next();
            GuiCell cell = new GuiCell(p.getPrizeIcon(), 1, i);
            ItemStack glass = new ItemStack(p.getGlassColor());
            GuiCell glassCell = new GuiCell(glass, 0, i);
            GuiCell glassCloneCell = new GuiCell(glass.clone(), 2, i);
            currentlyDisplayedGlass.add(glassCell);
            currentlyDisplayedGlass.add(glassCloneCell);
            currentlyDisplayed.put(cell, p);
            inventory.setItem(cell.getSlot(), cell.getItem());
            inventory.setItem(glassCell.getSlot(), glassCell.getItem());
            inventory.setItem(glassCloneCell.getSlot(), glassCloneCell.getItem());
            inventory.setItem(4, spinnerItem);
        }

        new BukkitRunnable() {
            final int animationLengthTicks = 20 * 10;
            int ticksPassed = 0;
            int ticksPerRotation = 1;
            int ticksSinceLastRotation = 0;

            @Override
            public void run() {
                if (ticksPassed >= animationLengthTicks) {
                    if (ticksPassed > animationLengthTicks + (20 * 3)) {
                        // Animation finished, give prize and close inventory
                        this.cancel();
                        player.closeInventory();
                        currentlyDisplayed.forEach((guiCell, cratePrize) -> {
                            if (guiCell.getCol() == 4) cratePrize.givePrize(player);
                        });
                    }
                } else {
                    if (ticksSinceLastRotation >= ticksPerRotation) {
                        // Shift prizes and update inventory display
                        currentlyDisplayed.forEach((guiCell, cratePrize) -> {
                            guiCell.moveCol(-1);
                            if (guiCell.displayable()) {
                                inventory.setItem(guiCell.getSlot(), guiCell.getItem());
                            }
                        });
                        currentlyDisplayedGlass.forEach((guiCell) -> {
                            guiCell.moveCol(-1);
                            if (guiCell.displayable()) {
                                inventory.setItem(guiCell.getSlot(), guiCell.getItem());
                            }
                        });

                        // Display the next prize
                        CratePrize nextPrize = prizeLoopIterator.next();
                        GuiCell nextGuiCell = new GuiCell(nextPrize.getPrizeIcon(), 1, 8);
                        ItemStack glass = new ItemStack(nextPrize.getGlassColor(), 1);
                        GuiCell glassCell = new GuiCell(glass, 0, 8);
                        GuiCell glassCloneCell = new GuiCell(glass.clone(), 2, 8);
                        currentlyDisplayed.put(nextGuiCell, nextPrize);
                        currentlyDisplayedGlass.add(glassCell);
                        currentlyDisplayedGlass.add(glassCloneCell);
                        inventory.setItem(nextGuiCell.getSlot(), nextGuiCell.getItem());
                        inventory.setItem(glassCell.getSlot(), glassCell.getItem());
                        inventory.setItem(glassCloneCell.getSlot(), glassCloneCell.getItem());
                        inventory.setItem(4, spinnerItem);
                        ticksSinceLastRotation = 0;
                        if (ticksPassed > (20 * 6)) {
                            ticksPerRotation++;
                        }
                    } else {
                        ticksSinceLastRotation++;
                    }
                }
                ticksPassed++;
            }
        }.runTaskTimer(PrisonCore.get(), 0, 1);
    }

}
