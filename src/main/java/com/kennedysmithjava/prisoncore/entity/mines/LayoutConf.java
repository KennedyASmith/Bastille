package com.kennedysmithjava.prisoncore.entity.mines;

import com.kennedysmithjava.prisoncore.PrisonCore;
import com.kennedysmithjava.prisoncore.entity.mines.objects.Floor;
import com.kennedysmithjava.prisoncore.entity.mines.objects.Wall;
import com.kennedysmithjava.prisoncore.util.regions.Offset;
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
    // INSTANCE & CONSTRUCT
    // -------------------------------------------- //

    protected static transient LayoutConf i;

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

    // ------------------------------------------------------------------------- //
    //  WALLS
    //
    //  Wall schematics only change if the player chooses to change them.
    //  The integer in the map below is only used for referencing.
    // ------------------------------------------------------------------------ //

    private Map<Integer, Wall> walls = MUtil.map(
               1, new Wall("Schematics/Default/Walls/Default.schematic", "&7Default Wall",
                    MUtil.list("&7Common", "&6", "The default wall design.", "&m", "Stone Variant"),
                    Material.STONE_BRICKS
                    ),
            2, new Wall("Schematics/Default/Walls/Default.schematic", "&7Default Wall",
                    MUtil.list("&7Common", "&6", "A spinoff of the default wall design.", "&m", "Stone Variant"),
                    Material.STONE_BRICKS
            ),
                    3, new Wall("Schematics/Default/Walls/Wall_Brown.schematic", "&7Wooden Wall",
                    MUtil.list("&7Common", "&6", "A massive wooden wall.", "&m", "Acacia Variant"),
                    Material.ACACIA_LOG
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
                    Material.GRASS,0,
                    MUtil.map(
                        3, "default_mine_1.schem",
                        4, "default_mine_2.schem",
                                5, "default_mine_3.schem",
                                6, "default_mine_4.schem",
                                7, "default_mine_5.schem",
                                8, "default_mine_6.schem",
                                9, "default_mine_7.schem"
                    ),
                    MUtil.list("&7Common", "&6", "&7The default mine layout.", "&m", "Wall Options:", "%compatible_walls%"),
                    MUtil.list(1, 2, 3),
                    "Schematics/Default/",
                    new Offset(-51, 50, -75, 0F, 0F),
                    new Offset(-51, 48, -50),
                    new Offset(-47, 51, -26, 0F, 180F),
                    new Offset(-51, 51, -26, 0F, 180F),
                    new Offset(-55, 51, -26, 0F, 180F),
                    new Offset(-27, 50, -50, 0F, 90F),
                    new Offset(-76, 51, -50, 0F, 270F),
                    new Offset(-45, 50, -31, 0F, 180F),
                    new Offset(-59, 50, -33),
                    new Offset(-59, 50, -34),
                    new Offset(-52, 54, -80, 0F, 0F),
                    new Offset(-50, 49, -80, 0F, 0F)
            )
    );

    // -------------------------------------------- //
    //                PATH METHODS
    // -------------------------------------------- //

    public Floor getPath(int id){
        return floors.get(id);
    }

    public String getPathSchematic(int id, int width) {
        return floors.get(id).getSchematic(width);
    }

    public ItemStack getPathIcon(int id) {
        Floor b = floors.get(id);
        return new ItemStack(b.getIcon(), b.getMaterialData());
    }

    public Set<Integer> getPathIDs() {
        return floors.keySet();
    }

    public boolean pathSchematicExists(int id, int width) {
        File file = new File(PrisonCore.get().getDataFolder() + File.separator + floors.get(id).getSchematic(width));
        return file.exists();
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
        return new ItemStack(b.getIcon());
    }

    public Set<Integer> getWallIDs() {
        return walls.keySet();
    }

    public boolean wallSchematicExists(int id) {
        File file = new File(PrisonCore.get().getDataFolder() + File.separator + walls.get(id).getSchematic());
        return file.exists();
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

