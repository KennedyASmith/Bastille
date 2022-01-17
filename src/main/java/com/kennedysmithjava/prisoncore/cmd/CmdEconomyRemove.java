package com.kennedysmithjava.prisoncore.cmd;

import com.kennedysmithjava.prisoncore.cmd.type.TypeMPlayer;
import com.kennedysmithjava.prisoncore.eco.CurrencyType;
import com.kennedysmithjava.prisoncore.cmd.type.TypeCurrency;
import com.kennedysmithjava.prisoncore.entity.player.MPlayer;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.type.primitive.TypeDouble;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class CmdEconomyRemove extends CoreCommand {

    public CmdEconomyRemove() {

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

        target.addBalance(currency, value);

        player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                "&eRemoved " + value + " " + currency.getDisplayName() + " &efrom "
                        + target.getPlayer().getName() + "'s balance!"));
    }

}
