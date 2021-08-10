package com.kennedysmithjava.prisonmines.cmd;

import com.kennedysmithjava.prisonmines.entity.MinesConf;

import java.util.List;

public class CmdMine extends MineCommand {
    // -------------------------------------------- //
    // INSTANCE
    // -------------------------------------------- //

    private static CmdMine i = new CmdMine();

    // -------------------------------------------- //
    // FIELDS
    // -------------------------------------------- //
    public CmdMineTeleport CmdMineTeleport = new CmdMineTeleport();

    public CmdMine() {

    }

    // -------------------------------------------- //
    // CONSTRUCT
    // -------------------------------------------- //

    public static CmdMine get()
    {
        return i;
    }

    // -------------------------------------------- //
    // OVERRIDE
    // -------------------------------------------- //

    @Override
    public List<String> getAliases()
    {
        return MinesConf.get().aliasesM;
    }

}
