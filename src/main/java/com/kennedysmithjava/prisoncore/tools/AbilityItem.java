package com.kennedysmithjava.prisoncore.tools;

import com.kennedysmithjava.prisoncore.event.AbilityUseEvent;
import com.kennedysmithjava.prisoncore.tools.ability.Ability;
import com.kennedysmithjava.prisoncore.tools.ability.AbilityType;
import com.kennedysmithjava.prisoncore.tools.ability.LeveledAbility;
import com.kennedysmithjava.prisoncore.util.Glow;
import com.kennedysmithjava.prisoncore.util.NBTUtil;
import com.massivecraft.massivecore.util.MUtil;
import de.tr7zw.nbtapi.NBTCompound;
import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import javax.annotation.Nullable;
import java.util.*;

public class AbilityItem {

    private static final String IS_ABILITY_ITEM = "isAbilityItem";

    private final LeveledAbility leveledAbility;
    private final NBTItem nbtItem;
    private final UUID uuid;

    /**
     * Create a new ItemStack, say for a reward
     * @param abilityType Type of the ability
     */
    public AbilityItem(AbilityType abilityType) {
        this(abilityType, 1, Collections.emptyMap());
    }

    /**
     * Create a new ItemStack, say for a reward
     * @param abilityType Type of the ability
     * @param level Level of the ability
     * @param bufferLevels Levels of each buffer
     */
    public AbilityItem(AbilityType abilityType, int level, Map<Buffer, Integer> bufferLevels) {
        this.uuid = UUID.randomUUID();
        this.leveledAbility = new LeveledAbility(abilityType, level, bufferLevels);
        this.nbtItem = new NBTItem(new ItemStack(getAbility().getItemMaterial(), 1));
        writeNBT(this.nbtItem);
    }

    /**
     * Get ability information from a pre-existing ability item
     * @param itemStack the ability ItemStack
     * @throws Exception if the ItemStack is not an ability item
     */
    public AbilityItem(ItemStack itemStack) throws Exception {
        this.nbtItem = new NBTItem(itemStack);
        this.leveledAbility = new LeveledAbility(this.nbtItem);
        Boolean isAbilityItem = this.nbtItem.getBoolean(IS_ABILITY_ITEM);
        if(isAbilityItem == null || !isAbilityItem) throw new Exception("ItemStack is not AbilityItem");
        this.uuid = NBTUtil.readUUID(this.nbtItem, "uuid");
    }

    /**
     * Create a new ability item, say from the 'splitting' of a pickaxe
     * @param leveledAbility ability with corresponding buffer levels
     */
    public AbilityItem(LeveledAbility leveledAbility) {
        this.leveledAbility = leveledAbility;
        this.nbtItem = new NBTItem(new ItemStack(getAbility().getItemMaterial(), 1));
        this.uuid = UUID.randomUUID();
        writeNBT(this.nbtItem);
    }

    private void writeNBT(NBTCompound nbtCompound) {
        nbtCompound.setBoolean(IS_ABILITY_ITEM, true);
        NBTUtil.writeUUID(nbtCompound, "uuid", this.uuid);
        this.leveledAbility.writeNBT(nbtCompound);
    }

    public AbilityType getAbilityType() {
        return this.leveledAbility.getAbilityType();
    }

    public Map<Buffer, Integer> getBufferLevels() {
        return this.leveledAbility.getBufferLevels();
    }

    public boolean isAllowedBuffer(Buffer buffer) {
        return this.leveledAbility.isAllowedBuffer(buffer);
    }

    public void attachBufferItem(BufferItem bufferItem) {
        this.upgradeBuffer(bufferItem.getBuffer(), bufferItem.getAmount());
        bufferItem.setAmount(0);
    }

    @Nullable
    public ItemStack stripBufferItem(Buffer buffer, int amount) {
        int max = this.leveledAbility.getBufferLevel(buffer);
        int actualAmount = Math.min(amount, max);
        if(actualAmount < 1) return null;
        this.downgradeBuffer(buffer, actualAmount);
        BufferItem result = new BufferItem(buffer);
        result.setAmount(actualAmount);
        return result.getItemStack();
    }

    public void upgradeBuffer(Buffer buffer) {
        this.leveledAbility.upgradeBuffer(buffer);
    }

    public void upgradeBuffer(Buffer buffer, int amount) {
        this.leveledAbility.upgradeBuffer(buffer, amount);
    }

    public boolean downgradeBuffer(Buffer buffer) {
        return this.leveledAbility.downgradeBuffer(buffer);
    }

    public boolean downgradeBuffer(Buffer buffer, int amount) {
        return this.leveledAbility.downgradeBuffer(buffer, amount);
    }

    public Set<Buffer> getBuffers() {
        return this.leveledAbility.getBuffers();
    }

    public Ability<?> getAbility() {
        return this.leveledAbility.getAbility();
    }

    public NBTItem getNBTItem() {
        return nbtItem;
    }

    public void perform(AbilityUseEvent event) {
        this.leveledAbility.perform(event);
    }

    public UUID getUuid() {
        return uuid;
    }

    public ItemStack getItemStack() {
        Ability<?> ability = getAbility();
        ItemStack result = this.nbtItem.getItem();
        ItemMeta itemMeta = result.getItemMeta();
        itemMeta.setDisplayName("" + ChatColor.RESET + ChatColor.BOLD
                + ChatColor.translateAlternateColorCodes('&', ability.getDisplayName())
                + ChatColor.RESET + " " + ChatColor.GRAY + "[" + ChatColor.GREEN + this.leveledAbility.getLevel() + ChatColor.GRAY + "]");

        List<String> lore = MUtil.list(
                "" + ChatColor.RESET + ChatColor.GRAY + "Pickaxe Ability",
                "" + ChatColor.RESET + ChatColor.GRAY + "[" + ChatColor.GREEN + "Lvl. " + this.leveledAbility.getLevel() + ChatColor.GRAY + "]",
                "",
                "" + ChatColor.RESET + ChatColor.GRAY + ability.getDescription(),
                "",
                "" + ChatColor.RESET + ChatColor.GRAY + "Buffers:");
        for(Map.Entry<Buffer, Integer> bufferLevel : getBufferLevels().entrySet()) {
            lore.add("" + ChatColor.RESET + ChatColor.BOLD
                    + ChatColor.translateAlternateColorCodes('&', bufferLevel.getKey().getDisplayName())
                    + ChatColor.GRAY + "[" + ChatColor.GREEN + bufferLevel.getValue() + ChatColor.GRAY + "]"
                    + ChatColor.DARK_GRAY + " ..."
            );
        }

        itemMeta.setLore(lore);
        result.setItemMeta(itemMeta);
        result.addEnchantment(Glow.getGlow(), 1);
        return result;
    }

    public static boolean isAbilityItem(ItemStack itemStack) {
        NBTItem nbtItem = new NBTItem(itemStack);
        Boolean isAbilityItem = nbtItem.getBoolean(IS_ABILITY_ITEM);
        return isAbilityItem != null && isAbilityItem;
    }

    public LeveledAbility getLeveledAbility() {
        return leveledAbility;
    }
}
