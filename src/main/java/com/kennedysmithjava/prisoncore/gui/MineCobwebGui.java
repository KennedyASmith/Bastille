package com.kennedysmithjava.prisoncore.gui;

import com.kennedysmithjava.prisoncore.PrisonCore;
import com.kennedysmithjava.prisoncore.entity.mines.Mine;
import com.kennedysmithjava.prisoncore.entity.mines.UpgradesConf;
import com.kennedysmithjava.prisoncore.entity.mines.upgrades.actions.ActionMineDistribution;
import com.kennedysmithjava.prisoncore.entity.player.MPlayer;
import com.kennedysmithjava.prisoncore.gui.buttons.GuiButton;
import com.kennedysmithjava.prisoncore.util.GuiCell;
import com.kennedysmithjava.prisoncore.util.ItemBuilder;
import com.massivecraft.massivecore.chestgui.ChestGui;
import com.massivecraft.massivecore.util.MUtil;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class MineCobwebGui extends BaseGui{

    /**
     * STATIC CONSTANTS
     */
    public static final List<Integer> centerGlass = MineMainGui.centerGlass;
    public static final  List<Integer> fillerGlassLeft = MineMainGui.fillerGlassLeft;
    public static final  List<Integer> fillerGlassRight = MineMainGui.fillerGlassRight;
    public static final  List<Integer> edgeGlassLeft = MineMainGui.edgeGlassLeft;
    public static final  List<Integer> edgeGlassRight = MineMainGui.edgeGlassRight;
    public static final  List<Integer> lockableButtons = MineMainGui.lockableButtons;

    private final Mine mine;

    private final MPlayer mPlayer;

    public static final int COBWEB_SLOT = 22;

    public MineCobwebGui(Player player, MPlayer mPlayer, Mine mine) {
        super(player, "&4&lMine Upgrades", 5, false, false);
        this.mine = mine;
        this.mPlayer = mPlayer;
    }

    @Override
    public void onBuild(Player player, ChestGui gui, Inventory inventory) {
        blockFill(Material.WHITE_STAINED_GLASS_PANE);

        /*
         * Set up cobweb clear item and actions
         */
        ItemBuilder cobwebBuilder = new ItemBuilder(Material.COBWEB)
                .name("&f&lClear Cobwebs")
                .lore(MUtil.list("", "&e&lCOST","&aFREE", "", "&7Clear the cobwebs from your mine!"));


        setItem(COBWEB_SLOT, cobwebBuilder.build());
        setAction(COBWEB_SLOT, event -> {
            /* Once the cobweb is clicked, disable its click action. */
            setAction(COBWEB_SLOT, inventoryClickEvent -> false);
            setItem(COBWEB_SLOT, Material.WHITE_STAINED_GLASS_PANE);
            MineMainGui mainGui = new MineMainGui(player, mPlayer);
            /* Open the GUI with animation */
            menuIntroductionAnimation(() -> {
                        /* The following runs after the animation */
                        mine.unlockUpgrade(UpgradesConf.initialUpgrade, true, true);
                        new ActionMineDistribution(1).apply(mPlayer);
                        close();
                        mainGui.open();
                    }, mainGui
            );

            return false;
        });

    }

    public void menuIntroductionAnimation(Runnable onFinish, MineMainGui mainGui){

        List<GuiCell> leftGuiCells = new ArrayList<>();
        List<GuiCell> leftDarkGreyGuiCells = new ArrayList<>();
        List<GuiCell> rightGuiCells = new ArrayList<>();
        List<GuiCell> rightDarkGreyGuiCells = new ArrayList<>();
        List<GuiCell> centeredGuiCells = new ArrayList<>();

        /* Group buttons into left, right or centered */
        List<GuiButton> buttons = MineMainGui.getButtons(mPlayer.getPlayer(), mPlayer, mainGui);
        buttons.forEach(button -> {
            ItemBuilder buttonBuilder = new ItemBuilder(button.getMaterial())
                    .name(button.getDisplayName())
                    .lore(button.getBaseLore());
            ItemStack item = buttonBuilder.build();
            GuiCell guiCell = new GuiCell(item, button.getSlot());
            if(guiCell.getCol() > 4){
                guiCell.moveCol(5);
                rightGuiCells.add(guiCell);
            } else if (guiCell.getCol() == 4){
                guiCell.moveRow(5);
                centeredGuiCells.add(guiCell);
            }
            else{
                guiCell.moveCol(-5);
                leftGuiCells.add(guiCell);
            }
        });

        fillerGlassLeft.forEach(slot -> {
            GuiCell guiCell = new GuiCell(Material.LIGHT_BLUE_STAINED_GLASS_PANE, slot);
            guiCell.moveCol(-5);
            leftGuiCells.add(guiCell);
        });

        edgeGlassLeft.forEach(slot -> {
            GuiCell guiCell = new GuiCell(Material.BLUE_STAINED_GLASS_PANE, slot);
            guiCell.moveCol(-2);
            leftDarkGreyGuiCells.add(guiCell);
        });

        fillerGlassRight.forEach(slot -> {
            GuiCell guiCell = new GuiCell(Material.LIGHT_BLUE_STAINED_GLASS_PANE, slot);
            guiCell.moveCol(5);
            rightGuiCells.add(guiCell);
        });

        edgeGlassRight.forEach(slot -> {
            GuiCell guiCell = new GuiCell(Material.BLUE_STAINED_GLASS_PANE, slot);
            guiCell.moveCol(2);
            rightDarkGreyGuiCells.add(guiCell);
        });

        /*
         * Runnable for the animation
         */
        new BukkitRunnable() {

            private int counter = 0;
            private int frame;
            private int locked = lockableButtons.size();

            @Override
            public void run() {

                /*
                 * Portion of the animation where all buttons and gray glass cover GUI
                 */
                if (frame <= 4 && counter == 6) {
                    rightGuiCells.forEach(guiCell -> {
                        guiCell.moveCol(-1);
                        if (guiCell.displayable()) setItem(guiCell.getSlot(), guiCell.getItem());
                    });

                    leftGuiCells.forEach(guiCell -> {
                        guiCell.moveCol(1);
                        if (guiCell.displayable()) setItem(guiCell.getSlot(), guiCell.getItem());
                    });

                    counter = 0;
                    frame++;
                    return;
                }

                /*
                 * Portion of the animation where black stained glass cover the corners of the GUI
                 */
                else if (frame > 4 && frame < 7 && counter == 6) {
                    rightDarkGreyGuiCells.forEach(guiCell -> {
                        guiCell.moveCol(-1);
                        if (guiCell.displayable()) setItem(guiCell.getSlot(), guiCell.getItem());
                    });

                    leftDarkGreyGuiCells.forEach(guiCell -> {
                        guiCell.moveCol(1);
                        if (guiCell.displayable()) setItem(guiCell.getSlot(), guiCell.getItem());
                    });

                    counter = 0;
                    frame++;
                    return;
                }
                else if(frame >= 7 && counter >= 6){

                    /*
                     * Portion of the animation where the main Cell upgrade icon moves towards the center of the GUI
                     */
                    if(frame <= 11){
                        centeredGuiCells.forEach(guiCell -> {
                            int previousSlot = guiCell.getSlot();
                            guiCell.moveRow(-1);
                            if (guiCell.displayable()) setItem(guiCell.getSlot(), guiCell.getItem());

                            ItemStack previousItem;
                            if (centerGlass.contains(previousSlot)) {
                                previousItem = new ItemBuilder(Material.WHITE_STAINED_GLASS_PANE).build();
                            } else {
                                previousItem = new ItemBuilder(Material.LIGHT_BLUE_STAINED_GLASS_PANE).build();
                            }

                            GuiCell previousGuiCell = new GuiCell(previousItem, previousSlot);
                            if (previousGuiCell.displayable()) setItem(previousGuiCell.getSlot(), previousGuiCell.getItem());

                        });
                    }

                    /*
                     * All lockable buttons change to iron bars one-by-one
                     */
                    int slot = lockableButtons.get(locked - 1);
                    ItemStack item = getInventory().getItem(slot);
                    assert item != null;
                    item.setType(Material.IRON_BARS);
                    locked--;

                    /*
                     * If no more buttons are left, end the animation here.
                     */
                    if(locked == 0){
                        this.cancel();
                        onFinish.run();
                        return;
                    }
                    counter = 0;
                    frame++;
                }
                counter++;
            }

        }.runTaskTimer(PrisonCore.get(), 0, 1);
    }
}


