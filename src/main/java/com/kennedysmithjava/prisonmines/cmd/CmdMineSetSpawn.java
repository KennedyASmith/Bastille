package com.kennedysmithjava.prisonmines.cmd;

import com.kennedysmithjava.prisonmines.Perm;
import com.kennedysmithjava.prisonmines.cmd.requirement.RequirementHasMine;
import com.kennedysmithjava.prisonmines.entity.Mine;
import com.kennedysmithjava.prisonmines.entity.MineColl;
import com.massivecraft.massivecore.command.requirement.RequirementHasPerm;
import com.massivecraft.massivecore.command.requirement.RequirementIsPlayer;
import org.bukkit.entity.Player;

public class CmdMineSetSpawn extends MineCommand {
    // -------------------------------------------- //
    // CONSTRUCT
    // -------------------------------------------- //

    public CmdMineSetSpawn() {
        //Requirement
        this.addRequirements(RequirementIsPlayer.get(), RequirementHasPerm.get(Perm.ADMIN), RequirementHasMine.get());

        //Description
        this.setDesc("Set the spawn of a mine.");
    }

    // -------------------------------------------- //
    // OVERRIDE
    // -------------------------------------------- //

    @Override
    public void perform() {
        if (!(sender instanceof Player)) return;
        Player playerSender = (Player) sender;
        Mine mine = MineColl.get().getByLocation(playerSender.getLocation());
        if(mine != null){
            mine.setSpawnPoint(playerSender.getLocation());
            msg("Changed this mine's spawn point");
        }else{
            msg("This area is not a mine.");
        }
    }

}
