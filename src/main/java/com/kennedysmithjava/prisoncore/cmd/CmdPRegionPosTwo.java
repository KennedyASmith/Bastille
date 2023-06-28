package com.kennedysmithjava.prisoncore.cmd;

import com.kennedysmithjava.prisoncore.Perm;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.requirement.RequirementHasPerm;
import com.massivecraft.massivecore.command.requirement.RequirementIsPlayer;
import org.bukkit.entity.Player;

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
        me.sendMessage("Position two saved.");
    }

}
