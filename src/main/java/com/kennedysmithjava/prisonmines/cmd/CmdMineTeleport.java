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

public class CmdMineTeleport extends MineCommand {
    // -------------------------------------------- //
    // CONSTRUCT
    // -------------------------------------------- //

    public CmdMineTeleport() {
        //Aliases
        this.addAliases("tp", "spawn");

        //Requirement
        this.addRequirements(RequirementIsPlayer.get(), RequirementHasPerm.get(Perm.ADMIN), RequirementHasMine.get());

        // Parameters
        this.addParameter( TypeMineOwner.get(), "player");

        //Description
        this.setDesc("Teleport to the spawn of the specified mine");
    }

    // -------------------------------------------- //
    // OVERRIDE
    // -------------------------------------------- //

    @Override
    public void perform() throws MassiveException {
        if (!(sender instanceof Player)) return;
        Player playerSender = (Player) sender;

        MPlayer mineOwner = readArg(MPlayer.get(sender));
        Mine mine = mineOwner.getMine();

        playerSender.teleport(mine.getSpawnPointLoc());
        msg("Teleported you to the mine owned by " + mineOwner.getName() + ".");

    }
}