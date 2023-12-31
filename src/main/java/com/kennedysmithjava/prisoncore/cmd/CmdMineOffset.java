package com.kennedysmithjava.prisoncore.cmd;

import com.kennedysmithjava.prisoncore.Perm;
import com.kennedysmithjava.prisoncore.cmd.requirement.RequirementHasMine;
import com.kennedysmithjava.prisoncore.cmd.type.TypeMPlayer;
import com.kennedysmithjava.prisoncore.entity.player.MPlayer;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.requirement.RequirementHasPerm;
import com.massivecraft.massivecore.command.requirement.RequirementIsPlayer;
import org.bukkit.entity.Player;

public class CmdMineOffset extends CoreCommand {
    // -------------------------------------------- //
    // CONSTRUCT
    // -------------------------------------------- //

    public CmdMineOffset() {

        //Requirement
        this.addRequirements(RequirementIsPlayer.get(), RequirementHasPerm.get(Perm.ADMIN), RequirementHasMine.get());

        // Parameters
        this.addParameter(TypeMPlayer.get(), "player");

        //Description
        this.setDesc("Find the Mine origin offset location of where you are standing. Only works in your own mine.");
    }

    // -------------------------------------------- //
    // OVERRIDE
    // -------------------------------------------- //

    @Override
    public void perform() throws MassiveException {
        if (!(sender instanceof Player)) return;
        MPlayer mPlayer = readArg();
        Player player = mPlayer.getPlayer();
        CmdOffset.sendOffsetInfo(player, mPlayer.getMine().getOrigin(), player.getLocation());
    }
}
