package com.kennedysmithjava.prisoncore.cmd.type;

import com.kennedysmithjava.prisoncore.crafting.PrisonObject;
import com.kennedysmithjava.prisoncore.crafting.objects.*;
import com.kennedysmithjava.prisoncore.crafting.objects.type.LogType;
import com.kennedysmithjava.prisoncore.crafting.objects.type.MetalType;
import com.kennedysmithjava.prisoncore.crafting.objects.type.StickType;
import com.kennedysmithjava.prisoncore.entity.farming.TreesConf;
import com.kennedysmithjava.prisoncore.entity.farming.objects.TreeTemplate;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.type.TypeAbstract;
import com.massivecraft.massivecore.comparator.ComparatorCaseInsensitive;
import org.bukkit.command.CommandSender;

import java.util.*;

public class TypePrisonObject extends TypeAbstract<PrisonObject> {
    // -------------------------------------------- //
    // INSTANCE & CONSTRUCT
    // -------------------------------------------- //
    public static Map<String, PrisonObject> objects = new HashMap<>();

    private static TypePrisonObject i = new TypePrisonObject();

    public TypePrisonObject() {
        super(TreeTemplate.class);

        for (StickType stickType : StickType.values()) {
            add("STICK_" + stickType.name(), new PrisonStick(stickType));
        }

        for (MetalType metalType : MetalType.values()) {
            add("METAL_" + metalType.name(), new PrisonMetal(metalType));
        }

        for (LogType logType : LogType.values()) {
            add("LOG_" + logType.name(), new PrisonLog(logType));
        }

        add("SAWDUST", new PrisonSawdust());
        add("BOWL", new PrisonBowl());
        add("STRING", new PrisonString());
        add("WOODEN_AXE", new PrisonWoodenAxe());
        add("WOODEN_SCYTHE", new PrisonWoodenScythe());
        add("WOODEN_PLANK", new PrisonWoodenPlank());
    }

    private static void add(String name, PrisonObject obj){
        objects.put(name.toLowerCase(), obj);
    }


    public static TypePrisonObject get() {
        return i;
    }

    // -------------------------------------------- //
    // OVERRIDE
    // -------------------------------------------- //



    @Override
    public PrisonObject read(String str, CommandSender sender) throws MassiveException {
        PrisonObject ret = objects.get(str);
        if (ret != null) return ret;
        throw new MassiveException().addMsg("<b>No tree matching \"<p>%s<b>\".", str);
    }

    @Override
    public Collection<String> getTabList(CommandSender sender, String arg) {
        // Create
        return objects.keySet();
    }

}