package com.kennedysmithjava.prisonmines.entity;

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
               1, new Distribution(Material.STONE, MUtil.map("STONE:0", 75.0, "WEB:0", 25.0)),
            2, new Distribution(Material.STONE, MUtil.map("STONE:0", 75.0, "STONE:5", 25.0)),
                    3, new Distribution(Material.STONE, MUtil.map("STONE:0", 50.0, "STONE:5", 50.0)),
                    4, new Distribution(Material.STONE, MUtil.map("STONE:0", 25.0, "STONE:5", 75.0)),
                    5, new Distribution(Material.STONE, MUtil.map("STONE:5", 75.0,"STONE:6", 25.0)),
                    6, new Distribution(Material.STONE, MUtil.map("STONE:5", 75.0,"STONE:6", 25.0))
    );

}
