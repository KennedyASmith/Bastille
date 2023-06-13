package com.kennedysmithjava.prisoncore.cmd;

import com.kennedysmithjava.prisoncore.Perm;
import com.kennedysmithjava.prisoncore.util.Color;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.requirement.RequirementHasPerm;
import com.massivecraft.massivecore.command.requirement.RequirementIsPlayer;
import org.bukkit.entity.Player;

public class CmdFishingAreaPos1 extends CoreCommand {
    // -------------------------------------------- //
    // CONSTRUCT
    // -------------------------------------------- //


    public CmdFishingAreaPos1() {

        //Requirement
        this.addRequirements(RequirementIsPlayer.get(), RequirementHasPerm.get(Perm.ADMIN));

        this.setDesc("Set pos1 for creating a fishing area");
        this.setAliases("pos1");
        this.setSetupPermBaseClassName("FISH_POS_ONE");
    }

    // -------------------------------------------- //
    // OVERRIDE
    // -------------------------------------------- //

    @Override
    public void perform() throws MassiveException {
        if (!(sender instanceof Player player)) return;
        CmdFishingAreaCreate.addToPosOneCache(player.getUniqueId(), player.getLocation());
        player.sendMessage(Color.get("&7[&fFishing&7] Position one saved."));
    }

}
