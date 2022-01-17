package com.kennedysmithjava.prisoncore.tools;

import com.kennedysmithjava.prisoncore.entity.tools.PickaxeType;
import com.kennedysmithjava.prisoncore.tools.ability.Ability;
import com.kennedysmithjava.prisoncore.tools.ability.LeveledAbility;
import com.kennedysmithjava.prisoncore.event.AbilityUseEvent;
import com.kennedysmithjava.prisoncore.cmd.CmdPickaxe;
import com.kennedysmithjava.prisoncore.tools.enchantment.BlockBreakEnchant;
import com.kennedysmithjava.prisoncore.tools.enchantment.DynamicEnchant;
import com.kennedysmithjava.prisoncore.tools.enchantment.Enchant;
import com.kennedysmithjava.prisoncore.engine.EngineTools;
import de.tr7zw.nbtapi.NBTCompound;
import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A @code{Pickaxe} object represents a cached ItemStack for a specific {@link PickaxeType}.
 *
 * Every @code{Pickaxe} is cached on instantiation.
 * Useful for faster {@link Tool} enchant queries.
 *
 * @see PickaxeType
 * @see Tool
 * @see CmdPickaxe
 * @see Enchant
 *
 * @author Kennedy Smith
 */

public class Pickaxe implements Tool {

    // -------------------------------------------- //
    // META
    // -------------------------------------------- //

    ItemStack item;
    NBTItem nbtItem;
    String originalUser;
    Map<Enchant<?>, Integer> enchants;
    Map<BlockBreakEnchant<?>, Integer> blockBreakEnchants;
    LeveledAbility leveledAbility;
    int durability;
    int maxDurability;
    PickaxeType type;
    String pickaxeUUID;

    public Pickaxe(PickaxeType type, ItemStack item, String pickaxeUUID) {
        this.type = type;
        this.nbtItem = new NBTItem(item);
        this.pickaxeUUID = pickaxeUUID;
        this.item = item;
        buildEnchants();
        buildDurability();
        if(hasLeveledAbility()) this.leveledAbility = buildAbility();
        this.nbtItem.setString("uuid", pickaxeUUID);
        this.nbtItem.applyNBT(item);
        cachedPickaxes.put(this.pickaxeUUID, this);
    }

    // -------------------------------------------- //
    // PICKAXE CACHE
    // -------------------------------------------- //

    /** Map of cached {@link Pickaxe} objects */
    public static Map<String, Pickaxe> cachedPickaxes = new HashMap<>();

    //Player UUID, Pickaxe
    public static Map<Pickaxe, ItemStack> updateQueue = new HashMap<>();

    /**
     * Used for updating lore on pickaxes.
     * Runs every 5 seconds.
     */
    public final static BukkitRunnable LORE_UPDATER = new BukkitRunnable() {
        @Override
        public void run() {
            List<Pickaxe> removable = new ArrayList<>();
            updateQueue.forEach((pickaxe, pickItem) -> {
                if(pickItem != null && cachedPickaxes.containsValue(pickaxe) && pickaxe.getType() != null){
                    ItemMeta meta = pickItem.getItemMeta();
                    meta.setLore(pickaxe.getType().getLore(
                            pickaxe.getEnchants(),
                            pickaxe.getLeveledAbility(),
                            pickaxe.getDurability(),
                            pickaxe.getMaxDurability()));
                    pickItem.setItemMeta(meta);
                    pickaxe.setItem(pickItem);
                }
                removable.add(pickaxe);
            });

            if(removable.size() > 0){
                removable.forEach(updateQueue::remove);
            }
        }
    };

    /**
     * Returns a cached {@link Pickaxe} for this uuid.
     * @param uuid UUID of the Pickaxe.
     * @throws NullPointerException if a cached {@link Pickaxe} doesn't exist for this uuid.
     */
    public static Pickaxe getByUUID(String uuid) {
        return cachedPickaxes.get(uuid);
    }

    public void setItem(ItemStack item) {
        this.item = item;
    }

    /**
     * Removes a {@link Pickaxe} from the cache.
     * */
    public static void removeFromCache(String playerUUID) {
        cachedPickaxes.remove(playerUUID);
    }

    public static void addToLoreUpdateQueue(Pickaxe pickaxe, ItemStack pickItem){
        updateQueue.putIfAbsent(pickaxe, pickItem);
    }
    // -------------------------------------------- //
    // ENCHANTS
    // -------------------------------------------- //

    /**
     * Builds a list of enchants from the NBTData of this {@link Pickaxe#item}
     * Runs {@link Pickaxe#enchantsModified()}
     */
    private void buildEnchants() {
        enchants = new HashMap<>();

        Map<?, ?> map = nbtItem.getObject("enchants", Map.class);
        if(map != null) {
            map.forEach((enchant, level) -> {
                if(!Enchant.exists((String) enchant)) return;
                Enchant<?> e = Enchant.getByName((String) enchant);
                Bukkit.broadcastMessage("En: " + e.getName() + " Lvl: " + level);
                enchants.put(e, (int) Math.round((Double) level));
            });
        }

        enchantsModified();
    }

    /**
     * Adds an enchant to the NBTData of this {@link Pickaxe#item}
     * Updates {@link Pickaxe#enchants}
     * Runs {@link Pickaxe#enchantsModified()}
     */
    public void addEnchant(Enchant<?> enchant, int level) {
        enchants.put(enchant, level);
        nbtItem.setObject("enchants", getEnchantsRaw());
        enchantsModified();
    }

    /**
     * Adds an enchant to the NBTData of this {@link Pickaxe#item}
     * Updates {@link Pickaxe#enchants}
     * Runs {@link Pickaxe#enchantsModified()}
     */
    public void addEnchant(String enchant, int level) {
        addEnchant(Enchant.getByName(enchant), level);
    }

    /**
     * Adds enchants to the NBTData of this {@link Pickaxe#item}
     * Updates {@link Pickaxe#enchants}
     * Runs {@link Pickaxe#enchantsModified()}
     */
    public void addEnchants(Map<String, Integer> enchants) {
        Map<Enchant<?>, Integer> newEnchants = new HashMap<>();
        enchants.forEach((s, lvl) -> newEnchants.put(Enchant.getByName(s), lvl));
        this.enchants.putAll(newEnchants);
        nbtItem.setObject("enchants", getEnchantsRaw());
        enchantsModified();
    }


    /**
     * Removes an enchant from the NBTData of this {@link Pickaxe#item}
     * Updates {@link Pickaxe#enchants}
     * Runs {@link Pickaxe#enchantsModified()}
     */
    public void removeEnchant(Enchant<?> enchant) {
        enchants.remove(enchant);
        nbtItem.setObject("enchants", getEnchantsRaw());
        enchantsModified();
    }

    /**
     * Reapplies {@link Pickaxe#nbtItem} to {@link Pickaxe#item}
     * Updates {@link Pickaxe#blockBreakEnchants}
     */
    public void enchantsModified(){
        nbtItem.applyNBT(item);
        Map<BlockBreakEnchant<?>, Integer> blockBreakEnchants = new HashMap<>();
        enchants.forEach((enchant, lvl) -> {
            if(enchant instanceof BlockBreakEnchant){
                BlockBreakEnchant<?> bbe = (BlockBreakEnchant<?>) enchant;
                blockBreakEnchants.put(bbe, lvl);
            } else if (enchant instanceof DynamicEnchant){
                DynamicEnchant<?> dynamicEnchant = (DynamicEnchant<?>) enchant;
                dynamicEnchant.onApply(getItem(), lvl);
            }
        });
        this.blockBreakEnchants = blockBreakEnchants;
    }

    /**
     * @return A mapping of {@link BlockBreakEnchant}s with their associated level for this {@link Pickaxe}
     */
    public Map<BlockBreakEnchant<?>, Integer> getBlockBreakEnchantments() {
        return blockBreakEnchants;
    }

    public boolean hasBlockBreakEnchantments(){
        return blockBreakEnchants.keySet().size() > 0;
    }

    public Map<Enchant<?>, Integer> getEnchants() {
        return enchants;
    }

    public Map<String, Integer> getEnchantsRaw(){
        Map<String, Integer> enchants = new HashMap<>();
        getEnchants().forEach((enchant, lvl) -> enchants.put(enchant.getID(), lvl));
        return enchants;
    }

    // -------------------------------------------- //
    // DURABILITY
    // -------------------------------------------- //

    public void buildDurability(){
        int durability = nbtItem.getInteger("durability");
        int maxDurability = nbtItem.getInteger("maxDurability");
        if(durability != 0 && maxDurability != 0) {
            this.durability = durability;
            this.maxDurability = maxDurability;
        }
    }

    public void setDurability(int durability) {
        this.durability = durability;
        nbtItem.applyNBT(item);
    }

    public int getDurability() {
        return durability;
    }

    public int getMaxDurability() {
        return maxDurability;
    }

    public void setMaxDurability(int maxDurability) {
        this.maxDurability = maxDurability;
    }

    // -------------------------------------------- //
    // BUFFERS
    // -------------------------------------------- //

    public Map<Buffer, Integer> getBufferLevels() {
        return this.leveledAbility.getBufferLevels();
    }

    // -------------------------------------------- //
    // LEVELED ABILITY
    // -------------------------------------------- //

    public void runAbility(AbilityUseEvent event) {
        if(this.leveledAbility != null)
            this.leveledAbility.perform(event);
    }

    @Nullable
    public Ability<?> getAbility() {
        return leveledAbility == null ? null : leveledAbility.getAbility();
    }

    @Nullable
    public LeveledAbility getLeveledAbility() {
        return leveledAbility;
    }

    public void setLeveledAbility(@Nullable LeveledAbility leveledAbility) {
        this.leveledAbility = leveledAbility;
        if(leveledAbility == null) {
            nbtItem.removeKey("leveledAbility");
        } else {
            NBTCompound nbtCompound = nbtItem.getOrCreateCompound("leveledAbility");
            leveledAbility.writeNBT(nbtCompound);
        }
        leveledAbilityModified();
    }

    @Nullable
    private LeveledAbility buildAbility() {
        NBTCompound nbtCompound = nbtItem.getCompound("leveledAbility");
        return nbtCompound == null ? null : new LeveledAbility(nbtCompound);
    }

    public boolean hasLeveledAbility(){
        return nbtItem.hasKey("leveledAbility") && nbtItem.getCompound("leveledAbility") != null;
    }

    private void leveledAbilityModified(){
        nbtItem.applyNBT(item);
    }

    /**
     * Strips off the LeveledAbility from the pickaxe
     * @return corresponding AbilityItem
     */
    public AbilityItem stripLeveledAbility() {
        if(this.leveledAbility == null) return null;
        AbilityItem abilityItem = new AbilityItem(this.leveledAbility);
        setLeveledAbility(null);
        return abilityItem;
    }

    /**
     * Attach an AbilityItem to the pickaxe.
     * Make sure to remove the AbilityItem afterwards
     * @param abilityItem the AbilityItem to add
     */
    public void attachLeveledAbility(AbilityItem abilityItem) {
        setLeveledAbility(abilityItem.getLeveledAbility());
    }

    // -------------------------------------------- //
    // MISC METHODS
    // -------------------------------------------- //

    /**
     * @return The UUID for the original stored user of this {@link Pickaxe}
     * @see EngineTools
     */
    public String getOriginalUser() {
        return originalUser;
    }

    /**
     *  Sets UUID for the original stored user of this {@link Pickaxe}
     * @see EngineTools
     */
    public void setOriginalUser(String originalUser) {
        this.originalUser = originalUser;
    }

    public NBTItem getNbtItem() {
        return nbtItem;
    }

    public PickaxeType getType() {
        return type;
    }

    /**
     * Gets item associated with this {@link Pickaxe}
     * @return The ItemStack
     */
    public ItemStack getItem() throws NullPointerException{
        if(item != null) return item;
        throw new NullPointerException();
    }



}
