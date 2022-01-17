package com.kennedysmithjava.prisoncore.cmd;

import com.kennedysmithjava.prisoncore.Perm;
import com.kennedysmithjava.prisoncore.cmd.requirement.RequirementHasMine;
import com.kennedysmithjava.prisoncore.entity.mines.Mine;
import com.kennedysmithjava.prisoncore.entity.mines.MineColl;
import com.massivecraft.massivecore.command.requirement.RequirementHasPerm;
import com.massivecraft.massivecore.command.requirement.RequirementIsPlayer;
import org.bukkit.entity.Player;

public class CmdMineSetSpawn extends CoreCommand {
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
