package com.kennedysmithjava.prisoncore.skill;

import com.massivecraft.massivecore.util.MUtil;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;

public enum SkillType {

    PLAYER(Material.TOTEM_OF_UNDYING, "&6Player", MUtil.list("&7Earn &ePlayer &7XP by making money!"), 9),
    MINING(Material.IRON_PICKAXE, "&6Mining", MUtil.list("&7Earn &eMining &7XP by playing!"), 9+1),
    MELEE(Material.IRON_SWORD, "&6Melee", MUtil.list("&7Earn &eMelee &7XP by fighting other players!"), 9+2),
    RANGED(Material.BOW, "&6Ranged", MUtil.list("&7Earn &eRanged &7XP by fighting other players!"), 9+3),
    DEFENSE(Material.SHIELD, "&6Defense", MUtil.list("&7Earn &eDefense &7XP by fighting other players!"), 9+4),
    FISHING(Material.FISHING_ROD, "&6Fishing", MUtil.list("&7Earn &eFishing &7XP by fishing!"), 9+5),
    WOODCUTTING(Material.IRON_AXE, "&6Woodcutting", MUtil.list("&7Earn &eWoodcutting &7XP by chopping trees!"), 9+6),
    CRAFTING(Material.CRAFTING_TABLE, "&6Crafting", MUtil.list("&7Earn &eCrafting &7XP by crafting items!"), 9+7),
    METALWORKING(Material.ANVIL, "&6Smithing", MUtil.list("&7Earn &eSmithing &7XP by crafting tools!"), 9+8),
    QUESTING(Material.COMPASS, "&6Questing", MUtil.list("&7Earn &eQuesting &7XP by completing quests!"), 9+9),
    ENCHANTING(Material.ENCHANTING_TABLE, "&6Enchanting", MUtil.list("&7Earn &eEnchanting &7XP by enchanting tools!"), 9+10),
    ALCHEMY(Material.POTION, "&6Alchemy", MUtil.list("&7Earn &eAlchemy &7XP by crafting potions!"), 9+11),
    GEMCUTTING(Material.DIAMOND, "&6Gemcutting", MUtil.list("&7Earn &eGemcutting &7XP by cutting gems!"), 9+12),
    LEADERSHIP(Material.WRITABLE_BOOK, "&6Leadership", MUtil.list("&7Earn &eLeadership &7XP by leading your gang!"), 9+13),
    REPUTATION(Material.BELL, "&6Reputation", MUtil.list("&7Earn &eReputation &7XP through NPC interaction!"), 9+14),
    TAMING(Material.SADDLE, "&6Taming", MUtil.list("&7Earn &eTaming &7XP by interacting with your mount and pets!"), 9+15),
    ACROBATICS(Material.FEATHER, "&6Acrobatics", MUtil.list("&7Earn &eAcrobatics &7XP by playing!"), 9+16),
    EXPLORATION(Material.SPYGLASS, "&6Exploration", MUtil.list("&7Earn &eExploration &7XP by exploring new areas!"), 9+17)
    ;

    private final Material icon;
    private final String displayName;

    private final List<String> lore;

    private final int guiSlot;

    SkillType(Material icon, String displayName, List<String> lore, int guiSlot) {
        this.icon = icon;
        this.displayName = displayName;
        this.lore = lore;
        this.guiSlot = guiSlot;
    }

    public String getDisplayName() {
        return displayName;
    }

    public List<String> getLore() {
        return new ArrayList<>(lore);
    }

    public int getGuiSlot() {
        return guiSlot;
    }

    public Material getIcon() {
        return icon;
    }

}
