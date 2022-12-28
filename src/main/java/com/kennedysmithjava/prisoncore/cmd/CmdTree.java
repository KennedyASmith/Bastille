package com.kennedysmithjava.prisoncore.cmd;

import com.massivecraft.massivecore.util.MUtil;

import java.util.List;

public class CmdTree extends CoreCommand {
    // -------------------------------------------- //
    // INSTANCE
    // -------------------------------------------- //

    private static final CmdTree i = new CmdTree();

    // -------------------------------------------- //
    // FIELDS
    // -------------------------------------------- //
    public CmdTreeCreate cmdTreeCreate = new CmdTreeCreate();

    // -------------------------------------------- //
    // CONSTRUCT
    // -------------------------------------------- //
    public CmdTree() {

    }

    public static CmdTree get() {
        return i;
    }

    // -------------------------------------------- //
    // OVERRIDE
    // -------------------------------------------- //

    @Override
    public List<String> getAliases() {
        return MUtil.list("ptree");
    }

}