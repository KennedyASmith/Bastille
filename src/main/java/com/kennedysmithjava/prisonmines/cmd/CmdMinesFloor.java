package com.kennedysmithjava.prisonmines.cmd;

import com.boydti.fawe.object.schematic.Schematic;
import com.kennedysmithjava.prisonmines.PrisonMines;
import com.kennedysmithjava.prisonmines.entity.Floor;
import com.kennedysmithjava.prisonmines.entity.LayoutConf;
import com.kennedysmithjava.prisonmines.util.Offset;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.type.primitive.TypeInteger;
import com.massivecraft.massivecore.util.MUtil;
import com.sk89q.worldedit.EmptyClipboardException;
import com.sk89q.worldedit.IncompleteRegionException;
import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.bukkit.BukkitUtil;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.bukkit.adapter.BukkitImplAdapter;
import com.sk89q.worldedit.bukkit.selections.Selection;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.regions.Region;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

public class CmdMinesFloor extends MineCommand {
    // -------------------------------------------- //
    // CONSTRUCT
    // -------------------------------------------- //

    public static Collection<String> signTypes = MUtil.list("mine", "spawn", "architect", "researcher", "collector", "portal1", "portal2", "enchant", "beacon");

    // -------------------------------------------- //
    // OVERRIDE
    // -------------------------------------------- //

    public CmdMinesFloor() {
        // Aliases
        this.addAliases("floor", "f");

        // Parameters
        this.addParameter(TypeInteger.get(), "id");
        this.addParameter(TypeInteger.get(), "width");

    }

    @Override
    public void perform() throws MassiveException {

        if (!(sender instanceof Player)) return;
        Player player = (Player) sender;

        WorldEditPlugin worldEdit = (WorldEditPlugin) Bukkit.getServer().getPluginManager().getPlugin("WorldEdit");
        /*Selection selection = worldEdit.getSelection(player);

        if (selection == null) {
            return;
        }*/
/*

        int id = readArg();
        int width = readArg();

        Region region;

        try {
            LocalSession session = worldEdit.getSession(player);
            region = session.getWorldSelection(BukkitUtil.getLocalWorld(player.getWorld()));
        } catch (IncompleteRegionException e) {
            msg("You cannot create a mine floor without a selection. Use //wand to select a region.");
            return;
        }

        Vector minPoint = region.getMinimumPoint();
        Vector maxPoint = region.getMaximumPoint();

        Location min = new Location(player.getWorld(), minPoint.getX(), minPoint.getY(), minPoint.getZ());
        Location max = new Location(player.getWorld(), maxPoint.getX(), maxPoint.getY(), maxPoint.getZ());

        Location origin = min.clone();

        Map<String, Location> locationMap = getLocationMap(min, max);

        AtomicReference<Offset> spawn = new AtomicReference<>();
        AtomicReference<Offset> mine = new AtomicReference<>();
        AtomicReference<Offset> architect = new AtomicReference<>();
        AtomicReference<Offset> researcher = new AtomicReference<>();
        AtomicReference<Offset> collector = new AtomicReference<>();
        AtomicReference<Offset> portalOne = new AtomicReference<>();
        AtomicReference<Offset> portalTwo = new AtomicReference<>();
        AtomicReference<Offset> enchantTable = new AtomicReference<>();
        AtomicReference<Offset> beacon = new AtomicReference<>();

        locationMap.forEach((s, location) -> {
            switch (s) {
                case "mine":
                    mine.set(getOffset(location, origin));
                    break;
                case "spawn":
                    spawn.set(getOffset(getRotatedLocation(location), origin));
                    break;
                case "portal1":
                    portalOne.set(getOffset(location, origin));
                    break;
                case "portal2":
                    portalTwo.set(getOffset(location, origin));
                    break;
                case "enchant":
                    enchantTable.set(getOffset(location, origin));
                    break;
                case "beacon":
                    beacon.set(getOffset(location, origin));
                    break;
                case "architect":
                    architect.set(getOffset(getRotatedLocation(location), origin));
                    break;
                case "researcher":
                    researcher.set(getOffset(getRotatedLocation(location), origin));
                    break;
                case "collector":
                    collector.set(getOffset(getRotatedLocation(location), origin));
            }
        });

        Map<Integer, Floor> floors = LayoutConf.get().getFloors();

        if(!floors.containsKey(id)){
            LayoutConf.get().setFloor(id, new Floor(
                    "&7&lMine_" + id,
                    MUtil.list("&7Common", "&6", "&7A mine layout.", "&m", "Wall Options:", "%compatible_walls%"),
                    MUtil.list(1),
                    Material.WOOD, 0,
                    "Schematics/Mine_" + id + "/",
                    MUtil.map(
                            width, "Mine_" + id + "_" + width + ".schematic"),
                    spawn.get(),
                    mine.get(),
                    architect.get(),
                    researcher.get(),
                    collector.get()
            ));
        }else{
            Floor floor = floors.get(id).clone();
            Map<Integer, String> fs = floor.getSchematics();
            fs.put(width, "Mine_" + id + "_" + width + ".schematic");
            LayoutConf.get().setFloor(id, floor);
        }

        List<SignBlock> signs = new ArrayList<>();

        locationMap.forEach((s, location) -> {
            Block block = location.getBlock();
            Sign sign = (Sign) block.getState();
            String type = sign.getLine(0).toLowerCase();
            signs.add(new SignBlock(block.getData(), block.getLocation(), type));
            block.setType(Material.AIR, false);
        });

        Floor finalFloor = LayoutConf.get().getPath(id);
        saveSchematic(finalFloor.getDirectory(), finalFloor.getSchematicPathname(width), region);
        msg(ChatColor.translateAlternateColorCodes('&', "&7[&f&lMCRivals&7] Schematic saved under " + finalFloor.getDirectory() + "/" + finalFloor.getSchematicPathname(width)));

        signs.forEach(signBlock -> {
            Location location = signBlock.getLocation();
            Block block = location.getBlock();
            block.setType(Material.SIGN_POST, false);
            block.setData(signBlock.getData(), false);
            Sign sign = (Sign) block.getState();
            sign.setLine(0, signBlock.getLine());
            sign.update();
        });*/
    }

    public Map<String, Location> getLocationMap(Location min, Location max) {
        Map<String, Location> locationMap = new HashMap<>();

        for (int x = min.getBlockX(); x <= max.getBlockX(); x++) {
            for (int y = min.getBlockY(); y <= max.getBlockY(); y++) {
                for (int z = min.getBlockZ(); z <= max.getBlockZ(); z++) {
                    Block block = min.getWorld().getBlockAt(x, y, z);
                    Material material = block.getType();
                    if(!material.equals(Material.SIGN_POST)) continue;
                    if (!(block.getState() instanceof Sign)) continue;
                    Sign sign = (Sign) block.getState();
                    String type = sign.getLine(0).toLowerCase();
                    if (!signTypes.contains(type)) continue;
                    locationMap.put(type, block.getLocation());
                }
            }
        }

        return locationMap;
    }

    public Location getRotatedLocation(Location location){
        Block block = location.getWorld().getBlockAt(location);
        byte data = block.getData();
        float yaw = getYawFromSignData(data);
        return new Location(location.getWorld(), location.getBlockX(), location.getBlockY(), location.getBlockZ(), yaw, 0F);
    }

    public float getYawFromSignData(byte data){

        float yaw = 0;

        switch(data){
            case 4:
            case 5:
            case 6:
            case 7:
                yaw = 90F;
                break;
            case 8:
            case 9:
            case 10:
            case 11:
                yaw = 180F;
                break;
            case 12:
            case 13:
            case 14:
            case 15:
                yaw = -90F;
                break;
        }

        return yaw;
    }

    public Offset getOffset(Location loc, Location origin){

        int xOffset = loc.getBlockX() - origin.getBlockX();
        int yOffset = loc.getBlockY() - origin.getBlockY();
        int zOffset = loc.getBlockZ() - origin.getBlockZ();
        float yaw = loc.getYaw();
        float pitch = loc. getPitch();

        return new Offset(xOffset, yOffset, zOffset, pitch, yaw);
    }

    public void saveSchematic(String directory, String name, Region region){
        try {
            File directories = new File(PrisonMines.get().getDataFolder() + "/" + directory);
            directories.mkdirs();

            File file = new File(directories + "/" +  name);
            file.createNewFile();

            Schematic schem = new Schematic(region);
            schem.save(file, ClipboardFormat.SCHEMATIC);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

class SignBlock{

    byte data;
    Location location;
    String line;

    SignBlock(byte data, Location location, String line){
        this.data = data;
        this.location = location;
        this.line = line;
    }

    public byte getData() {
        return data;
    }

    public Location getLocation() {
        return location;
    }

    public String getLine() {
        return line;
    }
}


