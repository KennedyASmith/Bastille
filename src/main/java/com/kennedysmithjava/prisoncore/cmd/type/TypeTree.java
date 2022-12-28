package com.kennedysmithjava.prisoncore.cmd.type;

import com.kennedysmithjava.prisoncore.entity.farming.TreesConf;
import com.kennedysmithjava.prisoncore.entity.farming.objects.TreeTemplate;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.type.TypeAbstract;
import com.massivecraft.massivecore.comparator.ComparatorCaseInsensitive;
import org.bukkit.command.CommandSender;

import java.util.Collection;
import java.util.Set;
import java.util.TreeSet;

public class TypeTree extends TypeAbstract<TreeTemplate>
{
    // -------------------------------------------- //
    // INSTANCE & CONSTRUCT
    // -------------------------------------------- //

    private static TypeTree i = new TypeTree();

    public TypeTree() {
        super(TreeTemplate.class);
    }

    public static TypeTree get() { return i; }

    // -------------------------------------------- //
    // OVERRIDE
    // -------------------------------------------- //


    @Override
    public TreeTemplate read(String str, CommandSender sender) throws MassiveException
    {
        TreeTemplate ret = TreesConf.get().getTreeTemplates().get(str);
        if(ret != null)
            return ret;

        throw new MassiveException().addMsg("<b>No tree matching \"<p>%s<b>\".", str);
    }

    @Override
    public Collection<String> getTabList(CommandSender sender, String arg) {

        // Create
        Set<String> ret = new TreeSet<>(ComparatorCaseInsensitive.get());

        // Fill
        for (String tree : TreesConf.get().getTreeTemplates().keySet())
        {
            ret.add(tree);
        }

        // Return
        return ret;
    }

}