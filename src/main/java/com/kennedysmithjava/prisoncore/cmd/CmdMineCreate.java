package com.kennedysmithjava.prisoncore.cmd;

import com.kennedysmithjava.prisoncore.Perm;
import com.kennedysmithjava.prisoncore.PrisonCore;
import com.kennedysmithjava.prisoncore.cmd.type.TypeMPlayer;
import com.kennedysmithjava.prisoncore.engine.EngineLimbo;
import com.kennedysmithjava.prisoncore.entity.mines.Mine;
import com.kennedysmithjava.prisoncore.entity.player.MPlayer;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.requirement.RequirementHasPerm;
import com.massivecraft.massivecore.command.requirement.RequirementIsPlayer;
import org.bukkit.entity.Player;

public class CmdMineCreate extends CoreCommand {
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
        MPlayer mPlayer = readArg();

        if(mPlayer.hasMine()){
            msg(mPlayer.getName() + " already has a mine.");
            return;
        }

        PrisonCore.createMine(mPlayer, () ->{

            Mine mine = mPlayer.getMine();
            mine.spawnNPCs();
            Player player = mPlayer.getPlayer();
            if(player != null){
                EngineLimbo.get().removeFromLimbo(mPlayer.getPlayer());
                player.teleport(mine.getSpawnPoint());
            }
        });

        msg("Mine successfully created.");
    }
}
