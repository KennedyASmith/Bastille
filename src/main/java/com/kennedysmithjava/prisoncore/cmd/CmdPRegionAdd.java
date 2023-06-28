package com.kennedysmithjava.prisoncore.cmd;

import com.kennedysmithjava.prisoncore.Perm;
import com.kennedysmithjava.prisoncore.cmd.type.TypeNamedRegion;
import com.kennedysmithjava.prisoncore.entity.Regions;
import com.kennedysmithjava.prisoncore.regions.RegionFlatSquare;
import com.kennedysmithjava.prisoncore.regions.RegionType;
import com.kennedysmithjava.prisoncore.util.Color;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.requirement.RequirementHasPerm;
import com.massivecraft.massivecore.command.requirement.RequirementIsPlayer;
import org.bukkit.Location;

public class CmdPRegionAdd extends CoreCommand {

    public CmdPRegionAdd() {
        this.addRequirements(RequirementHasPerm.get(Perm.PREGION));
        this.addRequirements(RequirementIsPlayer.get());

        // Aliases
        this.addAliases("add");
        this.setSetupPermBaseClassName("PREGION");

        this.addParameter(TypeNamedRegion.get());
    }

    @Override
    public void perform() throws MassiveException {
        CmdPRegion.addToPosOneCache(me.getUniqueId().toString(), me.getLocation());

        if(!CmdPRegion.bothCachesContain(me)){
            msg(Color.get("&7[&bServer&7] First select a region with &e/pregion pos1 &7and &e/pregion pos2."));
            return;
        }

        String regionName = readArg();
        RegionType type = Regions.get().getRegionType(regionName);
        Location pos1 = CmdPRegion.posOneCacheGet(me);
        Location pos2 = CmdPRegion.posTwoCacheGet(me);
        RegionFlatSquare region = new RegionFlatSquare(pos1, pos2);
        Regions.get().addRegion(regionName, region, type);
        msg(Color.get("&7[&bServer&7] Region saved."));
    }

}
