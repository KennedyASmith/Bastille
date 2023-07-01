package com.kennedysmithjava.prisoncore.engine;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.util.Vector;

public class EngineExperimentation implements Listener {

    /*@EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Action action = event.getAction();

        if (action.equals(Action.LEFT_CLICK_BLOCK) || action.equals(Action.RIGHT_CLICK_BLOCK)) {
                List<Block> lastTwoTargetBlocks = player.getLastTwoTargetBlocks(null, 100);
                if (lastTwoTargetBlocks.size() != 2 || !lastTwoTargetBlocks.get(1).getType().isOccluding()) return;
                Block targetBlock = lastTwoTargetBlocks.get(1);
                Block adjacentBlock = lastTwoTargetBlocks.get(0);
                BlockFace face = targetBlock.getFace(adjacentBlock);
                Bukkit.broadcastMessage(face.name());
                Bukkit.broadcastMessage("Facing: " + player.getFacing().name());
                Location pLocation = rayCastBlocks(player, 10);
                Bukkit.broadcastMessage("Pixel Location: " + pLocation.toString());
                ParticleEffect.REDSTONE.display(pLocation, Color.GREEN, player);
        }
    }*/

    public static Location rayCastBlocks(Player player, double maxDistance) {
        Location starting = player.getEyeLocation();
        Vector direction = starting.getDirection();
        Location check = starting.clone();

        double distanceTraveled = 0;
        while (distanceTraveled < maxDistance) {
            check = getRayTraceLocation(check, direction, 0.0625D);
            if (check.getBlock().getType() != Material.AIR && !(check.getBlock().isLiquid())) {
                break;
            }
            distanceTraveled += 0.0625D;
        }

        return check;
    }

    public static Location getRayTraceLocation(Location starting, Vector direction, double distance) {
        Location ending = starting.clone().add(direction.clone().multiply(distance));
        return ending;
    }


    private class SimplePoint{
        double x;
        double y;
        double z;

        public SimplePoint(double x, double y, double z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        public double getZ() {
            return z;
        }

        public double getX() {
            return x;
        }

        public double getY() {
            return y;
        }

        @Override
        public String toString() {
            return "SimplePoint{" +
                    "x=" + x +
                    ", y=" + y +
                    ", z=" + z +
                    '}';
        }
    }
    private BlockFace getBlockFace(Player player, Block clickedBlock) {
        BlockFace[] faces = BlockFace.values();

        for (BlockFace face : faces) {
            Block relativeBlock = clickedBlock.getRelative(face);
            if (relativeBlock.getLocation().equals(player.getTargetBlock(null, 5).getLocation())) {
                return face;
            }
        }

        return null;
    }

}
