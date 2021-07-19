package com.kennedysmithjava.prisonmines.cmd;

import com.kennedysmithjava.prisonmines.MineRegenCountdown;
import com.kennedysmithjava.prisonmines.PrisonMines;
import com.kennedysmithjava.prisonmines.cmd.type.TypeMineNameStrict;
import com.kennedysmithjava.prisonmines.entity.DistributionConf;
import com.kennedysmithjava.prisonmines.entity.Mine;
import com.kennedysmithjava.prisonmines.entity.MineColl;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.collections.MassiveMapDef;
import com.massivecraft.massivecore.command.type.primitive.TypeLong;
import com.massivecraft.massivecore.store.MStore;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.bukkit.selections.Selection;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;
import xyz.xenondevs.particle.ParticleBuilder;
import xyz.xenondevs.particle.ParticleEffect;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CmdMinesCreate extends MineCommand {
    // -------------------------------------------- //
    // CONSTRUCT
    // -------------------------------------------- //

    public CmdMinesCreate() {
        // Aliases
        this.addAliases("create", "new");

        // Parameters
        this.addParameter(TypeMineNameStrict.get(), "name");
        this.addParameter(TypeLong.get(), "timer");

    }

    // -------------------------------------------- //
    // OVERRIDE
    // -------------------------------------------- //

    @Override
    public void perform() throws MassiveException {
        if (!(sender instanceof Player)) return;
        Player player = (Player) sender;

        Location loc = player.getLocation();

        List<Location> locs = new ArrayList<>();
        int[] ang = new int[]{0, 30, 45, 60, 90, 120, 135, 150, 180, 210, 225, 240, 270, 300, 315, 330};
        int radius = 2;
        for (int a : ang) {
            double p = Math.toRadians(a);
            double x = radius * Math.cos(p);
            double z = radius * Math.sin(p);

            Bukkit.broadcastMessage("X: " + x + " Y: " + z);
            Location d = loc.clone().add(x, 0, z);
            locs.add(d);
        }

        BukkitScheduler scheduler = PrisonMines.get().getServer().getScheduler();
        scheduler.scheduleSyncRepeatingTask(PrisonMines.get(), new Runnable() {
            @Override
            public void run() {

                locs.forEach(location -> {
                    new ParticleBuilder(ParticleEffect.FLAME, location)
                            .setSpeed(0.0f)
                            .display();
                });

            }
        }, 0L, 20L);

        player.sendMessage("Distribution: ");
        DistributionConf.get().distribution.get(1).getRates().forEach((s, aDouble) -> player.sendMessage(s + " " + aDouble));

        String name = readArg();
        long timer = readArg();

        WorldEditPlugin worldEdit = (WorldEditPlugin) Bukkit.getServer().getPluginManager().getPlugin("WorldEdit");
        Selection selection = worldEdit.getSelection(player);

        if (selection == null) {
            msg("You cannot create a mine without a selection. Use //wand to select a region.");
            return;
        }

        Location pos1 = selection.getMinimumPoint();
        Location pos2 = selection.getMaximumPoint();

        String id = MStore.createId();
        Mine mine = MineColl.get().create(id);

        mine.setName(name);
        mine.setRegenTimer(timer);
        mine.setMinVar(pos1);
        mine.setMaxVar(pos2);
        mine.setBlockDistribution(getBlockDistribution(pos1, pos2));
        mine.setSpawnPoint(player.getLocation());
        MineColl.addCountdown(mine, new MineRegenCountdown(mine, 0));

    }

    public MassiveMapDef<String, Double> getBlockDistribution(Location min, Location max) {

        World world = min.getWorld();

        int size = 0;
        HashMap<String, Long> distribution = new HashMap<>();

        for (int x = min.getBlockX(); x <= max.getBlockX(); x++) {
            for (int y = min.getBlockY(); y <= max.getBlockY(); y++) {
                for (int z = min.getBlockZ(); z <= max.getBlockZ(); z++) {
                    Block block = world.getBlockAt(x, y, z);
                    Material material = block.getType();
                    if (material.equals(Material.AIR)) continue;
                    distribution.put(material.toString() + ":" + block.getData(), distribution.getOrDefault(material.toString() + ":" + block.getData(), 0L) + 1L);
                    size++;
                }
            }
        }

        long finalSize = size;
        MassiveMapDef<String, Double> percentage = new MassiveMapDef<>();

        distribution.forEach((material, blocks) ->
                {
                    Bukkit.broadcastMessage(material + " " + (blocks.doubleValue() / (double) finalSize) * 100);
                    percentage.put(material, (blocks.doubleValue() / (double) finalSize) * 100);
                }
        );
        return percentage;
    }

}
