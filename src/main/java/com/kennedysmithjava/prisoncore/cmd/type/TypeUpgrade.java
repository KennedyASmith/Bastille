package com.kennedysmithjava.prisoncore.cmd.type;

import com.kennedysmithjava.prisoncore.upgrades.UpgradeName;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.type.TypeAbstract;
import com.massivecraft.massivecore.comparator.ComparatorCaseInsensitive;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.*;

public class TypeUpgrade extends TypeAbstract<String>
{
    // -------------------------------------------- //
    // INSTANCE & CONSTRUCT
    // -------------------------------------------- //

    private static TypeUpgrade i = new TypeUpgrade();
    public static TypeUpgrade get() { return i; }
    public TypeUpgrade() {
        super(String.class);
    }

    // -------------------------------------------- //
    // OVERRIDE
    // -------------------------------------------- //

    @Override
    public String read(String str, CommandSender sender) throws MassiveException
    {
        String ret;

        UpgradeName upgradeName = UpgradeName.valueOf(str);
        Set<UpgradeName> upgradeValues = new HashSet<>(Arrays.asList(UpgradeName.values()));

        // Faction Id Exact
        if (upgradeValues.contains(upgradeName))
        {
            ret = upgradeName.get();
            if (ret != null) return ret;
        }

        throw new MassiveException().addMsg("<b>No upgrade matching \"<p>%s<b>\".", str);
    }

    @Override
    public Collection<String> getTabList(CommandSender sender, String arg)
    {
        // Create
        Set<String> ret = new TreeSet<>(ComparatorCaseInsensitive.get());

        // Fill
        for (UpgradeName upgrade : UpgradeName.values())
        {
            ret.add(ChatColor.stripColor(upgrade.get()));
        }

        // Return
        return ret;
    }

}
