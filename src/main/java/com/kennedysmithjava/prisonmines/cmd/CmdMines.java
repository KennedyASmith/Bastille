package com.kennedysmithjava.prisonmines.cmd;

import com.kennedysmithjava.prisonmines.PrisonMines;
import com.kennedysmithjava.prisonmines.entity.MinesConf;
import com.massivecraft.massivecore.command.MassiveCommandVersion;

import java.util.List;

public class CmdMines extends MineCommand
{
    // -------------------------------------------- //
    // INSTANCE
    // -------------------------------------------- //

    private static CmdMines i = new CmdMines();

    // -------------------------------------------- //
    // FIELDS
    // -------------------------------------------- //
    public CmdMinesCreate cmdMinesCreate = new CmdMinesCreate();
    public CmdMinesRename cmdMinesRename = new CmdMinesRename();
    public CmdMinesDelete cmdMinesDelete = new CmdMinesDelete();
    public CmdMinesRegen cmdMinesRegen = new CmdMinesRegen();
    public CmdMinesSetSpawn cmdMinesSetSpawn = new CmdMinesSetSpawn();
    public CmdMinesFloor cmdMinesFloor = new CmdMinesFloor();

    public MassiveCommandVersion cmdFactionsVersion = new MassiveCommandVersion(PrisonMines.get()).setAliases("v", "version");
    public CmdMines() {

    }

    // -------------------------------------------- //
    // CONSTRUCT
    // -------------------------------------------- //

    public static CmdMines get()
    {
        return i;
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
