package com.kennedysmithjava.prisoncore.placeholders;

import com.kennedysmithjava.prisoncore.PrisonCore;
import com.kennedysmithjava.prisoncore.entity.player.MPlayer;
import com.kennedysmithjava.prisoncore.entity.player.QuestProfile;
import com.kennedysmithjava.prisoncore.quest.Quest;
import com.kennedysmithjava.prisoncore.quest.QuestPath;
import com.kennedysmithjava.prisoncore.util.Color;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;

@SuppressWarnings("NullableProblems")
public class QuestPlaceholder extends PlaceholderExpansion {

        private final PrisonCore plugin;

        /**
         * Since we register the expansion inside our own plugin, we
         * can simply use this method here to get an instance of our
         * plugin.
         *
         * @param plugin The instance of our plugin.
         */
        public QuestPlaceholder(PrisonCore plugin) {
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
            return "quest";
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
         * %PrisonCore_quest_params%
         */
        @Override
        public String onPlaceholderRequest(Player player, String params) {
            MPlayer mPlayer = MPlayer.get(player);
            QuestProfile profile = mPlayer.getQuestProfile();

            if(params.equalsIgnoreCase("name")){
                QuestPath path = profile.getActiveQuestPath();
                if(path == null) return Color.get("&7None active");
                return Color.get(path.getQuestPathDisplayName());
            } else {
                Quest quest = profile.getActiveQuest();
                if(quest == null) return Color.get("&e/quest &7to start one!");
                return Color.get(quest.getShortProgressString());
            }
        }



}


