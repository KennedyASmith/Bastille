package com.kennedysmithjava.prisonmines.cmd;

import com.kennedysmithjava.prisonmines.cmd.type.TypeMine;
import com.kennedysmithjava.prisonmines.entity.Mine;
import com.massivecraft.massivecore.MassiveException;
import org.bukkit.entity.Player;

public class CmdMineTeleport extends MineCommand {
    // -------------------------------------------- //
    // CONSTRUCT
    // -------------------------------------------- //

    public CmdMineTeleport() {
        // Aliases
        this.addAliases("A", "B");

        // Parameters
        this.addParameter(TypeMine.get(), "name");
        this.setDesc("Teleport to the specified mine");
    }

    // -------------------------------------------- //
    // OVERRIDE
    // -------------------------------------------- //

    @Override
    public void perform() throws MassiveException {
        if (!(sender instanceof Player)) return;
        Player player = (Player) sender;
        Mine mine = readArg();

        player.teleport(mine.getSpawnPoint());
        player.sendMessage("Teleported you to " + mine.getName());

    }
}