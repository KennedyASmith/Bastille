package com.kennedysmithjava.prisoncore.gui;

import com.kennedysmithjava.prisoncore.util.Color;
import com.kennedysmithjava.prisoncore.util.ItemBuilder;
import com.massivecraft.massivecore.chestgui.ChestAction;
import com.massivecraft.massivecore.chestgui.ChestGui;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public abstract class BaseGui {

    private Inventory inventory;
    private ChestGui gui;
    private final Player player;

    private final BaseGui returnMenu;
    private final int size;

    private final boolean autoClosing;
    private final boolean autoRemoving;

    private final boolean allowBottomInv;
    private String name;

    public BaseGui(Player player, String name, int rows, boolean allowBottomInv, boolean autoClosing, BaseGui returnMenu, boolean autoRemoving){
        this.player = player;
        this.size = rows * 9;
        this.autoClosing = autoClosing;
        this.allowBottomInv = allowBottomInv;
        this.name = name;
        this.returnMenu = returnMenu;
        this.autoRemoving = autoRemoving;
    }

    public BaseGui(Player player, String name, int rows, boolean allowBottomInv, boolean autoClosing, BaseGui returnMenu){
        this.player = player;
        this.size = rows * 9;
        this.autoClosing = autoClosing;
        this.allowBottomInv = allowBottomInv;
        this.name = name;
        this.returnMenu = returnMenu;
        this.autoRemoving = true;
    }

    public BaseGui(Player player, String name, int rows, boolean allowBottomInv, boolean autoClosing){
        this.player = player;
        this.size = rows * 9;
        this.autoClosing = autoClosing;
        this.allowBottomInv = allowBottomInv;
        this.name = name;
        this.returnMenu = null;
        this.autoRemoving = true;
    }

    public abstract void onBuild(Player player, ChestGui gui, Inventory inventory);

    public void buildMenu(){
        if(gui != null) gui.remove();
        this.inventory = Bukkit.createInventory(null, size, Color.get(name));
        this.gui = ChestGui.getCreative(inventory);
        this.gui.setAutoclosing(autoClosing);
        this.gui.setBottomInventoryAllow(allowBottomInv);
        this.gui.setAutoremoving(true);
        this.gui.setSoundOpen(null);
        this.gui.setSoundClose(null);
        this.onBuild(player, gui, inventory);
        this.onBuildInner(player, gui, inventory);
    }

    public void open(){
        player.closeInventory();
        if(gui == null) buildMenu();
        player.openInventory(gui.getInventory());
        this.onOpen();
    }

    public void onBuildInner(Player player, ChestGui gui, Inventory inventory) {

    }
    public void onOpen(){

    }

    public void blockFill(Material material, int start, int end){
        for (int b = start; b < end + 1; b++) {
            ItemStack p = new ItemStack(material, 1);
            ItemMeta itemMeta = p.getItemMeta();
            if(itemMeta != null) itemMeta.setDisplayName(" ");
            p.setItemMeta(itemMeta);
            inventory.setItem(b, p);
        }
    }

    public void blockFill(Material material){
        blockFill(material, 0, size - 1);
    }

    public void setItem(int slot, ItemStack item){
        inventory.setItem(slot, item);
    }

    public void setItem(int slot, Material material){
        ItemStack p = new ItemStack(material, 1);
        ItemMeta itemMeta = p.getItemMeta();
        if(itemMeta != null) itemMeta.setDisplayName(" ");
        p.setItemMeta(itemMeta);
        inventory.setItem(slot, p);
    }

    public void setItem(int slot, Material material, String displayName, List<String> lore, boolean glow){
        ItemBuilder builder = new ItemBuilder(material).name(displayName).lore(lore);
        if(glow) builder.addGlow();
        setItem(slot, builder.build());
    }

    public int addItem(ItemStack item){
        int firstEmpty = inventory.firstEmpty();
        inventory.addItem(item);
        return firstEmpty;
    }

    public int addItem(Material material, String displayName, List<String> lore, boolean glow){
        ItemBuilder builder = new ItemBuilder(material).name(displayName).lore(lore);
        if(glow) builder.addGlow();
        return addItem(builder.build());
    }


    public void setAction(int slot, ChestAction chestAction){
        gui.setAction(slot, chestAction);
    }


    public boolean canReturnToLastMenu(){
        return returnMenu != null;
    }

    public void returnToLastMenu(){
        if(returnMenu == null) return;
        this.close();
        this.returnMenu.open();
    }

    public void close(){
        this.gui.remove();
        this.gui = null;
        this.inventory = null;
        this.player.closeInventory();
    }

    public Inventory getInventory() {
        return inventory;
    }

    public BaseGui getReturnMenu() {
        return returnMenu;
    }

    public String getName() {
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public Player getPlayer() {
        return player;
    }
}
