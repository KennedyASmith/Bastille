package com.kennedysmithjava.prisonmines;
import com.kennedysmithjava.prisonmines.entity.MinesConf;
import com.kennedysmithjava.prisonmines.entity.Mine;
import com.kennedysmithjava.prisonmines.entity.MineColl;
import com.kennedysmithjava.prisonmines.util.VoidGenerator;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.bukkit.BukkitUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.generator.ChunkGenerator;

import java.io.File;
import java.util.*;

public class MinesWorldManager {

    public static int WORLD_GAP = 8;

    VoidGenerator generator;
    String worldName = MinesConf.get().minesWorldName;

    public static MinesWorldManager i = new MinesWorldManager();
    public static MinesWorldManager get() {
        return i;
    }

    public ChunkGenerator getDefaultWorldGenerator(String worldName, String id) {
        return generator;
    }

    public MinesWorldManager(){
        File file = new File(PrisonMines.get().getServer().getWorldContainer(), worldName);
        if(!file.exists()) {
            World world = Bukkit.createWorld(new WorldCreator(worldName).generator(new VoidGenerator()));
            MinesConf.get().minesWorldDefaultGamerules.forEach(world::setGameRuleValue);
        }
        generator = new VoidGenerator();
    }

    public World getWorld(){
        File file = new File(PrisonMines.get().getServer().getWorldContainer(), worldName);
        if(!file.exists() || Bukkit.getWorld(worldName) == null) {
            return Bukkit.createWorld(new WorldCreator(worldName).generator(new VoidGenerator()));
        }
        return Bukkit.getWorld(worldName);
    }

    public Vector getUniqueLocation(){
        Collection<Mine> mines = MineColl.get().getAll();
        if(!(mines.size() > 0)){
            return BukkitUtil.toVector(new Location(getWorld(), 0, 10, 0));
        }else{
            return BukkitUtil.toVector(new Location(getWorld(),mines.size() * (1 << WORLD_GAP), 10, mines.size() * (1 << WORLD_GAP)));
        }
    }

}
