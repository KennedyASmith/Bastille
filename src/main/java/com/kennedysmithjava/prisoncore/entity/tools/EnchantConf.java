package com.kennedysmithjava.prisoncore.entity.tools;

import com.massivecraft.massivecore.command.editor.annotation.EditorName;
import com.massivecraft.massivecore.store.Entity;
import com.massivecraft.massivecore.util.MUtil;
import org.bukkit.Material;

import java.util.List;

@EditorName("config")
public class EnchantConf extends Entity<EnchantConf>
{
    // -------------------------------------------- //
    // META
    // -------------------------------------------- //

    protected static transient EnchantConf i;

    public static EnchantConf get() { return i; }

    @Override
    public EnchantConf load(EnchantConf that)
    {
        super.load(that);
        return this;
    }

    public String PickaxeEfficiencyEnchantName       = "Efficiency";    //Mine faster
    public String PickaxeSieveEnchantName            = "Sieve";          //Chance of getting ores from mine's distribution
    public String PickaxeRefinerEnchantName          = "Refiner";       //Increases value of award
    public String pickaxeVisionEnchantName           = "Night Vision";  //Get NV effect while holding
    public String PickaxeJackpotEnchantName          = "Jackpot";       //Multiplies ores
    public String PickaxeHasteEnchantName            = "Haste";         //Get haste effect while holding
    public String PickaxeExplosiveEnchantName        = "Explosive";     //Round hole
    public String PickaxeSpeedEnchantName            = "Speed";         //Get speed effect while holding
    public String PickaxeConeEnchantName             = "Cone";          //Cone shaped hole
    public String PickaxeLaserEnchantName            = "Laser";         //Breaks blocks in direction player is looking
    public String PickaxeJackhammerEnchantName       = "Jackhammer";    //Entire row of mine
    public String PickaxeVeinEnchantName             = "Vein Miner";    //Mines an entire vein of blocks. Takes up player's food.
    public String pickaxeMomentumEnchantName         = "Momentum";      //Multiplies value based on how long player is mining, like a combo.
    public String pickaxeShinyEnchantName            = "Shiny";         //Makes your pickaxe shiny. Nothing else.

    public Material PickaxeConeEnchantIcon             = Material.HOPPER;
    public Material PickaxeEfficiencyEnchantIcon       = Material.QUARTZ;
    public Material PickaxeExplosiveEnchantIcon        = Material.TNT;
    public Material PickaxeRefinerEnchantIcon          = Material.CAULDRON;
    public Material PickaxeJackhammerEnchantIcon       = Material.IRON_HORSE_ARMOR;
    public Material PickaxeJackpotEnchantIcon          = Material.GOLD_BLOCK;
    public Material PickaxeLaserEnchantIcon            = Material.BLAZE_ROD;
    public Material PickaxeSieveEnchantIcon            = Material.BOWL;
    public Material PickaxeHasteEnchantIcon            = Material.FEATHER;
    public Material PickaxeSpeedEnchantIcon            = Material.POTION;
    public Material PickaxeVeinEnchantIcon             = Material.VINE;
    public Material PickaxeVisionEnchantIcon           = Material.ENDER_EYE;

    public String PickaxeConeEnchantDisplayName             = "&c&l%name% Enchant";
    public String PickaxeEfficiencyEnchantDisplayName       = "&c&l%name% Enchant";
    public String PickaxeExplosiveEnchantDisplayName        = "&c&l%name% Enchant";
    public String PickaxeHasteEnchantDisplayName            = "&c&l%name% Enchant";
    public String PickaxeRefinerEnchantDisplayName          = "&c&l%name% Enchant";
    public String PickaxeJackhammerEnchantDisplayName       = "&c&l%name% Enchant";
    public String PickaxeJackpotEnchantDisplayName          = "&c&l%name% Enchant";
    public String PickaxeLaserEnchantDisplayName            = "&c&l%name% Enchant";
    public String PickaxeSieveEnchantDisplayName            = "&c&l%name% Enchant";
    public String PickaxeSpeedEnchantDisplayName            = "&c&l%name% Enchant";
    public String PickaxeVeinEnchantDisplayName             = "&c&l%name% Enchant";
    public String PickaxeXrayEnchantDisplayName             = "&c&l%name% Enchant";

    public String PickaxeConeEnchantLore                    = "&7%name% &7%roman_level% &8&o%level%";
    public String PickaxeEfficiencyEnchantLore              = "&7%name% &7%roman_level% &8&o%level%";
    public String PickaxeExplosiveEnchantLore               = "&6✺ &c%name% &7%roman_level% &8&o%level%";
    public String PickaxeRefinerEnchantLore                 = "&7%name% &7%roman_level% &8&o%level%";
    public String PickaxeHasteEnchantLore                   = "&7%name% &7%roman_level% &8&o%level%";
    public String PickaxeJackhammerEnchantLore              = "&7%name% &7%roman_level% &8&o%level%";
    public String PickaxeJackpotEnchantLore                 = "&7%name% &7%roman_level% &8&o%level%";
    public String PickaxeLaserEnchantLore                   = "&7%name% &7%roman_level% &8&o%level%";
    public String PickaxeSieveEnchantLore                   = "&7%name% &7%roman_level% &8&o%level%";
    public String PickaxeSpeedEnchantLore                   = "&7%name% &7%roman_level% &8&o%level%";
    public String PickaxeVeinEnchantLore                    = "&a☄ %name% &7%roman_level% &8&o%level%";
    public String PickaxeXrayEnchantLore                    = "&7%name%  &7%roman_level% &8&o%level%";

    public List<String> PickaxeConeEnchantGUILore             = MUtil.list("&7Breaks blocks in a large cone shape!");
    public List<String> PickaxeEfficiencyEnchantGUILore       = MUtil.list("&7Increases the speed of mining!");
    public List<String> PickaxeExplosiveEnchantGUILore        = MUtil.list("&7Breaks blocks with an explosion!");
    public List<String> PickaxeRefinerEnchantGUILore          = MUtil.list("&7Increases the value of the blocks you mine!");
    public List<String> PickaxeHasteEnchantGUILore            = MUtil.list("&7Gives you a haste potion effect", "&7while holding this item.");
    public List<String> PickaxeJackhammerEnchantGUILore       = MUtil.list("&7Increases the chance of mining", "&7an entire layer of a mine!");
    public List<String> PickaxeJackpotEnchantGUILore          = MUtil.list("&7Increases the chance finding", "&7crate keys, tokens, and money while mining!");
    public List<String> PickaxeLaserEnchantGUILore            = MUtil.list("&7Mines blocks in the direction you're facing!", "&7(It's a laser!)");
    public List<String> PickaxeSieveEnchantGUILore             = MUtil.list("&7Each level 2x the chance of finding", "&emetals &7while mining!");
    public List<String> PickaxeSpeedEnchantGUILore            = MUtil.list("&7Gives you a speed potion effect", "&7while holding this item.");
    public List<String> PickaxeVeinEnchantGUILore             = MUtil.list("&7Allows you to mine an entire vein of" + "&7blocks that share the same type!");
    public List<String> PickaxeXrayEnchantGUILore             = MUtil.list("&7Highlights the location of the rarest blocks in your mine!");

    public int PickaxeEfficiencyEnchantGUISlot       = 28;
    public int PickaxeSieveEnchantGUISlot            = 29;
    public int PickaxeRefinerEnchantGUISlot          = 30;
    public int PickaxeXrayEnchantGUISlot             = 31;
    public int PickaxeJackpotEnchantGUISlot          = 32;
    public int PickaxeHasteEnchantGUISlot            = 33;
    public int PickaxeExplosiveEnchantGUISlot        = 34;
    public int PickaxeSpeedEnchantGUISlot            = 38;
    public int PickaxeConeEnchantGUISlot             = 39;
    public int PickaxeLaserEnchantGUISlot            = 40;
    public int PickaxeJackhammerEnchantGUISlot       = 41;
    public int PickaxeVeinEnchantGUISlot             = 42;

    public int PickaxeConeEnchantMinLevel               = 1;
    public int PickaxeConeEnchantMaxLevel               = 5;

    public int PickaxeEfficiencyEnchantMinLevel         = 1;
    public int PickaxeEfficiencyEnchantMaxLevel         = 200;

    public int PickaxeExplosiveEnchantMinLevel          = 1;
    public int PickaxeExplosiveEnchantMaxLevel          = 5;

    public int PickaxeRefinerEnchantMinLevel            = 1;
    public int PickaxeRefinerEnchantMaxLevel            = 5;

    public int PickaxeHasteEnchantMinLevel              = 1;
    public int PickaxeHasteEnchantMaxLevel              = 5;

    public int PickaxeJackhammerEnchantMinLevel         = 1;
    public int PickaxeJackhammerEnchantMaxLevel         = 5;

    public int PickaxeJackpotEnchantMinLevel            = 1;
    public int PickaxeJackpotEnchantMaxLevel            = 5;

    public int PickaxeLaserEnchantMinLevel              = 1;
    public int PickaxeLaserEnchantMaxLevel              = 5;

    public int PickaxeLuckEnchantMinLevel               = 1;
    public int PickaxeLuckEnchantMaxLevel               = 5;

    public int PickaxeVeinEnchantMinLevel               = 1;
    public int PickaxeVeinEnchantMaxLevel               = 5;

}
