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
    public CmdMineSetSpawn cmdMineSize = new CmdMineSetSpawn();
    public CmdMineSetSpawn cmdMineTeleport = new CmdMineSetSpawn();

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
