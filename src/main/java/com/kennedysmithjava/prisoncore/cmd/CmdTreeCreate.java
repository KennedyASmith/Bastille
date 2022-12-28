package com.kennedysmithjava.prisoncore.cmd;

import com.kennedysmithjava.prisoncore.Perm;
import com.kennedysmithjava.prisoncore.cmd.type.TypeTree;
import com.kennedysmithjava.prisoncore.entity.farming.TreesConf;
import com.kennedysmithjava.prisoncore.entity.farming.objects.TreeTemplate;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.requirement.RequirementHasPerm;
import com.massivecraft.massivecore.command.requirement.RequirementIsPlayer;
import com.massivecraft.massivecore.mixin.MixinMessage;
import com.massivecraft.massivecore.util.Txt;

public class CmdTreeCreate extends CoreCommand {

    public CmdTreeCreate() {
        // Aliases
        this.addAliases("create");

        // Permissions and Requirements
        this.addRequirements(RequirementHasPerm.get(Perm.TREE));
        this.addRequirements(RequirementIsPlayer.get());

        // Parameters
        this.addParameter(TypeTree.get(), "name");
    }

    @Override
    public void perform() throws MassiveException {
        TreeTemplate treeTemplate = this.readArg();
        TreesConf.get().spawnNewTree(treeTemplate, me.getLocation().getBlock());

        MixinMessage.get().msgOne(me, Txt.parse("&eA tree has been created."));
    }

}
