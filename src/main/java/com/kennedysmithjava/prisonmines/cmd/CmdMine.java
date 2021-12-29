package com.kennedysmithjava.prisonmines.cmd;

import com.kennedysmithjava.prisonmines.entity.MinesConf;

import java.util.List;

public class CmdMine extends MineCommand
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
