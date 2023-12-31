package com.kennedysmithjava.prisoncore.cmd;

import com.kennedysmithjava.prisoncore.cmd.type.TypeMPlayer;
import com.kennedysmithjava.prisoncore.cmd.type.TypeCurrency;
import com.kennedysmithjava.prisoncore.eco.CurrencyType;
import com.kennedysmithjava.prisoncore.entity.player.MPlayer;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.type.primitive.TypeDouble;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class CmdEconomySet extends CoreCommand {

    public CmdEconomySet() {

        // Aliases
        this.addAliases("add");

        // Parameters
        this.addParameter(TypeMPlayer.get(), "Player");
        this.addParameter(TypeCurrency.get(), "Currency");
        this.addParameter(TypeDouble.get(), "Value");
    }

    @Override
    public void perform() throws MassiveException {
        if (!(sender instanceof Player)) return;
        Player player = (Player) sender;

        MPlayer target = readArg();
        CurrencyType currency = readArg();
        Double value = readArg();

        target.setBalance(currency, value);

        player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                "&eChanged " + target.getPlayer().getName() +
                        "'s balance to" + value + " " + currency.getDisplayName() + "!"));
    }

}