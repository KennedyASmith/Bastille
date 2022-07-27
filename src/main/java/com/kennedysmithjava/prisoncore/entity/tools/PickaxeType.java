package com.kennedysmithjava.prisoncore.entity.tools;

import com.kennedysmithjava.prisoncore.tools.Buffer;
import com.kennedysmithjava.prisoncore.tools.Pickaxe;
import com.kennedysmithjava.prisoncore.tools.Tool;
import com.kennedysmithjava.prisoncore.tools.ability.Ability;
import com.kennedysmithjava.prisoncore.tools.ability.LeveledAbility;
import com.kennedysmithjava.prisoncore.tools.enchantment.Enchant;
import com.kennedysmithjava.prisoncore.util.Color;
import com.kennedysmithjava.prisoncore.util.MiscUtil;
import com.massivecraft.massivecore.collections.MassiveMap;
import com.massivecraft.massivecore.store.Entity;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.*;

/**
 * A {@code PickaxeType} entity represents a single prebuilt configuration for a {@link Pickaxe} object.
 * A {@code Pickaxe} with a specific {@code PickaxeType} can be acquired in-game via CmdPickaxe
 *
 * @see Pickaxe
 * @see Tool
 * @see Enchant
 *
 * @author Kennedy Smith
 */


public class PickaxeType extends Entity<PickaxeType> {

    // -------------------------------------------- //
    // MASSIVECORE & OBJECT META
    // -------------------------------------------- //

    private final String name = null;
    private String displayName;
    private Material material;
    private String ability;
    private Map<String, Integer> buffers = new MassiveMap<>();
    private String description;
    private Map<String, Integer> enchants = new MassiveMap<>();
    private List<String> lore;
    private int maxDurability = 1000;
    private int startDurability = 1000;

    public static PickaxeType get(Object oid) {
        return PickaxeTypeColl.get().get(oid);
    }

    @Override
    public PickaxeType load(PickaxeType that) {

        this.setLore(that.lore);
        this.setMaterial(that.material);
        this.setDescription(that.description);
        this.setEnchants(that.enchants);
        this.setAbility(that.ability);
        this.setBuffers(that.buffers);
        this.setMaxDurability(that.maxDurability);
        this.setStartDurability(that.startDurability);

        return this;
    }

    public String getComparisonName() {
        return MiscUtil.getComparisonString(this.getName());
    }

    // -------------------------------------------- //
    // METHODS
    // -------------------------------------------- //

    /**
     * Builds an {@link ItemStack} and {@link Pickaxe} from this PickaxeType's configuration.
     * @return The built item for this {@link PickaxeType}
     */
    public ItemStack getItemStack() {
        Pickaxe pickaxe = Pickaxe.create(this);
        return pickaxe.getItem();
    }

    /**
     * Sets the stored {@link #displayName} for this @code{PickaxeType}
     * Primarily used for loading {@link #displayName} through {@link #load(PickaxeType)}
     * Adds color codes.
     */
    public void setDisplayName(String displayName) {
        this.displayName = Color.get(displayName);
        this.changed();
    }

    /**
     * Gets the stored {@link #displayName} for this @code{PickaxeType}
     * Primarily used for loading {@link #displayName} through {@link #load(PickaxeType)}
     *  @return The formatted display name for this @code{PickaxeType}.
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Gets the stored {@link #lore} for this @code{PickaxeType}
     * Primarily used for loading {@link #lore} through {@link #load(PickaxeType)}
     * @return The unformatted lore for this @code{PickaxeType}.
     */
    private List<String> getRawLore() {
        return lore;
    }

    /**
     * Constructs the lore for an item of this @code{PickaxeType}
     * @param enchants The enchants that will be used for the %enchants% placeholder in the lore.
     * @return The built lore
     */
    public List<String> getLore(Map<Enchant<?>, Integer> enchants, LeveledAbility ability, int currentDurability, int maxDurability) {
        ArrayList<String> lore = new ArrayList<>();
        List<String> rawLore = getRawLore();

        boolean isAbility = (ability != null);

        Map<Buffer, Integer> buffers = new HashMap<>();
        if(isAbility)
            buffers = ability.getBufferLevels();

        for (String s : rawLore) {
            if (s.contains("%buffers%")) {
                if (buffers.size() > 0) {
                    ArrayList<String> bufferLore = new ArrayList<>();
                    buffers.forEach((buffer, level) -> bufferLore.add(Color.get(buffer.getInfoLore().replaceAll("%level%", String.valueOf(level)))));
                    lore.addAll(bufferLore);
                } else {
                    lore.add(s.replaceAll("%buffers%", Color.get(("&8- No Buffers -"))));
                }
            } else if (s.contains("%enchants%")) {
                ArrayList<String> enchantLore = new ArrayList<>();
                enchants.forEach((e, level) -> enchantLore.add(Color.get((e.getLore().replaceAll("%level%", String.valueOf(level)).replaceAll("%roman_level%", MiscUtil.getRomanNumeral(level)).replaceAll("%name%", e.getName())))));
                lore.addAll(enchantLore);
            } else {
                lore.add(Color.get(
                        s.replaceAll("%displayname%", getDisplayName())
                                .replaceAll("%description%", getDescription())
                                .replaceAll("%buffer_max%", String.valueOf((isAbility ? ability.getAllowedBuffers().size() : "0")))
                                .replaceAll("%buffer_count%", String.valueOf(buffers.size()))
                                .replaceAll("%ability%", (isAbility ? ability.getAbility().getDisplayName() : "&7&m-- &7No Ability Equipped &7&m--"))
                                .replaceAll("%durHeader%", getHeadDurabilityLore(currentDurability, maxDurability))
                                .replaceAll("%durBar%", getDurabilityLore(currentDurability, maxDurability)))
                );
            }
        }
        return lore;
    }

    public String getHeadDurabilityLore(int currentDurability, int maxDurability){
        double percentage = (((double) currentDurability / (double) maxDurability) * 100);
        return "&aDurability: &7(&f"+ currentDurability + "&7/" + maxDurability + ") &a" + (int) percentage + "%";
    }

    public String getDurabilityLore(int currentDurability, int maxDurability){
        StringBuilder builder = new StringBuilder("&a‖‖‖‖‖‖‖‖‖‖‖‖‖‖‖‖‖‖‖‖‖‖‖‖‖‖‖‖‖‖‖‖‖‖‖‖‖‖‖‖");
        int length = builder.length();
        double percentage = (double) currentDurability / (double) maxDurability;
        int numberOfGreen = (int) (percentage * (double) length);
        builder.insert(numberOfGreen, "&7");
        return builder.toString();
    }


    /**
     * Sets the stored {@link #lore} for this @code{PickaxeType}
     * Primarily used for loading {@link #lore} through {@link #load(PickaxeType)}
     */
    public void setLore(List<String> lore) {
        this.lore = lore;
        this.changed();
    }

    /**
     * Gets the stored {@link #material} for this @code{PickaxeType}
     * Primarily used for loading {@link #material} through {@link #load(PickaxeType)}
     */
    public Material getMaterial() {
        return material;
    }

    /**
     * Sets the stored {@link #material} for this @code{PickaxeType}
     * Primarily used for loading {@link #material} through {@link #load(PickaxeType)}
     */
    public void setMaterial(Material material) {
        this.material = material;
        this.changed();
    }

    /**
     * Gets the stored {@link #description} for this @code{PickaxeType}
     * Primarily used for loading {@link #description} through {@link #load(PickaxeType)}
     */
    public String getDescription() {
        return description;
    }

    /**
     * Primarily used for loading {@link #material} through {@link #load(PickaxeType)}
     */
    public void setDescription(String description) {
        this.description = description;
        this.changed();
    }

    public String getName() {
        return this.name;
    }


    // -------------------------------------------- //
    // ENCHANTS
    // -------------------------------------------- //


    /**
     * Gets the default stored {@link #enchants} for this @code{PickaxeType}
     * @return Key: @code{EnchantID} Value: @code{Level of Enchant}
     * @see Enchant
     * @see Enchant#getID()
     * @see PickaxeType#getEnchants()
     */
    public Map<String, Integer> getEnchantsRaw() {
        return enchants;
    }

    /**
     *  Gets the default stored {@link #enchants} for this @code{PickaxeType}
     * @return Key: @code{Enchant} Value: @code{Level of Enchant}
     * @see Enchant
     * @see PickaxeType#getEnchantsRaw()
     */
    public Map<Enchant<?>, Integer> getEnchants() {

        Map<Enchant<?>, Integer> returnMap = new HashMap<>();
        getEnchantsRaw().forEach((enchant, lvl) -> returnMap.put(Enchant.getByName(enchant), lvl));
        return returnMap;
    }

    /**
     *  Add a default enchant to this PickaxeType
     * @see Enchant
     * @see PickaxeType#getEnchantsRaw()
     * @see PickaxeType#getEnchants()
     */
    public void addEnchant(Enchant<?> enchant, int level) {
        enchants.put(enchant.getID(), level);
        this.changed();
    }

    /**
     * Primarily used for loading {@link #enchants} through {@link #load(PickaxeType)}
     */
    public void setEnchants(Map<String, Integer> enchants) {
        this.enchants = enchants;
        this.changed();
    }

    public void setAbility(String ability){
        this.ability = ability;
        this.changed();
    }

    public Ability<?> getAbility() {
        return Ability.getByName(ability);
    }

    public void setBuffers(Map<String, Integer> buffers) {
        this.buffers = buffers;
    }

    public void addBuffer(Buffer buffer, int level) {
        this.buffers.put(buffer.getName(), level);
    }

    public Map<Buffer, Integer> getBuffers() {
        Map<Buffer, Integer> returnMap = new HashMap<>();
        buffers.forEach((buffer, lvl) -> returnMap.put(Buffer.get(buffer), lvl));
        return returnMap;
    }

    public int getMaxDurability() {
        return maxDurability;
    }

    public int getStartDurability() {
        return startDurability;
    }

    public void setMaxDurability(int maxDurability) {
        this.maxDurability = maxDurability;
    }

    public void setStartDurability(int startDurability) {
        this.startDurability = startDurability;
    }
}
