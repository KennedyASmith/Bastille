package com.kennedysmithjava.prisoncore;

import com.comphenix.protocol.ProtocolManager;
import com.kennedysmithjava.prisoncore.cmd.PouchCommand;
import com.kennedysmithjava.prisoncore.engine.EngineTools;
import com.kennedysmithjava.prisoncore.engine.EngineTrees;
import com.kennedysmithjava.prisoncore.entity.MConfColl;
import com.kennedysmithjava.prisoncore.entity.farming.FarmingConfColl;
import com.kennedysmithjava.prisoncore.entity.farming.FishingConfColl;
import com.kennedysmithjava.prisoncore.entity.farming.TreesConfColl;
import com.kennedysmithjava.prisoncore.entity.mines.*;
import com.kennedysmithjava.prisoncore.entity.mines.objects.Floor;
import com.kennedysmithjava.prisoncore.entity.mines.objects.Wall;
import com.kennedysmithjava.prisoncore.entity.npcs.FarmerConfColl;
import com.kennedysmithjava.prisoncore.entity.npcs.FarmerGuiConfColl;
import com.kennedysmithjava.prisoncore.entity.npcs.SkinsConfColl;
import com.kennedysmithjava.prisoncore.entity.player.*;
import com.kennedysmithjava.prisoncore.entity.tools.BufferConfColl;
import com.kennedysmithjava.prisoncore.entity.tools.EnchantConfColl;
import com.kennedysmithjava.prisoncore.entity.tools.PickaxeTypeColl;
import com.kennedysmithjava.prisoncore.entity.tools.PouchConfColl;
import com.kennedysmithjava.prisoncore.event.EventNewMine;
import com.kennedysmithjava.prisoncore.npc.NPCBlacksmithTrait;
import com.kennedysmithjava.prisoncore.npc.NPCLimboTrait;
import com.kennedysmithjava.prisoncore.npc.NPCLumberjackTrait;
import com.kennedysmithjava.prisoncore.npc.SkinManager;
import com.kennedysmithjava.prisoncore.placeholders.*;
import com.kennedysmithjava.prisoncore.quest.QuestPath;
import com.kennedysmithjava.prisoncore.quest.paths.PathIntroduction;
import com.kennedysmithjava.prisoncore.quest.paths.PathStarterGateway;
import com.kennedysmithjava.prisoncore.tools.Pickaxe;
import com.kennedysmithjava.prisoncore.tools.ability.*;
import com.kennedysmithjava.prisoncore.tools.enchantment.*;
import com.kennedysmithjava.prisoncore.tools.pouch.DatalessPouchable;
import com.kennedysmithjava.prisoncore.util.FAWEPaster;
import com.kennedysmithjava.prisoncore.util.Glow;
import com.kennedysmithjava.prisoncore.util.regions.MinesWorldManager;
import com.kennedysmithjava.prisoncore.util.regions.VoidGenerator;
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
import org.bukkit.generator.ChunkGenerator;

import java.lang.reflect.Field;
import java.util.List;
import java.util.UUID;

@SuppressWarnings("unused")
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

    // -------------------------------------------- //
    // OVERRIDE
    // -------------------------------------------- //

    @Override
    public void onEnableInner() {
        /* REGISTER QUESTS BEFORE COLLECTORS */
        QuestPath.register(PathIntroduction.get());
        QuestPath.register(PathStarterGateway.get());

        /* REGISTER ENCHANTS */
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
        this.registerGlow();

        /* REGISTER PICKAXE ABILITIES */
        Ability.register(AbilityAsteroidStrike.get());
        Ability.register(AbilityPulverize.get());
        Ability.register(AbilityTetris.get());
        Ability.register(AbilityBlackhole.get());


        /* REGISTER ALL CITIZENS TRAITS */
        CitizensAPI.getTraitFactory().registerTrait(TraitInfo.create(NPCLimboTrait.class));
        CitizensAPI.getTraitFactory().registerTrait(TraitInfo.create(NPCLumberjackTrait.class));
        CitizensAPI.getTraitFactory().registerTrait(TraitInfo.create(NPCBlacksmithTrait.class));

        /* ACTIVATE ENGINES/COLLECTORS */
        this.activateAuto();

        /* ACTIVATE POUCHES */
        this.activate(PouchCommand.class);
        ConfigurationSerialization.registerClass(DatalessPouchable.class);

        /* REGISTER ALL SKINS IN SKINS CONFIG */
        SkinManager.registerAll();

        /* MISC REGISTRY */
        Pickaxe.LORE_UPDATER.runTaskTimerAsynchronously(this, 20L, 5 * 20L);
        getServer().getPluginManager().registerEvents(new EngineTools(), this);

        /* ACTIVATE PLACEHOLDER EXPANSIONS */
        new MineCountdownPlaceholder(this).register();
        new TreeHologramPlaceholder(this).register();
        new MineCurrencyPlaceholder(this).register();
        new SkillPlaceholder(this).register();
        new QuestPlaceholder(this).register();
    }

    @Override
    public void onEnablePost() {
        nonPersistNPCRegistry = CitizensAPI.createAnonymousNPCRegistry(new MemoryNPCDataStore());
        EngineTrees.get().loadTreeMaterialCache();
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
        } catch (IllegalArgumentException ignored) {
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
                QuestProfileColl.class,
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
                TreesConfColl.class,
                FishingConfColl.class,
                SkillsConfColl.class,
                SkillProfileColl.class,
                SkinsConfColl.class
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


    @SuppressWarnings("NullableProblems")
    @Override
    public ChunkGenerator getDefaultWorldGenerator(String worldName, String id) {
        return new VoidGenerator();
    }

    // -------------------------------------------- //
    // STATIC METHODS
    // -------------------------------------------- //

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
        Location mineCenter = floor.getMineCenter().getFrom(origin);
        Location spawn = floor.getSpawn().getFrom(origin);
        Location architectLocation = floor.getArchitectNPC().getFrom(origin);
        Location researcherLocation = floor.getResearcherNPC().getFrom(origin);
        Location collectorLocation = floor.getCollectorNPC().getFrom(origin);

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
        mine.setPortalMinLocation(floor.getPortalMin().getFrom(origin));
        mine.setPortalMaxLocation(floor.getPortalMax().getFrom(origin));
        mine.setBeaconLocation(floor.getBeacon().getFrom(origin));
        mine.setChestLocation(floor.getChest().getFrom(origin));
        mine.setEnchantTableLocation(floor.getEnchantTable().getFrom(origin));
        mine.setOrigin(origin);
        mine.setMineCenter(mineCenter);
        mine.setRegenTimer(MinesConf.get().defaultResetTimer);
        mine.setFloorID(MinesConf.get().mineDefaultPathID);
        mine.setWallID(MinesConf.get().mineDefaultWallID);
        mine.setWidthVar(MinesConf.get().mineDefaultWidth);
        mine.setHeightVar(MinesConf.get().mineDefaultHeight);
        mine.setUnlockedDistributions(MUtil.list(0));
        mine.setBlockDistribution(0);
        mine.setAutoRegenEnabled(false);

        MineColl.get().attach(mine, id);

        //GENERATE UNBREAKABLE BORDER & GENERATE MINE BLOCKS
        mine.generateBorder(mine.getWidth(), mine.getWidth(), MinesConf.get().minesBorderMaterial);
        mine.generateMobilityArea(mine.getWidth(), mine.getWidth());

        //FIRST PASTE FLOOR
        FAWEPaster.paste(floor.getSchematic(mine.getWidth()), world.getName(), minCorner, false, () -> {
            //THEN PLACE WALL
            FAWEPaster.paste(wall.getSchematic(), world.getName(), minCorner, false, () -> {
                onComplete.run();
                mine.createRegenCountdown();
                mine.regen();
                mine.placeLever();
                Bukkit.getServer().getPluginManager().callEvent(new EventNewMine(player, mine));
            });
        });
    }

}
