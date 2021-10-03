package com.kennedysmithjava.prisonmines;

import com.kennedysmithjava.prisonmines.entity.Mine;
import com.kennedysmithjava.prisonmines.entity.MineColl;
import com.kennedysmithjava.prisonmines.entity.MinesConf;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.bukkit.BukkitUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldCreator;

import java.io.File;
import java.util.Collection;

public class MinesWorldManager {

    String worldName = "MinesWorld";
    World world;
    public static int WORLD_GAP = 8;

    public static MinesWorldManager i = new MinesWorldManager();
    public static MinesWorldManager get() {
        return i;
    }

        public MinesWorldManager(){
            world = Bukkit.createWorld(new WorldCreator(worldName).generator(PrisonMines.get().getDefaultWorldGenerator(worldName, "prisonminesMinesWorld")));
            world.setStorm(MinesConf.get().weatherEnabled);
            MinesConf.get().minesWorldDefaultGamerules.forEach(world::setGameRuleValue);
        }

        public World getWorld(){
            return world;
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
