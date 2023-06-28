package com.kennedysmithjava.prisoncore.cmd.type;

import com.kennedysmithjava.prisoncore.entity.Regions;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.type.primitive.TypeString;
import org.bukkit.command.CommandSender;

import java.util.Collection;
import java.util.Set;

public class TypeNamedRegion extends TypeString {
    // -------------------------------------------- //
    // INSTANCE & CONSTRUCT
    // -------------------------------------------- //

    private static final TypeNamedRegion i = new TypeNamedRegion();

    public static TypeNamedRegion get() {
        return i;
    }

    // -------------------------------------------- //
    // OVERRIDE
    // -------------------------------------------- //

    @Override
    public String read(String str, CommandSender sender) throws MassiveException {

        // Faction Id Exact

        Set<String> regionNames = Regions.get().getRegions().keySet();
        if (regionNames.contains(str)) {
            return str;
        }

        throw new MassiveException().addMsg("<b>No region matching \"<p>%s<b>\".", str);
    }

    @Override
    public Collection<String> getTabList(CommandSender sender, String arg) {
        // Return
        return Regions.get().getRegions().keySet();
    }

}
