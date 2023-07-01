package com.kennedysmithjava.prisoncore.tools;

import com.jeff_media.morepersistentdatatypes.DataType;
import com.kennedysmithjava.prisoncore.PrisonCore;
import com.kennedysmithjava.prisoncore.enchantment.Enchant;
import com.kennedysmithjava.prisoncore.entity.tools.HoeType;
import com.kennedysmithjava.prisoncore.entity.tools.HoeTypeColl;
import com.kennedysmithjava.prisoncore.entity.tools.PickaxeType;
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
 * A @code{Hoe} object represents a cached ItemStack for a specific {@link HoeType}.
 *
 * Every @code{Hoe} is cached on instantiation.
 *
 * @see PickaxeType
 * @see com.kennedysmithjava.prisoncore.cmd.CmdPickaxe
 * @see Enchant
 * @see <a href="https://www.spigotmc.org/threads/more-persistent-data-types-collections-maps-and-arrays-for-pdc.520677/">...</a>
 *
 * @author Kennedy Smith
 */
public class Hoe {

    // -------------------------------------------- //
    // STATIC HOE CACHE
    // -------------------------------------------- //

    /** Map of cached {@link Hoe} objects */
    public static Map<String, Hoe> cachedHoes = new HashMap<>();

    //Player UUID, Pickaxe
    public static Map<Hoe, ItemStack> updateQueue = new HashMap<>();


    // -------------------------------------------- //
    // NAMESPACE KEYS
    // -------------------------------------------- //
    private static transient final NamespacedKey KEY_HOE_DURABILITY = new NamespacedKey(PrisonCore.get(), "hoe_durability");
    private static transient final NamespacedKey KEY_HOE_MAX_DURABILITY = new NamespacedKey(PrisonCore.get(), "hoe_maxDurability");

    private static transient final NamespacedKey KEY_HOE_UUID = new NamespacedKey(PrisonCore.get(), "hoe_uuid");

    private static transient final NamespacedKey KEY_HOE_TYPE = new NamespacedKey(PrisonCore.get(), "hoe_type");

    /**
     * Used for updating lore on hoes.
     * Runs every 5 seconds.
     */
    public final static BukkitRunnable LORE_UPDATER = new BukkitRunnable() {
        @Override
        public void run() {
            List<Hoe> removable = new ArrayList<>();
            updateQueue.forEach((hoe, hoeItem) -> {
                 if(hoeItem != null && hoe.getType() != null){
                    ItemMeta meta = hoeItem.getItemMeta();
                    meta.setLore(hoe.getType().getLore(
                            hoe.getDurability(),
                            hoe.getMaxDurability()));
                    hoeItem.setItemMeta(meta);
                    hoe.setItem(hoeItem);
                }
                removable.add(hoe);
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
     * Returns a cached {@link Hoe} for this uuid.
     * @param uuid UUID of the Hoe.
     * @throws NullPointerException if a cached {@link Hoe} doesn't exist for this uuid.
     */
    public static Hoe getByUUID(String uuid) {
        return cachedHoes.get(uuid);
    }


    public static HoeType get(Object oid) {
        return HoeTypeColl.get().get(oid);
    }

    /**
     * Removes a {@link Hoe} from the cache.
     * */
    public static void removeFromCache(String playerUUID) {
        cachedHoes.remove(playerUUID);
    }

    public static void addToLoreUpdateQueue(Hoe hoe, ItemStack hoeItem){
        updateQueue.putIfAbsent(hoe, hoeItem);
    }

    // -------------------------------------------- //
    // PICKAXE OBJECT VALUES
    // -------------------------------------------- //
    private ItemStack item;
    private int durability;
    private int maxDurability;
    private HoeType type;
    private String pickaxeUUID;

    private String originalUser;

    public Hoe(HoeType type, ItemStack item, String pickaxeUUID) {
        this.type = type;
        this.pickaxeUUID = pickaxeUUID;
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
        this.durability = pdc.getOrDefault(KEY_HOE_DURABILITY, DataType.INTEGER, 404);
        this.maxDurability = pdc.getOrDefault(KEY_HOE_MAX_DURABILITY, DataType.INTEGER, 404);
    }

    public void setDurability(int durability) {
        this.durability = durability;
        ItemMeta meta = item.getItemMeta();
        PersistentDataContainer pdc = meta.getPersistentDataContainer();
        pdc.set(KEY_HOE_DURABILITY, DataType.INTEGER, this.durability);
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
        pdc.set(KEY_HOE_MAX_DURABILITY, DataType.INTEGER, this.maxDurability);
        item.setItemMeta(meta);
    }
    public HoeType getType() {
        return type;
    }

    /**
     * Gets item associated with this {@link Hoe}
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
    public static boolean isHoe(ItemStack i) {
        if (i == null || i.getType() == Material.AIR) return false;
        if(!isHoeType(i.getType())) return false;

        ItemMeta meta = i.getItemMeta();
        if(meta == null) return false;
        PersistentDataContainer pdc = meta.getPersistentDataContainer();
        if(pdc.isEmpty()) return false;
        return pdc.has(KEY_HOE_UUID, DataType.UUID);
    }
    static boolean isHoeType(Material material){
        return switch (material) {
            case DIAMOND_HOE, WOODEN_HOE, STONE_HOE, IRON_HOE, GOLDEN_HOE, NETHERITE_HOE -> true;
            default -> false;
        };
    }
    
    public static Hoe get(ItemStack i) {
        ItemMeta meta = i.getItemMeta();
        PersistentDataContainer pdc = meta.getPersistentDataContainer();
        String type = pdc.get(KEY_HOE_TYPE, DataType.STRING);
        UUID uuid = pdc.get(KEY_HOE_UUID, DataType.UUID);
        Hoe tool = Hoe.getByUUID(uuid.toString());
        return (tool != null) ? tool : new Hoe(HoeTypeColl.get().get(type), i, uuid.toString());
    }

    public static Hoe create(HoeType type){
        UUID uuid = UUID.randomUUID();

        ItemStack item = new ItemStack(type.getMaterial(), 1);
        ItemMeta meta = item.getItemMeta();

        PersistentDataContainer pdc = meta.getPersistentDataContainer();
        pdc.set(KEY_HOE_UUID, DataType.UUID, uuid);
        pdc.set(KEY_HOE_DURABILITY, DataType.INTEGER, type.getStartDurability());
        pdc.set(KEY_HOE_MAX_DURABILITY, DataType.INTEGER, type.getMaxDurability());
        pdc.set(KEY_HOE_TYPE, DataType.STRING, type.getId());

        meta.setDisplayName(type.getDisplayName());
        meta.setLore(Color.get(type.getLore(type.getStartDurability(), type.getMaxDurability())));
        meta.setUnbreakable(true);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_UNBREAKABLE);
        item.setItemMeta(meta);
        return new Hoe(type, item, uuid.toString());
    }


}
