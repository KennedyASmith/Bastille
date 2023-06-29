package com.kennedysmithjava.prisoncore.cmd;

import com.kennedysmithjava.prisoncore.maps.MapUtil;
import com.kennedysmithjava.prisoncore.maps.PrisonMapRenderer;
import com.kennedysmithjava.prisoncore.util.MiscUtil;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.requirement.RequirementIsPlayer;
import com.massivecraft.massivecore.util.MUtil;
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
    }

    @Override
    public List<String> getAliases()
    {
        return MUtil.list("map");
    }

}
