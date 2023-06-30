package com.kennedysmithjava.prisoncore.engine;

import com.kennedysmithjava.prisoncore.eco.WorthUtil;
import com.kennedysmithjava.prisoncore.entity.mines.objects.PrisonBlock;
import com.kennedysmithjava.prisoncore.pouch.DatalessPouchable;
import com.kennedysmithjava.prisoncore.pouch.Pouch;
import com.kennedysmithjava.prisoncore.pouch.PouchManager;
import com.kennedysmithjava.prisoncore.util.ClickHandler;
import com.kennedysmithjava.prisoncore.util.Color;
import com.kennedysmithjava.prisoncore.util.RemovableItem;
import com.massivecraft.massivecore.Engine;
import com.massivecraft.massivecore.chestgui.ChestGui;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class EngineCollectorGUI extends Engine {
    private static final EngineCollectorGUI i = new EngineCollectorGUI();

    public static EngineCollectorGUI get() {
        return i;
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onInventoryClick(InventoryClickEvent e) {
        if (!(e.getWhoClicked() instanceof Player)) return;

        Player player = (Player) e.getWhoClicked();
        Inventory openInventory = player.getOpenInventory().getTopInventory();

        if (openInventory == null)
            return;

        if(e.getClickedInventory() == null)
            return;

        if (e.getClickedInventory().equals(openInventory))
            return; // Let the ChestGUI handle

        ChestGui chestGui = ChestGui.getInventoryToGui().get(openInventory);
        if (chestGui == null)
            return;

        if (((Integer) chestGui.getMeta().getOrDefault("coincollector", -1)) == -1)
            return;

        e.setCancelled(true);

        ItemStack item = e.getCurrentItem(); // Item clicked
        if (item == null)
            return;

        //TODO: Ticket [PRI-66]
        boolean sellable = WorthUtil.isSellable.test(item); //Check if item is sellable
        if (!sellable){
            if(Pouch.isPouch(item)){ //If this item is a pouch
                addPouched(item, chestGui.getInventory());
            }
            return;
        }

        Inventory inv = chestGui.getInventory();
        RemovableItem removableItem = new RemovableItem(item);
        int slot = inv.firstEmpty();
        if(slot == -1)
            return; // The inventory is full

        inv.addItem(removableItem);
        chestGui.setAction(slot, ClickHandler.toChestAction(removableItem.getClickHandler()));
        player.getInventory().removeItem(item);
        player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1, 1);
    }

    public void addPouched(ItemStack pouchItem, Inventory inv){
        final PouchManager pouchManager = PouchManager.get();
        Pouch pouch = pouchManager.getPouch(pouchItem);

        Map<DatalessPouchable, Integer> pouched = pouch.getPouched();
        if(pouched.size() == 0) return;

        Map<DatalessPouchable, Integer> toRemove = new HashMap<>();

        pouched.forEach((pouchable, amount) -> { //Get all items from the pouch
            AtomicInteger total = new AtomicInteger();
            PrisonBlock pb = WorthUtil.fromName(Color.get(pouchable.getDisplayName()));
            int stacks = Math.floorDiv(amount, 64);
            int remainder = amount % 64;
            if(stacks > 0){
                int addedStacks = 0;
                for (int i = 0; i < stacks; i++) {
                    ItemStack item = pb.getProductItem(1);
                    item.setAmount(64);
                    Map<Integer,ItemStack> couldNotAdd = inv.addItem(item);
                    if(couldNotAdd.size() > 0){
                        couldNotAdd.forEach((integer, itemStack) -> total.addAndGet(64 - itemStack.getAmount()));
                        break;
                    }
                    addedStacks += 1;
                }
                total.addAndGet(64 * addedStacks);
            }

            ItemStack item = pb.getProductItem(1);
            item.setAmount(remainder);
            Map<Integer,ItemStack> couldNotAdd = inv.addItem(item);
            if(couldNotAdd.size() > 0){
                couldNotAdd.forEach((integer, itemStack) -> total.addAndGet(remainder - itemStack.getAmount()));
            }else{
                total.addAndGet(remainder);
            }
            toRemove.put(pouchable, total.get());
        });

        pouch.removeAllPouched(pouchItem, toRemove);
        pouch.updateCount();
    }
}
