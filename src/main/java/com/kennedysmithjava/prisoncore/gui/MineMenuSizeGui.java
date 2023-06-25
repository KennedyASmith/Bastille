package com.kennedysmithjava.prisoncore.gui;


import com.kennedysmithjava.prisoncore.eco.CostCurrency;
import com.kennedysmithjava.prisoncore.eco.CurrencyType;
import com.kennedysmithjava.prisoncore.entity.mines.Mine;
import com.kennedysmithjava.prisoncore.entity.mines.MinesConf;
import com.kennedysmithjava.prisoncore.entity.player.MPlayer;
import com.kennedysmithjava.prisoncore.gui.buttons.GuiButton;
import com.kennedysmithjava.prisoncore.gui.buttons.GuiMineSizeButton;
import com.kennedysmithjava.prisoncore.util.Color;
import com.kennedysmithjava.prisoncore.util.ItemBuilder;
import com.massivecraft.massivecore.chestgui.ChestGui;
import com.massivecraft.massivecore.util.MUtil;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.List;

public class MineMenuSizeGui extends UpgradesGui {

    boolean canUpgradeHeight;
    boolean canDowngradeHeight;
    boolean canUpgradeWidth;
    boolean canDowngradeWidth;
    int width;
    int height;
    
    private static final int INCREASE_WIDTH_SLOT = 14;
    private static final int DECREASE_WIDTH_SLOT = 32;
    private static final int INCREASE_HEIGHT_SLOT = 12;
    private static final int DECREASE_HEIGHT_SLOT = 30;

    public MineMenuSizeGui(Player player, String name, BaseGui returnMenu) {
        super(player, name, 9*6,  false, returnMenu);
        MPlayer mPlayer = MPlayer.get(getPlayer());
        Mine mine = mPlayer.getMine();
        this.height = mine.getHeight();
        this.width = mine.getWidth();
        this.canUpgradeHeight = height < 49;
        this.canDowngradeHeight = height > MinesConf.get().mineDefaultHeight;
        this.canUpgradeWidth = width < mine.getFloor().getMaxWidth();
        this.canDowngradeWidth = width > MinesConf.get().mineDefaultWidth;
    }

    @Override
    public void onBuild(Player player, ChestGui gui, Inventory inventory) {
        blockFill(Material.WHITE_STAINED_GLASS_PANE);

        /* Set item and ChestAction for the back button */
        int BACK_BUTTON_SLOT = 45;
        ItemBuilder backButtonBuilder = new ItemBuilder(Material.RED_WOOL, 1)
                .name("&c&lGo Back")
                .lore(MUtil.list(   "&7" + Color.strip(this.getReturnMenu().getName())));
        setItem(BACK_BUTTON_SLOT, backButtonBuilder.build());
        setAction(BACK_BUTTON_SLOT, inventoryClickEvent -> {
            returnToLastMenu();
            return false;
        });

        if(!canUpgradeWidth){
            setItem(INCREASE_WIDTH_SLOT, Material.GRAY_WOOL, "&aIncrease Width", MUtil.list("&7[MAXIMUM WIDTH REACHED]"), false);
        }

        if(!canDowngradeWidth){
            setItem(DECREASE_WIDTH_SLOT, Material.GRAY_WOOL, "&cDecrease Width", MUtil.list("&7[MINIMUM WIDTH REACHED]"), false);
        }

        if(!canUpgradeHeight){
            setItem(INCREASE_HEIGHT_SLOT, Material.GRAY_WOOL, "&aIncrease Height", MUtil.list("&7[MAXIMUM HEIGHT REACHED]"), false);
        }

        if(!canDowngradeHeight){
            setItem(DECREASE_HEIGHT_SLOT, Material.GRAY_WOOL, "&cDecrease Height", MUtil.list("&7[MINIMUM HEIGHT REACHED]"), false);
        }

    }

    @Override
    public List<GuiButton> getButtons() {
        MPlayer player = MPlayer.get(getPlayer());
        List<GuiButton> buttons = new ArrayList<>();


        if(canUpgradeWidth){
            double widthCost = Math.pow(10, width-1);
            buttons.add(
                    new GuiMineSizeButton(
                            player,
                            "&aIncrease Width",
                            width + 1, height,
                            INCREASE_WIDTH_SLOT,
                            MUtil.list("", "&7(&f" + width + "&7) &m->&r&7 (&e" + Math.addExact(width, 1) + "&7)"),
                            Material.LIME_WOOL,
                            MUtil.list(new CostCurrency(CurrencyType.CASH, widthCost))
                    )
            );
        }

        if(canDowngradeWidth){
            buttons.add(
                new GuiMineSizeButton(
                        player,
                        "&cDecrease Width",
                        width - 1, height,
                        DECREASE_WIDTH_SLOT,
                        MUtil.list("", "&7(&f" + width + "&7) &m->&r&7 (&e" + Math.addExact(width, -1) + "&7)"),
                        Material.LIME_WOOL,
                        MUtil.list(new CostCurrency(CurrencyType.GEMS, 100))
                )
            );
        }

        if(canUpgradeHeight){
            double heightCost = Math.pow(10, height-1);
            buttons.add(
                    new GuiMineSizeButton(
                            player,
                            "&aIncrease Height",
                            width , height + 1,
                            INCREASE_HEIGHT_SLOT,
                            MUtil.list("", "&7(&f" + height + "&7) &m->&r&7 (&e" + Math.addExact(height, 1) + "&7)"),
                            Material.LIME_WOOL,
                            MUtil.list(new CostCurrency(CurrencyType.CASH, heightCost))
                    )
            );
        }

        if(canDowngradeHeight){
            buttons.add(
                    new GuiMineSizeButton(
                            player,
                            "&cDecrease Height",
                            width, height - 1,
                            DECREASE_HEIGHT_SLOT,
                            MUtil.list("", "&7(&f" + height + "&7) &m->&r&7 (&e" + Math.addExact(height, -1) + "&7)"),
                            Material.LIME_WOOL,
                            MUtil.list(new CostCurrency(CurrencyType.GEMS, 100))
                    )
            );
        }

        return buttons;
    }
}
