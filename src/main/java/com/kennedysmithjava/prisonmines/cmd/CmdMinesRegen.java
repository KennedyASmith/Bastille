package com.kennedysmithjava.prisonmines.cmd;

import com.kennedysmithjava.prisonmines.cmd.type.TypeMine;
import com.kennedysmithjava.prisonmines.entity.Mine;
import com.kennedysmithjava.prisonmines.entity.MineColl;
import com.massivecraft.massivecore.MassiveException;
import org.bukkit.entity.Player;

public class CmdMinesRegen extends MineCommand {
    // -------------------------------------------- //
    // CONSTRUCT
    // -------------------------------------------- //

    public CmdMinesRegen() {
        // Aliases
        this.addAliases("regen", "reset");

        // Parameters
        this.addParameter(TypeMine.get(), "name");
        this.setDesc("Reset the blocks in a mine");
    }

    // -------------------------------------------- //
    // OVERRIDE
    // -------------------------------------------- //

    @Override
    public void perform() throws MassiveException {
        if (!(sender instanceof Player)) return;
        Mine mine = readArg();
        mine.getRegenCountdown().resetCounter();
        mine.regen();
        msg("Mine has been regenerated successfully!");
    }

}
