package com.kennedysmithjava.prisonmines.upgrades;

import com.kennedysmithjava.prisonmines.entity.LayoutConf;
import com.kennedysmithjava.prisonmines.entity.Mine;
import com.kennedysmithjava.prisonmines.entity.Floor;
import com.mcrivals.prisoncore.entity.MPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;

public class MineSizeUpgrade extends AbstractUpgrade{

    int height;
    int width;

    MineSizeUpgrade(int width, int height){
        this.height = height;
        this.width = width;
    }

    @Override
    public void apply(MPlayer player) {

        Bukkit.broadcastMessage("Purchased a mine size upgrade!");
        Mine mine = Mine.get(player.getMineID());

        World world = mine.getWorld();
        Location oldMax = mine.getMineMax();
        Location oldMin = mine.getMineMin();

        for (int x = oldMax.getBlockX(); x >= oldMin.getBlockX(); x--) {
            for (int y = oldMax.getBlockY(); y >= oldMin.getBlockY(); y--) {
                for (int z = oldMax.getBlockZ(); z >= oldMin.getBlockZ(); z--) {
                    world.getBlockAt(x, y,z).setType(Material.AIR);
                }
            }
        }

        Location origin = mine.getOrigin();
        Floor floor = LayoutConf.get().getPath(mine.getPathID());
        Location mineCenter = new Location(world, origin.getBlockX() + floor.getMineCenter().getX(), origin.getBlockY() + floor.getMineCenter().getY(), origin.getBlockZ() + floor.getMineCenter().getZ());

        Location maxMine = mineCenter.add(-(width - 2), 0, -(width - 2));
        Location minMine = maxMine.clone().add(width-1, -(height - 1), width-1);

        mine.setMin(minMine);
        mine.setMax(maxMine);

        mine.getRegenCountdown().setMine(mine);
        mine.getRegenCountdown().forceReset();
    }
}
