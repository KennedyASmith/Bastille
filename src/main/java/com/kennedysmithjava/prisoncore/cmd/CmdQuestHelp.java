package com.kennedysmithjava.prisoncore.cmd;

import com.massivecraft.massivecore.MassiveException;
import org.bukkit.entity.Player;

public class CmdQuestHelp extends CoreCommand {

    public CmdQuestHelp() {

        // Aliases
        this.addAliases("help");

    }

    @Override
    public void perform() throws MassiveException {
        if (!(sender instanceof Player)) return;
        Player player = (Player) sender;

        player.sendMessage("You don't deserve help.");
    }

}
