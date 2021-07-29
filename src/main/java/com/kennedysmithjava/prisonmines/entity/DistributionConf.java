package com.kennedysmithjava.prisonmines.entity;

import com.kennedysmithjava.prisonmines.util.DistributionPage;
import com.massivecraft.massivecore.command.editor.annotation.EditorName;
import com.massivecraft.massivecore.store.Entity;
import com.massivecraft.massivecore.util.MUtil;
import org.bukkit.Material;

import java.util.List;
import java.util.Map;

@EditorName("config")
public class DistributionConf extends Entity<DistributionConf>
{
    // -------------------------------------------- //
    // META
    // -------------------------------------------- //

    protected static transient DistributionConf i;


    public static DistributionConf get() { return i; }

    @Override
    public DistributionConf load(DistributionConf that)
    {
        super.load(that);
        return this;
    }

    // -------------------------------------------- //
    // COMMAND ALIASES
    // -------------------------------------------- //

    //

    public Map<Integer, Distribution> distribution = MUtil.map(
               1, new Distribution("&7Old Pebbles", Material.STONE, MUtil.map("STONE:0", 50.0, "WEB:0", 50.0),MUtil.list("&7Common", "", "&7Block Prices:", "&7- Stone &a$1")),
            2, new Distribution("&7Plentiful Pebbles", Material.STONE, MUtil.map("STONE:0", 75.0, "STONE:5", 25.0),MUtil.list("&7Common", "", "&7Block Prices:", "&7- Stone &a$1")),
                    3, new Distribution("&7Rich Pebbles", Material.STONE, MUtil.map("STONE:0", 50.0, "STONE:5", 50.0),MUtil.list("&7Common", "", "&7Block Prices:", "&7- Stone &a$1")),
                    4, new Distribution("&7Driveway Gravel",Material.STONE, MUtil.map("STONE:0", 25.0, "STONE:5", 75.0),MUtil.list("&7Common", "", "&7Block Prices:", "&7- Stone &a$1")),
                    5, new Distribution("&7Quarry Leftovers", Material.STONE, MUtil.map("STONE:5", 75.0,"STONE:6", 25.0),MUtil.list("&7Common", "", "&7Block Prices:", "&7- Stone &a$1")),
                    6, new Distribution("&7New Pebbles", Material.STONE, MUtil.map("STONE:5", 75.0,"STONE:6", 25.0),MUtil.list("&7Common", "", "&7Block Prices:", "&7- Stone &a$1")),
                    7, new Distribution("&7New Pebbles", Material.STONE, MUtil.map("DIAMOND:0", 100.0),MUtil.list("&7Common", "", "&7Block Prices:", "&7- Diamond &a$6"))

        );

    public Map<Integer, DistributionPage> pages = MUtil.map(
            1, new DistributionPage("&7&lCommon Blocks", MUtil.list(1,2,3)),
            2, new DistributionPage("&4&lRare Blocks", MUtil.list(4,5,6)),
            3, new DistributionPage("&8&lDonor Blocks", MUtil.list(7))
    );

}
