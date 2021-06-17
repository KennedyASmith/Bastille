package com.kennedysmithjava.prisonmines.upgrades;

import com.kennedysmithjava.prisonmines.entity.Distribution;
import com.kennedysmithjava.prisonmines.entity.Mine;
import com.mcrivals.prisoncore.entity.MPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;

public class ResourceUpgrade extends AbstractUpgrade{

    Distribution distribution;

    ResourceUpgrade(Distribution distribution){
        this.distribution = distribution;
    }

    @Override
    public void apply(MPlayer player) {

        Bukkit.broadcastMessage("Resource upgrade purchased!");

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

        mine.setBlockDistribution(distribution.getRates());

        mine.getRegenCountdown().setMine(mine);
        mine.getRegenCountdown().forceReset();
    }
}
