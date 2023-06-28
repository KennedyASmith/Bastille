package com.kennedysmithjava.prisoncore.cmd;

import com.kennedysmithjava.prisoncore.Perm;
import com.kennedysmithjava.prisoncore.util.Color;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.requirement.RequirementHasPerm;
import com.massivecraft.massivecore.command.requirement.RequirementIsPlayer;

public class CmdPRegionPosTwo extends CoreCommand {

    public CmdPRegionPosTwo() {

        this.addRequirements(RequirementHasPerm.get(Perm.PREGION));
        this.addRequirements(RequirementIsPlayer.get());

        // Aliases
        this.addAliases("pos2");
        this.setSetupPermBaseClassName("PREGION");
    }

    @Override
    public void perform() throws MassiveException {
        CmdPRegion.addToPosTwoCache(me.getUniqueId().toString(), me.getLocation());
        msg(Color.get("&7[&bServer&7] Position two saved."));
    }

}
