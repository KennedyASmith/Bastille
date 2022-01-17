package com.kennedysmithjava.prisoncore.cmd;

import com.kennedysmithjava.prisoncore.entity.mines.MinesConf;
import com.kennedysmithjava.prisoncore.util.Color;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CmdOffset extends CoreCommand {
    // -------------------------------------------- //
    // INSTANCE
    // -------------------------------------------- //

    private static final CmdOffset i = new CmdOffset();

    public static Map<String, Location> posOneCache = new HashMap<>();

    // -------------------------------------------- //
    // FIELDS
    // -------------------------------------------- //

    CmdOffsetPosOne offsetPosOne = new CmdOffsetPosOne();
    CmdOffsetPosTwo offsetPosTwo = new CmdOffsetPosTwo();
    CmdOffsetWand offsetWand = new CmdOffsetWand();

    // -------------------------------------------- //
    // CONSTRUCT
    // -------------------------------------------- //
    public CmdOffset() {

    }

    public static CmdOffset get() {
        return i;
    }

    // -------------------------------------------- //
    // OVERRIDE
    // -------------------------------------------- //

    @Override
    public List<String> getAliases() {
        return MinesConf.get().aliasesOffset;
    }

    public static void addToPosOneCache(String uuid, Location location){
        posOneCache.put(uuid, location);
    }

    public static void sendOffsetInfo(Player player, Location loc1, Location loc2){

        if(player == null) return;

        int xOffset = loc2.getBlockX() - loc1.getBlockX();
        int yOffset = loc2.getBlockY() - loc1.getBlockY();
        int zOffset = loc2.getBlockZ() - loc1.getBlockZ();

        String line1 = "&bX: &7" + xOffset + " &bY: &7" + yOffset + " &bZ: &7" + zOffset;
        String line2 = "&bPitch: &7" + loc2.getPitch() + " | &bYaw: &7" + loc2.getYaw();

        player.sendMessage(Color.get("&7--------[ &b&lOffset Results &7]--------"));
        player.sendMessage(Color.get(line1));
        player.sendMessage(Color.get(line2));
    }

}

