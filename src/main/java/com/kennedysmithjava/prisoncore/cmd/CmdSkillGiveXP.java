package com.kennedysmithjava.prisoncore.cmd;

import com.kennedysmithjava.prisoncore.Perm;
import com.kennedysmithjava.prisoncore.cmd.type.TypeMPlayer;
import com.kennedysmithjava.prisoncore.cmd.type.TypeSkill;
import com.kennedysmithjava.prisoncore.engine.EngineXP;
import com.kennedysmithjava.prisoncore.entity.player.MPlayer;
import com.kennedysmithjava.prisoncore.skill.SkillType;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.requirement.RequirementHasPerm;
import com.massivecraft.massivecore.command.requirement.RequirementIsPlayer;
import com.massivecraft.massivecore.command.type.primitive.TypeInteger;
import com.massivecraft.massivecore.util.MUtil;
import org.bukkit.entity.Player;

import java.util.List;

public class CmdSkillGiveXP extends CoreCommand {


    // -------------------------------------------- //
    // FIELDS
    // -------------------------------------------- //

    public CmdSkillGiveXP() {
        //Requirement
        this.addRequirements(RequirementIsPlayer.get(), RequirementHasPerm.get(Perm.ADMIN));

        this.setSetupPermBaseClassName("SKILL_GIVE");

        // Parameters
        this.addParameter(TypeMPlayer.get(), "player");
        this.addParameter(TypeSkill.get(), "skillType");
        this.addParameter(TypeInteger.get(), "xp");
    }

    @Override
    public List<String> getAliases() {
        return MUtil.list("xp");
    }


    @Override
    public void perform() throws MassiveException {

        if (!(sender instanceof Player)) return;
        MPlayer mPlayer = readArg();
        SkillType type = readArg();
        int xp = readArg();

        EngineXP.giveXP(type, mPlayer, xp);
    }

}
