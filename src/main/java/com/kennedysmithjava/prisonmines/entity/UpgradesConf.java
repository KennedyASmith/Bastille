package com.kennedysmithjava.prisonmines.entity;

import com.kennedysmithjava.prisonmines.upgrades.Upgrade;
import com.massivecraft.massivecore.command.editor.annotation.EditorName;
import com.massivecraft.massivecore.store.Entity;
import com.massivecraft.massivecore.util.MUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@EditorName("config")
public class UpgradesConf extends Entity<UpgradesConf>
{
    // -------------------------------------------- //
    // META
    // -------------------------------------------- //

    protected static transient UpgradesConf i;

    public static UpgradesConf get() { return i; }

    @Override
    public UpgradesConf load(UpgradesConf that)
    {
        super.load(that);
        return this;
    }



    

}

