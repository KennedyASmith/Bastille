package com.kennedysmithjava.prisonmines.entity;

import com.kennedysmithjava.prisonmines.util.BlockMaterial;
import com.kennedysmithjava.prisonmines.util.Color;
import com.kennedysmithjava.prisonmines.util.DistributionPage;
import com.massivecraft.massivecore.command.editor.annotation.EditorName;
import com.massivecraft.massivecore.nms.NmsItemStackCreate;
import com.massivecraft.massivecore.nms.NmsRecipe;
import com.massivecraft.massivecore.store.Entity;
import com.massivecraft.massivecore.util.MUtil;
import com.sk89q.jnbt.NBTUtils;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@EditorName("config")
public class BlocksConf extends Entity<BlocksConf>
{
    // -------------------------------------------- //
    // META
    // -------------------------------------------- //

    protected static transient BlocksConf i;


    public static BlocksConf get() { return i; }

    @Override
    public BlocksConf load(BlocksConf that)
    {
        super.load(that);
        return this;
    }

    // -------------------------------------------- //
    // COMMAND ALIASES
    // -------------------------------------------- //

    public Map<Integer, PrisonBlock> blocks = MUtil.map(

            //Common Rocks
            1,new PrisonBlock("Cobweb", new BlockMaterial(Material.WEB), new BlockMaterial(Material.WEB), 0.01, MUtil.list("&7Sticky!")),
    2, new PrisonBlock("&7(&e✦✧✧&7) &7Pebble", new BlockMaterial(Material.STONE, 5),new BlockMaterial(Material.STONE, 5), 0.01, MUtil.list("&7Good for skipping on water!")),
            3, new PrisonBlock("&7(&e✦✦✧&7) &7Pebble", new BlockMaterial(Material.STONE, 5),new BlockMaterial(Material.STONE, 5), 0.02, MUtil.list("&7Great for skipping on water!")),
            4, new PrisonBlock("&7(&e✦✦✦&7) &7Pebble", new BlockMaterial(Material.STONE, 5),new BlockMaterial(Material.STONE, 5), 0.03, MUtil.list("&7Amazing for skipping on water!")),
            5, new PrisonBlock("&7(&e✦✧&7) &7Gravel", new BlockMaterial(Material.COBBLESTONE, 5),new BlockMaterial(Material.COBBLESTONE, 5), 0.05, MUtil.list("&7Don't fall on it!")),
            6, new PrisonBlock("&7(&e✦✦&7) &7Gravel", new BlockMaterial(Material.COBBLESTONE, 5),new BlockMaterial(Material.COBBLESTONE, 5), 0.08, MUtil.list("&7Don't fall on it!")),
            7, new PrisonBlock("&7(&e✦✧✧&7) &7Rubble", new BlockMaterial(Material.STONE, 5),new BlockMaterial(Material.STONE, 5), 0.10, MUtil.list("&7Old construction site leftovers.")),
            8, new PrisonBlock("&7(&e✦✦✧&7) &7Rubble", new BlockMaterial(Material.STONE, 5),new BlockMaterial(Material.STONE, 5), 0.15, MUtil.list("&7Old construction site leftovers.")),
            9, new PrisonBlock("&7(&e✦✦✦&7) &7Rubble", new BlockMaterial(Material.STONE, 5),new BlockMaterial(Material.STONE, 5), 0.25, MUtil.list("&7Old construction site leftovers.")),
            10, new PrisonBlock("&7(&e✦✧&7) &7Cobblestone", new BlockMaterial(Material.COBBLESTONE, 5),new BlockMaterial(Material.COBBLESTONE, 5), 0.40, MUtil.list("&7Great material for pathways.")),
            11, new PrisonBlock("&7(&e✦✦&7) &7Cobblestone", new BlockMaterial(Material.COBBLESTONE, 5),new BlockMaterial(Material.COBBLESTONE, 5), 0.60, MUtil.list("&7Amazing material for pathways.")),
            12, new PrisonBlock("&7(&e✦✧&7) &7Rock", new BlockMaterial(Material.COBBLESTONE, 5),new BlockMaterial(Material.COBBLESTONE, 5), 0.85, MUtil.list("&7A good first pet.")),
            13, new PrisonBlock("&7(&e✦✦&7) &7Rock", new BlockMaterial(Material.COBBLESTONE, 5),new BlockMaterial(Material.COBBLESTONE, 5), 1.25, MUtil.list("&7A good first pet.")),
            14, new PrisonBlock("&7(&e✦✧&7) &7Boulder", new BlockMaterial(Material.STONE),new BlockMaterial(Material.STONE), 1.75, MUtil.list("&7Heavy!")),
            15, new PrisonBlock("&7(&e✦✦&7) &7Boulder", new BlockMaterial(Material.STONE),new BlockMaterial(Material.STONE), 2.50, MUtil.list("&7Heavy!")),
            16, new PrisonBlock("&7(&e✦✧✧&7) &7Slate", new BlockMaterial(Material.STONE, 6),new BlockMaterial(Material.STONE, 6), 3.25, MUtil.list("&7A layered, shiny, clay-like material.")),
            17, new PrisonBlock("&7(&e✦✦✧&7) &7Slate", new BlockMaterial(Material.STONE, 6),new BlockMaterial(Material.STONE, 6), 4.00, MUtil.list("&7A layered, shiny, clay-like material.")),
            18, new PrisonBlock("&7(&e✦✦✦&7) &7Slate", new BlockMaterial(Material.STONE, 6),new BlockMaterial(Material.STONE, 6), 5.00, MUtil.list("&7A layered, shiny, clay-like material.")),

            //Gemstone
            19, new PrisonBlock("&7Onyx", new BlockMaterial(Material.COBBLESTONE, 5),new BlockMaterial(Material.COBBLESTONE, 5), 0.05, MUtil.list("&7A layered, shiny, clay-like material.")),
            20, new PrisonBlock("&7Amethyst", new BlockMaterial(Material.COBBLESTONE, 5),new BlockMaterial(Material.COBBLESTONE, 5), 0.08, MUtil.list("&7A layered, shiny, clay-like material.")),
            21, new PrisonBlock("&7Ruby", new BlockMaterial(Material.STONE, 5),new BlockMaterial(Material.COBBLESTONE, 5), 0.10, MUtil.list("&7A layered, shiny, clay-like material.")),
            22, new PrisonBlock("&7Emerald", new BlockMaterial(Material.STONE, 5),new BlockMaterial(Material.COBBLESTONE, 5), 0.15, MUtil.list("&7A layered, shiny, clay-like material.")),
            23, new PrisonBlock("&7Sapphire", new BlockMaterial(Material.STONE, 5),new BlockMaterial(Material.COBBLESTONE, 5), 0.25, MUtil.list("&7A layered, shiny, clay-like material.")),
            24, new PrisonBlock("&7Opal", new BlockMaterial(Material.COBBLESTONE, 5),new BlockMaterial(Material.COBBLESTONE, 5), 0.05, MUtil.list("&7A layered, shiny, clay-like material.")),
            25, new PrisonBlock("&7Jadeite", new BlockMaterial(Material.COBBLESTONE, 5),new BlockMaterial(Material.COBBLESTONE, 5), 0.08, MUtil.list("&7A layered, shiny, clay-like material.")),
            26, new PrisonBlock("&7Diamond", new BlockMaterial(Material.STONE, 5),new BlockMaterial(Material.COBBLESTONE, 5), 0.10, MUtil.list("&7A layered, shiny, clay-like material.")),
            27, new PrisonBlock("&7Jasper", new BlockMaterial(Material.STONE, 5),new BlockMaterial(Material.COBBLESTONE, 5), 0.15, MUtil.list("&7A layered, shiny, clay-like material.")),
            28, new PrisonBlock("&7Opal", new BlockMaterial(Material.STONE, 5),new BlockMaterial(Material.COBBLESTONE, 5), 0.25, MUtil.list("&7A layered, shiny, clay-like material."))
    );

}

class PrisonBlock{

    String name;
    double value;
    BlockMaterial product;
    BlockMaterial block;
    boolean isSpecial;
    List<String> lore;

    PrisonBlock(String name, BlockMaterial block, BlockMaterial product, double value, List<String> lore){
        this.name = name;
        this.block = block;
        this.product = product;
        this.value = value;
        this.lore = lore;
        this.isSpecial = false;
    }

    PrisonBlock(String name, BlockMaterial block, BlockMaterial product, double value, List<String> lore, boolean isSpecial){
        this.name = name;
        this.block = block;
        this.product = product;
        this.value = value;
        this.lore = lore;
        this.isSpecial = isSpecial;
    }

    public String getName() {
        return name;
    }

    public double getValue() {
        return value;
    }

    public BlockMaterial getBlock() {
        return block;
    }

    public BlockMaterial getProduct() {
        return product;
    }

    public boolean isSpecial() {
        return isSpecial;
    }

    public List<String> getLore() {
        return lore;
    }

    public ItemStack getProductItem(int amount){
        ItemStack base = getProduct().getItem(amount);
        ItemMeta meta = base.getItemMeta();
        meta.setDisplayName(Color.get(getName()));
        meta.setLore(getLore());
        base.setItemMeta(meta);
        return base;
    }

    @Override
    public String toString() {
        return "PrisonBlock{" +
                "name='" + name + '\'' +
                ", value=" + value +
                ", product=" + product +
                ", block=" + block +
                ", isSpecial=" + isSpecial +
                '}';
    }
}