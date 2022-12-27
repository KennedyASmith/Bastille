package com.kennedysmithjava.prisoncore.eco;

import com.kennedysmithjava.prisoncore.PrisonCore;
import com.kennedysmithjava.prisoncore.entity.player.MPlayer;
import com.kennedysmithjava.prisoncore.util.Color;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.text.DecimalFormat;

public class MineCurrencyPlaceholder extends PlaceholderExpansion {

    private PrisonCore plugin;

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

            switch(type){
                case GEMS:
                case RESEARCH:
                    return String.valueOf(mPlayer.getBalance(type).intValue());
                default:
                    return new DecimalFormat("#.##").format(mPlayer.getBalance(type));
            }

        }catch (IllegalArgumentException e){
            return "Unknown CurrencyType '+" + currencyType + "'";
        }
    }



}