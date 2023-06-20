package com.kennedysmithjava.prisoncore.tools;

import com.jeff_media.morepersistentdatatypes.DataType;
import com.kennedysmithjava.prisoncore.PrisonCore;
import com.kennedysmithjava.prisoncore.engine.EngineTools;
import com.kennedysmithjava.prisoncore.entity.tools.PickaxeType;
import com.kennedysmithjava.prisoncore.entity.tools.PickaxeTypeColl;
import com.kennedysmithjava.prisoncore.event.EventAbilityUse;
import com.kennedysmithjava.prisoncore.tools.ability.Ability;
import com.kennedysmithjava.prisoncore.tools.ability.AbilityType;
import com.kennedysmithjava.prisoncore.tools.ability.LeveledAbility;
import com.kennedysmithjava.prisoncore.tools.enchantment.BlockBreakEnchant;
import com.kennedysmithjava.prisoncore.tools.enchantment.DynamicEnchant;
import com.kennedysmithjava.prisoncore.tools.enchantment.Enchant;
import com.kennedysmithjava.prisoncore.util.Color;
import com.massivecraft.massivecore.util.MUtil;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.scheduler.BukkitRunnable;

import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.Collectors;


/**
 * A @code{Pickaxe} object represents a cached ItemStack for a specific {@link PickaxeType}.
 *
 * Every @code{Pickaxe} is cached on instantiation.
 * Useful for faster {@link Tool} enchant queries.
 *
 * @see PickaxeType
 * @see Tool
 * @see com.kennedysmithjava.prisoncore.cmd.CmdPickaxe
 * @see Enchant
 * @see <a href="https://www.spigotmc.org/threads/more-persistent-data-types-collections-maps-and-arrays-for-pdc.520677/">...</a>
 *
 * @author Kennedy Smith
 */
public class Pickaxe  implements Tool {

    // -------------------------------------------- //
    // STATIC PICKAXE CACHE
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
                 if(pickItem != null && pickaxe.getType() != null){
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
    // Namespace Keys
    // -------------------------------------------- //

    private static final NamespacedKey enchantsKey = new NamespacedKey(PrisonCore.get(), "pick_enchants");
    private static final NamespacedKey durabilityKey = new NamespacedKey(PrisonCore.get(), "pick_durability");
    private static final NamespacedKey maxDurabilityKey = new NamespacedKey(PrisonCore.get(), "pick_maxDurability");

    private static final NamespacedKey abilityKey = new NamespacedKey(PrisonCore.get(), "pick_ability");

    private static final NamespacedKey bufferKey = new NamespacedKey(PrisonCore.get(), "pick_buffers");

    private static final NamespacedKey ogUserKey = new NamespacedKey(PrisonCore.get(), "pick_ogUser");
    
    private static final NamespacedKey uuidKey = new NamespacedKey(PrisonCore.get(), "pick_uuid");
    
    private static final NamespacedKey typeKey = new NamespacedKey(PrisonCore.get(), "pick_type");

    // -------------------------------------------- //
    // PICKAXE OBJECT VALUES
    // -------------------------------------------- //
    private ItemStack item;
    private Map<Enchant<?>, Integer> enchants;
    private Map<BlockBreakEnchant<?>, Integer> blockBreakEnchants;
    private LeveledAbility leveledAbility;
    private int durability;
    private int maxDurability;
    private PickaxeType type;
    private String pickaxeUUID;

    private String originalUser;

    public Pickaxe(PickaxeType type, ItemStack item, String pickaxeUUID) {
        this.type = type;
        this.pickaxeUUID = pickaxeUUID;
        this.item = item;
        buildEnchants();
        buildDurability();
        buildAbility();
    }

    public void setItem(ItemStack item) {
        this.item = item;
    }

    // -------------------------------------------- //
    // ENCHANTS
    // -------------------------------------------- //

    /**
     * Builds a list of enchants from the NBTData of this {@link Pickaxe#item}
     * Runs {@link Pickaxe}
     */
    private void buildEnchants() {
        ItemMeta meta = item.getItemMeta();
        PersistentDataContainer pdc = meta.getPersistentDataContainer();
        Map<String, Integer> map = pdc.getOrDefault(enchantsKey, DataType.asMap(DataType.STRING, DataType.INTEGER), new HashMap<>());
        enchants = new HashMap<>();
        if(map != null) {
            map.forEach((enchant, level) -> {
                if(!Enchant.exists(enchant)) return;
                Enchant<?> e = Enchant.getByName(enchant);
                enchants.put(e, level);
            });
        }
        enchantsModified(meta);
    }

    /**
     * Adds an enchant to the NBTData of this {@link Pickaxe#item}
     * Updates {@link Pickaxe#enchants}
     */
    public void addEnchant(Enchant<?> enchant, int level) {
        enchants.put(enchant, level);
        ItemMeta meta = item.getItemMeta();
        PersistentDataContainer pdc = meta.getPersistentDataContainer();
        pdc.set(enchantsKey, DataType.asMap(DataType.STRING, DataType.INTEGER), getEnchantsRaw());
        enchantsModified(meta);
    }

    /**
     * Adds an enchant to the NBTData of this {@link Pickaxe#item}
     * Updates {@link Pickaxe#enchants}
     */
    public void addEnchant(String enchant, int level) {
        addEnchant(Enchant.getByName(enchant), level);
    }

    /**
     * Adds enchants to the NBTData of this {@link Pickaxe#item}
     * Updates {@link Pickaxe#enchants}
     */
    public void addEnchants(Map<String, Integer> enchants) {
        Map<Enchant<?>, Integer> newEnchants = new HashMap<>();
        enchants.forEach((s, lvl) -> newEnchants.put(Enchant.getByName(s), lvl));
        this.enchants.putAll(newEnchants);
        ItemMeta meta = item.getItemMeta();
        PersistentDataContainer pdc = meta.getPersistentDataContainer();
        pdc.set(enchantsKey, DataType.asMap(DataType.STRING, DataType.INTEGER), enchants);
        enchantsModified(meta);
    }


    /**
     * Removes an enchant from the NBTData of this {@link Pickaxe#item}
     * Updates {@link Pickaxe#enchants}
     */
    public void removeEnchant(Enchant<?> enchant) {
        enchants.remove(enchant);
        ItemMeta meta = item.getItemMeta();
        PersistentDataContainer pdc = meta.getPersistentDataContainer();
        pdc.set(enchantsKey, DataType.asMap(DataType.STRING, DataType.INTEGER), getEnchantsRaw());
        enchantsModified(meta);
    }

    /**
     * Reapplies {@link Pickaxe} to {@link Pickaxe#item}
     * Updates {@link Pickaxe#blockBreakEnchants}
     */
    public void enchantsModified(ItemMeta meta){
        item.setItemMeta(meta);
        Map<BlockBreakEnchant<?>, Integer> blockBreakEnchants = new HashMap<>();
        enchants.forEach((enchant, lvl) -> {
            if(enchant instanceof BlockBreakEnchant<?> bbe){
                blockBreakEnchants.put(bbe, lvl);
            } else if (enchant instanceof DynamicEnchant<?> dynamicEnchant){
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
        return this.enchants;
    }

    public void setEnchants(Map<Enchant<?>, Integer> enchants) {
        this.enchants = new HashMap<>();
        this.addEnchants(enchants.entrySet().stream()
                .collect(Collectors.toMap(
                        entry -> entry.getKey().getID(),
                        Map.Entry::getValue
                )));
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
        ItemMeta meta = item.getItemMeta();
        PersistentDataContainer pdc = meta.getPersistentDataContainer();
        this.durability = pdc.getOrDefault(durabilityKey, DataType.INTEGER, 404);
        this.maxDurability = pdc.getOrDefault(maxDurabilityKey, DataType.INTEGER, 404);
    }

    public void setDurability(int durability) {
        this.durability = durability;
        ItemMeta meta = item.getItemMeta();
        PersistentDataContainer pdc = meta.getPersistentDataContainer();
        pdc.set(durabilityKey, DataType.INTEGER, this.durability);
        item.setItemMeta(meta);
    }

    public int getDurability() {
        return durability;
    }

    public int getMaxDurability() {
        return maxDurability;
    }

    public void setMaxDurability(int maxDurability) {
        this.maxDurability = maxDurability;
        ItemMeta meta = item.getItemMeta();
        PersistentDataContainer pdc = meta.getPersistentDataContainer();
        pdc.set(maxDurabilityKey, DataType.INTEGER, this.maxDurability);
        item.setItemMeta(meta);
    }

    // -------------------------------------------- //
    // LEVELED ABILITY
    // -------------------------------------------- //

    private void buildAbility() {
        ItemMeta meta = item.getItemMeta();
        PersistentDataContainer pdc = meta.getPersistentDataContainer();

        Map<String, String> abilityInfo = pdc.getOrDefault(abilityKey,
                DataType.asMap(DataType.STRING, DataType.STRING), new HashMap<>());

        if(abilityInfo.size() > 0){
            AbilityType type = AbilityType.getFromId(abilityInfo.get("type"));
            int level = Integer.parseInt(abilityInfo.get("level"));
            Map<Buffer, Integer> buffers = Buffer.deserialize(pdc.getOrDefault(bufferKey,
                            DataType.asMap(DataType.STRING, DataType.INTEGER), new HashMap<>()));
            this.leveledAbility = new LeveledAbility(type, level, buffers);
        }
    }

    public void setLeveledAbility(@Nullable LeveledAbility leveledAbility) {
        this.leveledAbility = leveledAbility;
        ItemMeta meta = item.getItemMeta();
        PersistentDataContainer pdc = meta.getPersistentDataContainer();
        pdc.set(abilityKey, DataType.asMap(DataType.STRING, DataType.STRING), MUtil.map("type", leveledAbility.getAbilityType().getId(), "level", String.valueOf(leveledAbility.getLevel())));
        pdc.set(bufferKey, DataType.asMap(DataType.STRING, DataType.INTEGER), Buffer.serialize(leveledAbility.getBufferLevels()));
        leveledAbilityModified(meta);
    }


    @Nullable
    public Ability<?> getAbility() {
        return leveledAbility == null ? null : leveledAbility.getAbility();
    }

    @Nullable
    public LeveledAbility getLeveledAbility() {
        return leveledAbility;
    }


    public void runAbility(EventAbilityUse event) {
        if(this.leveledAbility != null)
            this.leveledAbility.perform(event);
    }

    public boolean hasLeveledAbility(){
        return getLeveledAbility() != null;
    }

    private void leveledAbilityModified(ItemMeta meta){
        item.setItemMeta(meta);
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
    // BUFFERS
    // -------------------------------------------- //

    public Map<Buffer, Integer> getBufferLevels() {
        return this.leveledAbility.getBufferLevels();
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

    public void forceLoreUpdate(){
        if(item != null && this.getType() != null){
            ItemMeta meta = item.getItemMeta();
            meta.setLore(this.getType().getLore(
                    this.getEnchants(),
                    this.getLeveledAbility(),
                    this.getDurability(),
                    this.getMaxDurability()));
            item.setItemMeta(meta);
            this.setItem(item);
        }
    }
    public static boolean isPickaxe(ItemStack i) {
        if (i == null || i.getType() == Material.AIR) return false;
        if(!isPickaxeType(i.getType())) return false;

        ItemMeta meta = i.getItemMeta();
        PersistentDataContainer pdc = meta.getPersistentDataContainer();
        if(pdc.isEmpty()) return false;
        if(!pdc.has(uuidKey, DataType.UUID)) return false;
        return true;
    }
    static boolean isPickaxeType(Material material){
        return switch (material) {
            case DIAMOND_PICKAXE, WOODEN_PICKAXE, STONE_PICKAXE, IRON_PICKAXE, GOLDEN_PICKAXE, NETHERITE_PICKAXE -> true;
            default -> false;
        };
    }
    
    public static Pickaxe get(ItemStack i) {
        ItemMeta meta = i.getItemMeta();
        PersistentDataContainer pdc = meta.getPersistentDataContainer();
        String type = pdc.get(typeKey, DataType.STRING);
        UUID uuid = pdc.get(uuidKey, DataType.UUID);
        Pickaxe tool = Pickaxe.getByUUID(uuid.toString());
        return (tool != null) ? tool : new Pickaxe(PickaxeTypeColl.get().get(type), i, uuid.toString());
    }

    public static Pickaxe create(PickaxeType type){

        UUID uuid = UUID.randomUUID();

        LeveledAbility leveledAbility = null;

        ItemStack item = new ItemStack(type.getMaterial(), 1);
        ItemMeta meta = item.getItemMeta();

        PersistentDataContainer pdc = meta.getPersistentDataContainer();
        pdc.set(uuidKey, DataType.UUID, uuid);
        pdc.set(durabilityKey, DataType.INTEGER, type.getStartDurability());
        pdc.set(maxDurabilityKey, DataType.INTEGER, type.getMaxDurability());
        pdc.set(typeKey, DataType.STRING, type.getId());

        if(!Objects.equals(type.getAbilityString(), "none") && type.getAbilityString() != null){
            leveledAbility = new LeveledAbility(type.getAbility().getAbilityType(), 1, type.getBuffers());
            pdc.set(abilityKey, DataType.asMap(DataType.STRING, DataType.STRING),
                    MUtil.map("type", leveledAbility.getAbilityType().getId(), "level", String.valueOf(leveledAbility.getLevel())));
            pdc.set(bufferKey, DataType.asMap(DataType.STRING, DataType.INTEGER), Buffer.serialize(leveledAbility.getBufferLevels()));
        }

        meta.setDisplayName(type.getDisplayName());
        meta.setLore(Color.get(type.getLore(type.getEnchants(), leveledAbility, type.getStartDurability(), type.getMaxDurability())));
        meta.setUnbreakable(true);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_UNBREAKABLE);
        item.setItemMeta(meta);

        Pickaxe pickaxe = new Pickaxe(type, item, uuid.toString());
        pickaxe.addEnchants(type.getEnchantsRaw());

        return pickaxe;
    }


}
