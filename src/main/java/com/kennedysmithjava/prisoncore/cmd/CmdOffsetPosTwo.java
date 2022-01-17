package com.kennedysmithjava.prisoncore.cmd;

import com.massivecraft.massivecore.MassiveException;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class CmdOffsetPosTwo extends CoreCommand {

    public CmdOffsetPosTwo() {

        // Aliases
        this.addAliases("pos2", "two", "2");

    }

    @Override
    public void perform() throws MassiveException {
        if (!(sender instanceof Player)) return;
        Player player = (Player) sender;

        Location loc1 = CmdOffset.posOneCache.get(player.getUniqueId().toString());
        if(loc1 == null) {
            player.sendMessage("You must select a first position with /offset pos1");
            return;
        }

        CmdOffset.sendOffsetInfo(player, loc1, player.getLocation());
    }



}
