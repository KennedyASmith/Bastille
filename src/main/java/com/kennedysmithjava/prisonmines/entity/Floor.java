package com.kennedysmithjava.prisonmines.entity;

import com.kennedysmithjava.prisonmines.util.Offset;
import org.bukkit.Material;

import java.util.List;
import java.util.Map;

public class Floor {

    String displayName;
    Material icon;
    int materialData;
    Map<Integer, String> schematics;
    Offset spawn;
    Offset mineCenter;
    Offset architectNPC;
    Offset researcherNPC;
    Offset collectorNPC;
    List<String> lore;
    List<Integer> compatibleWalls;
    String directory;

    public Floor(String displayName, List<String> lore, List<Integer> compatibleWalls, Material icon, int materialData, String directory, Map<Integer, String> schematics, Offset spawn, Offset mineCenter, Offset architectNPC, Offset researcherNPC, Offset collectorNPC) {
        this.displayName = displayName;
        this.icon = icon;
        this.materialData = materialData;
        this.schematics = schematics;
        this.spawn = spawn;
        this.mineCenter = mineCenter;
        this.architectNPC = architectNPC;
        this.researcherNPC = researcherNPC;
        this.collectorNPC = collectorNPC;
        this.lore = lore;
        this.compatibleWalls = compatibleWalls;
        this.directory = directory;
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

    public String getDisplayName() {
        return displayName;
    }

    public List<String> getLore() {
        return lore;
    }

    public List<Integer> getCompatibleWalls() {
        return compatibleWalls;
    }

    public String getDirectory() {
        return directory;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void setArchitectNPC(Offset architectNPC) {
        this.architectNPC = architectNPC;
    }

    public void setIcon(Material icon) {
        this.icon = icon;
    }

    public void setMaterialData(int materialData) {
        this.materialData = materialData;
    }

    public void setMineCenter(Offset mineCenter) {
        this.mineCenter = mineCenter;
    }

    public void setResearcherNPC(Offset researcherNPC) {
        this.researcherNPC = researcherNPC;
    }

    public void setSchematics(Map<Integer, String> schematics) {
        this.schematics = schematics;
    }

    public void setSpawn(Offset spawn) {
        this.spawn = spawn;
    }

    public void setDirectory(String directory) {
        this.directory = directory;
    }

    public Offset getCollectorNPC() {
        return collectorNPC;
    }

    public Floor clone(){
        return new Floor(getDisplayName(), getLore(), getCompatibleWalls(), getIcon(), getMaterialData(), getDirectory(), getSchematics(), getSpawn() ,getMineCenter() ,getArchitectNPC(), getResearcherNPC(), getCollectorNPC());
    }



    @Override
    public String toString() {
        return "Floor{" +
                "displayName='" + displayName + '\'' +
                ", icon=" + icon +
                ", materialData=" + materialData +
                ", schematics=" + schematics +
                ", spawn=" + spawn +
                ", mineCenter=" + mineCenter +
                ", architectNPC=" + architectNPC +
                ", researcherNPC=" + researcherNPC +
                ", lore=" + lore +
                ", compatibleWalls=" + compatibleWalls +
                ", directory='" + directory + '\'' +
                '}';
    }
}
