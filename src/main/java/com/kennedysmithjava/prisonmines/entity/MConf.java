package com.kennedysmithjava.prisonmines.entity;

import com.kennedysmithjava.prisonmines.PrisonMines;
import com.massivecraft.massivecore.command.editor.annotation.EditorName;
import com.massivecraft.massivecore.store.Entity;
import com.massivecraft.massivecore.util.MUtil;

import java.util.List;
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

    //
    public List<String> aliasesM = MUtil.list("mine", "m");
    public List<String> aliasesMAdmin = MUtil.list("mines");

    public String timeZone = "America/New_York";

    public int mineNameLengthMin = 3;
    public int mineNameLengthMax = 9;

    public String getTimeZone() {
        return timeZone;
    }
}

