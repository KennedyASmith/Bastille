package com.kennedysmithjava.prisoncore.cmd;

import com.kennedysmithjava.prisoncore.Perm;
import com.massivecraft.massivecore.command.requirement.RequirementHasPerm;
import com.massivecraft.massivecore.command.requirement.RequirementIsPlayer;
import com.massivecraft.massivecore.util.MUtil;

import java.util.List;

public class CmdSkillGive extends CoreCommand {


    // -------------------------------------------- //
    // FIELDS
    // -------------------------------------------- //
    public CmdSkillGiveXP cmdSkillGiveXP = new CmdSkillGiveXP();
    public CmdSkillGiveLevel cmdSkillGiveLevel = new CmdSkillGiveLevel();

    public CmdSkillGive() {
        //Requirement
        this.addRequirements(RequirementIsPlayer.get(), RequirementHasPerm.get(Perm.ADMIN));

        this.setSetupPermBaseClassName("SKILL_GIVE");

        //Description
        this.setDesc("Give skill XP or levels to a player.");
    }

    @Override
    public List<String> getAliases() {
        return MUtil.list("give", "g");
    }

}
