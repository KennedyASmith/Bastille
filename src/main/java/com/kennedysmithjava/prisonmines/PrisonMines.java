package com.kennedysmithjava.prisonmines;

import com.kennedysmithjava.prisonmines.engine.EngineOffsetWand;
import com.kennedysmithjava.prisonmines.engine.EnginePersonalMines;
import com.kennedysmithjava.prisonmines.entity.*;
import com.kennedysmithjava.prisonmines.traits.ArchitectTrait;
import com.kennedysmithjava.prisonmines.util.VoidGenerator;
import com.massivecraft.massivecore.MassivePlugin;
import com.massivecraft.massivecore.collections.MassiveList;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.trait.TraitInfo;
import org.bukkit.Bukkit;
import org.bukkit.generator.ChunkGenerator;

import java.io.File;
import java.util.List;
import java.util.logging.Level;

public class PrisonMines extends MassivePlugin
{

    public final static String ID_NONE = "none";
    public static File schematicConfigFile = null;


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
        schematicConfigFile = new File(PrisonMines.get().getDataFolder() + File.separator + "Schematics");
        if(!schematicConfigFile.exists()) schematicConfigFile.mkdirs();

        if (getServer().getPluginManager().getPlugin("Citizens") == null || !getServer().getPluginManager().getPlugin("Citizens").isEnabled()) {
            getLogger().log(Level.SEVERE, "Citizens 2.0 not found or not enabled");
            getServer().getPluginManager().disablePlugin(this);
        }

        // Activate
        this.activateAuto();

        pluginEnableMillis = System.currentTimeMillis();

        CitizensAPI.getTraitFactory().registerTrait(TraitInfo.create(ArchitectTrait.class).withName("architecttrait"));
        CitizensAPI.getTraitFactory().registerTrait(TraitInfo.create(ArchitectTrait.class).withName("researchertrait"));

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
                MineColl.class,
                DistributionConfColl.class,
                UpgradesConfColl.class,
                UpgradesGUIConfColl.class,
                LayoutConfColl.class
        );
    }

    @Override
    public List<Class<?>> getClassesActiveEngines()
    {
        List<Class<?>> ret = super.getClassesActiveEngines();

        ret.add(EnginePersonalMines.class);
        ret.add(EngineOffsetWand.class);

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


    @Override
    public ChunkGenerator getDefaultWorldGenerator(String worldName, String id) {
        return new VoidGenerator();
    }

    public static File getSchematicConfigFile() {
        return schematicConfigFile;
    }

    public static File getSchematicFolder() {
        return schematicConfigFile.getParentFile();
    }
}
