package com.kennedysmithjava.prisonmines.entity;

import com.kennedysmithjava.prisonmines.PrisonMines;
import com.massivecraft.massivecore.collections.MassiveMapDef;
import com.massivecraft.massivecore.command.editor.annotation.EditorName;
import com.massivecraft.massivecore.store.Entity;
import com.massivecraft.massivecore.util.MUtil;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Set;

@EditorName("config")
public class MConf extends Entity<MConf>
{
    // -------------------------------------------- //
    // META
    // -------------------------------------------- //

    protected static transient MConf i;


    public static MConf get() { return i; }

    @Override
    public MConf load(MConf that)
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

    public Long defaultResetTimer = 10L;
    public int mineNameLengthMin = 3;
    public int mineNameLengthMax = 9;
    public Long getDefaultResetTimer() {
        return defaultResetTimer;
    }

    // -------------------------------------------- //
    // MISC
    // -------------------------------------------- //

    public String timeZone = "America/New_York";
    public Map<String, String> minesWorldDefaultGamerules = MUtil.map("doMobSpawning", "false", "doDaylightCycle", "true", "doMobLoot", "false", "doTileDrops", "false", "keepInventory", "true", "mobGriefing", "false");

    public String architectLineNoPermission = "&7[&bRon&7] I don't think you have permission to order from me, pal!";
    public String architectLineWelcome = "&7[&bRon&7] What can I do for ya today, chief?";

}

