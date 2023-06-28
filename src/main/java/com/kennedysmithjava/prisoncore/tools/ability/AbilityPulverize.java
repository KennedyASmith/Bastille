package com.kennedysmithjava.prisoncore.tools.ability;

import com.kennedysmithjava.prisoncore.entity.tools.BufferConf;
import com.kennedysmithjava.prisoncore.event.EventAbilityUse;
import com.kennedysmithjava.prisoncore.tools.Buffer;
import com.kennedysmithjava.prisoncore.regions.BlockArea;
import com.kennedysmithjava.prisoncore.regions.BlockPos;
import com.kennedysmithjava.prisoncore.regions.Scaling;
import com.kennedysmithjava.prisoncore.util.vfx.CuboidSequence;
import com.massivecraft.massivecore.util.MUtil;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class AbilityPulverize extends Ability<AbilityPulverize> {

    private static final AbilityPulverize INSTANCE = new AbilityPulverize();

    private static final int MAX_POWER = 10;

    public AbilityPulverize() {
        super(AbilityType.PULVERIZE);
    }

    public static AbilityPulverize get() {
        return INSTANCE;
    }

    @Override
    public String getDisplayName() {
        return "§6Pulverize";
    }

    @Override
    public Collection<String> getAllowedToolTypes() {
        return null;
    }

    @Override
    public void run(EventAbilityUse event, Map<Buffer, Integer> buffers) {
        Integer power = buffers.get(BufferConf.POWER);
        if (power == null || power < 0) power = 0;
        Player player = event.getPlayer();
        player.sendMessage("Power: " + power);
        Block clickedBlock = event.getBlock();
        if (clickedBlock == null)
            clickedBlock = event.getPlayer().getTargetBlock((Set<Material>) null, 7);
        World world = clickedBlock.getWorld();
        BlockPos blockPos = new BlockPos(clickedBlock);
        player.sendMessage("Pos: " + blockPos);
        //lets just assume they hit the top middle block of a 5x5x5 for now
        //when we get the area for the mine we will be able to do breakArea.boundedBy(mineArea)
        BlockArea area = BlockArea.from(blockPos.add(-2, -4, -2), blockPos.add(2, 0, 2));

        CuboidSequence cubeSequence = new CuboidSequence(area);
        cubeSequence.standOnCorner();
        cubeSequence.draw(new Location(world, 0, 0, 0));

        player.sendMessage("Area: " + area);
        area.getRandom(new Random(), Scaling.linear(power, MAX_POWER))
                .map(pos -> pos.getBlock(world))
                .forEach(block -> {
                    world.playEffect(block.getLocation().add(0.5, 0.5, 0.5), Effect.SMOKE, 1);
                    block.breakNaturally();
                });
    }

    @Override
    public Material getItemMaterial() {
        return Material.PISTON;
    }

    @Override
    public Map<Buffer, Integer> getMaxBufferLevels() {
        return MUtil.map(
                BufferConf.EFFICIENCY, 10,
                BufferConf.SPEED, 10,
                BufferConf.POWER, 10);
    }
}
