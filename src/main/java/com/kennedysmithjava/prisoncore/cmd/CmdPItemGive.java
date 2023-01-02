package com.kennedysmithjava.prisoncore.cmd;

import com.kennedysmithjava.prisoncore.Perm;
import com.kennedysmithjava.prisoncore.cmd.type.TypePrisonObject;
import com.kennedysmithjava.prisoncore.cmd.type.TypeTree;
import com.kennedysmithjava.prisoncore.crafting.PrisonObject;
import com.kennedysmithjava.prisoncore.engine.EngineTrees;
import com.kennedysmithjava.prisoncore.entity.farming.objects.TreeTemplate;
import com.kennedysmithjava.prisoncore.util.Color;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.requirement.RequirementHasPerm;
import com.massivecraft.massivecore.command.requirement.RequirementIsPlayer;
import com.massivecraft.massivecore.command.type.primitive.TypeInteger;
import com.massivecraft.massivecore.mixin.MixinMessage;
import com.massivecraft.massivecore.util.Txt;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;

public class CmdPItemGive extends CoreCommand {

    public CmdPItemGive() {
        // Aliases
        this.addAliases("give");

        // Permissions and Requirements
        this.addRequirements(RequirementHasPerm.get(Perm.PITEM));
        this.addRequirements(RequirementIsPlayer.get());

        // Parameters
        this.addParameter(TypePrisonObject.get(), "name");
        this.addParameter(TypeInteger.get(), "amount");
    }

    @Override
    public void perform() throws MassiveException {
        PrisonObject object = this.readArg();
        int amount = this.readArg();
        me.getInventory().addItem(object.give(amount));
        msg(Color.get("&7[Server] You've been given &ex" + amount + " of &e" + ChatColor.stripColor(Color.get(object.getName()))));
    }

}
