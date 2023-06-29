package com.kennedysmithjava.prisoncore.cmd;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.kennedysmithjava.prisoncore.Perm;
import com.kennedysmithjava.prisoncore.entity.Regions;
import com.kennedysmithjava.prisoncore.regions.RegionFlatSquare;
import com.kennedysmithjava.prisoncore.util.LoopIterator;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.requirement.RequirementHasPerm;
import com.massivecraft.massivecore.command.requirement.RequirementIsPlayer;
import com.massivecraft.massivecore.command.type.primitive.TypeInteger;
import com.massivecraft.massivecore.util.MUtil;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.data.BlockData;

import java.util.*;

public class CmdPRegionVisualize extends CoreCommand {

    private final ProtocolManager protocolManager;
    public CmdPRegionVisualize() {
        this.addRequirements(RequirementHasPerm.get(Perm.PREGION));
        this.addRequirements(RequirementIsPlayer.get());

        // Aliases
        this.addAliases("visualize");
        this.setSetupPermBaseClassName("PREGION");
        this.addParameter(130, TypeInteger.get(), "height");

        protocolManager = ProtocolLibrary.getProtocolManager();
    }

    private static Map<UUID, List<Location>> lastVisualizations = new HashMap<>();

    @Override
    public void perform() throws MassiveException {

        int y = readArg();
        List<BlockData> wools = MUtil.list(
                Material.RED_WOOL.createBlockData(),
                Material.BLUE_WOOL.createBlockData(),
                Material.LIME_WOOL.createBlockData(),
                Material.YELLOW_WOOL.createBlockData(),
                Material.PINK_WOOL.createBlockData(),
                Material.LIGHT_BLUE_WOOL.createBlockData(),
                Material.GREEN_WOOL.createBlockData(),
                Material.WHITE_WOOL.createBlockData(),
                Material.CYAN_WOOL.createBlockData(),
                Material.LIGHT_GRAY_WOOL.createBlockData(),
                Material.MAGENTA_WOOL.createBlockData());
        LoopIterator<BlockData> woolerator = new LoopIterator<>(wools);
        World world = me.getWorld();
        List<Location> lVisuals = lastVisualizations.get(me.getUniqueId());

        if(lVisuals != null){
            BlockData airData = Material.AIR.createBlockData();
            for (Location lVisual : lVisuals) {
                me.sendBlockChange(lVisual, airData);
            }
        }

        List<Location> visualizations = new ArrayList<>();
        for (Set<RegionFlatSquare> regionSet : Regions.get().getRegions().values()) {
            BlockData regionColor = woolerator.next();
            for (RegionFlatSquare r : regionSet) {
                for (int x = r.getMinX(); x <= r.getMaxX(); x++) {
                    for (int z = r.getMinZ(); z <= r.getMaxZ(); z++) {
                        Location bLocation = new Location(world, x, y, z);
                        me.sendBlockChange(bLocation, regionColor);
                        visualizations.add(bLocation);
                    }
                }
            }
        }

        lastVisualizations.put(me.getUniqueId(), visualizations);
        msg("&7[&bServer&7] Visualizing regions. The blocks you see are packets, rejoin the server to remove the blocks.");
    }

}
