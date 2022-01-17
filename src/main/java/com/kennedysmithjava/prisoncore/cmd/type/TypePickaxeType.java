package com.kennedysmithjava.prisoncore.cmd.type;

import com.kennedysmithjava.prisoncore.entity.tools.PickaxeType;
import com.kennedysmithjava.prisoncore.entity.tools.PickaxeTypeColl;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.type.TypeAbstract;
import com.massivecraft.massivecore.comparator.ComparatorCaseInsensitive;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.Collection;
import java.util.Set;
import java.util.TreeSet;

public class TypePickaxeType extends TypeAbstract<PickaxeType> {
    // -------------------------------------------- //
    // INSTANCE & CONSTRUCT
    // -------------------------------------------- //

    private static final TypePickaxeType i = new TypePickaxeType();

    public TypePickaxeType() {
        super(PickaxeType.class);
    }

    public static TypePickaxeType get() {
        return i;
    }

    // -------------------------------------------- //
    // OVERRIDE
    // -------------------------------------------- //

    @Override
    public String getNameInner(PickaxeType value) {
        return ChatColor.stripColor(value.getName());
    }

    @Override
    public PickaxeType read(String str, CommandSender sender) throws MassiveException {
        PickaxeType ret;

        // Faction Id Exact
        if (PickaxeTypeColl.get().containsId(str)) {
            ret = PickaxeTypeColl.get().get(str);
            if (ret != null) return ret;
        }

        // Faction Name Exact
        ret = PickaxeTypeColl.get().getByName(str);
        if (ret != null) return ret;

        throw new MassiveException().addMsg("<b>No pickaxe type matching \"<p>%s<b>\".", str);
    }

    @Override
    public Collection<String> getTabList(CommandSender sender, String arg) {
        // Create
        Set<String> ret = new TreeSet<>(ComparatorCaseInsensitive.get());

        // Fill
        for (PickaxeType player : PickaxeTypeColl.get().getAll()) {
            ret.add(ChatColor.stripColor(player.getName()));
        }

        // Return
        return ret;
    }

}
