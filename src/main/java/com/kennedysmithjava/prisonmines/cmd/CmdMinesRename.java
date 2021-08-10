package com.kennedysmithjava.prisonmines.cmd;

import com.kennedysmithjava.prisonmines.cmd.type.TypeMine;
import com.kennedysmithjava.prisonmines.cmd.type.TypeMineNameStrict;
import com.kennedysmithjava.prisonmines.entity.mine.Mine;
import com.massivecraft.massivecore.MassiveException;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class CmdMinesRename extends MineCommand {

    public CmdMinesRename() {

        // Aliases
        this.addAliases("rename", "r", "change");

        // Parameters
        this.addParameter(TypeMine.get(), "oldName");
        this.addParameter(TypeMineNameStrict.get(), "newName");
    }

    @Override
    public void perform() throws MassiveException {
        if (!(sender instanceof Player)) return;
        Player player = (Player) sender;
        Mine mine = readArg();
        String newName = readArg();

        player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                "&cRawr, i changed the name for " + mine.getName() + " it's now " + newName));

        mine.setName(newName);
    }

}
