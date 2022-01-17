package com.kennedysmithjava.prisoncore.entity.mines;

import com.massivecraft.massivecore.command.editor.annotation.EditorName;
import com.massivecraft.massivecore.store.Entity;

@EditorName("config")
public class WarrenGUIConf extends Entity<WarrenGUIConf> {
    // -------------------------------------------- //
    // META
    // -------------------------------------------- //

    protected static transient WarrenGUIConf i;
    public int guiBorderColor = 15;
    public int guiSize = 45;

    public static WarrenGUIConf get() {
        return i;
    }

    @Override
    public WarrenGUIConf load(WarrenGUIConf that) {
        super.load(that);
        return this;
    }

}

