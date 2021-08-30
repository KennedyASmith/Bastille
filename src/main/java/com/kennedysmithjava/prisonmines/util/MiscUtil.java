package com.kennedysmithjava.prisonmines.util;

import com.boydti.fawe.FaweAPI;
import com.boydti.fawe.bukkit.v0.FaweAdapter_All;
import com.boydti.fawe.object.schematic.Schematic;
import com.kennedysmithjava.prisonmines.MinesWorldManager;
import com.kennedysmithjava.prisonmines.PrisonMines;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.blocks.BaseBlock;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.ClipboardFormats;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.util.Direction;
import org.bukkit.*;
import org.bukkit.block.Block;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

public class MiscUtil {


    public static BlockMaterial westLadder = new BlockMaterial(Material.LADDER, 5);
    public static BlockMaterial eastLadder = new BlockMaterial(Material.LADDER, 4);
    public static BlockMaterial northLadder = new BlockMaterial(Material.LADDER, 3);
    public static BlockMaterial southLadder = new BlockMaterial(Material.LADDER, 2);


    public static HashSet<String> substanceChars = new HashSet<>(Arrays.asList(new String[]{
            "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F", "G", "H",
            "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z",
            "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r",
            "s", "t", "u", "v", "w", "x", "y", "z"
    }));


    //SORTS DESC
    public static <X extends Comparable<X>>  Map<X, Integer> sortByValue(Map<X, Integer> unsortMap) {
        List<Map.Entry<X, Integer>> list = new LinkedList<>(unsortMap.entrySet());

        // Sorting the list based on values
        list.sort((o1, o2) -> o2.getValue().compareTo(o1.getValue()) == 0
        ? o2.getKey().compareTo(o1.getKey())
        : o2.getValue().compareTo(o1.getValue()));
        return list.stream().collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (a, b) -> b, LinkedHashMap::new));

    }

    public static BlockMaterial getLadder(Direction direction){
        switch (direction){
            case SOUTH:
                return southLadder;
            case EAST:
                return eastLadder;
            case WEST:
                return westLadder;
            default:
                return northLadder;
        }
    }

    public static String getComparisonString(String str)
    {
        String ret = "";

        str = ChatColor.stripColor(str);
        str = str.toLowerCase();

        for (char c : str.toCharArray())
        {
            if (substanceChars.contains(String.valueOf(c)))
            {
                ret += c;
            }
        }
        return ret.toLowerCase();
    }

    public static void blockFill(Location min, Location max, BlockMaterial material){

        int minX = Math.min(min.getBlockX(), max.getBlockX());
        int minY = Math.min(min.getBlockY(), max.getBlockY());
        int minZ = Math.min(min.getBlockZ(), max.getBlockZ());

        int maxX = Math.max(min.getBlockX(), max.getBlockX());
        int maxY = Math.max(min.getBlockY(), max.getBlockY());
        int maxZ = Math.max(min.getBlockZ(), max.getBlockZ());

        World world = min.getWorld();
        for (int x = minX; x <= maxX; x++){
            for (int z = minZ; z <= maxZ; z++){
                for (int y = minY; y <= maxY; y++){
                    Block b = world.getBlockAt(x, y, z);
                    b.setType(material.getMaterial(), false);
                    b.setData(material.getData(), false);
                }
            }
        }
    }

    public static FAWETracker pasteSchematic(String filepath, Vector location){
        return pasteSchematic(filepath, location, false);
    }

    public static FAWETracker pasteSchematic(String filepath, Vector location, boolean destroy) {
        MinesWorldManager worldManager = MinesWorldManager.get();

        FAWETracker tracker = new FAWETracker();

        try {
            File file = new File(PrisonMines.get().getDataFolder() + File.separator + filepath);
            Schematic schematic = ClipboardFormats.findByFile(file).load(file);
            Clipboard clipboard = schematic.getClipboard();

            if(destroy){
                FaweAdapter_All adapter = new FaweAdapter_All();
                Vector minimumPoint = clipboard.getMinimumPoint();
                Vector maximumPoint = clipboard.getMaximumPoint();

                for (int x = minimumPoint.getBlockX() - 1; x <= maximumPoint.getBlockX(); x++) {
                    for (int y = minimumPoint.getBlockY() - 1; y <= maximumPoint.getBlockY(); y++) {
                        for (int z = minimumPoint.getBlockZ() - 1; z <= maximumPoint.getBlockZ(); z++) {

                            if(!adapter.getMaterial(clipboard.getBlock(new Vector(x, y, z)).getType()).equals(Material.AIR)){
                                BaseBlock baseBlock = new BaseBlock(adapter.getBlockId(Material.PISTON_MOVING_PIECE), 0);
                                clipboard.setBlock(x, y, z, baseBlock);
                            }
                        }
                    }
                }

            }

            EditSession es = schematic.paste(FaweAPI.getWorld(worldManager.getWorld().getName()), location, false, false, null);

            es.setFastMode(true);
            es.getQueue().setProgressTask(tracker);
            } catch (Throwable e) {
            e.printStackTrace();
        }

        return tracker;
    }





}

