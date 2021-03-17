package com.kennedysmithjava.prisonmines.cmd.type;

import com.kennedysmithjava.prisonmines.entity.Mine;
import com.kennedysmithjava.prisonmines.entity.MineColl;
import com.massivecraft.massivecore.MassiveCore;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.type.TypeAbstract;
import com.massivecraft.massivecore.comparator.ComparatorCaseInsensitive;
import com.massivecraft.massivecore.util.IdUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

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

        // Faction Name Exact
        ret = MineColl.get().getByName(str);
        if (ret != null) return ret;

        throw new MassiveException().addMsg("<b>No mine matching \"<p>%s<b>\".", str);
    }

    @Override
    public Collection<String> getTabList(CommandSender sender, String arg)
    {
        // Create
        Set<String> ret = new TreeSet<>(ComparatorCaseInsensitive.get());

        // Fill
        for (Mine mine : MineColl.get().getAll())
        {
            ret.add(ChatColor.stripColor(mine.getName()));
        }

        // Return
        return ret;
    }

}
