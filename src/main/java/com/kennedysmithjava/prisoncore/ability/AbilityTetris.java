package com.kennedysmithjava.prisoncore.ability;

import com.kennedysmithjava.prisoncore.PrisonCore;
import com.kennedysmithjava.prisoncore.entity.tools.BufferConf;
import com.kennedysmithjava.prisoncore.event.EventAbilityUse;
import com.kennedysmithjava.prisoncore.tools.Buffer;
import com.kennedysmithjava.prisoncore.regions.BlockArea;
import com.kennedysmithjava.prisoncore.regions.BlockPos;
import com.massivecraft.massivecore.util.MUtil;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public class AbilityTetris extends Ability<AbilityTetris> {

    private static AbilityTetris INSTANCE = new AbilityTetris();

    public static AbilityTetris get() {
        return INSTANCE;
    }

    public AbilityTetris() {
        super(AbilityType.TETRIS);
    }

    @Override
    public void run(EventAbilityUse event, Map<Buffer, Integer> buffers) {
        Integer power = buffers.get(BufferConf.POWER);
        if (power == null || power < 0) power = 0;

        Block clickedBlock = event.getBlock();
        if (clickedBlock == null)
            clickedBlock = event.getPlayer().getTargetBlock((Set<Material>) null, 7);
        World world = clickedBlock.getWorld();

        BlockPos blockPos = new BlockPos(clickedBlock);
        Player player = event.getPlayer();
        player.sendMessage("Pos: " + blockPos);
        BlockArea destroyZone = BlockArea.from(blockPos, blockPos);
        destroyZone = destroyZone.growXZ(power);
        player.sendMessage("Destroy Zone: " + destroyZone);
        for(BlockPos pos : destroyZone) {
            player.sendMessage("Destroyed: " + pos);
            pos.getBlock(world).breakNaturally(); //Add to result or register somewhere
        }
        BlockArea fallDownZone = destroyZone.shift(0, 1, 0).addMaxY(10);

        fallDownZone.forEach(pos -> {
            Block b = pos.getBlock(world);
            byte data = b.getData();
            Material m = b.getType();
            b.breakNaturally();
            FallingBlock fb = world.spawnFallingBlock(b.getLocation(), m, data);
            fb.setDropItem(false);
            event.addAffectedBlock(b.getLocation());
        });

        new BukkitRunnable() {
            @Override
            public void run() {
                onFinish(event);
            }
        }.runTaskLater(PrisonCore.get(), 20L);
    }

    @Override
    public String getDisplayName() {
        return "§6Tetris";
    }

    @Override
    public Collection<String> getAllowedToolTypes() {
        return null;
    }

    @Override
    public Material getItemMaterial() {
        return Material.BRICK;
    }

    @Override
    public Map<Buffer, Integer> getMaxBufferLevels() {
        return MUtil.map(
                BufferConf.EFFICIENCY, 10,
                BufferConf.SPEED, 10,
                BufferConf.POWER, 10);
    }
}
