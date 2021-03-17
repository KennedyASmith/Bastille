package com.kennedysmithjava.prisonmines;

import com.kennedysmithjava.prisonmines.entity.MConfColl;
import com.kennedysmithjava.prisonmines.entity.MineColl;
import com.massivecraft.massivecore.MassivePlugin;
import com.massivecraft.massivecore.collections.MassiveList;

import java.util.List;

public class PrisonMines extends MassivePlugin
{

    public final static String ID_NONE = "none";

    // -------------------------------------------- //
    // INSTANCE & CONSTRUCT
    // -------------------------------------------- //

    private static PrisonMines i;

    public PrisonMines()
    {
        PrisonMines.i = this;
    }

    public static PrisonMines get()
    {
        return i;
    }

    // -------------------------------------------- //
    // OVERRIDE
    // -------------------------------------------- //

    @Override
    public void onEnableInner()
    {

        // Activate
        this.activateAuto();

        pluginEnableMillis = System.currentTimeMillis();

    }

    private Long pluginEnableMillis = null;

    public Long getPluginEnableMillis() {
        return pluginEnableMillis;
    }

    @Override
    public List<Class<?>> getClassesActiveColls()
    {
        // MConf should always be activated first for all plugins. It's simply a standard. The config should have no dependencies.
        // MFlag and MPerm are both dependency free.
        return new MassiveList<Class<?>>(
                MConfColl.class,
                MineColl.class
        );
    }

    @Override
    public List<Class<?>> getClassesActiveEngines()
    {
        List<Class<?>> ret = super.getClassesActiveEngines();
        return ret;
    }


    @Override
    public void onDisable()
    {
        super.onDisable();
    }

    @Override
    public boolean isVersionSynchronized()
    {
        return false;
    }

}
