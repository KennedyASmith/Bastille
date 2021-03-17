package com.kennedysmithjava.prisonmines.cmd;

import com.kennedysmithjava.prisonmines.MineRegenCountdown;
import com.kennedysmithjava.prisonmines.cmd.type.TypeMineNameStrict;
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

import java.util.HashMap;

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
        String name = readArg();
        long timer = readArg();

        WorldEditPlugin worldEdit = (WorldEditPlugin) Bukkit.getServer().getPluginManager().getPlugin("WorldEdit");
        Selection selection = worldEdit.getSelection(player);

        if (selection == null) {
            msg("pls no make mine without selection uwu");
            return;
        }

        Location pos1 = selection.getMinimumPoint();
        Location pos2 = selection.getMaximumPoint();

        String id = MStore.createId();
        Mine mine = MineColl.get().create(id);

        mine.setName(name);
        mine.setRegenTimer(timer);
        mine.setHasTimer(true);
        mine.setMin(pos1);
        mine.setMax(pos2);
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

        Bukkit.broadcastMessage("e" + size);
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
