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
        Player player = (Player) sender;
        String name = readArg();

        Mine mine = MineColl.get().getByName(name);
        //If mine has an automatic timer, reset it
        if(mine.isHasTimer()) mine.getRegenCountdown().forceReset();
        mine.regen();
        msg("Mine has been regenerated successfully!");
    }

}
