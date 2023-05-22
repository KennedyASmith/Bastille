package com.kennedysmithjava.prisoncore;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.kennedysmithjava.prisoncore.cmd.PouchCommand;
import com.kennedysmithjava.prisoncore.eco.MineCurrencyPlaceholder;
import com.kennedysmithjava.prisoncore.engine.EngineTools;
import com.kennedysmithjava.prisoncore.engine.EngineTrees;
import com.kennedysmithjava.prisoncore.entity.MConfColl;
import com.kennedysmithjava.prisoncore.entity.farming.FarmingConfColl;
import com.kennedysmithjava.prisoncore.entity.farming.TreesConf;
import com.kennedysmithjava.prisoncore.entity.farming.TreesConfColl;
import com.kennedysmithjava.prisoncore.entity.mines.objects.Floor;
import com.kennedysmithjava.prisoncore.entity.npcs.FarmerConfColl;
import com.kennedysmithjava.prisoncore.entity.npcs.FarmerGuiConfColl;
import com.kennedysmithjava.prisoncore.entity.player.MPlayer;
import com.kennedysmithjava.prisoncore.entity.player.MPlayerColl;
import com.kennedysmithjava.prisoncore.entity.tools.BufferConfColl;
import com.kennedysmithjava.prisoncore.entity.tools.EnchantConfColl;
import com.kennedysmithjava.prisoncore.entity.tools.PickaxeTypeColl;
import com.kennedysmithjava.prisoncore.entity.tools.PouchConfColl;
import com.kennedysmithjava.prisoncore.entity.mines.objects.Wall;
import com.kennedysmithjava.prisoncore.entity.mines.*;
import com.kennedysmithjava.prisoncore.event.EventNewMine;
import com.kennedysmithjava.prisoncore.npc.spawn.NPCBlacksmithTrait;
import com.kennedysmithjava.prisoncore.npc.spawn.NPCLimboTrait;
import com.kennedysmithjava.prisoncore.npc.spawn.NPCLumberjackTrait;
import com.kennedysmithjava.prisoncore.tools.Pickaxe;
//import com.kennedysmithjava.prisoncore.quest.QuestManager;
//import com.kennedysmithjava.prisoncore.quest.QuestProfile;
import com.kennedysmithjava.prisoncore.tools.ability.*;
import com.kennedysmithjava.prisoncore.tools.enchantment.*;
import com.kennedysmithjava.prisoncore.tools.pouch.DatalessPouchable;
import com.kennedysmithjava.prisoncore.util.*;
import com.massivecraft.massivecore.MassivePlugin;
import com.massivecraft.massivecore.collections.MassiveList;
import com.massivecraft.massivecore.util.MUtil;
import com.sk89q.worldedit.math.BlockVector3;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.MemoryNPCDataStore;
import net.citizensnpcs.api.npc.NPCRegistry;
import net.citizensnpcs.api.trait.TraitInfo;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.generator.ChunkGenerator;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;

public class PrisonCore extends MassivePlugin {

    // -------------------------------------------- //
    // INSTANCE & CONSTRUCT
    // -------------------------------------------- //
    private static PrisonCore i;

    public PrisonCore() {
        PrisonCore.i = this;
    }

    //public static Map<MPlayer, QuestProfile> activeTutorials = new HashMap<>();

    public static PrisonCore get() {
        return i;
    }

    //QuestManager questManager;
    private ProtocolManager protocolManager = null;
    public static NPCRegistry nonPersistNPCRegistry;
    private final static String registryName = "MinesNPC";

    // -------------------------------------------- //
    // OVERRIDE
    // -------------------------------------------- //

    @Override
    public void onEnableInner() {


        this.activate(PouchCommand.class);

        // ACTIVATE ENGINES/COLLECTORS
        this.activateAuto();

        Enchant.register(PickaxeConeEnchant.get());
        Enchant.register(PickaxeEfficiencyEnchant.get());
        Enchant.register(PickaxeExplosiveEnchant.get());
        Enchant.register(PickaxeHasteEnchant.get());
        Enchant.register(PickaxeJackhammerEnchant.get());
        Enchant.register(PickaxeJackpotEnchant.get());
        Enchant.register(PickaxeLaserEnchant.get());
        Enchant.register(PickaxeLuckEnchant.get());
        Enchant.register(PickaxeNightVisionEnchant.get());
        Enchant.register(PickaxeRefinerEnchant.get());
        Enchant.register(PickaxeSpeedEnchant.get());
        Enchant.register(PickaxeVeinEnchant.get());

        Ability.register(AbilityAsteroidStrike.get());
        Ability.register(AbilityPulverize.get());
        Ability.register(AbilityTetris.get());
        Ability.register(AbilityBlackhole.get());

        this.registerGlow(); //Register glow enchantment

        new MineCountdownPlaceholder(this).register();
        new MineCurrencyPlaceholder(this).register();

        ConfigurationSerialization.registerClass(DatalessPouchable.class);

        CitizensAPI.getTraitFactory().registerTrait(TraitInfo.create(NPCLimboTrait.class));
        CitizensAPI.getTraitFactory().registerTrait(TraitInfo.create(NPCLumberjackTrait.class));
        CitizensAPI.getTraitFactory().registerTrait(TraitInfo.create(NPCBlacksmithTrait.class));

        Pickaxe.LORE_UPDATER.runTaskTimerAsynchronously(this, 20L, 5 * 20L);
        getServer().getPluginManager().registerEvents(new EngineTools(), this);
        //this.questManager = new QuestManager();
        this.protocolManager = ProtocolLibrary.getProtocolManager();
    }

    @Override
    public void onEnablePost() {
        nonPersistNPCRegistry = CitizensAPI.createAnonymousNPCRegistry(new MemoryNPCDataStore());
        try {
            EngineTrees.get().loadTreeMaterialCache();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void registerGlow() {
        try {
            Field f = Enchantment.class.getDeclaredField("acceptingNew");
            f.setAccessible(true);
            f.set(null, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            NamespacedKey key = new NamespacedKey(this, "glowKey");
            Glow glow = new Glow(key);
            Enchantment.registerEnchantment(glow);
        } catch (IllegalArgumentException e) {
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Class<?>> getClassesActiveColls() {
        // MConf should always be activated first for all plugins. It's simply a standard. The config should have no dependencies.
        // MFlag and MPerm are both dependency free.

        return new MassiveList<>(
                MConfColl.class,
                MPlayerColl.class,
                MinesConfColl.class,
                BlocksConfColl.class,
                DistributionConfColl.class,
                MineColl.class,
                LayoutConfColl.class,
                PouchConfColl.class,
                EnchantConfColl.class,
                PickaxeTypeColl.class,
                BufferConfColl.class,
                WarrenConfColl.class,
                WarrenGUIConfColl.class,
                ArchitectConfColl.class,
                ArchitectGUIConfColl.class,
                CoinCollectorConfColl.class,
                CoinCollectorGuiConfColl.class,
                FarmingConfColl.class,
                FarmerConfColl.class,
                FarmerGuiConfColl.class,
                TreesConfColl.class
        );
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

    @Override
    public boolean isVersionSynchronized() {
        return false;
    }


    @Override
    public ChunkGenerator getDefaultWorldGenerator(String worldName, String id) {
        return new VoidGenerator();
    }

    // -------------------------------------------- //
    // STATIC METHODS
    // -------------------------------------------- //


    /*public QuestManager getQuestManager() {
        return questManager;
    }*/

    public static NPCRegistry getNonPersistNPCRegistry() {
        return nonPersistNPCRegistry;
    }

    public ProtocolManager getProtocolManager() {
        return protocolManager;
    }

    @SuppressWarnings("unused")
    public static void createMine(MPlayer player, Runnable onComplete) {

        //GENERATE A RANDOM ID FOR MINE & ASSIGN TO PLAYER
        String id = UUID.randomUUID().toString();
        Mine mine = new Mine();
        player.setMineID(id);

        // DEFINE ESSENTIAL LAYOUTS AND LOCATIONS
        MinesWorldManager worldManager = MinesWorldManager.get();
        World world = worldManager.getWorld();
        LayoutConf layoutConf = LayoutConf.get();
        Floor floor = layoutConf.getPath(MinesConf.get().mineDefaultWallID);
        Wall wall = layoutConf.getWall(MinesConf.get().mineDefaultWallID);
        int width = MinesConf.get().mineDefaultWidth;
        int height = MinesConf.get().mineDefaultHeight;

        BlockVector3 minCorner = worldManager.getUniqueLocation();

        Location origin = new Location(world, minCorner.getBlockX(), minCorner.getBlockY(), minCorner.getBlockZ());
        Location mineCenter = floor.getMineCenter().get(origin);
        Location spawn = floor.getSpawn().get(origin);
        Location architectLocation = floor.getArchitectNPC().get(origin);
        Location researcherLocation = floor.getResearcherNPC().get(origin);
        Location collectorLocation = floor.getCollectorNPC().get(origin);

        Location maxMine = mineCenter.clone().add(-(width - 2), 0, -(width - 2));
        Location minMine = maxMine.clone().add(width - 1, -(height - 1), width - 1);

        // SAVE ESSENTIAL MINE VALUES
        mine.setName(MinesConf.get().mineDefaultName.replaceAll("%player%", player.getPlayer().getName()));
        mine.setMineMin(minMine);
        mine.setMineMax(maxMine);
        mine.setSpawnPoint(spawn);
        mine.setArchitectLocation(architectLocation);
        mine.setResearcherLocation(researcherLocation);
        mine.setCollectorLocation(collectorLocation);
        mine.setPortalMinLocation(floor.getPortalMin().get(origin));
        mine.setPortalMaxLocation(floor.getPortalMax().get(origin));
        mine.setBeaconLocation(floor.getBeacon().get(origin));
        mine.setChestLocation(floor.getChest().get(origin));
        mine.setEnchantTableLocation(floor.getEnchantTable().get(origin));
        mine.setOrigin(origin);
        mine.setMineCenter(mineCenter);
        mine.setRegenTimer(MinesConf.get().defaultResetTimer);
        mine.setFloorID(MinesConf.get().mineDefaultPathID);
        mine.setWallID(MinesConf.get().mineDefaultWallID);
        mine.setWidthVar(MinesConf.get().mineDefaultWidth);
        mine.setHeightVar(MinesConf.get().mineDefaultHeight);
        mine.setUnlockedDistributions(MUtil.list(1));
        mine.setBlockDistribution(1);
        mine.setAutoRegenEnabled(false);

        MineColl.get().attach(mine, id);

        //GENERATE UNBREAKABLE BORDER & GENERATE MINE BLOCKS
        mine.generateBorder(mine.getWidth(), mine.getWidth(), MinesConf.get().minesBorderMaterial);
        mine.generateMobilityArea(mine.getWidth(), mine.getWidth());

        //FIRST PASTE FLOOR
        FAWEPaster.paste(floor.getSchematic(mine.getWidth()), world.getName(), minCorner, false, new Runnable() {
            @Override
            public void run() {
                //THEN PLACE WALL
                FAWEPaster.paste(wall.getSchematic(), world.getName(), minCorner, false, new Runnable() {
                    @Override
                    public void run() {
                        onComplete.run();
                        mine.createRegenCountdown();
                        mine.regen();
                        mine.placeLever();
                        Bukkit.getServer().getPluginManager().callEvent(new EventNewMine(player, mine));
                    }
                });
            }
        });
    }

}
