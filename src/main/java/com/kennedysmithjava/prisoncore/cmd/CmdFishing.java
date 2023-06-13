package com.kennedysmithjava.prisoncore.cmd;

import com.kennedysmithjava.prisoncore.Perm;
import com.massivecraft.massivecore.command.requirement.RequirementHasPerm;
import com.massivecraft.massivecore.command.requirement.RequirementIsPlayer;
import com.massivecraft.massivecore.util.MUtil;

import java.util.List;

@SuppressWarnings("unused")
public class CmdFishing extends CoreCommand
{
    // -------------------------------------------- //
    // INSTANCE
    // -------------------------------------------- //

    private static final CmdFishing i = new CmdFishing();

    public static CmdFishing get()
    {
        return i;
    }

    // -------------------------------------------- //
    // FIELDS
    // -------------------------------------------- //
    public CmdFishingAreaCreate fishingAreaCreate = new CmdFishingAreaCreate();
    public CmdFishingAreaPos1 cmdFishingAreaPos1 = new CmdFishingAreaPos1();
    public CmdFishingAreaPos2 cmdFishingAreaPos2 = new CmdFishingAreaPos2();

    public CmdFishing() {
        this.addRequirements(RequirementIsPlayer.get(), RequirementHasPerm.get(Perm.ADMIN));
    }

    // -------------------------------------------- //
    // OVERRIDE
    // -------------------------------------------- //

    @Override
    public List<String> getAliases()
    {
        return MUtil.list("fishing", "fish");
    }

}
