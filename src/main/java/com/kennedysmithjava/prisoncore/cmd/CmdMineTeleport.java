package com.kennedysmithjava.prisoncore.cmd;

import com.kennedysmithjava.prisoncore.Perm;
import com.kennedysmithjava.prisoncore.cmd.requirement.RequirementHasMine;
import com.kennedysmithjava.prisoncore.cmd.type.TypeMineOwner;
import com.kennedysmithjava.prisoncore.entity.mines.Mine;
import com.kennedysmithjava.prisoncore.entity.player.MPlayer;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.requirement.RequirementHasPerm;
import com.massivecraft.massivecore.command.requirement.RequirementIsPlayer;
import org.bukkit.entity.Player;

public class CmdMineTeleport extends CoreCommand {
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

        MPlayer mineOwner = readArg();
        Mine mine = mineOwner.getMine();

        playerSender.teleport(mine.getSpawnPoint());
        msg("Teleported you to the mine owned by " + mineOwner.getName() + ".");

    }
}