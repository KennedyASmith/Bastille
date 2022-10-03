package com.kennedysmithjava.prisoncore.cmd;

import com.kennedysmithjava.prisoncore.entity.mines.MinesConf;

import java.util.List;

public class CmdMine extends CoreCommand
{
    // -------------------------------------------- //
    // INSTANCE
    // -------------------------------------------- //

    private static CmdMine i = new CmdMine();

    public static CmdMine get()
    {
        return i;
    }

    // -------------------------------------------- //
    // FIELDS
    // -------------------------------------------- //
    public CmdMineCreate cmdMineCreate = new CmdMineCreate();
    public CmdMineDelete cmdMineDelete = new CmdMineDelete();
    public CmdMineRegen cmdMineRegen = new CmdMineRegen();
    public CmdMineSetSpawn cmdMineSetSpawn = new CmdMineSetSpawn();
    public CmdMineSize cmdMineSize = new CmdMineSize();
    public CmdMineOffset cmdMineOffset = new CmdMineOffset();
    public CmdMineTeleport cmdMineTeleport = new CmdMineTeleport();

    public CmdMine() {

    }

    // -------------------------------------------- //
    // OVERRIDE
    // -------------------------------------------- //

    @Override
    public List<String> getAliases()
    {
        return MinesConf.get().aliasesMAdmin;
    }

}
