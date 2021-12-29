package com.kennedysmithjava.prisonmines.cmd;

import com.kennedysmithjava.prisonmines.Perm;
import com.kennedysmithjava.prisonmines.cmd.requirement.RequirementHasMine;
import com.kennedysmithjava.prisonmines.cmd.type.TypeMineOwner;
import com.kennedysmithjava.prisonmines.entity.Mine;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.requirement.RequirementHasPerm;
import com.massivecraft.massivecore.command.requirement.RequirementIsPlayer;
import com.mcrivals.prisoncore.entity.MPlayer;
import org.bukkit.entity.Player;

public class CmdMineRegen extends MineCommand {
    // -------------------------------------------- //
    // CONSTRUCT
    // -------------------------------------------- //

    public CmdMineRegen() {
        //Requirement
        this.addRequirements(RequirementIsPlayer.get(), RequirementHasPerm.get(Perm.ADMIN), RequirementHasMine.get());

        //Parameters
        this.addParameter( TypeMineOwner.get(), "player");

        //Description
        this.setDesc("Reset the blocks in a mine");
    }

    // -------------------------------------------- //
    // OVERRIDE
    // -------------------------------------------- //

    @Override
    public void perform() throws MassiveException {
        if (!(sender instanceof Player)) return;
        MPlayer mineOwner = readArg(MPlayer.get(sender));
        Mine mine = mineOwner.getMine();

        mine.getRegenCountdown().resetCounter();
        mine.regen();
        msg("Mine has been regenerated successfully!");
    }

}
