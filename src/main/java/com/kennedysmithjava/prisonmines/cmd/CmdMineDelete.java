package com.kennedysmithjava.prisonmines.cmd;

import com.kennedysmithjava.prisonmines.Perm;
import com.kennedysmithjava.prisonmines.cmd.requirement.RequirementHasMine;
import com.kennedysmithjava.prisonmines.cmd.type.TypeMineOwner;
import com.kennedysmithjava.prisonmines.entity.Mine;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.requirement.RequirementHasPerm;
import com.massivecraft.massivecore.command.requirement.RequirementIsPlayer;
import com.mcrivals.prisoncore.PrisonCore;
import com.mcrivals.prisoncore.entity.MConf;
import com.mcrivals.prisoncore.entity.MPlayer;
import org.bukkit.entity.Player;

public class CmdMineDelete extends MineCommand {
    // -------------------------------------------- //
    // CONSTRUCT
    // -------------------------------------------- //

    public CmdMineDelete() {
        //Requirement
        this.addRequirements(RequirementIsPlayer.get(), RequirementHasPerm.get(Perm.ADMIN), RequirementHasMine.get());

        // Parameters
        this.addParameter( TypeMineOwner.get(), "player");

        //Description
        this.setDesc("Delete a personal mine");
    }

    // -------------------------------------------- //
    // OVERRIDE
    // -------------------------------------------- //

    @Override
    public void perform() throws MassiveException {
        if (!(sender instanceof Player)) return;
        MPlayer mineOwner = readArg(MPlayer.get(sender));

        Mine mine = mineOwner.getMine();
        mine.removeCountdown();
        mine.despawnNPCs();
        mine.removeLever();
        mineOwner.setMineID("none");
        if(mineOwner.getPlayer() != null){
            mineOwner.getPlayer().teleport(MConf.get().getSpawnLocation());
        }

        msg("You have removed the mine owned by " + mineOwner.getName() + ".");
    }

}
