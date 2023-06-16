package com.kennedysmithjava.prisoncore.placeholders;

import com.kennedysmithjava.prisoncore.PrisonCore;
import com.kennedysmithjava.prisoncore.entity.player.MPlayer;
import com.kennedysmithjava.prisoncore.entity.player.Skill;
import com.kennedysmithjava.prisoncore.entity.player.SkillProfile;
import com.kennedysmithjava.prisoncore.entity.player.SkillsConf;
import com.kennedysmithjava.prisoncore.skill.SkillType;
import com.kennedysmithjava.prisoncore.util.Color;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;

@SuppressWarnings("NullableProblems")
public class SkillPlaceholder extends PlaceholderExpansion {

        private final PrisonCore plugin;

        /**
         * Since we register the expansion inside our own plugin, we
         * can simply use this method here to get an instance of our
         * plugin.
         *
         * @param plugin The instance of our plugin.
         */
        public SkillPlaceholder(PrisonCore plugin) {
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
            return "skill";
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
         * %PrisonCore_skill_params%
         */
        @Override
        public String onPlaceholderRequest(Player player, String params) {
            MPlayer mPlayer = MPlayer.get(player);
            SkillProfile profile = mPlayer.getSkillProfile();
            SkillType featuredSkillType = profile.getFeaturedSkill();
            Skill skill = profile.getSkill(featuredSkillType);

            String returnable = switch (params.toLowerCase()) {
                case "level" -> String.valueOf(skill.getCurrentLevel());
                case "bar" -> skill.getXPBar(SkillsConf.getXpRequired(featuredSkillType, skill.getCurrentLevel()));
                case "xp" -> String.valueOf(skill.getCurrentXP());
                case "maxxp" -> String.valueOf(SkillsConf.getXpRequired(featuredSkillType, skill.getCurrentLevel()));
                default -> featuredSkillType.getDisplayName();
            };
            return Color.get(returnable);
        }



}


