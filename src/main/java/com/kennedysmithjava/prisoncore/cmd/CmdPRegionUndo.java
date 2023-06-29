package com.kennedysmithjava.prisoncore.cmd;

import com.kennedysmithjava.prisoncore.Perm;
import com.kennedysmithjava.prisoncore.entity.Regions;
import com.kennedysmithjava.prisoncore.regions.RegionFlatSquare;
import com.kennedysmithjava.prisoncore.regions.RegionWrapper;
import com.kennedysmithjava.prisoncore.util.Color;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.requirement.RequirementHasPerm;
import com.massivecraft.massivecore.command.requirement.RequirementIsPlayer;
import org.bukkit.Location;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CmdPRegionUndo extends CoreCommand {

    private static Map<UUID, RegionWrapper> lastRegions = new HashMap<>();
    public CmdPRegionUndo() {
        this.addRequirements(RequirementHasPerm.get(Perm.PREGION));
        this.addRequirements(RequirementIsPlayer.get());

        // Aliases
        this.addAliases("create");
        this.setSetupPermBaseClassName("PREGION");

    }

    @Override
    public void perform() throws MassiveException {
        RegionWrapper region = lastRegions.get(me.getUniqueId());
        Location pos1 = CmdPRegion.posOneCacheGet(me);
        Location pos2 = CmdPRegion.posTwoCacheGet(me);
        RegionFlatSquare regionS = new RegionFlatSquare(pos1, pos2);
        Regions.get().removeRegion(region.name(), regionS, region.type());
        msg(Color.get("&7[&bServer&7] Region undone."));
    }

    public static void setLastRegionNames(UUID playerUUID, RegionWrapper wrapper) {
        CmdPRegionUndo.lastRegions.put(playerUUID, wrapper);
    }
}
