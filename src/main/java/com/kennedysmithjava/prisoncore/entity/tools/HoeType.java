package com.kennedysmithjava.prisoncore.entity.tools;

import com.kennedysmithjava.prisoncore.tools.Hoe;
import com.kennedysmithjava.prisoncore.tools.Pickaxe;
import com.kennedysmithjava.prisoncore.util.Color;
import com.kennedysmithjava.prisoncore.util.MiscUtil;
import com.kennedysmithjava.prisoncore.util.RarityName;
import com.massivecraft.massivecore.store.Entity;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

/**
 * A {@code HoeType} entity represents a single prebuilt configuration for a Scythe object.
 *
 * @author Kennedy Smith
 */


public class HoeType extends Entity<HoeType> {

    // -------------------------------------------- //
    // MASSIVECORE & OBJECT META
    // -------------------------------------------- //

    private final String name = null;
    private String displayName;
    private Material material;
    private String description;
    private List<String> lore;
    private int maxDurability = 1000;
    private int startDurability = 1000;

    private RarityName rarityName = RarityName.COMMON;
    private double rarity = 0.5;

    @Override
    public HoeType load(HoeType that) {

        this.setDisplayName(that.displayName);
        this.setLore(that.lore);
        this.setMaterial(that.material);
        this.setDescription(that.description);
        this.setMaxDurability(that.maxDurability);
        this.setStartDurability(that.startDurability);
        this.setRarityName(that.rarityName);
        this.setRarity(that.rarity);

        return this;
    }

    public static HoeType get(Object oid) {
        return HoeTypeColl.get().get(oid);
    }

    public String getComparisonName() {
        return MiscUtil.getComparisonString(this.getName());
    }

    // -------------------------------------------- //
    // METHODS
    // -------------------------------------------- //

    /**
     * Builds an {@link ItemStack} and {@link Pickaxe} from this PickaxeType's configuration.
     * @return The built item for this {@link HoeType}
     */
    public ItemStack getItemStack() {
        Hoe hoe = Hoe.create(this);
        return hoe.getItem();
    }

    /**
     * Sets the stored {@link #displayName} for this @code{PickaxeType}
     * Primarily used for loading {@link #displayName} through {@link #load(HoeType)}
     * Adds color codes.
     */
    public void setDisplayName(String displayName) {
        this.displayName = Color.get(displayName);
        this.changed();
    }

    /**
     * Gets the stored {@link #displayName} for this @code{PickaxeType}
     * Primarily used for loading {@link #displayName} through {@link #load(HoeType)}
     *  @return The formatted display name for this @code{PickaxeType}.
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Gets the stored {@link #lore} for this @code{PickaxeType}
     * Primarily used for loading {@link #lore} through {@link #load(HoeType)}
     * @return The unformatted lore for this @code{PickaxeType}.
     */
    private List<String> getRawLore() {
        return lore;
    }

    /**
     * Constructs the lore for an item of this type
     * @return The built lore
     */
    public List<String> getLore(int currentDurability, int maxDurability) {
        ArrayList<String> lore = new ArrayList<>();
        List<String> rawLore = getRawLore();

        for (String s : rawLore) {
            lore.add(Color.get(
                    s.replaceAll("%displayname%", getDisplayName())
                            .replaceAll("%description%", getDescription())
                            .replaceAll("%durHeader%", getHeadDurabilityLore(currentDurability, maxDurability))
                            .replaceAll("%durBar%", getDurabilityLore(currentDurability, maxDurability)))
            );
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
     * Primarily used for loading {@link #lore} through {@link #load(HoeType)}
     */
    public void setLore(List<String> lore) {
        this.lore = lore;
        this.changed();
    }

    /**
     * Gets the stored {@link #material} for this @code{PickaxeType}
     * Primarily used for loading {@link #material} through {@link #load(HoeType)}
     */
    public Material getMaterial() {
        return material;
    }

    /**
     * Sets the stored {@link #material} for this @code{PickaxeType}
     * Primarily used for loading {@link #material} through {@link #load(HoeType)}
     */
    public void setMaterial(Material material) {
        this.material = material;
        this.changed();
    }

    /**
     * Gets the stored {@link #description} for this @code{PickaxeType}
     * Primarily used for loading {@link #description} through {@link #load(HoeType)}
     */
    public String getDescription() {
        return description;
    }

    /**
     * Primarily used for loading {@link #material} through {@link #load(HoeType)}
     */
    public void setDescription(String description) {
        this.description = description;
        this.changed();
    }

    public String getName() {
        return this.name;
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

    public double getRarity() {
        return rarity;
    }

    public void setRarity(double rarity) {
        this.rarity = rarity;
    }

    public RarityName getRarityName() {
        return rarityName;
    }

    public void setRarityName(RarityName rarityName) {
        this.rarityName = rarityName;
    }
}
