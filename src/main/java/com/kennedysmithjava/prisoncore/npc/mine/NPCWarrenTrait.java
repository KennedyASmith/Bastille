package com.kennedysmithjava.prisoncore.npc.mine;

import com.kennedysmithjava.prisoncore.PrisonCore;
import com.kennedysmithjava.prisoncore.entity.mines.Mine;
import com.kennedysmithjava.prisoncore.entity.mines.UpgradesConf;
import com.kennedysmithjava.prisoncore.entity.mines.WarrenConf;
import com.kennedysmithjava.prisoncore.entity.mines.WarrenGUIConf;
import com.kennedysmithjava.prisoncore.entity.player.MPlayer;
import com.kennedysmithjava.prisoncore.entity.player.MPlayerColl;
import com.kennedysmithjava.prisoncore.upgrades.MainUpgrade;
import com.kennedysmithjava.prisoncore.upgrades.UpgradeGUI;
import com.kennedysmithjava.prisoncore.upgrades.actions.AbstractAction;
import com.kennedysmithjava.prisoncore.upgrades.actions.ActionMineDistribution;
import com.kennedysmithjava.prisoncore.upgrades.buttons.GUIButton;
import com.kennedysmithjava.prisoncore.upgrades.buttons.GUIButtonMainUpgrade;
import com.kennedysmithjava.prisoncore.util.Color;
import com.massivecraft.massivecore.chestgui.ChestGui;
import com.massivecraft.massivecore.util.MUtil;
import net.citizensnpcs.api.event.NPCRightClickEvent;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.trait.Trait;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class NPCWarrenTrait extends Trait {

    public NPCWarrenTrait() {
        super("researchertrait");
    }


    public static List<Integer> centerGlass = MUtil.list(12, 13, 14,21, 23, 30, 31, 32);
    public static List<Integer> fillerGlassLeft = MUtil.list(2,4,10,19,20,28,38);
    public static List<Integer> fillerGlassRight = MUtil.list(6,16,24,25,34,42,40);
    public static List<Integer> edgeGlassLeft = MUtil.list(0,1,9,18,27,36,37);
    public static List<Integer> edgeGlassRight = MUtil.list(7,8,17,26,35,44,43);
    public static List<Integer> lockableButtons = MUtil.list(3,5,15,33,41,39,29,11);
    public static ItemStack lightGreyItem = new ItemStack(Material.LIGHT_GRAY_STAINED_GLASS_PANE);
    public static ItemStack darkGreyItem = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
    public static ItemStack whiteItem = new ItemStack(Material.WHITE_STAINED_GLASS_PANE);

    @EventHandler
    public void click(NPCRightClickEvent event) {
        if(event.getNPC() != this.getNPC()) return;
        if(!event.getNPC().hasTrait(NPCWarrenTrait.class)) return;
        Player player = event.getClicker();
        MPlayer mPlayer = MPlayerColl.get().getByPlayer(player);
        if (mPlayer == null) return;
        player.sendMessage(Color.get(WarrenConf.get().researcherLineWelcome));
        openGUI(mPlayer);
    }

    public void openGUI(MPlayer player) {

        Mine mine = player.getMine();
        UpgradeGUI upGui = UpgradesConf.upgradeGUIs.get("MAIN");

        if(!mine.isUpgradeUnlocked(UpgradesConf.initialUpgrade)){

            /*
             * Set up inventory
             */
            ChestGui gui = ChestGui.getCreative(Bukkit.createInventory(null, WarrenGUIConf.get().guiSize, Color.get("&4&lMine Upgrades &r&7- Menu")));
            gui.setBottomInventoryAllow(false);
            gui.setAutoclosing(false);
            Inventory inv = gui.getInventory();
            blockFill(inv, Material.WHITE_STAINED_GLASS_PANE);

            /*
             * Set up cobweb clear item and actions
             */
            ItemStack cobwebButtonItem = new ItemStack(Material.COBWEB, 1);
            ItemMeta meta = cobwebButtonItem.getItemMeta();
            meta.setDisplayName(Color.get("&f&lClear Cobwebs"));
            meta.setLore(Color.get(MUtil.list("", "&e&lCOST","&aFREE", "", "&7Clear the cobwebs from your mine!")));
            cobwebButtonItem.setItemMeta(meta);
            inv.setItem(22, cobwebButtonItem);
            gui.setAction(22, event -> {
                HumanEntity whoClicked = event.getWhoClicked();
                if(!(whoClicked instanceof Player)) return false;
                gui.setAction(22, inventoryClickEvent -> false);
                inv.setItem(22, new ItemStack(Material.WHITE_STAINED_GLASS_PANE, 1));

                /* Open the GUI with animation */
                menuIntroductionAnimation(gui, upGui.getButtons(), () -> {
                            mine.unlockUpgrade(UpgradesConf.initialUpgrade, true, true);
                            new ActionMineDistribution(1);
                            openMainMenu(player, upGui);
                        }
                );

                return false;
            });

            player.getPlayer().openInventory(gui.getInventory());

        }else{
            /*
             * Open the GUI without animation
             */

            openMainMenu(player, upGui);
        }

    }

    public static void openMainMenu(MPlayer player, UpgradeGUI upGui){

        if(player.getPlayer() == null) return;

        ChestGui gui = ChestGui.getCreative(Bukkit.createInventory(null, WarrenGUIConf.get().guiSize, Color.get("&4&lMine Upgrades &r&7- Menu")));
        gui.setBottomInventoryAllow(false);
        gui.setAutoclosing(false);
        gui.setAutoclosing(true);
        Inventory inv = gui.getInventory();

        List<GUIButton> buttons = upGui.getButtons();
        List<Integer> lightGrey = MUtil.list(2,4,10,19,20,28,38,6,16,24,25,34,42,40);
        List<Integer> darkGrey = MUtil.list(0,1,9,18,27,36,37,7,8,17,26,35,44,43);

        ItemMeta lightGreyItemMeta = lightGreyItem.getItemMeta();
        lightGreyItemMeta.setDisplayName(" ");
        lightGreyItem.setItemMeta(lightGreyItemMeta);
        darkGreyItem.setItemMeta(lightGreyItemMeta);
        whiteItem.setItemMeta(lightGreyItemMeta);

        lightGrey.forEach(slot -> inv.setItem(slot, lightGreyItem.clone()));
        darkGrey.forEach(slot -> inv.setItem(slot, darkGreyItem.clone()));
        centerGlass.forEach(slot -> inv.setItem(slot, whiteItem.clone()));

        Mine mine = player.getMine();

        buttons.forEach(button -> {

            Material material = button.getMaterial();
            ItemStack item = new ItemStack(material, 1);
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(Color.get(button.getName()));
            meta.setLore(Color.get(button.getLore()));
            item.setItemMeta(meta);

            List<AbstractAction> actions = button.getOnClick();
            if(button instanceof GUIButtonMainUpgrade){
                MainUpgrade mainUpgrade = UpgradesConf.mainUpgrades.get(mine.getLevel() + 1);
                item.setAmount(mine.getLevel());
                if(mainUpgrade == null) {
                    meta.setLore(Color.get(MUtil.list("", "&7Max mine level reached: &a" + mine.getLevel())));
                    item.setItemMeta(meta);
                    item.setType(Material.IRON_BARS);
                    inv.setItem(button.getSlot(), item);
                    return;
                }else{
                    actions.addAll(mainUpgrade.getAbstractActions());
                    List<String> lore = mainUpgrade.getLore();
                    List<String> newLore = new ArrayList<>();
                    lore.forEach(s -> newLore.add(Color.get(s.replaceAll("%level%", Color.get("&7[&e" + mine.getLevel() + "&7] &m->&r &7[&a" + (mine.getLevel() + 1) + "&7]")))));
                    meta.setLore(newLore);
                }
            }

            for (String requiredUpgrade : button.getRequiredUnlockedUpgrades()) {
                if(!mine.isUpgradeUnlocked(requiredUpgrade)){
                    List<String> lore = Color.get(button.getLockedLore());
                    lore.set(0, Color.get("&7[&cLOCKED&7]"));
                    item.setType(Material.IRON_BARS);
                    meta.setLore(lore);
                    meta.setDisplayName(Color.get("&c" + ChatColor.stripColor(meta.getDisplayName())));
                    break;
                }
            }

            item.setItemMeta(meta);
            inv.setItem(button.getSlot(), item);
            gui.setAction(button.getSlot(), inventoryClickEvent -> {
                button.getOnClick().forEach(abstractAction -> abstractAction.apply(player)); return false;
            });
        });

        player.getPlayer().openInventory(gui.getInventory());
    }


    // Called every tick
    @Override
    public void run() {

    }

    //Run code when your trait is attached to a NPC.
    @Override
    public void onAttach() {

    }

    // Run code when the NPC is despawned. This is called before the entity actually despawns so npc.getEntity() is still valid.
    @Override
    public void onDespawn() {

    }

    //Run code when the NPC is spawned. Note that npc.getEntity() will be null until this method is called.
    //This is called AFTER onAttach and AFTER Load when the server is started.
    @Override
    public void onSpawn() {
        npc.data().set(NPC.NAMEPLATE_VISIBLE_METADATA, false);
    }

    //run code when the NPC is removed. Use this to tear down any repeating tasks.
    @Override
    public void onRemove() {

    }

    public void blockFill(Inventory inv, Material material){
        for (int b = 0; b < inv.getSize(); b++) {
            ItemStack p = new ItemStack(material, 1);
            ItemMeta itemMeta = p.getItemMeta();
            itemMeta.setDisplayName(" ");
            p.setItemMeta(itemMeta);
            inv.setItem(b, p);
        }
    }

    public void menuIntroductionAnimation(ChestGui gui, List<GUIButton> buttons, Runnable onFinish){

        Inventory inv = gui.getInventory();

        List<Cell> leftCells = new ArrayList<>();
        List<Cell> leftDarkGreyCells = new ArrayList<>();
        List<Cell> rightCells = new ArrayList<>();
        List<Cell> rightDarkGreyCells = new ArrayList<>();
        List<Cell> centeredCells = new ArrayList<>();

        buttons.forEach(button -> {
            ItemStack item = new ItemStack(button.getMaterial());
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(Color.get(button.getName()));
            meta.setLore(Color.get(button.getLore()));
            item.setItemMeta(meta);

            Cell cell = new Cell(item, button.getSlot());
            if(cell.col > 4){
                cell.moveCol(5);
                rightCells.add(cell);
            } else if (cell.col == 4){
                cell.moveRow(5);
                centeredCells.add(cell);
            }
            else{
                cell.moveCol(-5);
                leftCells.add(cell);
            }
        });

        fillerGlassLeft.forEach(slot -> {
            Cell cell = new Cell(lightGreyItem.clone(), slot);
            cell.moveCol(-5);
            leftCells.add(cell);
        });

        edgeGlassLeft.forEach(slot -> {
            Cell cell = new Cell(darkGreyItem.clone(), slot);
            cell.moveCol(-2);
            leftDarkGreyCells.add(cell);
        });

        fillerGlassRight.forEach(slot -> {
            Cell cell = new Cell(lightGreyItem.clone(), slot);
            cell.moveCol(5);
            rightCells.add(cell);
        });

        edgeGlassRight.forEach(slot -> {
            Cell cell = new Cell(darkGreyItem.clone(), slot);
            cell.moveCol(2);
            rightDarkGreyCells.add(cell);
        });


        /*
         * Runnable for the animation
         */
        new BukkitRunnable() {

            int counter = 0;
            int frame;
            int locked = lockableButtons.size();

            @Override
            public void run() {

                /*
                 * Portion of the animation where all buttons and gray glass cover GUI
                 */
                if (frame <= 4 && counter == 6) {
                    rightCells.forEach(cell -> {
                        cell.moveCol(-1);
                        if (cell.displayable()) inv.setItem(cell.getSlot(), cell.getItem());
                    });

                    leftCells.forEach(cell -> {
                        cell.moveCol(1);
                        if (cell.displayable()) inv.setItem(cell.getSlot(), cell.getItem());
                    });

                    counter = 0;
                    frame++;
                    return;
                }

                /*
                 * Portion of the animation where black stained glass cover the corners of the GUI
                 */
                else if (frame > 4 && frame < 7 && counter == 6) {
                    rightDarkGreyCells.forEach(cell -> {
                        cell.moveCol(-1);
                        if (cell.displayable()) inv.setItem(cell.getSlot(), cell.getItem());
                    });

                    leftDarkGreyCells.forEach(cell -> {
                        cell.moveCol(1);
                        if (cell.displayable()) inv.setItem(cell.getSlot(), cell.getItem());
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
                        centeredCells.forEach(cell -> {
                            int previousSlot = cell.getSlot();
                            cell.moveRow(-1);
                            if (cell.displayable()) inv.setItem(cell.getSlot(), cell.getItem());

                            ItemStack previousItem;
                            if (centerGlass.contains(previousSlot)) {
                                previousItem = whiteItem.clone();
                            } else {
                                previousItem = lightGreyItem.clone();
                            }

                            Cell previousCell = new Cell(previousItem, previousSlot);
                            if (previousCell.displayable()) inv.setItem(previousCell.getSlot(), previousCell.getItem());

                        });
                    }

                    /*
                     * All lockable buttons change to iron bars one-by-one
                     */
                    int slot = lockableButtons.get(locked - 1);
                    ItemStack item = inv.getItem(slot);
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







@FunctionalInterface
interface ClickEvent<A>{
     void run(A a);
}

class Cell {

    int row;
    int col;
    int invSize = WarrenGUIConf.get().guiSize;
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

    public void setItem(ItemStack item) {
        this.item = item;
    }
}