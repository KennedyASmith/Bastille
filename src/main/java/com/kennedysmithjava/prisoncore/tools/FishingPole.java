package com.kennedysmithjava.prisoncore.tools;

import com.jeff_media.morepersistentdatatypes.DataType;
import com.kennedysmithjava.prisoncore.PrisonCore;
import com.kennedysmithjava.prisoncore.entity.tools.FishingPoleType;
import com.kennedysmithjava.prisoncore.entity.tools.FishingPoleTypeColl;
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
 * A @code{FishingPole} object represents a cached ItemStack for a specific {@link com.kennedysmithjava.prisoncore.entity.tools.FishingPoleType}.
 *
 * Every @code{FishingPole} is cached on instantiation.
 *
 * @see com.kennedysmithjava.prisoncore.entity.tools.FishingPoleType
 * @see <a href="https://www.spigotmc.org/threads/more-persistent-data-types-collections-maps-and-arrays-for-pdc.520677/">...</a>
 *
 * @author Kennedy Smith
 */
@SuppressWarnings("DataFlowIssue")
public class FishingPole {

    // -------------------------------------------- //
    // STATIC POLE CACHE
    // -------------------------------------------- //

    /** Map of cached {@link FishingPole} objects */
    public static Map<String, FishingPole> cachedPoles = new HashMap<>();

    public static Map<FishingPole, ItemStack> updateQueue = new HashMap<>();


    // -------------------------------------------- //
    // NAMESPACE KEYS
    // -------------------------------------------- //
    private static final NamespacedKey KEY_POLE_DURABILITY = new NamespacedKey(PrisonCore.get(), "pole_durability");
    private static final NamespacedKey KEY_POLE_MAX_DURABILITY = new NamespacedKey(PrisonCore.get(), "pole_maxDurability");

    private static final NamespacedKey KEY_POLE_UUID = new NamespacedKey(PrisonCore.get(), "pole_uuid");

    private static final NamespacedKey KEY_POLE_TYPE = new NamespacedKey(PrisonCore.get(), "pole_type");

    /**
     * Used for updating lore on fishing poles.
     * Runs every 5 seconds.
     */
    public final static BukkitRunnable LORE_UPDATER = new BukkitRunnable() {
        @Override
        public void run() {
            List<FishingPole> removable = new ArrayList<>();
            updateQueue.forEach((pole, poleItem) -> {
                 if(poleItem != null && pole.getType() != null){
                    ItemMeta meta = poleItem.getItemMeta();
                    meta.setLore(pole.getType().getLore(
                            pole.getDurability(),
                            pole.getMaxDurability()));
                    poleItem.setItemMeta(meta);
                    pole.setItem(poleItem);
                }
                removable.add(pole);
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
     * Returns a cached {@link FishingPole} for this uuid.
     * @param uuid UUID of the Fishing pole.
     * @throws NullPointerException if a cached {@link FishingPole} doesn't exist for this uuid.
     */
    public static FishingPole getByUUID(String uuid) {
        return cachedPoles.get(uuid);
    }


    public static FishingPoleType get(Object oid) {
        return FishingPoleTypeColl.get().get(oid);
    }

    /**
     * Removes a {@link FishingPole} from the cache.
     * */
    public static void removeFromCache(String playerUUID) {
        cachedPoles.remove(playerUUID);
    }

    public static void addToLoreUpdateQueue(FishingPole pole, ItemStack poleItem){
        updateQueue.putIfAbsent(pole, poleItem);
    }

    // -------------------------------------------- //
    // POLE OBJECT VALUES
    // -------------------------------------------- //
    private final FishingPoleType type;
    private ItemStack item;
    private int durability;
    private int maxDurability;
    private String poleUUID;

    private String originalUser;

    public FishingPole(FishingPoleType type, ItemStack item, String poleUUID) {
        this.type = type;
        this.poleUUID = poleUUID;
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
        this.durability = pdc.getOrDefault(KEY_POLE_DURABILITY, DataType.INTEGER, 404);
        this.maxDurability = pdc.getOrDefault(KEY_POLE_MAX_DURABILITY, DataType.INTEGER, 404);
    }

    public void setDurability(int durability) {
        this.durability = durability;
        ItemMeta meta = item.getItemMeta();
        PersistentDataContainer pdc = meta.getPersistentDataContainer();
        pdc.set(KEY_POLE_DURABILITY, DataType.INTEGER, this.durability);
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
        pdc.set(KEY_POLE_MAX_DURABILITY, DataType.INTEGER, this.maxDurability);
        item.setItemMeta(meta);
    }
    public FishingPoleType getType() {
        return type;
    }

    /**
     * Gets item associated with this {@link FishingPole}
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
    public static boolean isPole(ItemStack i) {
        if (i == null || i.getType() == Material.AIR) return false;
        if(i.getType() != Material.FISHING_ROD) return false;

        ItemMeta meta = i.getItemMeta();
        if(meta == null) return false;
        PersistentDataContainer pdc = meta.getPersistentDataContainer();
        if(pdc.isEmpty()) return false;
        return pdc.has(KEY_POLE_UUID, DataType.UUID);
    }
    public static FishingPole get(ItemStack i) {
        ItemMeta meta = i.getItemMeta();
        PersistentDataContainer pdc = meta.getPersistentDataContainer();
        String type = pdc.get(KEY_POLE_TYPE, DataType.STRING);
        UUID uuid = pdc.get(KEY_POLE_UUID, DataType.UUID);
        FishingPole tool = FishingPole.getByUUID(uuid.toString());
        return (tool != null) ? tool : new FishingPole(FishingPoleTypeColl.get().get(type), i, uuid.toString());
    }

    public static FishingPole create(FishingPoleType type){
        UUID uuid = UUID.randomUUID();

        ItemStack item = new ItemStack(type.getMaterial(), 1);
        ItemMeta meta = item.getItemMeta();

        PersistentDataContainer pdc = meta.getPersistentDataContainer();
        pdc.set(KEY_POLE_UUID, DataType.UUID, uuid);
        pdc.set(KEY_POLE_DURABILITY, DataType.INTEGER, type.getStartDurability());
        pdc.set(KEY_POLE_MAX_DURABILITY, DataType.INTEGER, type.getMaxDurability());
        pdc.set(KEY_POLE_TYPE, DataType.STRING, type.getId());

        meta.setDisplayName(type.getDisplayName());
        meta.setLore(Color.get(type.getLore(type.getStartDurability(), type.getMaxDurability())));
        meta.setUnbreakable(true);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_UNBREAKABLE);
        item.setItemMeta(meta);
        return new FishingPole(type, item, uuid.toString());
    }


}
