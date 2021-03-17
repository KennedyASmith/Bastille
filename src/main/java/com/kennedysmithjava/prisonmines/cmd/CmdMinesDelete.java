package com.kennedysmithjava.prisonmines.cmd;

import com.kennedysmithjava.prisonmines.cmd.type.TypeMine;
import com.kennedysmithjava.prisonmines.entity.Mine;
import com.kennedysmithjava.prisonmines.entity.MineColl;
import com.massivecraft.massivecore.MassiveException;
import org.bukkit.entity.Player;

public class CmdMinesDelete extends MineCommand {
    // -------------------------------------------- //
    // CONSTRUCT
    // -------------------------------------------- //

    public CmdMinesDelete() {
        // Aliases
        this.addAliases("d, delete");

        // Parameters
        this.addParameter(TypeMine.get(), "name");
        this.setDesc("Delete a mine");
    }

    // -------------------------------------------- //
    // OVERRIDE
    // -------------------------------------------- //

    @Override
    public void perform() throws MassiveException {
        if (!(sender instanceof Player)) return;
        Player player = (Player) sender;
        Mine mine = readArg();
        String name = mine.getName();

        //If mine has an automatic timer, reset it
        if(mine.isHasTimer()) mine.getRegenCountdown().cancel();
        msg("You have removed the mine " + name);
        mine.detach();
    }

}
