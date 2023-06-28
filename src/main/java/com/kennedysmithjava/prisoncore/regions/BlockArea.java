package com.kennedysmithjava.prisoncore.regions;

import com.kennedysmithjava.prisoncore.util.XYPair;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;

import java.util.Iterator;
import java.util.Objects;
import java.util.Random;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class BlockArea implements Iterable<BlockPos> {

    private final BlockPos min;
    private final BlockPos max;

    protected BlockArea(BlockPos min, BlockPos max) {
        this.min = min;
        this.max = max;
    }

    public static BlockArea from(BlockPos pos1, BlockPos pos2) {
        XYPair<BlockPos, BlockPos> pair = BlockPos.getMinMax(pos1, pos2);
        return new BlockArea(pair.x, pair.y);
    }

    public BlockPos getMin() {
        return min;
    }

    public BlockPos getMax() {
        return max;
    }

    public int getArea() {
        BlockPos area = max.add(1, 1, 1).sub(min); //Add 1 because max and min are inclusive
        return area.getX() * area.getY() * area.getZ();
    }

    public BlockPos getDimensions() {
        return max.add(1, 1, 1).sub(min); //Add 1 because max and min are inclusive
    }

    public BlockPos getCenter() {
        return max.sub(min).divide(2, 2, 2).add(min);
    }

    public Location getCenter(World world) {
        return new Location(world,
                (max.getX() - min.getX()) / 2d + min.getX() + 0.5,
                (max.getY() - min.getY()) / 2d + min.getY() + 0.5,
                (max.getZ() - min.getZ()) / 2d + min.getZ() + 0.5);
    }

    public int maxX() {
        return max.getX();
    }

    public int maxY() {
        return max.getY();
    }

    public int maxZ() {
        return max.getZ();
    }

    public int minX() {
        return min.getX();
    }

    public int minY() {
        return min.getY();
    }

    public int minZ() {
        return min.getZ();
    }

    public boolean contains(int x, int y, int z) {
        return x >= min.getX() && y >= min.getY() && z >= min.getZ()
                && x <= max.getX() && y <= max.getY() && z <= max.getZ();
    }

    public boolean contains(BlockPos pos) {
        return contains(pos.getX(), pos.getY(), pos.getZ());
    }

    public boolean contains(Location location) {
        return contains(location.getBlockX(), location.getBlockY(), location.getBlockZ());
    }

    public boolean contains(Block block) {
        return contains(block.getX(), block.getY(), block.getZ());
    }

    public BlockPos getRandom(Random random) {
        BlockPos dims = getDimensions();
        int x = random.nextInt(dims.getX());
        int y = random.nextInt(dims.getY());
        int z = random.nextInt(dims.getZ());
        return min.add(x, y, z);
    }

    public Stream<BlockPos> getRandom(Random random, double percent) {
        int area = getArea();
        BlockPos dims = getDimensions();
        IntStream indexes = random.ints((long) (area * percent), 0, area);
        return indexes.distinct().mapToObj(i -> {
            int x = i % dims.getX();
            int y = (i / dims.getX()) % dims.getY();
            int z = i / (dims.getY() * dims.getX());
            return min.add(x, y, z);
        });
    }

    public BlockArea addMaxY(int y) {
        y = Math.abs(y);
        return new BlockArea(min, max.addY(y));
    }

    /**
     * Move the area by a vector relative to the current location
     * @param pos pos to move by
     * @return new BlockArea
     */
    public BlockArea shift(BlockPos pos) {
        return new BlockArea(min.add(pos), max.add(pos));
    }

    public BlockArea shift(int x, int y, int z) {
        return new BlockArea(min.add(x, y, z), max.add(x, y, z));
    }

    public BlockArea grow(int apothem) {
        apothem = Math.abs(apothem);
        return new BlockArea(min.sub(apothem, apothem, apothem), max.add(apothem, apothem, apothem));
    }

    public BlockArea growY(int blocks) {
        blocks = Math.abs(blocks);
        return new BlockArea(min.sub(0, blocks, 0), max.add(0, blocks, 0));
    }

    public BlockArea growX(int blocks) {
        blocks = Math.abs(blocks);
        return new BlockArea(min.sub(blocks, 0, 0), max.add(blocks, 0, 0));
    }

    public BlockArea growZ(int blocks) {
        blocks = Math.abs(blocks);
        return new BlockArea(min.sub(0, 0, blocks), max.add(0, 0, blocks));
    }

    public BlockArea growXZ(int blocks) {
        blocks = Math.abs(blocks);
        return new BlockArea(min.sub(blocks, 0, blocks), max.add(blocks, 0, blocks));
    }

    public BlockArea shrink(int apothem) {
        apothem = Math.abs(apothem);
        BlockPos newMin = min.add(apothem, apothem, apothem);
        BlockPos newMax = max.sub(apothem, apothem, apothem);
        if (newMin.getX() > newMax.getX())
            newMin = newMin.withX(newMax.getX());
        if (newMin.getY() > newMax.getY())
            newMin = newMin.withY(newMax.getY());
        if (newMin.getZ() > newMax.getZ())
            newMin = newMin.withZ(newMax.getZ());
        return new BlockArea(newMin, newMax);
    }

    public BlockArea boundedBy(BlockArea bound) {
        BlockPos min = BlockPos.getMinMax(this.min, bound.getMin()).y; //get max of the mins
        BlockPos max = BlockPos.getMinMax(this.max, bound.getMax()).x; //get min of the maxs
        return BlockArea.from(min, max);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BlockArea blockArea = (BlockArea) o;
        return Objects.equals(min, blockArea.min) && Objects.equals(max, blockArea.max);
    }

    @Override
    public int hashCode() {
        return Objects.hash(min, max);
    }

    public String toString() {
        return "BlockArea(min=" + this.getMin() + ", max=" + this.getMax() + ")";
    }

    @Override
    public Iterator<BlockPos> iterator() {
        return new Iterator<BlockPos>() {

            private BlockPos cursor = min;

            @Override
            public boolean hasNext() {
                return !cursor.equals(max);
            }

            @Override
            public BlockPos next() {
                BlockPos temp = cursor;
                if (cursor.getX() < max.getX()) {
                    cursor = cursor.addX(1);
                } else if (cursor.getY() < max.getY()) {
                    cursor = cursor.withX(min.getX());
                    cursor = cursor.addY(1);
                } else if (cursor.getZ() < max.getZ()) {
                    cursor = cursor.withY(min.getY()).withX(min.getX());
                    cursor = cursor.addZ(1);
                }
                return temp;
            }
        };
    }

    public Iterator<Block> blocks(World world) {
        return new Iterator<Block>() {
            private final Iterator<BlockPos> baseIter = iterator();

            @Override
            public boolean hasNext() {
                return baseIter.hasNext();
            }

            @Override
            public Block next() {
                return baseIter.next().getBlock(world);
            }
        };
    }
}
