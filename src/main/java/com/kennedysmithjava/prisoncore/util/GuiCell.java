package com.kennedysmithjava.prisoncore.util;

import com.kennedysmithjava.prisoncore.entity.mines.WarrenGUIConf;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class GuiCell {

    int row;
    int col;
    int invSize = WarrenGUIConf.get().guiSize;
    ItemStack item;

    public GuiCell(ItemStack item, int slot) {

        row = Math.floorDiv(slot, 9);

        col = (slot - (9 * row));
        this.item = item;
    }

    public GuiCell(Material material, int slot) {

        row = Math.floorDiv(slot, 9);

        col = (slot - (9 * row));
        this.item = new ItemBuilder(material).name(" ").build();
    }

    public GuiCell(ItemStack item, int row, int col) {
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

    public void setItem(ItemStack item) {
        this.item = item;
    }
}
