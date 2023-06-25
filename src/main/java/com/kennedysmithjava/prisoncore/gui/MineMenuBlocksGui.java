package com.kennedysmithjava.prisoncore.gui;


import com.kennedysmithjava.prisoncore.eco.CostCurrency;
import com.kennedysmithjava.prisoncore.eco.CurrencyType;
import com.kennedysmithjava.prisoncore.entity.mines.Distribution;
import com.kennedysmithjava.prisoncore.entity.mines.DistributionConf;
import com.kennedysmithjava.prisoncore.entity.player.MPlayer;
import com.kennedysmithjava.prisoncore.gui.buttons.GuiButton;
import com.kennedysmithjava.prisoncore.gui.buttons.GuiDistributionButton;
import com.kennedysmithjava.prisoncore.util.Color;
import com.kennedysmithjava.prisoncore.util.DistributionPage;
import com.kennedysmithjava.prisoncore.util.ItemBuilder;
import com.massivecraft.massivecore.chestgui.ChestGui;
import com.massivecraft.massivecore.util.MUtil;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class MineMenuBlocksGui extends UpgradesGui {

    int pageNum;
    DistributionPage page;
    boolean hasNextPage;
    boolean hasPreviousPage;

    public MineMenuBlocksGui(Player player, BaseGui returnMenu, int pageNum) {
        super(player, "Blocks", 5,  true, returnMenu);
        this.pageNum = pageNum;
        this.page = DistributionConf.get().pages.get(pageNum);
        this.setName(page.getName());
        this.hasNextPage = pageNum < DistributionConf.get().pages.size() - 1;
        this.hasPreviousPage = pageNum > 0;
    }

    @Override
    public void onBuild(Player player, ChestGui gui, Inventory inventory) {
        blockFill(Material.BLUE_STAINED_GLASS_PANE, 0, 8);
        blockFill(Material.LIGHT_BLUE_STAINED_GLASS_PANE, 9, 35);
        blockFill(Material.BLUE_STAINED_GLASS_PANE, 36, 44);

        /* Set item and ChestAction for the back button */
        int BACK_BUTTON_SLOT = 36;
        ItemBuilder backButtonBuilder = new ItemBuilder(Material.RED_WOOL, 1)
                .name("&c&lGo Back")
                .lore(MUtil.list(   "&7" + Color.strip(this.getReturnMenu().getName())));
        setItem(BACK_BUTTON_SLOT, backButtonBuilder.build());
        setAction(BACK_BUTTON_SLOT, inventoryClickEvent -> {
            close();
            returnToLastMenu();
            return false;
        });


        if(hasNextPage){
            int nextPageSlot = 41;
            DistributionPage nextPage = DistributionConf.get().pages.get(pageNum + 1);
            ItemBuilder nextPageBuilder = new ItemBuilder(Material.ARROW, 1)
                    .name("&c&lNext Page")
                    .lore("&7Page: " + Color.strip(nextPage.getName()));
            setItem(nextPageSlot, nextPageBuilder.build());
            setAction(nextPageSlot, inventoryClickEvent -> {
                close();
                MineMenuBlocksGui nextPageMenu = new MineMenuBlocksGui(player, getReturnMenu(), pageNum + 1);
                nextPageMenu.open();
                return false;
            });
        }

        if(hasPreviousPage){
            int nextPageSlot = 39;
            DistributionPage lastPage = DistributionConf.get().pages.get(pageNum - 1);
            ItemBuilder lastPageBuilder = new ItemBuilder(Material.ARROW, 1)
                    .name("&c&lLast Page")
                    .lore("&7Page: " + Color.strip(lastPage.getName()));
            setItem(nextPageSlot, lastPageBuilder.build());
            setAction(nextPageSlot, inventoryClickEvent -> {
                close();
                MineMenuBlocksGui lastPageMenu = new MineMenuBlocksGui(player, getReturnMenu(), pageNum - 1);
                lastPageMenu.open();
                return false;
            });
        }

    }

    @Override
    public List<GuiButton> getButtons() {

        List<GuiButton> buttons = new ArrayList<>();

        MPlayer mPlayer = MPlayer.get(getPlayer());
        List<Integer> fillableSlots = MUtil.list(10,11,12,13,14,15,16,19,20,21,22,23,24,25,28,29,30,31,32,33,34);
        Map<Integer, Distribution> distributions = DistributionConf.get().distribution;
        Iterator<Integer> pageIterator = page.getDistributionIDs().iterator();

        for (Integer slot : fillableSlots) {
            if(pageIterator.hasNext()){
                int distributionID = pageIterator.next();
                Distribution distribution = distributions.get(distributionID);
                GuiDistributionButton distributionButton = new GuiDistributionButton(
                        mPlayer,
                        distribution,
                        distributionID,
                        slot,
                        distribution.getLore(),
                        distribution.getIcon(),
                        MUtil.list(new CostCurrency(CurrencyType.CASH, 100)));
                buttons.add(distributionButton);
            }else{
                setItem(slot, Material.WHITE_STAINED_GLASS_PANE);
            }
        }

        return buttons;
    }
}
