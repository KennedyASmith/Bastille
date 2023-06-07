package com.kennedysmithjava.prisoncore.entity.mines.objects;

import com.kennedysmithjava.prisoncore.blockhandler.BlockWrapper;
import com.kennedysmithjava.prisoncore.util.regions.Offset;
import org.bukkit.Material;

import java.util.List;
import java.util.Map;

public class Floor {

    String displayName;
    Material icon;
    int materialData;
    Map<Integer, String> schematics;
    List<String> lore;
    List<Integer> compatibleWalls;
    String directory;

    Offset spawn;
    Offset mineCenter;
    Offset architectNPC;
    Offset researcherNPC;
    Offset collectorNPC;
    Offset enchantTable;
    Offset beacon;
    Offset chest;
    Offset anvil;
    Offset portalMax;
    Offset portalMin;

    public Floor(String displayName, Material icon, int materialData, Map<Integer, String> schematics, List<String> lore, List<Integer> compatibleWalls, String directory, Offset spawn, Offset mineCenter, Offset architectNPC, Offset researcherNPC, Offset collectorNPC, Offset enchantTable, Offset beacon, Offset chest, Offset anvil, Offset portalMax, Offset portalMin) {
        this.displayName = displayName;
        this.icon = icon;
        this.materialData = materialData;
        this.schematics = schematics;
        this.lore = lore;
        this.compatibleWalls = compatibleWalls;
        this.directory = directory;
        this.spawn = spawn;
        this.mineCenter = mineCenter;
        this.architectNPC = architectNPC;
        this.researcherNPC = researcherNPC;
        this.collectorNPC = collectorNPC;
        this.enchantTable = enchantTable;
        this.beacon = beacon;
        this.chest = chest;
        this.anvil = anvil;
        this.portalMax = portalMax;
        this.portalMin = portalMin;
    }

    public Material getIcon() {
        return icon;
    }

    public int getMaterialData() {
        return materialData;
    }

    public Map<Integer, String> getSchematics() {
        return schematics;
    }

    public String getSchematic(int width) {
        return getDirectory() + getSchematicPathname(width);
    }

    public String getSchematicPathname(int width) {
        return getSchematics().get(width);
    }

    public Offset getArchitectNPC() {
        return architectNPC;
    }

    public Offset getMineCenter() {
        return mineCenter;
    }

    public Offset getResearcherNPC() {
        return researcherNPC;
    }

    public Offset getCollectorNPC() {
        return collectorNPC;
    }

    public Offset getSpawn() {
        return spawn;
    }

    @SuppressWarnings("unused")
    public String getDisplayName() {
        return displayName;
    }

    public List<String> getLore() {
        return lore;
    }

    @SuppressWarnings("unused")
    public List<Integer> getCompatibleWalls() {
        return compatibleWalls;
    }

    public String getDirectory() {
        return directory;
    }


    public Offset getBeacon() {
        return beacon;
    }

    public Offset getChest() {
        return chest;
    }

    public Offset getEnchantTable() {
        return enchantTable;
    }

    public Offset getPortalMax() {
        return portalMax;
    }

    public Offset getPortalMin() {
        return portalMin;
    }

    public Offset getAnvil() {
        return anvil;
    }

    public BlockWrapper getEnchantTableBlock(){
        return new BlockWrapper(Material.ENCHANTING_TABLE);
    }
    public BlockWrapper getBeaconBlock(){
        return new BlockWrapper(Material.BEACON);
    }
    public BlockWrapper getChestBlock(){
        return new BlockWrapper(Material.CHEST);
    }
    public BlockWrapper getAnvilBlock(){
        return new BlockWrapper(Material.ANVIL);
    }

    @Override
    public String toString() {
        return "Floor{" +
                "displayName='" + displayName + '\'' +
                ", icon=" + icon +
                ", materialData=" + materialData +
                ", schematics=" + schematics +
                ", lore=" + lore +
                ", compatibleWalls=" + compatibleWalls +
                ", directory='" + directory + '\'' +
                ", spawn=" + spawn +
                ", mineCenter=" + mineCenter +
                ", architectNPC=" + architectNPC +
                ", researcherNPC=" + researcherNPC +
                ", collectorNPC=" + collectorNPC +
                ", enchantTable=" + enchantTable +
                ", beacon=" + beacon +
                ", chest=" + chest +
                ", anvil=" + anvil +
                ", portalMax=" + portalMax +
                ", portalMin=" + portalMin +
                '}';
    }
}
