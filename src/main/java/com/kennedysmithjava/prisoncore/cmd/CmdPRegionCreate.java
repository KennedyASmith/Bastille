package com.kennedysmithjava.prisoncore.cmd;

import com.kennedysmithjava.prisoncore.Perm;
import com.kennedysmithjava.prisoncore.cmd.type.TypeRegionType;
import com.kennedysmithjava.prisoncore.entity.Regions;
import com.kennedysmithjava.prisoncore.regions.RegionFlatSquare;
import com.kennedysmithjava.prisoncore.regions.RegionType;
import com.kennedysmithjava.prisoncore.regions.RegionWrapper;
import com.kennedysmithjava.prisoncore.util.Color;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.requirement.RequirementHasPerm;
import com.massivecraft.massivecore.command.requirement.RequirementIsPlayer;
import com.massivecraft.massivecore.command.type.primitive.TypeString;
import org.bukkit.Location;

public class CmdPRegionCreate extends CoreCommand {

    public CmdPRegionCreate() {
        this.addRequirements(RequirementHasPerm.get(Perm.PREGION));
        this.addRequirements(RequirementIsPlayer.get());

        // Aliases
        this.addAliases("create");
        this.setSetupPermBaseClassName("PREGION");

        this.addParameter(TypeRegionType.get());
        this.addParameter(TypeString.get());
    }

    @Override
    public void perform() throws MassiveException {

        if(!CmdPRegion.bothCachesContain(me)){
            msg(Color.get("&7[&bServer&7] First select a region with &e/pregion pos1 &7and &e/pregion pos2."));
            return;
        }

        RegionType type = readArg();
        String regionName = readArg();
        Location pos1 = CmdPRegion.posOneCacheGet(me);
        Location pos2 = CmdPRegion.posTwoCacheGet(me);
        RegionFlatSquare region = new RegionFlatSquare(pos1, pos2);
        Regions.get().addRegion(regionName, region, type);
        msg(Color.get("&7[&bServer&7] Region saved."));
        CmdPRegionUndo.setLastRegionNames(me.getUniqueId(), new RegionWrapper(region, type, regionName));
    }

}
