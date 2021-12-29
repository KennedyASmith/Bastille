package com.kennedysmithjava.prisonmines.cmd;

import com.kennedysmithjava.prisonmines.Perm;
import com.kennedysmithjava.prisonmines.cmd.requirement.RequirementHasMine;
import com.kennedysmithjava.prisonmines.cmd.type.TypeMineOwner;
import com.kennedysmithjava.prisonmines.entity.Mine;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.requirement.RequirementHasPerm;
import com.massivecraft.massivecore.command.requirement.RequirementIsPlayer;
import com.massivecraft.massivecore.command.type.primitive.TypeInteger;
import com.mcrivals.prisoncore.entity.MPlayer;
import org.bukkit.entity.Player;

public class CmdMineSize extends MineCommand {
    // -------------------------------------------- //
    // CONSTRUCT
    // -------------------------------------------- //

    public CmdMineSize() {
        //Requirement
        this.addRequirements(RequirementIsPlayer.get(), RequirementHasPerm.get(Perm.ADMIN), RequirementHasMine.get());

        this.addParameter(3, TypeInteger.get(), "width");
        this.addParameter(3, TypeInteger.get(), "height");
        this.addParameter( TypeMineOwner.get(), "player");

        //Description
        this.setDesc("Change the size of a mine.");
    }

    // -------------------------------------------- //
    // OVERRIDE
    // -------------------------------------------- //

    @Override
    public void perform() throws MassiveException {
        if (!(sender instanceof Player)) return;
        int width = readArg();
        int height = readArg();
        MPlayer mineOwner = readArg(MPlayer.get(sender));
        Mine mine = mineOwner.getMine();
        mine.setHeightVar(height);
        mine.setWidth(width, () -> msg("Successfully set mine width to " + width + " and height to " + height + "."));
    }

}
