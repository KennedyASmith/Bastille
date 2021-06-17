package com.kennedysmithjava.prisonmines.entity;

import com.kennedysmithjava.prisonmines.PrisonMines;
import com.kennedysmithjava.prisonmines.util.Offset;
import com.massivecraft.massivecore.command.editor.annotation.EditorName;
import com.massivecraft.massivecore.store.Entity;
import com.massivecraft.massivecore.util.MUtil;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.Map;
import java.util.Set;

@EditorName("config")
public class LayoutConf extends Entity<LayoutConf> {
    // -------------------------------------------- //
    // META
    // -------------------------------------------- //

    protected static transient LayoutConf i;


    // -------------------------------------------- //
    // STORED DATA
    // -------------------------------------------- //

    public Integer defaultWallSchem = 1;
    public Integer defaultPathSchem = 1;

    // ------------------------------------------------------------------------- //
    //  WALLS
    //
    //  Wall schematics only change if the player chooses to change them.
    //  The integer in the map below is only used for referencing.
    // ------------------------------------------------------------------------ //

    private Map<Integer, Wall> walls = MUtil.map(
            1, new Wall("Schematics/Default/Walls/Default.schematic", "&7Default Wall",
                    MUtil.list("&7Common", "&6", "The default wall design.", "&m", "Spruce Variant"),
                    Material.SAPLING, 0
                    )
    );

    // ------------------------------------------------------------------------- //
    //  PATHS
    //
    //  Path schematics change depending on the player's horizontal mine size.
    //
    //  The default path included below contains 3 separate schematics:
    //  The schematic "path1" is active when the player's mine size is 1 (default).
    //  The schematic "path2" is active when the player's mine size is 10.
    //  The schematic "path3" is active when the player's mine size is 30.
    // ------------------------------------------------------------------------ //

    private Map<Integer, Floor> floors = MUtil.map(
            1, new Floor(
                    "&7&lDefault Mine",
                    MUtil.list("&7Common", "&6", "&7The default mine layout.", "&m", "Wall Options:", "%compatible_walls%"),
                    MUtil.list(1),
                    Material.WOOD, 0,
                    "Schematics/Default/",
                    MUtil.map(
                                        1, "Default_1.schematic",
                                    10, "path2.schematic",
                                            30, "path3.schematic"),
                    new Offset(28, 50, 50, 0F, -90F),
                    new Offset(50, 49, 50),
                    new Offset(50, 50, 50, 0F, -90F),
                    new Offset(28, 50, 50, 0F, -90F)
            )
    );

    public static LayoutConf get() {
        return i;
    }

    @Override
    public LayoutConf load(LayoutConf that) {

        this.setFloors(that.floors);
        this.setWalls(that.walls);

        super.load(that);
        return this;
    }


    // -------------------------------------------- //
    //                PATH METHODS
    // -------------------------------------------- //

    public Floor getPath(int id){
        return floors.get(id);
    }

    public String getPathSchematic(int id, int level) {
        return floors.get(id).getSchematic(level);
    }

    public ItemStack getPathIcon(int id) {
        Floor b = floors.get(id);
        return new ItemStack(b.getIcon(), b.getMaterialData());
    }

    public Set<Integer> getPathIDs() {
        return floors.keySet();
    }

    public boolean pathSchematicExists(int id, int level) {
        File file = new File(PrisonMines.get().getDataFolder() + File.separator + floors.get(id).getSchematic(level));
        return file.exists();
    }

    public String getDefaultPathSchem() {
        return getPathSchematic(defaultPathSchem, 1);
    }

    public Map<Integer, Floor> getFloors() {
        return floors;
    }

    public void setFloors(Map<Integer, Floor> floors) {
        this.floors = floors;
        this.changed();
    }

    public void setFloor(Integer id, Floor floor){
        floors.put(id, floor);
        this.changed();
    }

    // -------------------------------------------- //
    //                WALL METHODS
    // -------------------------------------------- //

    public String getWallSchematic(int id) {
        return walls.get(id).getSchematic();
    }

    public Wall getWall(int id){
        return walls.get(id);
    }

    public ItemStack getWallIcon(int id) {
        Wall b = walls.get(id);
        return new ItemStack(b.getIcon(), b.getData());
    }

    public Set<Integer> getWallIDs() {
        return walls.keySet();
    }

    public boolean wallSchematicExists(int id) {
        File file = new File(PrisonMines.get().getDataFolder() + File.separator + walls.get(id).getSchematic());
        return file.exists();
    }

    public String getDefaultWallSchem() {
        return getWallSchematic(defaultWallSchem);
    }

    public Map<Integer, Wall> getWalls() {
        return walls;
    }

    public void setWall(Integer id, Wall wall){
        walls.put(id, wall);
        this.changed();
    }

    public void setWalls(Map<Integer, Wall> walls) {
        this.walls = walls;
    }
}

