package com.kennedysmithjava.prisoncore.util;

import com.fastasyncworldedit.core.Fawe;
import com.fastasyncworldedit.core.FaweAPI;
import com.fastasyncworldedit.core.util.task.RunnableVal;
import com.kennedysmithjava.prisoncore.PrisonCore;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.world.block.BaseBlock;
import org.bukkit.Material;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.IOException;

public class FAWEPaster {
    public static void paste(String filepath, String world, BlockVector3 to, boolean destroy, RunnableVal<Boolean> whenDone) {
            Runnable r = () -> {
                if (whenDone != null) {
                    whenDone.value = false;
                }
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
                        FaweAPI.getWorld(world);
                        File file = new File(PrisonCore.get().getDataFolder() + File.separator + filepath);
                        Clipboard clipboard = FaweAPI.load(file);
                        if(destroy) destroyClipboard(clipboard);
                        clipboard.paste(editSession, to, false, false, true);
                        if (whenDone != null) {
                            whenDone.value = true;
                            if (whenDone != null) {
                                new BukkitRunnable() {
                                    @Override
                                    public void run() {
                                        whenDone.run();
                                    }
                                }.runTask(PrisonCore.get());
                            }
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            };

            if (Fawe.isMainThread()) {
                com.fastasyncworldedit.core.util.TaskManager.taskManager().async(r);
            } else {
                r.run();
            }
        }

    private static void destroyClipboard(Clipboard clipboard){
        BlockVector3 minimumPoint = clipboard.getMinimumPoint();
        BlockVector3 maximumPoint = clipboard.getMaximumPoint();
        for (int x = minimumPoint.getBlockX() - 1; x <= maximumPoint.getBlockX(); x++) {
            for (int y = minimumPoint.getBlockY() - 1; y <= maximumPoint.getBlockY(); y++) {
                for (int z = minimumPoint.getBlockZ() - 1; z <= maximumPoint.getBlockZ(); z++) {
                    if(!BukkitAdapter.adapt(clipboard.getBlock(BlockVector3.at(x, y, z)).getBlockType()).equals(Material.AIR)){
                        BaseBlock baseBlock = new BaseBlock(BukkitAdapter.asBlockType(Material.MOVING_PISTON));
                        clipboard.setBlock(x, y, z, baseBlock);
                    }
                }
            }
        }
    }

}
