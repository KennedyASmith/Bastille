package com.kennedysmithjava.prisoncore.cmd;

import com.kennedysmithjava.prisoncore.entity.tools.*;
import com.kennedysmithjava.prisoncore.maps.MapUtil;
import com.kennedysmithjava.prisoncore.maps.PrisonMapRenderer;
import com.kennedysmithjava.prisoncore.util.MiscUtil;
import com.kennedysmithjava.prisoncore.util.RarityName;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.requirement.RequirementIsPlayer;
import com.massivecraft.massivecore.util.MUtil;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class CmdMap extends CoreCommand
{
    // -------------------------------------------- //
    // INSTANCE
    // -------------------------------------------- //

    private static CmdMap i = new CmdMap();

    public static CmdMap get()
    {
        return i;
    }


    public CmdMap() {
        this.addRequirements(RequirementIsPlayer.get());
        this.setSetupPermBaseClassName("MAP");
    }

    // -------------------------------------------- //
    // OVERRIDE
    // -------------------------------------------- //


    @Override
    public void perform() throws MassiveException {
        if(senderIsConsole) return;
        if(MapUtil.hasMap(me)){
            MapUtil.removeMaps(me);
            msg("&7[&bServer&7] You have disabled your map! Get a new map with &e/map&7!");
        }else {
            ItemStack map = PrisonMapRenderer.mapPlayer(me, me.getLocation());
            MiscUtil.givePlayerItem(me, map, 1);
            msg("&7[&bServer&7] You have been given a map! Disable it with &e/map&7!");
        }
        
        
        HoeType hoeType = new HoeType();
        hoeType.setDisplayName("Test Hoe");
        hoeType.setLore(MUtil.list(
                "&7This hoe",
                "&7is just a test object!",
                " ",
                "%durHeader%",
                "&7%durBar%&7"));
        hoeType.setMaterial(Material.IRON_HOE);
        hoeType.setDescription(" - ");
        hoeType.setMaxDurability(1000);
        hoeType.setStartDurability(1000);
        hoeType.setRarityName(RarityName.COMMON);
        hoeType.setRarity(0.85);

        AxeType axeType = new AxeType();
        axeType.setDisplayName("Test Axe");
        axeType.setLore(MUtil.list(
                "&7This axe",
                "&7is just a test object!",
                " ",
                "%durHeader%",
                "&7%durBar%&7"));
        axeType.setMaterial(Material.IRON_AXE);
        axeType.setDescription(" - ");
        axeType.setMaxDurability(1000);
        axeType.setStartDurability(1000);
        axeType.setRarityName(RarityName.COMMON);
        axeType.setRarity(0.85);

        FishingPoleType poleType = new FishingPoleType();
        poleType.setDisplayName("Test Fishing Pole");
        poleType.setLore(MUtil.list(
                "&7This pole",
                "&7is just a test object!",
                " ",
                "%durHeader%",
                "&7%durBar%&7"));
        poleType.setMaterial(Material.FISHING_ROD);
        poleType.setDescription(" - ");
        poleType.setMaxDurability(1000);
        poleType.setStartDurability(1000);
        poleType.setRarityName(RarityName.COMMON);
        poleType.setRarity(0.85);

        FishingPoleTypeColl.get().attach(poleType);
        HoeTypeColl.get().attach(hoeType);
        AxeTypeColl.get().attach(axeType);
    }

    @Override
    public List<String> getAliases()
    {
        return MUtil.list("map");
    }

}
