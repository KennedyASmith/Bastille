package com.kennedysmithjava.prisonmines.cmd;

import com.kennedysmithjava.prisonmines.Perm;
import com.kennedysmithjava.prisonmines.PrisonMines;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.requirement.RequirementHasPerm;
import com.massivecraft.massivecore.command.requirement.RequirementIsPlayer;
import com.mcrivals.prisoncore.cmd.type.TypeMPlayer;
import com.mcrivals.prisoncore.entity.MPlayer;
import org.bukkit.entity.Player;

public class CmdMineCreate extends MineCommand {
    // -------------------------------------------- //
    // CONSTRUCT
    // -------------------------------------------- //

    public CmdMineCreate() {

        //Requirement
        this.addRequirements(RequirementIsPlayer.get(), RequirementHasPerm.get(Perm.ADMIN));

        // Parameters
        this.addParameter(TypeMPlayer.get(), "player");

        //Description
        this.setDesc("Create a new personal mine");
    }

    // -------------------------------------------- //
    // OVERRIDE
    // -------------------------------------------- //

    @Override
    public void perform() throws MassiveException {
        if (!(sender instanceof Player)) return;
        MPlayer player = readArg(MPlayer.get(sender));

        if(player.hasMine()){
            msg(player.getName() + " already has a mine.");
            return;
        }

        PrisonMines.createMine(player, () -> {
            msg("Mine successfully created for " + player.getName() + ".");
        });

    }
}
