package com.kennedysmithjava.prisoncore.tools;

import com.jeff_media.morepersistentdatatypes.DataType;
import com.kennedysmithjava.prisoncore.PrisonCore;
import com.kennedysmithjava.prisoncore.entity.tools.*;
import com.kennedysmithjava.prisoncore.util.Color;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

/**
 * A @code{Axe} object represents a cached ItemStack for a specific {@link com.kennedysmithjava.prisoncore.entity.tools.AxeType}.
 *
 * Every @code{Axe} is cached on instantiation.
 *
 * @see com.kennedysmithjava.prisoncore.entity.tools.AxeType
 * @see <a href="https://www.spigotmc.org/threads/more-persistent-data-types-collections-maps-and-arrays-for-pdc.520677/">...</a>
 *
 * @author Kennedy Smith
 */
@SuppressWarnings("DataFlowIssue")
public class Axe {

    // -------------------------------------------- //
    // STATIC AXE CACHE
    // -------------------------------------------- //

    /** Map of cached {@link Axe} objects */
    public static Map<String, Axe> cachedAxes = new HashMap<>();

    public static Map<Axe, ItemStack> updateQueue = new HashMap<>();


    // -------------------------------------------- //
    // NAMESPACE KEYS
    // -------------------------------------------- //
    private static final NamespacedKey KEY_AXE_DURABILITY = new NamespacedKey(PrisonCore.get(), "axe_durability");
    private static final NamespacedKey KEY_AXE_MAX_DURABILITY = new NamespacedKey(PrisonCore.get(), "axe_maxDurability");

    private static final NamespacedKey KEY_AXE_UUID = new NamespacedKey(PrisonCore.get(), "axe_uuid");

    private static final NamespacedKey KEY_AXE_TYPE = new NamespacedKey(PrisonCore.get(), "axe_type");

    /**
     * Used for updating lore on axes.
     * Runs every 5 seconds.
     */
    public final static BukkitRunnable LORE_UPDATER = new BukkitRunnable() {
        @Override
        public void run() {
            List<Axe> removable = new ArrayList<>();
            updateQueue.forEach((axe, axeItem) -> {
                 if(axeItem != null && axe.getType() != null){
                    ItemMeta meta = axeItem.getItemMeta();
                    meta.setLore(axe.getType().getLore(
                            axe.getDurability(),
                            axe.getMaxDurability()));
                    axeItem.setItemMeta(meta);
                    axe.setItem(axeItem);
                }
                removable.add(axe);
            });

            if(removable.size() > 0){
                removable.forEach(updateQueue::remove);
            }
        }
    };

    static {
        LORE_UPDATER.runTaskTimerAsynchronously(PrisonCore.get(), 20L, 5 * 20L);
    }

    /**
     * Returns a cached {@link Axe} for this uuid.
     * @param uuid UUID of the Axe.
     * @throws NullPointerException if a cached {@link Axe} doesn't exist for this uuid.
     */
    public static Axe getByUUID(String uuid) {
        return cachedAxes.get(uuid);
    }


    public static AxeType get(Object oid) {
        return AxeTypeColl.get().get(oid);
    }

    /**
     * Removes a {@link Axe} from the cache.
     * */
    public static void removeFromCache(String playerUUID) {
        cachedAxes.remove(playerUUID);
    }

    public static void addToLoreUpdateQueue(Axe axe, ItemStack axeItem){
        updateQueue.putIfAbsent(axe, axeItem);
    }

    // -------------------------------------------- //
    // AXE OBJECT VALUES
    // -------------------------------------------- //
    private final AxeType type;
    private ItemStack item;
    private int durability;
    private int maxDurability;
    private String axeUUID;

    private String originalUser;

    public Axe(AxeType type, ItemStack item, String axeUUID) {
        this.type = type;
        this.axeUUID = axeUUID;
        this.item = item;
        buildDurability();
    }

    public void setItem(ItemStack item) {
        this.item = item;
    }

    // -------------------------------------------- //
    // DURABILITY
    // -------------------------------------------- //

    public void buildDurability(){
        ItemMeta meta = item.getItemMeta();
        PersistentDataContainer pdc = meta.getPersistentDataContainer();
        this.durability = pdc.getOrDefault(KEY_AXE_DURABILITY, DataType.INTEGER, 404);
        this.maxDurability = pdc.getOrDefault(KEY_AXE_MAX_DURABILITY, DataType.INTEGER, 404);
    }

    public void setDurability(int durability) {
        this.durability = durability;
        ItemMeta meta = item.getItemMeta();
        PersistentDataContainer pdc = meta.getPersistentDataContainer();
        pdc.set(KEY_AXE_DURABILITY, DataType.INTEGER, this.durability);
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
        pdc.set(KEY_AXE_MAX_DURABILITY, DataType.INTEGER, this.maxDurability);
        item.setItemMeta(meta);
    }
    public AxeType getType() {
        return type;
    }

    /**
     * Gets item associated with this {@link Axe}
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
                    this.getDurability(),
                    this.getMaxDurability()));
            item.setItemMeta(meta);
            this.setItem(item);
        }
    }
    public static boolean isAxe(ItemStack i) {
        if (i == null || i.getType() == Material.AIR) return false;
        if(!isAxeType(i.getType())) return false;

        ItemMeta meta = i.getItemMeta();
        if(meta == null) return false;
        PersistentDataContainer pdc = meta.getPersistentDataContainer();
        if(pdc.isEmpty()) return false;
        return pdc.has(KEY_AXE_UUID, DataType.UUID);
    }

    static boolean isAxeType(Material material){
        return switch (material) {
            case DIAMOND_AXE, WOODEN_AXE, STONE_AXE, IRON_AXE, GOLDEN_AXE, NETHERITE_AXE -> true;
            default -> false;
        };
    }
    public static Axe get(ItemStack i) {
        ItemMeta meta = i.getItemMeta();
        PersistentDataContainer pdc = meta.getPersistentDataContainer();
        String type = pdc.get(KEY_AXE_TYPE, DataType.STRING);
        UUID uuid = pdc.get(KEY_AXE_UUID, DataType.UUID);
        Axe tool = Axe.getByUUID(uuid.toString());
        return (tool != null) ? tool : new Axe(AxeTypeColl.get().get(type), i, uuid.toString());
    }

    public static Axe create(AxeType type){
        UUID uuid = UUID.randomUUID();

        ItemStack item = new ItemStack(type.getMaterial(), 1);
        ItemMeta meta = item.getItemMeta();

        PersistentDataContainer pdc = meta.getPersistentDataContainer();
        pdc.set(KEY_AXE_UUID, DataType.UUID, uuid);
        pdc.set(KEY_AXE_DURABILITY, DataType.INTEGER, type.getStartDurability());
        pdc.set(KEY_AXE_MAX_DURABILITY, DataType.INTEGER, type.getMaxDurability());
        pdc.set(KEY_AXE_TYPE, DataType.STRING, type.getId());

        meta.setDisplayName(type.getDisplayName());
        meta.setLore(Color.get(type.getLore(type.getStartDurability(), type.getMaxDurability())));
        meta.setUnbreakable(true);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_UNBREAKABLE);
        item.setItemMeta(meta);
        return new Axe(type, item, uuid.toString());
    }


}
