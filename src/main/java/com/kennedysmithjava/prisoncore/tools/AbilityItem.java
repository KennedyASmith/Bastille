package com.kennedysmithjava.prisoncore.tools;

import com.jeff_media.morepersistentdatatypes.DataType;
import com.kennedysmithjava.prisoncore.PrisonCore;
import com.kennedysmithjava.prisoncore.event.EventAbilityUse;
import com.kennedysmithjava.prisoncore.tools.ability.Ability;
import com.kennedysmithjava.prisoncore.tools.ability.AbilityType;
import com.kennedysmithjava.prisoncore.tools.ability.LeveledAbility;
import com.kennedysmithjava.prisoncore.util.Glow;
import com.massivecraft.massivecore.util.MUtil;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;

import javax.annotation.Nullable;
import java.util.*;

public class AbilityItem {

    private static final NamespacedKey abilityTypeKey = new NamespacedKey(PrisonCore.get(), "abilityType");
    private static final NamespacedKey abilityLevelKey = new NamespacedKey(PrisonCore.get(), "abilityLevel");
    private static final NamespacedKey abilityUUIDKey = new NamespacedKey(PrisonCore.get(), "abilityUUID");
    private static final NamespacedKey buffersKey = new NamespacedKey(PrisonCore.get(), "abilityBuffers");
    private final LeveledAbility leveledAbility;
    private final ItemStack item;

    private final ItemMeta meta;
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
        this.item = new ItemStack(getAbility().getItemMaterial(), 1);
        this.meta = item.getItemMeta();
        meta.getPersistentDataContainer().set(abilityTypeKey, DataType.STRING, abilityType.getId());
        meta.getPersistentDataContainer().set(buffersKey, DataType.asMap(DataType.STRING, DataType.INTEGER), Buffer.serialize(bufferLevels));
        meta.getPersistentDataContainer().set(abilityLevelKey, DataType.INTEGER, level);
        meta.getPersistentDataContainer().set(abilityUUIDKey, DataType.UUID, uuid);
    }

    /**
     * Get ability information from a pre-existing ability item
     * @param itemStack the ability ItemStack
     */
    public AbilityItem(ItemStack itemStack){
        this.item = itemStack;
        this.meta = itemStack.getItemMeta();
        AbilityType abilityType = AbilityType.getFromId(meta.getPersistentDataContainer().get(abilityTypeKey, DataType.STRING));
        Map<Buffer, Integer> buffers = Buffer.deserialize(meta.getPersistentDataContainer().get(buffersKey, DataType.asMap(DataType.STRING, DataType.INTEGER)));
        int level = meta.getPersistentDataContainer().get(abilityLevelKey, DataType.INTEGER);
        this.leveledAbility = new LeveledAbility(abilityType, level, buffers);
        this.uuid = meta.getPersistentDataContainer().get(abilityUUIDKey, DataType.UUID);
    }

    /**
     * Create a new ability item, say from the 'splitting' of a pickaxe
     * @param leveledAbility ability with corresponding buffer levels
     */
    public AbilityItem(LeveledAbility leveledAbility) {
        this(leveledAbility.getAbilityType(), leveledAbility.getLevel(), leveledAbility.getBufferLevels());
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

    public void perform(EventAbilityUse event) {
        this.leveledAbility.perform(event);
    }

    public UUID getUuid() {
        return uuid;
    }

    public ItemStack getItemStack() {
        Ability<?> ability = getAbility();
        ItemStack result = this.item;
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
        ItemMeta meta = itemStack.getItemMeta();
        PersistentDataContainer pdc = meta.getPersistentDataContainer();
        return !pdc.isEmpty() && pdc.has(abilityTypeKey, DataType.STRING);
    }

    public LeveledAbility getLeveledAbility() {
        return leveledAbility;
    }
}
