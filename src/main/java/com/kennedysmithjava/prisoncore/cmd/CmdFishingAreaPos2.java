package com.kennedysmithjava.prisoncore.cmd;

import com.kennedysmithjava.prisoncore.Perm;
import com.kennedysmithjava.prisoncore.util.Color;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.requirement.RequirementHasPerm;
import com.massivecraft.massivecore.command.requirement.RequirementIsPlayer;
import org.bukkit.entity.Player;

public class CmdFishingAreaPos2 extends CoreCommand {
    // -------------------------------------------- //
    // CONSTRUCT
    // -------------------------------------------- //


    public CmdFishingAreaPos2() {

        //Requirement
        this.addRequirements(RequirementIsPlayer.get(), RequirementHasPerm.get(Perm.ADMIN));

        this.setDesc("Set pos2 for creating a fishing area");
        this.setAliases("pos2");
        this.setSetupPermBaseClassName("FISH_POS_TWO");
    }

    // -------------------------------------------- //
    // OVERRIDE
    // -------------------------------------------- //

    @Override
    public void perform() throws MassiveException {
        if (!(sender instanceof Player player)) return;
        CmdFishingAreaCreate.addToPosTwoCache(player.getUniqueId(), player.getLocation());
        player.sendMessage(Color.get("&7[&fFishing&7] Position two saved."));
    }

}
