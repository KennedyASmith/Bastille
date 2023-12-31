package com.kennedysmithjava.prisoncore.placeholders;

import com.kennedysmithjava.prisoncore.PrisonCore;
import com.kennedysmithjava.prisoncore.eco.CurrencyType;
import com.kennedysmithjava.prisoncore.entity.player.MPlayer;
import com.kennedysmithjava.prisoncore.util.Color;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;

import java.text.DecimalFormat;

@SuppressWarnings("NullableProblems")
public class MineCurrencyPlaceholder extends PlaceholderExpansion {

    private final PrisonCore plugin;

    /**
     * Since we register the expansion inside our own plugin, we
     * can simply use this method here to get an instance of our
     * plugin.
     *
     * @param plugin The instance of our plugin.
     */
    public MineCurrencyPlaceholder(PrisonCore plugin) {
        this.plugin = plugin;
    }


    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public boolean canRegister() {
        return true;
    }

    @Override
    public String getIdentifier() {
        return "currency";
    }

    @Override
    public String getAuthor() {
        return plugin.getDescription().getAuthors().toString();
    }


    @Override
    public String getVersion() {
        return plugin.getDescription().getVersion();
    }


    /**
     * %PrisonCore_currency_CurrencyType%
     */
    @Override
    public String onPlaceholderRequest(Player player, String currencyType) {
        try{
            CurrencyType type = CurrencyType.valueOf(currencyType);

            MPlayer mPlayer = MPlayer.get(player);
            if(mPlayer == null) return Color.get("&7--/--");

            return switch (type) {
                case GEMS, RESEARCH -> String.valueOf(mPlayer.getBalance(type).intValue());
                default -> new DecimalFormat("#.##").format(mPlayer.getBalance(type));
            };

        }catch (IllegalArgumentException e){
            return "Unknown CurrencyType '+" + currencyType + "'";
        }
    }



}