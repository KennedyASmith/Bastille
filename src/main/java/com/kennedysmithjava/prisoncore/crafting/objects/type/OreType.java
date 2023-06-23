package com.kennedysmithjava.prisoncore.crafting.objects.type;

import com.massivecraft.massivecore.util.MUtil;
import org.bukkit.Material;

import java.util.List;

public enum OreType {
    TIN_ORE("&6Tin Ore", MetalType.TIN_INGOT, 2, 10,10, MUtil.list("&7A common ore with a bright appearance.", " &r", "&7Often found embedded within the top layers of rock formations."), Material.DIORITE),
    COPPER_ORE("&6Copper Ore", MetalType.COPPER_INGOT, 3,10,11, MUtil.list("&7A common reddish-brown ore. ", " &r",  "&7Great conductivity!"), Material.COPPER_ORE),
    IRON_ORE("&fIron Ore", MetalType.IRON_INGOT, 4,10,12, MUtil.list("&7A reliable ore that appears grayish in color. ", " &r",  "&7It is a fundamental material used in the production of iron and steel."), Material.IRON_ORE),
    SILVER_ORE("&7Silver Ore", MetalType.SILVER_INGOT, 5, 10,13, MUtil.list("&7A metallic ore with a silver-white color. ", " &r",  "&7It is typically found in deep underground deposits alongside iron."), Material.DEEPSLATE_IRON_ORE),
    GOLD_ORE("&eGold Ore", MetalType.GOLD_INGOT, 5,10,14, MUtil.list("&7A soft and precious ore that has a distinct yellow color. ", " &r",  "&7It is highly sought after for its beauty and value in jewelry and various industries"), Material.GOLD_ORE),
    ADAMANTINE_ORE("&dAdamantine Ore", MetalType.ADAMANTINE_INGOT, 7,10,15, MUtil.list("&7A rare and durable ore that appears in a brownish hue. ", " &r",  "&7It is known for its exceptional strength."), Material.RAW_IRON_BLOCK),
    MITHRIL_ORE("&3Mithril Ore", MetalType.MITHRIL_INGOT, 8, 10,16, MUtil.list("&7An almost mythical ore that possesses a shimmering blue hue. ", " &r",  "&7It is renowned for its properties associated with the creation of enchanted items."), Material.DIAMOND_ORE),
    TITANIUM_ORE("&8Titanium Ore", MetalType.TITANIUM_INGOT, 9, 10,19, MUtil.list("&7A strong and lightweight ore that exhibits a dark gray color. ", " &r",  "&7It is used in a wide range of industries due to its excellent strength-to-weight ratio."), Material.DEEPSLATE_COAL_ORE),
    PROMETHIUM_ORE("&9Promethium Ore", MetalType.PROMETHIUM_INGOT, 10, 10,20, MUtil.list("&7A rare ore with a vibrant blue color. ", " &r",  "&7It emits a faint glow and is associated with mystical properties in certain legends and lore."), Material.PRISMARINE),
    AETHERIUM_ORE("&aAetherium Ore", MetalType.AETHERIUM_INGOT, 11, 10,21, MUtil.list("&7A rare and valuable ore with a radiant golden appearance. ", " &r",  "&7It is often associated with divine properties and is highly sought after for its mystical nature."), Material.DEEPSLATE_GOLD_ORE),
    DRAGONSTONE_ORE("&cDratinium Ore", MetalType.DRAGONSTONE_INGOT, 12,10,22, MUtil.list("&7An ancient ore with a scale-like appearance. ", " &r",  "&7It is said to be connected to dragons and is highly prized for its extreme rarity."), Material.ANCIENT_DEBRIS),
    ELYSIUM_ORE("&2Elysium Ore", MetalType.ELYSIUM_INGOT, 13,10,23, MUtil.list("&7A beautiful ore with a deep green hue. ", " &r",  "&7Its discovery is often considered a once-in-a-lifetime stroke of luck."), Material.DEEPSLATE_EMERALD_ORE),
    CELESTIUM_ORE("&bCelestium Ore", MetalType.CELESTIUM_ORE, 14,10, 24, MUtil.list("&7A rare ore that exhibits a bright sky-blue color. ", " &r",  "&7It is known for its conductivity and is a key component in machines"), Material.DEEPSLATE_REDSTONE_ORE),
    PRIMORDIAL_ORE("&dPrimordial Ore", MetalType.PRIMORDIAL_INGOT, 15, 10,25, MUtil.list("&7An ancient and mysterious ore with bone-like appearance. ", " &r",  "&7It is often linked to the stories of legendary creatures."), Material.DIAMOND_ORE),
    MAGNITE_ORE("&bMagnius Ore", MetalType.MAGNITE_INGOT, 16, 10,29, MUtil.list("&7A uniquely rare ore that emits a pulsating purple glow. ", " &r",  "&7It is known for its exceptional heat resistance."), Material.MAGMA_BLOCK),
    ARCANITE_ORE("&bArcanite Ore", MetalType.ARCANITE_INGOT, 17, 10,30, MUtil.list("&7A  golden ore with a distinct red undertone. ", " &r",  "&7It is often associated with arcane magic and is sought after by  spellcasters."), Material.NETHER_GOLD_ORE),
    OSMIUM_ORE("&8Osmium Ore", MetalType.OSMIUM_INGOT, 18, 10, 31, MUtil.list("&7A dark metallic ore with a purplish hue. ", " &r",  "&7It possesses exceptionally useful properties in tools and equipment."), Material.WARPED_HYPHAE),
    PALLADIUM_ORE("&8Palladium Ore", MetalType.PALLADIUM_INGOT, 19, 10,32, MUtil.list("&7A legendary ore that displays a shiny silver-white color. ", " &r",  "&7Sought after for its catalytic properties."), Material.NETHER_QUARTZ_ORE),
    VOIDSTONE_ORE("&8Voidite Ore", MetalType.VOIDITE_INGOT, 20, 10,33, MUtil.list("&7An enigmatic ore that emanates a deep black aura. ", " &r",  "&7It is associated with dark energies and is the subject of various legends."), Material.CRYING_OBSIDIAN);
    private final String displayName;
    private final Material bukkitMaterial;
    private final List<String> description;
    private final MetalType metalType;

    private final int forgeGuiSlot;

    private final int requiredSkillLevel;

    private final int smeltingXP;

    OreType(String displayName,
            MetalType metalType,
            int requiredSkillLevel, int smeltingXP,
            int forgeGuiSlot, List<String> description, Material bukkitMaterial) {
        this.smeltingXP = smeltingXP;
        this.displayName = displayName;
        this.bukkitMaterial = bukkitMaterial;
        this.forgeGuiSlot = forgeGuiSlot;
        this.description = description;
        this.metalType = metalType;
        this.requiredSkillLevel = requiredSkillLevel;
    }

    public String getDisplayName() {
        return displayName;
    }

    public Material getMaterial() {
        return bukkitMaterial;
    }

    public List<String> getDescription() {
        return description;
    }

    public MetalType getMetalType() {
        return metalType;
    }

    public int getForgeGuiSlot() {
        return forgeGuiSlot;
    }

    public int getRequiredSkillLevel() {
        return requiredSkillLevel;
    }

    public int getSmeltingXP() {
        return smeltingXP;
    }
}


