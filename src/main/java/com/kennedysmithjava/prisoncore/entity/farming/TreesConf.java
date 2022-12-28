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

    protected static transient TreesConf i;

    public static TreesConf get() {
        return i;
    }

    //ToDo Make it so this actually works, right now it DOESNT load in trees, it resets them on reboot.
    @Override
    public TreesConf load(TreesConf that) {
        super.load(that);
        this.trees = that.trees;
        this.treeTemplates = that.treeTemplates;
        return this;
    }

    @Getter
    public Map<TwoDVector, Tree> trees = new MassiveMap<>();

    @Getter
    public Map<String, TreeTemplate> treeTemplates = MUtil.map(
            "acacia", new TreeTemplate("acacia", "acacaia_schematic.schematic",
                    3,
                    50,
                    new BlockWrapper(Material.ACACIA_LOG),
                    new BlockWrapper(Material.STRIPPED_ACACIA_LOG),
                    new BlockWrapper(Material.ACACIA_SAPLING),
                    new Offset(2, 0, 2))
    );


    public void respawnTree() {

    }

    public void removeTree() {

    }

    private TreeTemplate getTreeTemplate(String name) {
        return treeTemplates.get(name);
    }


}
