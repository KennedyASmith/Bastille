package com.kennedysmithjava.prisoncore.entity.npcs;

import com.massivecraft.massivecore.command.editor.annotation.EditorName;
import com.massivecraft.massivecore.store.Entity;

@EditorName("config")
public class FarmerGuiConf extends Entity<FarmerGuiConf> {
    // -------------------------------------------- //
    // META
    // -------------------------------------------- //

    protected static transient FarmerGuiConf i;

    public String guiName = "&eFarmer Gui";
    public int guiBorderColor = 15;
    public int guiSize = 27;

    public static FarmerGuiConf get() {
        return i;
    }

    @Override
    public FarmerGuiConf load(FarmerGuiConf that) {
        super.load(that);
        return this;
    }

}

