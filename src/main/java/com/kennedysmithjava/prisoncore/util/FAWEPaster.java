package com.kennedysmithjava.prisoncore.util;

import com.fastasyncworldedit.core.FaweAPI;
import com.kennedysmithjava.prisoncore.PrisonCore;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.session.ClipboardHolder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;

import java.io.File;
import java.io.FileInputStream;

public class FAWEPaster {
    public static void paste(String filepath, String world, BlockVector3 to, boolean destroy, Runnable whenDone) {
        try {
            File file = new File(PrisonCore.get().getDataFolder() + File.separator + filepath);

            ClipboardFormat format = ClipboardFormats.findByFile(file);
            ClipboardReader reader = format.getReader(new FileInputStream(file));
            Clipboard clipboard = reader.read();

            if (destroy) {
                World w = Bukkit.getWorld(world);
                BlockVector3 cbOrigin = clipboard.getOrigin();
                BlockVector3 minimumPoint = clipboard.getMinimumPoint();
                BlockVector3 maximumPoint = clipboard.getMaximumPoint();
                for (int x = minimumPoint.getBlockX(); x <= maximumPoint.getBlockX(); x++) {
                    for (int y = minimumPoint.getBlockY(); y <= maximumPoint.getBlockY(); y++) {
                        for (int z = minimumPoint.getBlockZ(); z <= maximumPoint.getBlockZ(); z++) {
                            if (!BukkitAdapter.adapt(clipboard.getBlock(BlockVector3.at(x, y, z)).getBlockType()).equals(Material.AIR)) {
                                int xDist = x - cbOrigin.getBlockX();
                                int yDist = y - cbOrigin.getBlockY();
                                int zDist = z - cbOrigin.getBlockZ();
                                w.getBlockAt(to.getBlockX() + xDist, to.getBlockY() + yDist, to.getBlockZ() + zDist).setType(Material.AIR);
                            }
                        }
                    }
                }
            } else {
                try (EditSession es = WorldEdit.getInstance().newEditSession(FaweAPI.getWorld(world))) {
                    Operation operation = new ClipboardHolder(clipboard)
                            .createPaste(es)
                            .to(to)
                            .ignoreAirBlocks(true)
                            .build();
                    Operations.complete(operation);
                }
            }
            com.fastasyncworldedit.core.util.TaskManager.taskManager().taskNow(whenDone, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
