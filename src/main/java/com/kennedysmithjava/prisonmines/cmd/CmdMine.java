package com.kennedysmithjava.prisonmines.cmd;

import com.kennedysmithjava.prisonmines.PrisonMines;
import com.kennedysmithjava.prisonmines.entity.MConf;
import com.massivecraft.massivecore.command.MassiveCommandVersion;

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
        return MConf.get().aliasesM;
    }

}
