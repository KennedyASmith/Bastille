package com.kennedysmithjava.prisoncore.cmd;

import com.massivecraft.massivecore.util.MUtil;

import java.util.List;

public class CmdPItem extends CoreCommand {
    // -------------------------------------------- //
    // INSTANCE
    // -------------------------------------------- //

    private static final CmdPItem i = new CmdPItem();

    // -------------------------------------------- //
    // FIELDS
    // -------------------------------------------- //
    public CmdPItemGive cmdPItemGive = new CmdPItemGive();

    // -------------------------------------------- //
    // CONSTRUCT
    // -------------------------------------------- //
    public CmdPItem() {

    }

    public static CmdPItem get() {
        return i;
    }

    // -------------------------------------------- //
    // OVERRIDE
    // -------------------------------------------- //

    @Override
    public List<String> getAliases() {
        return MUtil.list("pitem");
    }

}