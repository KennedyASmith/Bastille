package com.kennedysmithjava.prisoncore.entity.farming;

import com.kennedysmithjava.prisoncore.PrisonCore;
import com.kennedysmithjava.prisoncore.blockhandler.BlockWrapper;
import com.kennedysmithjava.prisoncore.entity.farming.objects.Tree;
import com.kennedysmithjava.prisoncore.entity.farming.objects.TreeTemplate;
import com.kennedysmithjava.prisoncore.entity.farming.objects.TwoDVector;
import com.kennedysmithjava.prisoncore.util.FAWEPaster;
import com.kennedysmithjava.prisoncore.util.regions.Offset;
import com.massivecraft.massivecore.ps.PS;
import com.massivecraft.massivecore.store.Entity;
import com.massivecraft.massivecore.util.MUtil;
import com.sk89q.worldedit.math.BlockVector3;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class TreesConf extends Entity<TreesConf> {

    // -------------------------------------------- //
    // META
    // -------------------------------------------- //

    protected static transient TreesConf i;

    public static TreesConf get() {
        return i;
    }

    @Override
    public TreesConf load(TreesConf that) {
        super.load(that);
        return this;
    }

    @Getter
    private Map<String, TreeTemplate> treeTemplates = MUtil.map(
            "acacia", new TreeTemplate("acacia", "acacaia_schematic.schematic",
                    3,
                    50,
                    new BlockWrapper(Material.ACACIA_LOG),
                    new BlockWrapper(Material.STRIPPED_ACACIA_LOG),
                    new BlockWrapper(Material.ACACIA_SAPLING),
                    new Offset(2, 0, -2))
    );

    @Getter
    private Map<TwoDVector, Tree> trees = new HashMap<>();

    public void spawnNewTree(TreeTemplate template, Block block) {
        Offset offset = template.getOffset();
        Location storedLocation = offset.get(block.getLocation());

        // Creates the tree object
        TwoDVector twoDVector = new TwoDVector(storedLocation.getWorld().getName(), storedLocation.getBlockX(), storedLocation.getBlockZ());
        Tree tree = new Tree(template.getName(), 0, PS.valueOf(storedLocation));
        trees.put(twoDVector, tree);

        // Spawns the schematic
        FAWEPaster.paste(PrisonCore.get().getDataFolder() + File.separator + "Schematics" + File.separator + "Trees" + File.separator + template.getSchematicName(),
                storedLocation.getWorld().getName(),
                BlockVector3.at(storedLocation.getBlockX(), storedLocation.getBlockY(), storedLocation.getBlockZ()),
                false,
                () -> {
                }
        );
        // Save
        save();
    }

    public void respawnTree() {

    }

    public void removeTree() {

    }

//    private void spawnSchematic(String schematicName, Location location, boolean destroy) {
//        try {
//            File file = new File(PrisonCore.get().getDataFolder() + File.separator + "schematics" + File.separator + schematicName);
//            Schematic schematic = ClipboardFormats.findByFile(file).load(file);
//            Clipboard clipboard = schematic.getClipboard();
//
//            if (destroy) {
//                FaweAdapter_All adapter = new FaweAdapter_All();
//                Vector minimumPoint = clipboard.getMinimumPoint();
//                Vector maximumPoint = clipboard.getMaximumPoint();
//
//                for (int x = minimumPoint.getBlockX() - 1; x <= maximumPoint.getBlockX(); x++) {
//                    for (int y = minimumPoint.getBlockY() - 1; y <= maximumPoint.getBlockY(); y++) {
//                        for (int z = minimumPoint.getBlockZ() - 1; z <= maximumPoint.getBlockZ(); z++) {
//                            if (!adapter.getMaterial(clipboard.getBlock(new Vector(x, y, z)).getType()).equals(Material.AIR)) {
//                                BaseBlock baseBlock = new BaseBlock(adapter.getBlockId(Material.PISTON_MOVING_PIECE), 0); //REPRESENTS AIR
//                                clipboard.setBlock(x, y, z, baseBlock);
//                            }
//                        }
//                    }
//                }
//            }
//
//            // .. to fix, tnx
//            EditSession es = schematic.paste(FaweAPI.getWorld("Spawn"), new Vector(location.getBlockX(), location.getBlockY(), location.getBlockZ()), false, false, null);
//
//            es.setFastMode(true);
//
//        } catch (Throwable e) {
//            e.printStackTrace();
//        }
//    }

    private void save() {
        this.changed();
    }

    public Tree getTree(Block block) {
        TwoDVector twoDVector = new TwoDVector(block.getWorld().getName(), block.getLocation().getBlockX(), block.getLocation().getBlockZ());

        for (TwoDVector n : trees.keySet()) {
            if (n.equals(twoDVector))
                return trees.get(n);
        }

        return null;
    }

    private TreeTemplate getTreeTemplate(String name) {
        return treeTemplates.get(name);
    }


}
