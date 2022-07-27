package com.kennedysmithjava.prisoncore.tools;

import com.jeff_media.morepersistentdatatypes.DataType;
import com.kennedysmithjava.prisoncore.PrisonCore;
import com.kennedysmithjava.prisoncore.util.Glow;
import com.massivecraft.massivecore.util.MUtil;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;

import java.util.List;

public class BufferItem {

    private static final NamespacedKey bufferTypeKey = new NamespacedKey(PrisonCore.get(), "bufferType");

    private final Buffer buffer;
    private ItemStack item;
    private ItemMeta meta;

    /**
     * Create a new ItemStack, say for a reward
     * @param buffer Buffer to apply
     */
    public BufferItem(Buffer buffer) {
        this.buffer = buffer;
        this.item = new ItemStack(Material.SUGAR, 1);
        this.meta = item.getItemMeta();
    }

    /**
     * Get buffer information from a pre-existing buffer item
     * @param itemStack the buffer ItemStack
     * @throws Exception if the ItemStack is not an ability item
     */
    public BufferItem(ItemStack itemStack) throws Exception {
        ItemMeta meta = item.getItemMeta();
        PersistentDataContainer pdc = meta.getPersistentDataContainer();
        if(pdc.isEmpty() || !pdc.has(bufferTypeKey, DataType.STRING)) throw new Exception("ItemStack is not BufferItem");
        String bufferName = pdc.get(bufferTypeKey, DataType.STRING);
        this.buffer = Buffer.get(bufferName);
    }

    public Buffer getBuffer() {
        return buffer;
    }

    public ItemStack getNBTItem() {
        this.item.setItemMeta(meta);
        return item;
    }

    public static boolean isBufferItem(ItemStack itemStack) {
        PersistentDataContainer pdc = itemStack.getItemMeta().getPersistentDataContainer();
        return !pdc.isEmpty() && pdc.has(bufferTypeKey, DataType.STRING);
    }

    public int getAmount() {
        return this.item.getAmount();
    }

    public void setAmount(int amount) {
        this.item.setAmount(amount);
    }

    public ItemStack getItemStack() {
        Buffer buffer = this.buffer;
        ItemStack result = this.item;
        ItemMeta itemMeta = result.getItemMeta();
        itemMeta.setDisplayName("" + ChatColor.RESET + ChatColor.BOLD
                + ChatColor.translateAlternateColorCodes('&', buffer.getDisplayName())
                + ChatColor.RESET + " " + ChatColor.GRAY + "Buffer");

        List<String> lore = MUtil.list(
                "" + ChatColor.RESET + ChatColor.GRAY + "Applies a buffer to an ability");

        itemMeta.setLore(lore);
        result.setItemMeta(itemMeta);
        result.addEnchantment(Glow.getGlow(), 1);
        return result;
    }

}
