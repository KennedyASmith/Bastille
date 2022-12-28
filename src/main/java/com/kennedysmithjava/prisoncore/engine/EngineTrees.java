package com.kennedysmithjava.prisoncore.engine;

import com.kennedysmithjava.prisoncore.entity.farming.TreesConf;
import com.kennedysmithjava.prisoncore.entity.farming.objects.Tree;
import com.kennedysmithjava.prisoncore.entity.farming.objects.TwoDVector;
import com.massivecraft.massivecore.Engine;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;

public class EngineTrees extends Engine {

    public static EngineTrees i = new EngineTrees();

    public static EngineTrees get() {
        return i;
    }

    /**
     * Handles blocks on trees being removed
     *
     * @param evt event
     */
    @EventHandler(ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent e) {
        Tree tree = getTree(e.getBlock());
        if (tree == null) return;


//        try {
//            TreesConf.get().chopTree(tree, evt.getPlayer(), evt);
//        } catch (NoSuchFieldException e) {
//            e.printStackTrace();
//        }
    }


    public Tree getTree(Block block) {
        TwoDVector twoDVector = new TwoDVector(block.getWorld().getName(), block.getLocation().getBlockX(), block.getLocation().getBlockZ());

//        for (TwoDVector n : trees.keySet()) {
//            if (n.equals(twoDVector))
//                return trees.get(n);
//        }

        return null;
    }


}
