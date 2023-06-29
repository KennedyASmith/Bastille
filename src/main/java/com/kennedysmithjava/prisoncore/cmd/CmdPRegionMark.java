package com.kennedysmithjava.prisoncore.cmd;

import com.kennedysmithjava.prisoncore.Perm;
import com.kennedysmithjava.prisoncore.cmd.type.TypeMapCursor;
import com.kennedysmithjava.prisoncore.cmd.type.TypeNamedRegion;
import com.kennedysmithjava.prisoncore.entity.Regions;
import com.kennedysmithjava.prisoncore.entity.farming.objects.TwoDVector;
import com.kennedysmithjava.prisoncore.util.Color;
import com.kennedysmithjava.prisoncore.util.MapMarker;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.requirement.RequirementHasPerm;
import com.massivecraft.massivecore.command.requirement.RequirementIsPlayer;
import com.massivecraft.massivecore.command.type.primitive.TypeString;
import org.bukkit.map.MapCursor;

public class CmdPRegionMark extends CoreCommand {

    public CmdPRegionMark() {
        this.addRequirements(RequirementHasPerm.get(Perm.PREGION));
        this.addRequirements(RequirementIsPlayer.get());

        // Aliases
        this.addAliases("mark");
        this.setSetupPermBaseClassName("PREGION");

        this.addParameter(TypeNamedRegion.get());
        this.addParameter(TypeMapCursor.get());
        this.addParameter(TypeString.get());

    }

    @Override
    public void perform() throws MassiveException {

        String regionName = readArg();
        MapCursor.Type cursorType = readArg();
        String cursorName = readArg();

        TwoDVector dVector = new TwoDVector(me.getWorld().getName(), me.getLocation().getBlockX(), me.getLocation().getBlockZ());
        Regions.get().addRegionMarker(regionName, new MapMarker(cursorType, cursorName, dVector));
        msg(Color.get("&7[&bServer&7] Cursor saved."));
    }

}
