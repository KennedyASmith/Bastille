package com.kennedysmithjava.prisoncore.engine;

import com.kennedysmithjava.prisoncore.crafting.objects.PrisonLog;
import com.kennedysmithjava.prisoncore.entity.farming.TreesConf;
import com.kennedysmithjava.prisoncore.entity.farming.objects.Tree;
import com.kennedysmithjava.prisoncore.entity.farming.objects.TreeTemplate;
import com.kennedysmithjava.prisoncore.entity.farming.objects.TwoDVector;
import com.kennedysmithjava.prisoncore.util.FAWEPaster;
import com.kennedysmithjava.prisoncore.util.MiscUtil;
import com.kennedysmithjava.prisoncore.util.regions.Offset;
import com.massivecraft.massivecore.Engine;
import com.massivecraft.massivecore.ps.PS;
import com.sk89q.worldedit.math.BlockVector3;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@SuppressWarnings("DataFlowIssue")
public class EngineTrees extends Engine {

    public static EngineTrees i = new EngineTrees();

    public static EngineTrees get() {
        return i;
    }


    @Getter
    public transient Map<Tree, Long> treeRegenerateTimes = new HashMap<>();


    public transient Set<Material> treeCantBlockCache = new HashSet<>();

    public void loadTreeMaterialCache() {
        TreesConf.get().getTreeTemplates().forEach((s, template) ->
                treeCantBlockCache.add(template.getBlockToReplaceWhenBroken().getMaterial()));

        TreesConf.get().getTrees().forEach((twoDVector, tree) -> {
            if(tree.isNeedsRegeneration()){
                treeRegenerateTimes.put(tree, tree.getRegenerationTime());
            }
        });
    }

    /**
     * Handles blocks on trees being removed
     *
     * @param e event
     */
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent e) {
        if (!e.getBlock().getType().isBurnable()) return;

        Tree tree = getTree(e.getBlock());
        if (tree == null) return;
        e.setCancelled(true);
        TreeTemplate template = TreesConf.get().getTreeTemplates().get(tree.getName());
        if (template.getBlockToReplaceWhenBroken().getMaterial().equals(e.getBlock().getType())) return;
        tree.setBlocksHit(tree.getBlocksHit() + 1);
        MiscUtil.givePlayerItem(e.getPlayer(), PrisonLog.giveFromMaterial(e.getBlock().getType()), 1);
        e.getBlock().setType(template.getBlockToReplaceWhenBroken().getMaterial());
        tree.changed();
        if (tree.getBlocksHit() >= template.getTimesNeededToReset())
            chopTree(tree, template);
    }

    public void pasteTree(Tree tree) {
        TreeTemplate template = TreesConf.get().getTreeTemplates().get(tree.getName());
        Offset offset = template.getOffset();
        Location offsetLocation = offset.getFrom(tree.getSpawnPoint().asBukkitLocation());

        FAWEPaster.paste("Schematics" + File.separator + "Trees" + File.separator + template.getSchematicName(),
                offsetLocation.getWorld().getName(),
                BlockVector3.at(offsetLocation.getBlockX(), offsetLocation.getBlockY(), offsetLocation.getBlockZ()),
                false,
                () -> {
                }
        );
        tree.setRegenerationTime(0);
        tree.changed();
        TreesConf.get().changed();
    }

    public void chopTree(Tree tree, TreeTemplate template) {
        Offset offset = template.getOffset();
        Location offsetLocation = offset.getFrom(tree.getSpawnPoint().asBukkitLocation());
        FAWEPaster.paste("Schematics" + File.separator + "Trees" + File.separator + template.getSchematicName(),
                tree.getSpawnPoint().getWorld(),
                BlockVector3.at(offsetLocation.getX(), offsetLocation.getY(), offsetLocation.getZ()),
                true,
                () -> {
                }
        );
        tree.setBlocksHit(0);
        tree.changed();
        tree.getSpawnPoint().asBukkitLocation().getBlock().setType(template.getSaplingBlock().getMaterial());
        //ToDo Make this random number!
        tree.setRegenerationTime(25 * 1000);
        tree.setNeedsRegeneration(true);
        treeRegenerateTimes.put(tree, System.currentTimeMillis());
    }




    public void spawnNewTree(TreeTemplate template, Block block) {
        Location storedLocation = block.getLocation();

        // Creates the tree object
        TwoDVector twoDVector = new TwoDVector(storedLocation.getWorld().getName(), storedLocation.getBlockX(), storedLocation.getBlockZ());
        Tree tree = new Tree(template.getName(), 0, PS.valueOf(storedLocation), 0, false);

        Offset offset = template.getOffset();
        Location offsetLocation = offset.getFrom(block.getLocation());

        // Spawns the schematic
        FAWEPaster.paste("Schematics" + File.separator + "Trees" + File.separator + template.getSchematicName(),
                storedLocation.getWorld().getName(),
                BlockVector3.at(offsetLocation.getBlockX(), offsetLocation.getBlockY(), offsetLocation.getBlockZ()),
                false,
                () -> {
                }
        );
        TreesConf.get().addTree(twoDVector, tree);

        try {
            loadTreeMaterialCache();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Tree getTree(Block block) {
        TwoDVector twoDVector = new TwoDVector(block.getWorld().getName(), block.getLocation().getBlockX(), block.getLocation().getBlockZ());
        Map<TwoDVector, Tree> trees = TreesConf.get().getTrees();
        for (TwoDVector n : trees.keySet()) {
            if (n.equals(twoDVector)){
                return trees.get(n);
            }
        }

        return null;
    }
}
