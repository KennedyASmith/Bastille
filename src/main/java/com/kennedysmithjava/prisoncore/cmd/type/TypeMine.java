package com.kennedysmithjava.prisoncore.cmd.type;

import com.kennedysmithjava.prisoncore.entity.mines.Mine;
import com.kennedysmithjava.prisoncore.entity.mines.MineColl;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.type.TypeAbstract;
import com.massivecraft.massivecore.comparator.ComparatorCaseInsensitive;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.Collection;
import java.util.Set;
import java.util.TreeSet;

public class TypeMine extends TypeAbstract<Mine>
{
    // -------------------------------------------- //
    // INSTANCE & CONSTRUCT
    // -------------------------------------------- //

    private static TypeMine i = new TypeMine();
    public static TypeMine get() { return i; }
    public TypeMine() { super(Mine.class); }

    // -------------------------------------------- //
    // OVERRIDE
    // -------------------------------------------- //

    @Override
    public String getNameInner(Mine value)
    {
        return ChatColor.stripColor(value.getName());
    }

    @Override
    public Mine read(String str, CommandSender sender) throws MassiveException
    {
        Mine ret;

        // Faction Id Exact
        if (MineColl.get().containsId(str))
        {
            ret = MineColl.get().get(str);
            if (ret != null) return ret;
        }

        throw new MassiveException().addMsg("<b>No mine matching \"<p>%s<b>\".", str);
    }

    @Override
    public Collection<String> getTabList(CommandSender sender, String arg)
    {
        // Create
        Set<String> ret = new TreeSet<>(ComparatorCaseInsensitive.get());

        // Fill
        for (String mine : MineColl.get().getIds())
        {
            ret.add(ChatColor.stripColor(mine));
        }

        // Return
        return ret;
    }

}
