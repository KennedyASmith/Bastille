package com.kennedysmithjava.prisonmines.cmd;

import com.kennedysmithjava.prisonmines.cmd.type.TypeMine;
import com.kennedysmithjava.prisonmines.entity.mine.Mine;
import com.kennedysmithjava.prisonmines.entity.mine.MineColl;
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
        mine.getRegenCountdown().resetCounter();
        mine.regen();
        msg("Mine has been regenerated successfully!");
    }

}
