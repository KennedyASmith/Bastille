package com.kennedysmithjava.prisoncore.entity.mines;

import com.massivecraft.massivecore.command.editor.annotation.EditorName;
import com.massivecraft.massivecore.store.Entity;

@EditorName("config")
public class ArchitectGUIConf extends Entity<ArchitectGUIConf>
{
    // -------------------------------------------- //
    // META
    // -------------------------------------------- //

    protected static transient ArchitectGUIConf i;

    public static ArchitectGUIConf get() { return i; }

    @Override
    public ArchitectGUIConf load(ArchitectGUIConf that)
    {
        super.load(that);
        return this;
    }


}

