package com.kennedysmithjava.prisoncore.cmd;

import com.kennedysmithjava.prisoncore.Perm;
import com.kennedysmithjava.prisoncore.cmd.type.TypeMineOwner;
import com.kennedysmithjava.prisoncore.entity.MConf;
import com.kennedysmithjava.prisoncore.entity.mines.Mine;
import com.kennedysmithjava.prisoncore.entity.player.MPlayer;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.requirement.RequirementHasPerm;
import com.massivecraft.massivecore.command.requirement.RequirementIsPlayer;
import org.bukkit.entity.Player;

public class CmdMineDelete extends CoreCommand {
    // -------------------------------------------- //
    // CONSTRUCT
    // -------------------------------------------- //

    public CmdMineDelete() {
        //Requirement
        this.addRequirements(RequirementIsPlayer.get(), RequirementHasPerm.get(Perm.ADMIN));

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
        MPlayer mineOwner = readArg();

        Mine mine = mineOwner.getMine();
        if(mine.isRegenCountdownActive()){
            mine.removeCountdown();
        }
        mine.despawnNPCs();
        mine.removeLever();
        mineOwner.setMineID("none");
        if(mineOwner.getPlayer() != null){
            mineOwner.getPlayer().teleport(MConf.get().getSpawnLocation());
        }

        msg("You have removed the mine owned by " + mineOwner.getName() + ".");
    }

}
