package com.kennedysmithjava.prisoncore.entity.mines;

import com.google.common.collect.Lists;
import com.massivecraft.massivecore.command.editor.annotation.EditorName;
import com.massivecraft.massivecore.store.Entity;
import org.bukkit.Material;

import java.util.List;

@EditorName("config")
public class CoinCollectorGuiConf extends Entity<CoinCollectorGuiConf> {
    // -------------------------------------------- //
    // META
    // -------------------------------------------- //

    protected static transient CoinCollectorGuiConf i;

    public String guiName = "&fCoin Collector";
    public int guiBorderColor = 15;
    public int guiSize = 9 * 6;

    // ==A=C=B== (53)
    public int confirmSlot = 9 * 5 + 2; // A
    public int selectSlot = 9 * 5 + 6; // B
    public int sellAllSlot = 9 * 5 + 4; // C

    public String sellAllName
            = "&b&lSELL ALL";
    public List<String> sellAllLore
            = Lists.newArrayList("&7Click to sell all of the items", "&7in your inventory.");
    public Material sellAllMaterial = Material.PAPER;

    public String confirmName
            = "&a&lCONFIRM SELL";
    public List<String> confirmLore
            = Lists.newArrayList("&7Click to sell all items inside the", "&7inventory.");
    public Material confirmMaterial = Material.LIME_WOOL;

    public String selectName
            = "&d&lSELECT ALL";
    public List<String> selectLore
            = Lists.newArrayList("&7Click to select all sellable items", "&7in your inventory.");
    public Material selectMaterial = Material.HOPPER;

    public String selectLockedName
            = "&c&lSELECT ALL";
    public List<String> selectLockedLore
            = Lists.newArrayList("&7[&cLOCKED&7]");
    public Material selectLockedMaterial = Material.IRON_BARS;

    public String autoSellName
            = "&a&lAUTO SELL";
    public List<String> autoSellLore
            = Lists.newArrayList("&7Automatically selling inventory contents", "&7every &d&l%t &7seconds.");
    public Material autoSellMaterial = Material.EMERALD;


    public static CoinCollectorGuiConf get() {
        return i;
    }

    @Override
    public CoinCollectorGuiConf load(CoinCollectorGuiConf that) {
        super.load(that);
        return this;
    }

}

