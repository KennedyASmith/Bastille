package com.kennedysmithjava.prisoncore.cmd;

import com.kennedysmithjava.prisoncore.entity.player.MPlayer;
import com.kennedysmithjava.prisoncore.skill.SkillListGUI;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.util.MUtil;
import org.bukkit.entity.Player;

import java.util.List;

public class CmdSkill extends CoreCommand {
    // -------------------------------------------- //
    // INSTANCE
    // -------------------------------------------- //

    private static final CmdSkill i = new CmdSkill();

    // -------------------------------------------- //
    // FIELDS
    // -------------------------------------------- //

    CmdSkillGive cmdSkillGive = new CmdSkillGive();

    //PUT SUBCOMMANDS HERE

    // -------------------------------------------- //
    // CONSTRUCT
    // -------------------------------------------- //
    public CmdSkill() {
        this.setSetupPermBaseClassName("SKILL_GUI");
    }

    public static CmdSkill get() {
        return i;
    }

    // -------------------------------------------- //
    // OVERRIDE
    // -------------------------------------------- //

    @Override
    public List<String> getAliases() {
        return MUtil.list("skill", "skills", "sk");
    }

    @Override
    public void perform() throws MassiveException {
        if (!(sender instanceof Player)) return;
        MPlayer player = MPlayer.get(sender);
        SkillListGUI skillListGUI = new SkillListGUI(player);
        skillListGUI.open();
    }
}
