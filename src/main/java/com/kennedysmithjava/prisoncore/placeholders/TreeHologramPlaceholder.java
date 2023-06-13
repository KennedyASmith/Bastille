package com.kennedysmithjava.prisoncore.placeholders;

import com.kennedysmithjava.prisoncore.PrisonCore;
import com.kennedysmithjava.prisoncore.entity.farming.TreesConf;
import com.kennedysmithjava.prisoncore.entity.farming.objects.Tree;
import com.kennedysmithjava.prisoncore.util.Color;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;

@SuppressWarnings("NullableProblems")
public class TreeHologramPlaceholder extends PlaceholderExpansion {

        private final PrisonCore plugin;

        /**
         * Since we register the expansion inside our own plugin, we
         * can simply use this method here to get an instance of our
         * plugin.
         *
         * @param plugin The instance of our plugin.
         */
        public TreeHologramPlaceholder(PrisonCore plugin) {
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
            return "tree";
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
         * %PrisonCore_tree_treeID%
         */
        @Override
        public String onPlaceholderRequest(Player player, String treeID) {
            Tree tree = TreesConf.get().getTreesRaw().get(treeID);
            String placeholder = "&a&oReady to chop!";
            if(tree.isNeedsRegeneration()) placeholder = "&c&oGrowing...";
            return Color.get(placeholder);
        }



}


