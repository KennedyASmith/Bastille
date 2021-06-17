package com.kennedysmithjava.prisonmines.animation;

import com.kennedysmithjava.prisonmines.entity.Mine;
import com.kennedysmithjava.prisonmines.entity.UpgradesGUIConf;
import com.kennedysmithjava.prisonmines.upgrades.Upgrade;
import com.mcrivals.prisoncore.entity.MPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class UpgradeGUIAnimation extends BukkitRunnable {

    Inventory inv;
    MPlayer player;
    int counter;
    int frame;
    Mine mine;
    List<Cell> cells = new ArrayList<>();

    public UpgradeGUIAnimation(Inventory inv, MPlayer player) {

        this.player = player;
        this.mine = Mine.get(player.getMineID());
        this.inv = inv;

        player.getPlayer().openInventory(inv);

        for (int i = 0; i < (inv.getSize()); ) {

            Cell cell = new Cell(inv.getItem(i).clone(), i);
            cells.add(cell);

            ItemStack p = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 1);
            cells.add(new Cell(p, cell.getRow(), cell.getCol() + 9));

            i++;
        }

        List<Upgrade> newUpgrades = UpgradesGUIConf.get().getUpgrades().get(mine.getLevel() + 1);
        Upgrade newMainUpgrade = UpgradesGUIConf.get().getMainMineUpgrades().get(mine.getLevel() + 1);

        Inventory newInv = Bukkit.createInventory(null, 9 * 5);

        for (int i = 0; i < (inv.getSize()); ) {
            ItemStack p = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 1);
            newInv.setItem(i, p);
            i++;
        }

        newUpgrades.forEach(upgrade -> {
            newInv.setItem(upgrade.getSlot(), new ItemStack(upgrade.getIcon(), 1));
        });

        newInv.setItem(newMainUpgrade.getSlot(), new ItemStack(newMainUpgrade.getIcon(), 1));


        for (int i = 0; i < (newInv.getSize()); ) {

            ItemStack item = newInv.getItem(i);
            if(item == null) continue;
            Cell cell = new Cell(item.clone(), i);
            cell.moveCol(9);
            cells.add(cell);

            i++;
        }

    }

    @Override
    public void run() {

        if (counter == 6) {

            cells.forEach(cell -> {
                cell.moveCol(-1);
                if (cell.displayable()) {
                    inv.setItem(cell.getSlot(), cell.getItem());
                }
            });

            counter = 0;
            frame++;
        }
        if (frame == 9) {
            this.cancel();
        }

        counter++;
    }

}

class Cell {

    int row;
    int col;
    int invSize = UpgradesGUIConf.get().guiSize;
    ItemStack item;

    Cell(ItemStack item, int slot) {

        row = Math.floorDiv(slot, 9);

        col = (slot - (9 * row));
        this.item = item;
    }

    Cell(ItemStack item, int row, int col) {
        this.row = row;
        this.col = col;
        this.item = item;
    }

    /**
     * Moves the object in vertical virtual space
     *
     * @param offset
     */
    public void moveRow(int offset) {
        this.row = row + offset;
    }

    /**
     * Moves the object in horizontal virtual space
     *
     * @param offset
     */
    public void moveCol(int offset) {
        this.col = col + offset;
    }

    public boolean displayable() {
        boolean isTrueOne = row >= 0 && row <= ((invSize / 9) - 1);
        boolean isTrueTwo = (col >= 0 && col <= 8);
        return isTrueOne && isTrueTwo;
    }

    public int getSlot() {
        return (9 * row) + (col);
    }

    public ItemStack getItem() {
        return item;
    }

    public int getCol() {
        return col;
    }

    public int getRow() {
        return row;
    }
}
