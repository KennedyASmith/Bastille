package com.kennedysmithjava.prisoncore.cmd;

import com.kennedysmithjava.prisoncore.maps.MapUtil;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.util.MUtil;

import java.util.List;

public class CmdMap extends CoreCommand
{
    // -------------------------------------------- //
    // INSTANCE
    // -------------------------------------------- //

    private static CmdMap i = new CmdMap();

    public static CmdMap get()
    {
        return i;
    }

    // -------------------------------------------- //
    // FIELDS
    // -------------------------------------------- //
    public CmdMapCreate cmdMapCreate = new CmdMapCreate();

    public CmdMap() {

    }

    // -------------------------------------------- //
    // OVERRIDE
    // -------------------------------------------- //


    @Override
    public void perform() throws MassiveException {
        if(senderIsConsole) return;
        MapUtil.removeMaps(me);

    }

    @Override
    public List<String> getAliases()
    {
        return MUtil.list("pmap");
    }

}
