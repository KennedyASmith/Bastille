package com.kennedysmithjava.prisonmines.cmd;

import com.massivecraft.massivecore.MassiveException;
import org.bukkit.entity.Player;

public class CmdOffsetPosOne extends MineCommand {

    public CmdOffsetPosOne() {

        // Aliases
        this.addAliases("pos1", "one", "1");

    }

    @Override
    public void perform() throws MassiveException {
        if (!(sender instanceof Player)) return;
        Player player = (Player) sender;

        CmdOffset.addToPosOneCache(player.getUniqueId().toString(), player.getLocation());
        player.sendMessage("Position one saved.");
    }

}
