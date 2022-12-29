package com.kennedysmithjava.prisoncore.entity.farming;

import com.kennedysmithjava.prisoncore.blockhandler.BlockWrapper;
import com.kennedysmithjava.prisoncore.entity.farming.objects.Tree;
import com.kennedysmithjava.prisoncore.entity.farming.objects.TreeTemplate;
import com.kennedysmithjava.prisoncore.entity.farming.objects.TwoDVector;
import com.kennedysmithjava.prisoncore.util.regions.Offset;
import com.massivecraft.massivecore.collections.MassiveMap;
import com.massivecraft.massivecore.command.editor.annotation.EditorName;
import com.massivecraft.massivecore.store.Entity;
import com.massivecraft.massivecore.util.MUtil;
import lombok.Getter;
import org.bukkit.Material;

import java.util.Map;

@EditorName("config")
public class TreesConf extends Entity<TreesConf> {

    // -------------------------------------------- //
    // META
    // -------------------------------------------- //

    protected static TreesConf i;

    public static TreesConf get() {
        return i;
    }

    @Override
    public TreesConf load(TreesConf that) {
        this.setTrees(that.trees);
        this.setTreeTemplates(that.treeTemplates);
        return this;
    }

    public Map<TwoDVector, Tree> trees = new MassiveMap<>();

    public Map<String, TreeTemplate> treeTemplates = MUtil.map(
            "acacia", new TreeTemplate("acacia", "acacaia_schematic.schematic",
                    3,
                    50,
                    new BlockWrapper(Material.ACACIA_LOG),
                    new BlockWrapper(Material.STRIPPED_ACACIA_LOG),
                    new BlockWrapper(Material.ACACIA_SAPLING),
                    new Offset(2, 0, 2))
    );

    private TreeTemplate getTreeTemplate(String name) {
        return treeTemplates.get(name);
    }


    public Map<String, TreeTemplate> getTreeTemplates() {
        return treeTemplates;
    }

    public void setTreeTemplates(Map<String, TreeTemplate> treeTemplates) {
        this.treeTemplates = treeTemplates;
        this.changed();
    }

    public Map<TwoDVector, Tree> getTrees() {
        return trees;
    }

    public void addTree(TwoDVector vector, Tree tree){
        trees.put(vector, tree);
        this.changed();
    }

    public void setTrees(Map<TwoDVector, Tree> trees) {
        this.trees = trees;
        this.changed();
    }
}
