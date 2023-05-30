package com.kennedysmithjava.prisoncore.util.regions;

import com.kennedysmithjava.prisoncore.PrisonCore;
import com.kennedysmithjava.prisoncore.entity.mines.Mine;
import com.kennedysmithjava.prisoncore.entity.mines.MineColl;
import com.kennedysmithjava.prisoncore.entity.mines.MinesConf;
import com.sk89q.worldedit.math.BlockVector3;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;

import java.util.Collection;

public class MinesWorldManager {

    public static String worldName = "MinesWorld";
    public static int WORLD_GAP = 8;
    public static MinesWorldManager i = new MinesWorldManager();
    public static MinesWorldManager get() {
        return i;
    }
    private World world;

        public MinesWorldManager(){
            world = Bukkit.createWorld(new WorldCreator(worldName).generator(PrisonCore.get().getDefaultWorldGenerator(worldName, "prisonminesMinesWorld")));
            world.setStorm(MinesConf.get().weatherEnabled);
            MinesConf.get().minesWorldDefaultGamerules.forEach(world::setGameRuleValue);
        }

        public World getWorld(){
            return world;
        }

        public BlockVector3 getUniqueLocation(){
            Collection<Mine> mines = MineColl.get().getAll();
            if(!(mines.size() > 0)){
                return BlockVector3.at (0, 10, 0);
            }else{
                return BlockVector3.at (mines.size() * (1 << WORLD_GAP), 10, mines.size() * (1 << WORLD_GAP));
            }
        }

    public static String getWorldName() {
        return worldName;
    }
}
