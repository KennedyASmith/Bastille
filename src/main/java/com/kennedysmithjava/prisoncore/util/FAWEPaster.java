package com.kennedysmithjava.prisoncore.util;

import com.fastasyncworldedit.core.FaweAPI;
import com.kennedysmithjava.prisoncore.PrisonCore;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.math.BlockVector3;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;

import java.io.File;
import java.io.IOException;

public class FAWEPaster {
    public static void paste(String filepath, String world, BlockVector3 to, boolean destroy, Runnable whenDone) {
            Runnable r = () -> {
                try {
                    try (EditSession editSession = WorldEdit
                            .getInstance()
                            .newEditSessionBuilder()
                            .world(FaweAPI.getWorld(world))
                            .checkMemory(false)
                            .fastMode(true)
                            .limitUnlimited()
                            .changeSetNull()
                            .build()) {
                        File file = new File(PrisonCore.get().getDataFolder() + File.separator + filepath);
                        Clipboard clipboard = FaweAPI.load(file);
                        if(destroy){
                            World w = Bukkit.getWorld(world);
                            BlockVector3 minimumPoint = clipboard.getMinimumPoint();
                            BlockVector3 maximumPoint = clipboard.getMaximumPoint();
                            for (int x = minimumPoint.getBlockX() - 1; x <= maximumPoint.getBlockX(); x++) {
                                for (int y = minimumPoint.getBlockY() - 1; y <= maximumPoint.getBlockY(); y++) {
                                    for (int z = minimumPoint.getBlockZ() - 1; z <= maximumPoint.getBlockZ(); z++) {
                                        if(!BukkitAdapter.adapt(clipboard.getBlock(BlockVector3.at(x, y, z)).getBlockType()).equals(Material.AIR)){
                                            w.getBlockAt(to.getBlockX() + x, to.getBlockX()+ y,to.getBlockX() + z).setType(Material.AIR);
                                            Bukkit.broadcastMessage("Breaking block at X: " + (to.getBlockX() + x) + " Y: " + (to.getBlockY() + y) + " Z: " + (to.getBlockZ() + z));
                                        }
                                    }
                                }
                            }
                        }else{
                            clipboard.paste(editSession, to, false, false, true);
                        }
                        com.fastasyncworldedit.core.util.TaskManager.taskManager().taskNow(whenDone, false);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            };
            com.fastasyncworldedit.core.util.TaskManager.taskManager().taskNow(r, false);
        }

}
