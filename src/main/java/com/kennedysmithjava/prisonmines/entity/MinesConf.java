package com.kennedysmithjava.prisonmines.entity;

import com.kennedysmithjava.prisonmines.util.BlockMaterial;
import com.massivecraft.massivecore.command.editor.annotation.EditorName;
import com.massivecraft.massivecore.store.Entity;
import com.massivecraft.massivecore.util.MUtil;
import org.bukkit.Material;

import java.util.List;
import java.util.Map;

@EditorName("config")
public class MinesConf extends Entity<MinesConf>
{

    // -------------------------------------------- //
    // INSTANCE & CONSTRUCT
    // -------------------------------------------- //

    protected static transient MinesConf i;


    public static MinesConf get() { return i; }

    @Override
    public MinesConf load(MinesConf that)
    {
        super.load(that);
        return this;
    }

    // -------------------------------------------- //
    // COMMAND ALIASES
    // -------------------------------------------- //

    public List<String> aliasesM = MUtil.list("mine", "m");
    public List<String> aliasesMAdmin = MUtil.list("mines");
    public List<String> aliasesOffset = MUtil.list("o", "offset");

    // -------------------------------------------- //
    // MINE SETTINGS
    // -------------------------------------------- //

    public Long defaultResetTimer = 25L;
    public int mineNameLengthMin = 3;
    public int mineNameLengthMax = 9;
    public int mineDefaultHeight = 3;
    public int mineDefaultWidth = 3;
    public int mineDefaultWallID = 1;
    public int mineDefaultPathID = 1;
    public String mineDefaultName = "%player%'s Mine";

    public BlockMaterial minesBorderMaterial = new BlockMaterial(Material.BEDROCK, (byte) 0);

    public boolean weatherEnabled = false;
    public Map<String, String> minesWorldDefaultGamerules = MUtil.map("doMobSpawning", "false", "doDaylightCycle", "true", "doMobLoot", "false", "doTileDrops", "false", "keepInventory", "true", "mobGriefing", "false");

    // -------------------------------------------- //
    // MISC
    // -------------------------------------------- //

    public String timeZone = "America/New_York";

}


