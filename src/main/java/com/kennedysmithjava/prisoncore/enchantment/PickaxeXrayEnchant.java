package com.kennedysmithjava.prisoncore.enchantment;

import com.kennedysmithjava.prisoncore.PrisonCore;
import com.kennedysmithjava.prisoncore.blockhandler.BlockWrapper;
import com.kennedysmithjava.prisoncore.crafting.Recipe;
import com.kennedysmithjava.prisoncore.eco.Cost;
import com.kennedysmithjava.prisoncore.eco.CostCurrency;
import com.kennedysmithjava.prisoncore.eco.CostSkillLevel;
import com.kennedysmithjava.prisoncore.eco.CurrencyType;
import com.kennedysmithjava.prisoncore.engine.EngineCooldown;
import com.kennedysmithjava.prisoncore.entity.mines.Mine;
import com.kennedysmithjava.prisoncore.entity.player.MPlayer;
import com.kennedysmithjava.prisoncore.entity.tools.EnchantConf;
import com.kennedysmithjava.prisoncore.skill.SkillType;
import com.kennedysmithjava.prisoncore.util.CooldownReason;
import com.massivecraft.massivecore.util.MUtil;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.npc.NPCRegistry;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Shulker;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class PickaxeXrayEnchant extends HandEquipEnchant<PickaxeXrayEnchant> {

    public static PickaxeXrayEnchant i = new PickaxeXrayEnchant();
    public static PickaxeXrayEnchant get() { return i; }

    //Player UUID, enchant level
    public static Map<UUID, Integer> equippedXrayUsers = new HashMap<>();
    public static List<Player> refreshableUsers = new ArrayList<>();
    public static Map<UUID, Map<Location, Integer>> activeLocations = new HashMap<>();
    public static Map<UUID, Map<Location, UUID>> activeEntities = new HashMap<>();

    PickaxeXrayEnchant(){
        new BukkitRunnable() {
            @Override
            public void run() {
                activeLocations.clear(); //Invalidate cache every minute
                for (UUID uuid : activeEntities.keySet()) {
                    hideAllCubes(uuid);
                }
            }
        }.runTaskTimer(PrisonCore.get(), 20, 20*60);
        new BukkitRunnable() {
            @Override
            public void run() {
                /* Uses a queue to spawn magmas for users that have just equipped/run out of blocks */
                for (Player player : refreshableUsers) {
                    if(player == null) continue;
                    UUID uuid = player.getUniqueId();
                    if(!equippedXrayUsers.containsKey(uuid)) continue;
                    int level = equippedXrayUsers.get(uuid);
                    Map<Location, Integer> xrayLocations = activeLocations.getOrDefault(
                            player.getUniqueId(),
                            getXrayLocations(player, level));
                    activeLocations.put(player.getUniqueId(), xrayLocations);
                    for (Map.Entry<Location, Integer> entry : xrayLocations.entrySet()) {
                        Location location = entry.getKey();
                        int entityID = entry.getValue();
                        summonGlowingMagmaCube(player.getUniqueId(), location, entityID);
                    }
                }
                refreshableUsers.clear();
            }
        }.runTaskTimer(PrisonCore.get(), 20, 20);
    }

    @Override
    public void onEquip(Player player, int level) {
        if(!EngineCooldown.inCooldown(player.getUniqueId(), CooldownReason.XRAY_ENCHANT)){
            EngineCooldown.add(player.getUniqueId(), 20, CooldownReason.XRAY_ENCHANT);
            equippedXrayUsers.put(player.getUniqueId(), level);
            if(activeLocations.containsKey(player.getUniqueId())) return;
            refreshableUsers.add(player); //Adds to queue to summon magma
        }
    }

    private static Map<Location, Integer> getXrayLocations(Player player, int numberOfBlocks){
        MPlayer mPlayer = MPlayer.get(player);
        Mine mine = mPlayer.getMine();
        if(mine == null) return new HashMap<>();

        Map<BlockWrapper, Double> distribution = mine.getBlockDistribution();
        BlockWrapper wrapper = findRarestBlock(distribution);
        Material rarestMaterial = wrapper.getMaterial();

        Location mineMin = mine.getMineMin();
        Location mineMax = mine.getMineMax();

        int minX = Math.min(mineMin.getBlockX(), mineMax.getBlockX());
        int minY = Math.min(mineMin.getBlockY(), mineMax.getBlockY());
        int minZ = Math.min(mineMin.getBlockZ(), mineMax.getBlockZ());

        int maxX = Math.max(mineMin.getBlockX(), mineMax.getBlockX());
        int maxY = Math.max(mineMin.getBlockY(), mineMax.getBlockY());
        int maxZ = Math.max(mineMin.getBlockZ(), mineMax.getBlockZ());

        World world = mineMin.getWorld();

        int entityId = player.getEntityId() + 1;
        int blocksFound = 0;

        Map<Location, Integer> locations = new HashMap<>();

        EXIT_LOOPS:
        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                for (int z = minZ; z <= maxZ; z++) {
                    Location currentLocation = new Location(world, x + 0.5, y, z + 0.5);
                    Block aboveBlock = currentLocation.clone().add(0, 1, 0).getBlock();
                    if(aboveBlock.getType() != Material.AIR) continue; //Only highlight non-exposed blocks
                    Block currentBlock = currentLocation.getBlock();
                    Material blockType = currentBlock.getType();
                    if(blockType == rarestMaterial && blocksFound < numberOfBlocks){
                        locations.put(currentLocation, entityId);
                        entityId++;
                        blocksFound++;
                        if(blocksFound == numberOfBlocks) break EXIT_LOOPS;
                    }
                }
            }
        }
        Bukkit.broadcastMessage("Xray Locations: " + locations);
        return locations;
    }

    @Override
    public void onDequip(Player player, int level) {
        if(!EngineCooldown.inCooldown(player.getUniqueId(), CooldownReason.XRAY_ENCHANT)){
            hideAllCubes(player.getUniqueId());
            refreshableUsers.remove(player);
            equippedXrayUsers.remove(player.getUniqueId());
        }
    }

    public static void summonGlowingMagmaCube(UUID player, Location location, int entityId) {
        NPCRegistry registry = PrisonCore.getNonPersistNPCRegistry();
        NPC npc = registry.createNPC(EntityType.SHULKER, player + "_" + entityId);
        npc.setProtected(true);
        npc.spawn(location);
        npc.setAlwaysUseNameHologram(false);
        Entity entity = npc.getEntity();
        Shulker shulker = (Shulker) entity;
        shulker.setInvisible(true);
        shulker.addPotionEffect(PotionEffectType.GLOWING.createEffect(20*60, 2));
        entity.setGravity(false);
        entity.setCustomName("xray");
        entity.setCustomNameVisible(false);
        entity.setPersistent(false);
        entity.setSilent(true);
        Map<Location, UUID> actives = activeEntities.getOrDefault(player, new HashMap<>());
        actives.put(location.subtract(0.5, 0, 0.5), npc.getUniqueId());
        activeEntities.put(player, actives);
    }

    public static void removeAnyCubes(Player player, List<Location> possibleLocations) {
        UUID playerUUID = player.getUniqueId();
        Map<Location, UUID> magmaCubes = activeEntities.get(playerUUID);
        if(magmaCubes == null){
            Bukkit.broadcastMessage("Magmas null");
            return;
        }
        NPCRegistry registry = PrisonCore.getNonPersistNPCRegistry();
        List<Location> removables = new ArrayList<>();
        Bukkit.broadcastMessage("Magmas: " + magmaCubes.keySet());
        Bukkit.broadcastMessage("Affected: " + possibleLocations);
        for (Location location : magmaCubes.keySet()) {
            if(!possibleLocations.contains(location)) continue;
            UUID cubeUUID = magmaCubes.get(location);
            NPC npc = registry.getByUniqueId(cubeUUID);
            if(npc == null) continue;
            npc.despawn();
            npc.destroy();
            removables.add(location);
        }
        Map<Location, Integer> locations = activeLocations.get(playerUUID);
        removables.forEach(locations::remove);
        removables.forEach(magmaCubes::remove);
        if(removables.size() > 0 && locations.size() == 0){
            activeLocations.remove(playerUUID);
            refreshableUsers.add(player); //Spawn new cubes
        }else {
            activeLocations.put(playerUUID, locations);
        }
    }

    public static void hideAllCubes(UUID playerUUID) {
        Map<Location, UUID> magmaCubes = activeEntities.get(playerUUID);
        if(magmaCubes == null) return;
        NPCRegistry registry = PrisonCore.getNonPersistNPCRegistry();
        List<Location> removables = new ArrayList<>();
        for (Location location : magmaCubes.keySet()) {
            UUID cubeUUID = magmaCubes.get(location);
            NPC npc = registry.getByUniqueId(cubeUUID);
            if(npc == null) continue;
            npc.despawn();
            npc.destroy();
            removables.add(location);
        }
        removables.forEach(magmaCubes::remove);
    }

    public static boolean isXrayPlayer(UUID uuid){
        Map<Location, Integer> locations = activeLocations.get(uuid);
        Bukkit.broadcastMessage("UUID Player: " + locations);
        return locations != null;
    }

    public static BlockWrapper findRarestBlock(Map<BlockWrapper, Double> blockMap) {
        BlockWrapper rarestBlock = null;
        double smallestRarity = Double.MAX_VALUE;

        for (Map.Entry<BlockWrapper, Double> entry : blockMap.entrySet()) {
            BlockWrapper block = entry.getKey();
            double rarity = entry.getValue();

            if (rarity < smallestRarity) {
                smallestRarity = rarity;
                rarestBlock = block;
            }
        }

        return rarestBlock;
    }

    @Override
    public void onApply(ItemStack tool, int level) {

    }

    @Override
    public void onRemove(ItemStack tool) {

    }

    @Override
    public String getID() {
        return "NV";
    }

    @Override
    public String getDisplayName() {
        return EnchantConf.get().PickaxeXrayEnchantDisplayName;
    }

    @Override
    public int getMaxLevel() {
        return 10;
    }

    @Override
    public int getMinLevel() {
        return 1;
    }

    @Override
    public Collection<String> getAllowedToolIDs() {
        return null;
    }

    @Override
    public int getEnchantGUISlot() {
        return EnchantConf.get().PickaxeXrayEnchantGUISlot;
    }

    @Override
    public List<String> getUnformattedGUILore() {
        return EnchantConf.get().PickaxeXrayEnchantGUILore;
    }

    @Override
    public String getMagnitudeString(int level) {
        return String.valueOf(level);
    }

    @Override
    public String getName() {
        return EnchantConf.get().pickaxeVisionEnchantName;
    }

    @Override
    public String getLore() {
        return EnchantConf.get().PickaxeXrayEnchantLore;
    }

    @Override
    public Material getIcon() {
        return EnchantConf.get().PickaxeVisionEnchantIcon;
    }

    @Override
    public Recipe getEnchantBookRecipe() {
        return Recipe.ENCHANT_BOOK_XRAY;
    }


    @Override
    public List<Cost> getCraftCosts() {
        return MUtil.list(
                new CostCurrency(CurrencyType.CASH, 1000),
                new CostSkillLevel(SkillType.ENCHANTING, 2),
                new CostSkillLevel(SkillType.CRAFTING, 2)
        );
    }

    @Override
    public List<Cost> getApplyCosts() {
        return MUtil.list(
                new CostCurrency(CurrencyType.CASH, 500),
                new CostSkillLevel(SkillType.ENCHANTING, 2)
        );
    }

}
