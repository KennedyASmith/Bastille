package com.kennedysmithjava.prisoncore.cmd;

import com.kennedysmithjava.prisoncore.Perm;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.requirement.RequirementHasPerm;
import com.massivecraft.massivecore.command.requirement.RequirementIsPlayer;

public class CmdPRegionPosOne extends CoreCommand {

    public CmdPRegionPosOne() {
        this.addRequirements(RequirementHasPerm.get(Perm.PREGION));
        this.addRequirements(RequirementIsPlayer.get());

        // Aliases
        this.addAliases("pos1");
        this.setSetupPermBaseClassName("PREGION");
    }

    @Override
    public void perform() throws MassiveException {
        CmdPRegion.addToPosOneCache(me.getUniqueId().toString(), me.getLocation());
        me.sendMessage("Position two saved.");
    }

}
