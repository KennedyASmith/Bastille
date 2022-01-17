package com.kennedysmithjava.prisoncore.entity.mines;

import com.kennedysmithjava.prisoncore.blockhandler.BlockWrapper;
import com.kennedysmithjava.prisoncore.blockhandler.Product;
import com.kennedysmithjava.prisoncore.entity.mines.objects.PrisonBlock;
import com.massivecraft.massivecore.command.editor.annotation.EditorName;
import com.massivecraft.massivecore.store.Entity;
import com.massivecraft.massivecore.util.MUtil;
import org.bukkit.Material;

import java.util.Map;

@EditorName("config")
public class BlocksConf extends Entity<BlocksConf> {
    // -------------------------------------------- //
    // META
    // -------------------------------------------- //

    protected static transient BlocksConf i;


    public int getId(PrisonBlock block) {
        return this.blocks.entrySet().stream().filter(e -> e.getValue().equals(block)).map(Map.Entry::getKey).findAny().orElse(-1);
    }
    public Map<Integer, PrisonBlock> blocks = MUtil.map(

            //Common Rocks
            1, new PrisonBlock("Cobweb", new BlockWrapper(Material.COBWEB), new Product(Material.COBWEB), 0.01, MUtil.list("&7Sticky!")),
            2, new PrisonBlock("&7(&e✦✧✧&7) &7Pebble", new BlockWrapper(Material.STONE), new Product(Material.STONE), 0.01, MUtil.list("&7Good for skipping on water!")),
            3, new PrisonBlock("&7(&e✦✦✧&7) &7Pebble", new BlockWrapper(Material.STONE), new Product(Material.STONE), 0.02, MUtil.list("&7Great for skipping on water!")),
            4, new PrisonBlock("&7(&e✦✦✦&7) &7Pebble", new BlockWrapper(Material.STONE), new Product(Material.STONE), 0.03, MUtil.list("&7Amazing for skipping on water!")),
            5, new PrisonBlock("&7(&e✦✧&7) &7Gravel", new BlockWrapper(Material.COBBLESTONE), new Product(Material.COBBLESTONE), 0.05, MUtil.list("&7Don't fall on it!")),
            6, new PrisonBlock("&7(&e✦✦&7) &7Gravel", new BlockWrapper(Material.COBBLESTONE), new Product(Material.COBBLESTONE), 0.08, MUtil.list("&7Don't fall on it!")),
            7, new PrisonBlock("&7(&e✦✧✧&7) &7Rubble", new BlockWrapper(Material.STONE), new Product(Material.STONE), 0.10, MUtil.list("&7Old construction site leftovers.")),
            8, new PrisonBlock("&7(&e✦✦✧&7) &7Rubble", new BlockWrapper(Material.STONE), new Product(Material.STONE), 0.15, MUtil.list("&7Old construction site leftovers.")),
            9, new PrisonBlock("&7(&e✦✦✦&7) &7Rubble", new BlockWrapper(Material.STONE), new Product(Material.STONE), 0.25, MUtil.list("&7Old construction site leftovers.")),
            10, new PrisonBlock("&7(&e✦✧&7) &7Cobblestone", new BlockWrapper(Material.COBBLESTONE), new Product(Material.COBBLESTONE), 0.40, MUtil.list("&7Great material for pathways.")),
            11, new PrisonBlock("&7(&e✦✦&7) &7Cobblestone", new BlockWrapper(Material.COBBLESTONE), new Product(Material.COBBLESTONE), 0.60, MUtil.list("&7Amazing material for pathways.")),
            12, new PrisonBlock("&7(&e✦✧&7) &7Rock", new BlockWrapper(Material.COBBLESTONE), new Product(Material.COBBLESTONE), 0.85, MUtil.list("&7A good first pet.")),
            13, new PrisonBlock("&7(&e✦✦&7) &7Rock", new BlockWrapper(Material.COBBLESTONE), new Product(Material.COBBLESTONE), 1.25, MUtil.list("&7A good first pet.")),
            14, new PrisonBlock("&7(&e✦✧&7) &7Boulder", new BlockWrapper(Material.STONE), new Product(Material.STONE), 1.75, MUtil.list("&7Heavy!")),
            15, new PrisonBlock("&7(&e✦✦&7) &7Boulder", new BlockWrapper(Material.STONE), new Product(Material.STONE), 2.50, MUtil.list("&7Heavy!")),
            16, new PrisonBlock("&7(&e✦✧✧&7) &7Slate", new BlockWrapper(Material.STONE), new Product(Material.STONE), 3.25, MUtil.list("&7A layered, shiny, clay-like material.")),
            17, new PrisonBlock("&7(&e✦✦✧&7) &7Slate", new BlockWrapper(Material.STONE), new Product(Material.STONE), 4.00, MUtil.list("&7A layered, shiny, clay-like material.")),
            18, new PrisonBlock("&7(&e✦✦✦&7) &7Slate", new BlockWrapper(Material.STONE), new Product(Material.STONE), 5.00, MUtil.list("&7A layered, shiny, clay-like material.")),

            //Gemstone
            19, new PrisonBlock("&7Onyx", new BlockWrapper(Material.COBBLESTONE), new Product(Material.COBBLESTONE), 0.05, MUtil.list("&7A layered, shiny, clay-like material.")),
            20, new PrisonBlock("&7Amethyst", new BlockWrapper(Material.COBBLESTONE), new Product(Material.COBBLESTONE), 0.08, MUtil.list("&7A layered, shiny, clay-like material.")),
            21, new PrisonBlock("&7Ruby", new BlockWrapper(Material.STONE), new Product(Material.COBBLESTONE), 0.10, MUtil.list("&7A layered, shiny, clay-like material.")),
            22, new PrisonBlock("&7Emerald", new BlockWrapper(Material.STONE), new Product(Material.COBBLESTONE), 0.15, MUtil.list("&7A layered, shiny, clay-like material.")),
            23, new PrisonBlock("&7Sapphire", new BlockWrapper(Material.STONE), new Product(Material.COBBLESTONE), 0.25, MUtil.list("&7A layered, shiny, clay-like material.")),
            24, new PrisonBlock("&7Opal", new BlockWrapper(Material.COBBLESTONE), new Product(Material.COBBLESTONE), 0.05, MUtil.list("&7A layered, shiny, clay-like material.")),
            25, new PrisonBlock("&7Jadeite", new BlockWrapper(Material.COBBLESTONE), new Product(Material.COBBLESTONE), 0.08, MUtil.list("&7A layered, shiny, clay-like material.")),
            26, new PrisonBlock("&7Diamond", new BlockWrapper(Material.STONE), new Product(Material.COBBLESTONE), 0.10, MUtil.list("&7A layered, shiny, clay-like material.")),
            27, new PrisonBlock("&7Jasper", new BlockWrapper(Material.STONE), new Product(Material.COBBLESTONE), 0.15, MUtil.list("&7A layered, shiny, clay-like material.")),
            28, new PrisonBlock("&7Opal", new BlockWrapper(Material.STONE), new Product(Material.COBBLESTONE), 0.25, MUtil.list("&7A layered, shiny, clay-like material."))
    );

    public static BlocksConf get() {
        return i;
    }

    // -------------------------------------------- //
    // COMMAND ALIASES
    // -------------------------------------------- //

    @Override
    public BlocksConf load(BlocksConf that) {
        super.load(that);
        return this;
    }

}

