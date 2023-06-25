package com.kennedysmithjava.prisoncore.entity.mines;

import com.kennedysmithjava.prisoncore.util.DistributionPage;
import com.massivecraft.massivecore.command.editor.annotation.EditorName;
import com.massivecraft.massivecore.store.Entity;
import com.massivecraft.massivecore.util.MUtil;
import org.bukkit.Material;

import java.util.Map;

@EditorName("config")
public class DistributionConf extends Entity<DistributionConf> {
    // -------------------------------------------- //
    // META
    // -------------------------------------------- //

    protected static DistributionConf i;


    public static DistributionConf get() {
        return i;
    }

    public DistributionConf() {

    }

    @SuppressWarnings("NullableProblems")
    @Override
    public DistributionConf load(DistributionConf that) {
        super.load(that);
        return this;
    }

    // -------------------------------------------- //
    // COMMAND ALIASES
    // -------------------------------------------- //

    public Map<Integer, Distribution> distribution = MUtil.map(

            0, new Distribution("&7Dusty Cobweb Pit", Material.GRAVEL,
                    MUtil.map(  
                            
                            3, 3,
                            
                            2, 8,
                                    1, 10),
                    MUtil.list("&7Common", "", "&fBlocks:", "&740% - &7Pebble &a$0.10", "&710% - &7Pebble &a$0.20", "&750% - &7Cobweb &a$0.00")),
            1, new Distribution("&7Dusty Pit", Material.STONE,
                    MUtil.map(  3, 1,
                            2, 10), MUtil.list("&7Common", "", "&fBlocks:", "&790% - &7✦✧✧ &7Pebble &a$0.10", "&710% - &e✦✦✧ &7Pebble &a$0.20")
                    )
    );

    public Map<Integer, DistributionPage> pages = MUtil.map(
            0, new DistributionPage("&7&lCommon Blocks", MUtil.list(0, 1)),
            1, new DistributionPage("&4&lRare Blocks", MUtil.list(0)),
            2, new DistributionPage("&8&lDonor Blocks", MUtil.list(0))
    );

}
