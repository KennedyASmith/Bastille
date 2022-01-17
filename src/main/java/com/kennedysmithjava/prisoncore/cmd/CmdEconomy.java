package com.kennedysmithjava.prisoncore.cmd;

import com.kennedysmithjava.prisoncore.entity.MConf;

import java.util.List;

public class CmdEconomy extends CoreCommand {
    // -------------------------------------------- //
    // INSTANCE
    // -------------------------------------------- //

    private static final CmdEconomy i = new CmdEconomy();

    // -------------------------------------------- //
    // FIELDS
    // -------------------------------------------- //
    public CmdEconomyAdd CmdEconomyAdd = new CmdEconomyAdd();
    public CmdEconomyBalance CmdEconomyBalance = new CmdEconomyBalance();

    // -------------------------------------------- //
    // CONSTRUCT
    // -------------------------------------------- //
    public CmdEconomy() {

    }

    public static CmdEconomy get() {
        return i;
    }

    // -------------------------------------------- //
    // OVERRIDE
    // -------------------------------------------- //

    @Override
    public List<String> getAliases() {
        return MConf.get().aliasesEco;
    }

}
