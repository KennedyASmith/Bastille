package com.kennedysmithjava.prisoncore.cmd;

import com.kennedysmithjava.prisoncore.Perm;
import com.kennedysmithjava.prisoncore.PrisonCore;
import com.kennedysmithjava.prisoncore.cmd.type.TypeMPlayer;
import com.kennedysmithjava.prisoncore.engine.EngineLimbo;
import com.kennedysmithjava.prisoncore.engine.EngineLoadingScreen;
import com.kennedysmithjava.prisoncore.entity.mines.Mine;
import com.kennedysmithjava.prisoncore.entity.player.MPlayer;
import com.kennedysmithjava.prisoncore.npc.NPCLimboTrait;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.requirement.RequirementHasPerm;
import com.massivecraft.massivecore.command.requirement.RequirementIsPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.concurrent.atomic.AtomicBoolean;

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
        Player player = mPlayer.getPlayer();

        if(mPlayer.hasMine()){
            msg(mPlayer.getName() + " already has a mine.");
            return;
        }

        AtomicBoolean mineFinished = new AtomicBoolean(false);
        EngineLimbo.get().removeFromLimbo(player);
        EngineLoadingScreen.addLoadingScreen(player, player.getLocation());

        PrisonCore.createMine(mPlayer, () ->{
            Mine mine = mPlayer.getMine();
            mine.spawnNPCs();
            mineFinished.set(true);
        });

        new BukkitRunnable() {
            @Override
            public void run() {
                if (mineFinished.get()) {
                    NPCLimboTrait.concludeLimbo(mPlayer);
                    this.cancel();
                }
            }
        }.runTaskTimer(PrisonCore.get(), 0, 20 * 2);

        msg("Mine successfully created.");
    }
}
