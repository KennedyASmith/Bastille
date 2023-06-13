package com.kennedysmithjava.prisoncore.cmd;

import com.kennedysmithjava.prisoncore.Perm;
import com.kennedysmithjava.prisoncore.crafting.objects.type.FishType;
import com.kennedysmithjava.prisoncore.entity.farming.FishingConf;
import com.kennedysmithjava.prisoncore.entity.farming.objects.FishingArea;
import com.kennedysmithjava.prisoncore.util.Color;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.requirement.RequirementHasPerm;
import com.massivecraft.massivecore.command.requirement.RequirementIsPlayer;
import com.massivecraft.massivecore.command.type.primitive.TypeInteger;
import com.massivecraft.massivecore.command.type.primitive.TypeString;
import com.massivecraft.massivecore.util.MUtil;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CmdFishingAreaCreate extends CoreCommand {
    // -------------------------------------------- //
    // CONSTRUCT
    // -------------------------------------------- //


    public static Map<String, Location> posOneCache = new HashMap<>();
    public static Map<String, Location> posTwoCache = new HashMap<>();

    public CmdFishingAreaCreate() {

        //Requirement
        this.addRequirements(RequirementIsPlayer.get(), RequirementHasPerm.get(Perm.ADMIN));

        // Parameters
        this.addParameter(TypeString.get(), "Name");
        this.addParameter(TypeInteger.get(), "Fishing_Level_Required");

        //Description
        this.setDesc("Create a fishing area");
        this.setAliases("create");
        this.setSetupPermBaseClassName("FISH_CREATE");
    }

    /**
     * The cache for storing pos1 for the process of creating a fishing area.
     * @param uuid uuid of the player
     * @param location corner 1 of the fishing area
     */
    public static void addToPosOneCache(UUID uuid, Location location){
        posOneCache.put(uuid.toString(), location);
    }

    /**
     * The cache for storing pos2 for the process of creating a fishing area.
     * @param uuid uuid of the player
     * @param location corner 2 of the fishing area
     */
    public static void addToPosTwoCache(UUID uuid, Location location){
        posTwoCache.put(uuid.toString(), location);
    }

    // -------------------------------------------- //
    // OVERRIDE
    // -------------------------------------------- //

    @Override
    public void perform() throws MassiveException {
        if (!(sender instanceof Player)) return;

        String uuid = ((Player) sender).getUniqueId().toString();
        String name = readArg();
        int fishing_level_required = readArg();

        if(!posTwoCache.containsKey(uuid) || !posOneCache.containsKey(uuid)){
            sender.sendMessage(Color.get("&7[&fFishing&7] Please first select an area for fishing with &f/fishing area pos1 &7and &f/fishing area pos2"));
            return;
        }

        Location pos1 = posOneCache.get(uuid);
        Location pos2 = posTwoCache.get(uuid);
        FishingArea area = new FishingArea(
                pos1.getBlockX(),
                pos2.getBlockX(),
                pos1.getBlockY(),
                pos1.getBlockZ(),
                pos2.getBlockZ(),
                fishing_level_required,
                MUtil.list(FishType.BASS, FishType.COD));

        FishingConf.get().addFishingArea(name, area);
        sender.sendMessage(Color.get("&7[&fFishing&7] &aSuccessfully created a new fishing area."));
        posOneCache.remove(uuid);
        posTwoCache.remove(uuid);
    }

}
