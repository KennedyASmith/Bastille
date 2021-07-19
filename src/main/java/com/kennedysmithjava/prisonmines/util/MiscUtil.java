package com.kennedysmithjava.prisonmines.util;

import com.boydti.fawe.FaweAPI;
import com.boydti.fawe.bukkit.v0.FaweAdapter_All;
import com.kennedysmithjava.prisonmines.MinesWorldManager;
import com.kennedysmithjava.prisonmines.PrisonMines;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.blocks.BaseBlock;
import com.sk89q.worldedit.extent.clipboard.ClipboardFormats;
import org.bukkit.*;
import org.bukkit.block.Block;

import java.io.File;
import java.util.Arrays;
import java.util.HashSet;

public class MiscUtil {

    public static HashSet<String> substanceChars = new HashSet<>(Arrays.asList(new String[]{
            "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F", "G", "H",
            "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z",
            "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r",
            "s", "t", "u", "v", "w", "x", "y", "z"
    }));

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
        Bukkit.broadcastMessage("Successful fill!");
    }


    public static FAWETracker pasteSchematic(String filepath, Vector location) {
        MinesWorldManager worldManager = MinesWorldManager.get();

        FAWETracker tracker = new FAWETracker();

        try {
            File file = new File(PrisonMines.get().getDataFolder() + File.separator + filepath);
            EditSession es = ClipboardFormats.findByFile(file).load(file).paste(FaweAPI.getWorld(worldManager.getWorld().getName()), location, false, false, null);
            es.setFastMode(true);
            es.getQueue().setProgressTask(tracker);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return tracker;
    }

}
