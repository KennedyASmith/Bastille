package com.kennedysmithjava.prisoncore;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.ChunkGenerator;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class VoidGenerator extends ChunkGenerator {

    public List<BlockPopulator> getDefaultPopulators(final World world) {
        return Arrays.asList(new BlockPopulator[0]);
    }

    public boolean canSpawn(final World world, final int x, final int z) {
        return true;
    }

    public int xyzToByte(final int x, final int y, final int z) {
        return (x * 16 + z) * 128 + y;
    }

    public byte[] generate(final World world, final Random rand, final int chunkx, final int chunkz) {
        final byte[] result = new byte[32768];
        if (chunkx == 0 && chunkz == 0) {
            result[this.xyzToByte(0, 64, 0)] = (byte) Material.BEDROCK.getId();
        }
        return result;
    }

    public Location getFixedSpawnLocation(final World world, final Random random) {
        return new Location(world, 0.0, 66.0, 0.0);
    }


}
