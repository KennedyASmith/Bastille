package com.kennedysmithjava.prisoncore.cmd;

import com.massivecraft.massivecore.util.MUtil;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CmdPRegion extends CoreCommand {
    // -------------------------------------------- //
    // INSTANCE
    // -------------------------------------------- //

    private static final CmdPRegion i = new CmdPRegion();

    public static Map<String, Location> posOneCache = new HashMap<>();
    public static Map<String, Location> posTwoCache = new HashMap<>();

    // -------------------------------------------- //
    // FIELDS
    // -------------------------------------------- //

    CmdPRegionPosOne pRegionPosOne = new CmdPRegionPosOne();
    CmdPRegionPosTwo pRegionPosTwo = new CmdPRegionPosTwo();
    CmdPRegionAdd pRegionAdd = new CmdPRegionAdd();
    CmdPRegionCreate pRegionCreate = new CmdPRegionCreate();
    CmdPRegionVisualize regionVisualize = new CmdPRegionVisualize();
    CmdPRegionUndo regionUndo = new CmdPRegionUndo();

    // -------------------------------------------- //
    // CONSTRUCT
    // -------------------------------------------- //
    public CmdPRegion() {

    }

    public static CmdPRegion get() {
        return i;
    }

    // -------------------------------------------- //
    // OVERRIDE
    // -------------------------------------------- //

    @Override
    public List<String> getAliases() {
        return MUtil.list("reg", "pregion");
    }

    public static void addToPosOneCache(String uuid, Location location){
        posOneCache.put(uuid, location);
    }

    public static void addToPosTwoCache(String uuid, Location location){
        posTwoCache.put(uuid, location);
    }


    public static boolean bothCachesContain(Player player){
        if(posTwoCache.get(player.getUniqueId().toString()) == null) return false;
        return posOneCache.get(player.getUniqueId().toString()) != null;
    }
    public static Location posTwoCacheGet(Player player) {
        return posTwoCache.get(player.getUniqueId().toString());
    }

    public static Location posOneCacheGet(Player player) {
        return posOneCache.get(player.getUniqueId().toString());
    }
}

