package com.kennedysmithjava.prisoncore.util;

import com.kennedysmithjava.prisoncore.PrisonCore;
import com.kennedysmithjava.prisoncore.entity.mines.Mine;
import com.kennedysmithjava.prisoncore.entity.mines.MineColl;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;

public class MineCountdownPlaceholder extends PlaceholderExpansion {

        private PrisonCore plugin;

        /**
         * Since we register the expansion inside our own plugin, we
         * can simply use this method here to get an instance of our
         * plugin.
         *
         * @param plugin The instance of our plugin.
         */
        public MineCountdownPlaceholder(PrisonCore plugin) {
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
            return "countdown";
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
         * %PrisonCore_countdown_mineID%
         */
        @Override
        public String onPlaceholderRequest(Player player, String mineID) {
            Mine mine = MineColl.get().get(mineID);
            if(mine == null) return "";
            MineRegenCountdown regenCountdown = mine.getRegenCountdown();
            if(regenCountdown == null) return "";
            if(regenCountdown.isRegenerating()) return "&7[&eRegenerating&7]";
            if(regenCountdown.getTimeLeft() == 0){
                return "&7[&eReady&7]";
            }else{
                return mine.getRegenCountdown().getTimeLeftFormatted();
            }
        }



}


