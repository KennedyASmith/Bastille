package com.kennedysmithjava.prisoncore.cmd;

import com.kennedysmithjava.prisoncore.entity.player.MPlayer;
import com.kennedysmithjava.prisoncore.entity.player.MPlayerColl;
import com.kennedysmithjava.prisoncore.util.Color;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.type.primitive.TypeString;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class CmdEconomyBalance extends CoreCommand {

    public CmdEconomyBalance() {

        // Aliases
        this.addAliases("bal", "balance");

        // Parameters
        this.addParameter("<me>", TypeString.get(), "Player");
    }

    @Override
    public void perform() throws MassiveException {
        if (!(sender instanceof Player)) return;

        String playerName = readArg();
        Player player = (Player) sender;


        MPlayer mPlayer = null;


        if (playerName.equalsIgnoreCase("<me>")) {
            mPlayer = MPlayerColl.get().getByPlayer(player);
        }

        if (mPlayer == null) {
            OfflinePlayer oP = Bukkit.getOfflinePlayer(playerName);
            if (oP == null || !oP.hasPlayedBefore()) {
                player.sendMessage(Color.get("&c That player never joined the server!"));
                return;
            } else {
                mPlayer = MPlayerColl.get().getByPlayer(oP.getPlayer());
            }
        }


        player.sendMessage(Color.get("&a&n" + mPlayer.getPlayer().getName() + "'s Balance:"));
        player.sendMessage(ChatColor.RESET.toString());
        mPlayer.getEconomy().forEach((type, value) -> {
            player.sendMessage(Color.get("   " + type.getDisplayName() + ": " + value));
        });

        player.sendMessage(Color.get("&a&m----------------------------"));

    }

}
