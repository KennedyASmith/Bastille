package com.kennedysmithjava.prisoncore.tools;

import com.kennedysmithjava.prisoncore.util.Glow;
import com.massivecraft.massivecore.util.MUtil;
import de.tr7zw.nbtapi.NBTCompound;
import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class BufferItem {

    private static final String IS_BUFFER_ITEM = "isBufferItem";
    private static final String BUFFER_TYPE = "bufferType";

    private final Buffer buffer;
    private final NBTItem nbtItem;

    /**
     * Create a new ItemStack, say for a reward
     * @param buffer Buffer to apply
     */
    public BufferItem(Buffer buffer) {
        this.buffer = buffer;
        this.nbtItem = new NBTItem(new ItemStack(Material.SUGAR, 1));
        writeNBT(this.nbtItem);
    }

    /**
     * Get buffer information from a pre-existing buffer item
     * @param itemStack the buffer ItemStack
     * @throws Exception if the ItemStack is not an ability item
     */
    public BufferItem(ItemStack itemStack) throws Exception {
        this.nbtItem = new NBTItem(itemStack);
        Boolean isBufferItem = this.nbtItem.getBoolean(IS_BUFFER_ITEM);
        if(isBufferItem == null || !isBufferItem) throw new Exception("ItemStack is not BufferItem");
        String bufferName = this.nbtItem.getString(BUFFER_TYPE);
        this.buffer = Buffer.get(bufferName);
    }

    public Buffer getBuffer() {
        return buffer;
    }

    public NBTItem getNBTItem() {
        return nbtItem;
    }

    private void writeNBT(NBTCompound nbtCompound) {
        nbtCompound.setBoolean(IS_BUFFER_ITEM, true);
        nbtCompound.setString(BUFFER_TYPE, buffer.getName());
    }

    public static boolean isBufferItem(ItemStack itemStack) {
        NBTItem nbtItem = new NBTItem(itemStack);
        Boolean isAbilityItem = nbtItem.getBoolean(IS_BUFFER_ITEM);
        return isAbilityItem != null && isAbilityItem;
    }

    public int getAmount() {
        return this.nbtItem.getItem().getAmount();
    }

    public void setAmount(int amount) {
        this.nbtItem.getItem().setAmount(amount);
    }

    public ItemStack getItemStack() {
        Buffer buffer = this.buffer;
        ItemStack result = this.nbtItem.getItem();
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
