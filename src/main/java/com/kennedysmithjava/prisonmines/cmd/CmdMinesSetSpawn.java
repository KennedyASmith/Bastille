package com.kennedysmithjava.prisonmines.cmd;

import com.kennedysmithjava.prisonmines.cmd.type.TypeMine;
import com.kennedysmithjava.prisonmines.entity.mine.Mine;
import com.massivecraft.massivecore.MassiveException;
import org.bukkit.entity.Player;

public class CmdMinesSetSpawn extends MineCommand {
    // -------------------------------------------- //
    // CONSTRUCT
    // -------------------------------------------- //

    public CmdMinesSetSpawn() {
        // Aliases
        this.addAliases("setspawn");
        // Parameters
        this.addParameter(TypeMine.get(), "name");
        this.setDesc("Modify the mine's spawn point.");
    }

    // -------------------------------------------- //
    // OVERRIDE
    // -------------------------------------------- //

    @Override
    public void perform() throws MassiveException {
        if (!(sender instanceof Player)) return;
        Player player = (Player) sender;
        Mine mine = readArg();

        mine.setSpawnPoint(player.getLocation());
        player.sendMessage("Changed " + mine.getName() + "'s Spawn point");

    }

}
