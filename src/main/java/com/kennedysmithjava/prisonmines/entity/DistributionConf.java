package com.kennedysmithjava.prisonmines.entity;

import com.kennedysmithjava.prisonmines.util.DistributionPage;
import com.massivecraft.massivecore.command.editor.annotation.EditorName;
import com.massivecraft.massivecore.store.Entity;
import com.massivecraft.massivecore.util.MUtil;
import org.bukkit.Material;

import java.util.Map;

@EditorName("config")
public class DistributionConf extends Entity<DistributionConf>
{
    // -------------------------------------------- //
    // META
    // -------------------------------------------- //

    protected static DistributionConf i;


    public static DistributionConf get() { return i; }

    public DistributionConf() {

    }

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
               1, new Distribution("&7Old Hole", Material.GRAVEL, MUtil.map(1, 15.0,2, 35.0, 3, 50.0),   MUtil.list("&7Common", "", "&fBlocks:", "&7S. Pebbles 35% &a$0.10", "&7Lg. Pebbles 15% &a$0.20", "&7Cobweb 50% &a$0.00")),
            2, new Distribution("&7Old Pebbles", Material.STONE, MUtil.map(1, 15.0,2, 35.0, 3, 50.0),        MUtil.list("&7Common", "", "&fBlocks:", "&7S. Pebbles 50% &a$0.10", "&7Lg. Pebbles 50% &a$0.20", "&7Cobweb 50% &a$0.00")),
                    3, new Distribution("&7Plentiful Pebbles", Material.STONE, MUtil.map(1, 15.0,2, 35.0, 3, 50.0),  MUtil.list("&7Common", "", "&fBlocks:", "&7S. Pebbles 50% &a$0.10", "&7Lg. Pebbles 50% &a$0.20", "&7Cobweb 50% &a$0.00")),
                    4, new Distribution("&7Driveway Gravel",Material.STONE, MUtil.map(1, 15.0,2, 35.0, 3, 50.0),     MUtil.list("&7Common", "", "&fBlocks:", "&7S. Pebbles 50% &a$0.10", "&7Lg. Pebbles 50% &a$0.20", "&7Cobweb 50% &a$0.00")),
                    5, new Distribution("&7Quarry Leftovers", Material.STONE, MUtil.map(1, 15.0,2, 35.0, 3, 50.0),   MUtil.list("&7Common", "", "&fBlocks:", "&7S. Pebbles 50% &a$0.10", "&7Lg. Pebbles 50% &a$0.20", "&7Cobweb 50% &a$0.00")),
                    6, new Distribution("&7New Pebbles", Material.STONE, MUtil.map(1, 15.0,2, 35.0, 3, 50.0),        MUtil.list("&7Common", "", "&fBlocks:", "&7S. Pebbles 50% &a$0.10", "&7Lg. Pebbles 50% &a$0.20", "&7Cobweb 50% &a$0.00")),
                    7, new Distribution("&7New Pebbles", Material.STONE,MUtil.map(1, 15.0,2, 35.0, 3, 50.0),        MUtil.list("&7Common", "", "&7Block Prices:", "&7- Diamond &a$6"))

        );

    public Map<Integer, DistributionPage> pages = MUtil.map(
            1, new DistributionPage("&7&lCommon Blocks", MUtil.list(1,2,3)),
            2, new DistributionPage("&4&lRare Blocks", MUtil.list(4,5,6)),
            3, new DistributionPage("&8&lDonor Blocks", MUtil.list(7))
    );

}
