package com.kennedysmithjava.prisoncore.util.regions;

import com.kennedysmithjava.prisoncore.PrisonCore;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.generator.ChunkGenerator;

import java.util.Random;

public class VoidGenerator extends ChunkGenerator {

    PrisonCore plugin;

    public VoidGenerator(){
        this.plugin = PrisonCore.get();
    }

    public byte[][] generateBlockSections(World world, Random random, int chunkX, int chunkZ, BiomeGrid biomeGrid){
        byte[][] result = new byte[world.getMaxHeight() / 16][];
        return result;
    }

    public Location getFixedSpawnLocation(World world, Random random) {
        return new Location(world, plugin.getConfig().getInt("bedrock.x"), plugin.getConfig().getInt("bedrock.y") + 1, plugin.getConfig().getInt("bedrock.z"));
    }

    private void setBlock(byte[][] result, int x, int y, int z, byte blkid) {
        if (result[y >> 4] == null) {
            result[y >> 4] = new byte[4096];
        }
        result[y >> 4][((y & 0xF) << 8) | (z << 4) | x] = blkid;
    }
}
