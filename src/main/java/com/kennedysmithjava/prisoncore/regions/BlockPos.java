package com.kennedysmithjava.prisoncore.regions;

import com.kennedysmithjava.prisoncore.util.Pair;
import com.kennedysmithjava.prisoncore.util.XYPair;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.util.Vector;

public class BlockPos {
    private final int x;
    private final int y;
    private final int z;

    public BlockPos(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public BlockPos(Location location) {
        this(location.getBlockX(), location.getBlockY(), location.getBlockZ());
    }

    public BlockPos(Block block) {
        this(block.getX(), block.getY(), block.getZ());
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

    public BlockPos addX(int x) {
        return add(x, 0, 0);
    }

    public BlockPos addY(int y) {
        return add(0, y, 0);
    }

    public BlockPos addZ(int z) {
        return add(0, 0, z);
    }

    public BlockPos add(BlockPos pos) {
        return add(pos.x, pos.y, pos.z);
    }

    public BlockPos add(int x, int y, int z) {
        return new BlockPos(this.x + x, this.y + y, this.z + z);
    }

    public BlockPos sub(BlockPos pos) {
        return sub(pos.x, pos.y, pos.z);
    }

    public BlockPos sub(int x, int y, int z) {
        return add(-x, -y, -z);
    }

    public BlockPos withX(int x) {
        return new BlockPos(x, this.y, this.z);
    }

    public BlockPos withY(int y) {
        return new BlockPos(this.x, y, this.z);
    }

    public BlockPos withZ(int z) {
        return new BlockPos(this.x, this.y, z);
    }

    public BlockPos negate() {
        return multiply(-1, -1, -1);
    }

    public BlockPos multiply(int x, int y, int z) {
        return new BlockPos(this.x * x, this.y * y, this.z * z);
    }

    public BlockPos divide(int x, int y, int z) {
        return new BlockPos(this.x / x, this.y / y, this.z / z);
    }

    public Block getBlock(World world) {
        return world.getBlockAt(x, y, z);
    }

    public Vector toVector() {
        return new Vector(x, y, z);
    }

    public Location centerLocation(World world) {
        return new Location(world, x + 0.5, y + 0.5, z + 0.5);
    }

    public Location toLocation(World world) {
        return new Location(world, x, y, z);
    }

    public Chunk getChunk(World world) {
        return world.getChunkAt(this.x >> 4, this.z >> 4);
    }

    public BlockPos getMin(BlockPos pos) {
        return new BlockPos(Math.min(this.x, pos.x), Math.min(this.y, pos.y), Math.min(this.z, pos.z));
    }

    public BlockPos getMax(BlockPos pos) {
        return new BlockPos(Math.max(this.x, pos.x), Math.max(this.y, pos.y), Math.max(this.z, pos.z));
    }

    public Pair<Integer, Integer> getChunkCoords() {
        return new Pair<>(this.x >> 4, this.z >> 4);
    }

    /**
     * Get a minimum and maximum position from 2 points
     * @param pos1 any position
     * @param pos2 any position
     * @return a min and max position
     */
    public static XYPair<BlockPos, BlockPos> getMinMax(BlockPos pos1, BlockPos pos2) {
        int minX = Math.min(pos1.x, pos2.x);
        int minY = Math.min(pos1.y, pos2.y);
        int minZ = Math.min(pos1.z, pos2.z);
        int maxX = Math.max(pos1.x, pos2.x);
        int maxY = Math.max(pos1.y, pos2.y);
        int maxZ = Math.max(pos1.z, pos2.z);
        return new XYPair<>(new BlockPos(minX, minY, minZ), new BlockPos(maxX, maxY, maxZ));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BlockPos blockPos = (BlockPos) o;
        return x == blockPos.x && y == blockPos.y && z == blockPos.z;
    }

    @Override
    public int hashCode() {
        return this.x ^ this.z << 12 ^ this.y << 24;
    }

    public String toString() {
        return "BlockPos(x=" + this.getX() + ", y=" + this.getY() + ", z=" + this.getZ() + ")";
    }
}
