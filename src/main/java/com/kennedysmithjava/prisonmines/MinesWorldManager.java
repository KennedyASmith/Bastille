package com.kennedysmithjava.prisonmines;

import com.kennedysmithjava.prisonmines.entity.Mine;
import com.kennedysmithjava.prisonmines.entity.MineColl;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.bukkit.BukkitUtil;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.generator.ChunkGenerator;
import uk.m4xy.mineworldmanager.MineWorldManager;

import java.util.Collection;

public class MinesWorldManager {

    public static MinesWorldManager i = new MinesWorldManager();

    public static MinesWorldManager get() {
        return i;
    }

    public ChunkGenerator getDefaultWorldGenerator(String worldName, String id) {
        return MineWorldManager.get().getDefaultWorldGenerator(worldName, id);
    }

    public World getWorld() {
        return MineWorldManager.get().getWorld();
    }

    public Vector getUniqueLocation() {
        Collection<Mine> mines = MineColl.get().getAll();
        if (!(mines.size() > 0)) {
            return BukkitUtil.toVector(new Location(getWorld(), 0, 10, 0));
        } else {
            return BukkitUtil.toVector(new Location(getWorld(), mines.size() * (1 << MineWorldManager.get().getWorldGap()),
                    10, mines.size() * (1 << MineWorldManager.get().getWorldGap())));
        }
    }

}
