package com.kennedysmithjava.prisonmines.pouch;

import com.massivecraft.massivecore.Engine;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryPickupItemEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.UUID;

import static com.kennedysmithjava.prisonmines.pouch.Pouch.isPouch;
public class PouchDetectionEngine extends Engine {

    private static final PouchDetectionEngine i = new PouchDetectionEngine();

    public static PouchDetectionEngine get() {
        return i;
    }

    public PouchDetectionEngine() {}


    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        this.registerPouchesInInventory(event.getPlayer());
    }

    @EventHandler
    public void onInventoryInteract(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) {
            return;
        }
        
        if (isPouch(event.getCurrentItem()) || isPouch(event.getCursor())) {
            this.registerPouchesInInventory(((Player) event.getWhoClicked()));
        }
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent event) {
        if (isPouch(event.getItemDrop().getItemStack())) {
            this.registerPouchesInInventory(event.getPlayer());
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        if (event.getDrops().isEmpty()) {
            return;
        }

        this.registerPouchesInInventory(event.getEntity());
    }

    @EventHandler
    public void onPickup(InventoryPickupItemEvent event) {
        if (!(event.getInventory() instanceof PlayerInventory)) {
            ItemStack itemStack = event.getItem().getItemStack();
            if (isPouch(itemStack)) {
                PlayerInventory inventory = (PlayerInventory) event.getInventory();
                PlayerPouchHandler.get().registerPouch(inventory.getHolder().getUniqueId(), new Pouch(itemStack));
            }
        }
    }

    private void registerPouchesInInventory(Player player) {
        UUID uuid = player.getUniqueId();
        PlayerPouchHandler.get().remove(uuid);

        player.getInventory().forEach(i -> {
            if (i != null) {
                if (isPouch(i)) {
                    PlayerPouchHandler.get().registerPouch(uuid, new Pouch(i));
                }
            }
        });
    }

}
