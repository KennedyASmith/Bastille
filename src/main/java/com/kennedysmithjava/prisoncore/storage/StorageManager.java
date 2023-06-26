package com.kennedysmithjava.prisoncore.storage;

import com.drtshock.playervaults.vaultmanagement.VaultManager;
import com.kennedysmithjava.prisoncore.entity.player.MPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class StorageManager {

    private static final StorageManager instance = new StorageManager();
    public static StorageManager get() { return instance; }

    private final VaultManager vaultManager;

    public StorageManager() {
        vaultManager = VaultManager.getInstance();

    }

    public void saveChest(Player player, StorageType type, Inventory chestInventory) {
        vaultManager.saveVault(chestInventory, player.getName(), type.getId());
    }

    public Inventory loadChest(MPlayer player, StorageType type) {
        return vaultManager.getVault(player.getName(), type.getId());
    }

    public static Inventory expandInventory(Inventory originalInventory, int targetSize) {
        // Create a new inventory with the desired size
        Inventory expandedInventory = Bukkit.createInventory(null, targetSize);

        // Copy the contents from the original inventory to the expanded inventory
        ItemStack[] originalContents = originalInventory.getContents();
        ItemStack[] expandedContents = new ItemStack[targetSize];

        System.arraycopy(originalContents, 0, expandedContents, 0, Math.min(originalContents.length, targetSize));
        expandedInventory.setContents(expandedContents);

        return expandedInventory;
    }

}

