package com.kennedysmithjava.prisoncore.cmd;

import com.kennedysmithjava.prisoncore.Perm;
import com.kennedysmithjava.prisoncore.cmd.requirement.RequirementHasMine;
import com.kennedysmithjava.prisoncore.cmd.type.TypeMineOwner;
import com.kennedysmithjava.prisoncore.entity.mines.Mine;
import com.kennedysmithjava.prisoncore.entity.player.MPlayer;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.requirement.RequirementHasPerm;
import com.massivecraft.massivecore.command.requirement.RequirementIsPlayer;
import com.massivecraft.massivecore.command.type.primitive.TypeInteger;
import org.bukkit.entity.Player;

public class CmdMineSize extends CoreCommand {
    // -------------------------------------------- //
    // CONSTRUCT
    // -------------------------------------------- //

    public CmdMineSize() {
        //Requirement
        this.addRequirements(RequirementIsPlayer.get(), RequirementHasPerm.get(Perm.ADMIN), RequirementHasMine.get());

        this.addParameter( TypeMineOwner.get(), "player");
        this.addParameter(3, TypeInteger.get(), "width");
        this.addParameter(3, TypeInteger.get(), "height");

        //Description
        this.setDesc("Change the size of a mine.");
    }

    // -------------------------------------------- //
    // OVERRIDE
    // -------------------------------------------- //

    @Override
    public void perform() throws MassiveException {
        if (!(sender instanceof Player)) return;
        MPlayer mineOwner = readArg();
        int width = readArg();
        int height = readArg();
        Mine mine = mineOwner.getMine();
        mine.setHeightVar(height);
        mine.setWidth(width, () -> msg("Successfully set mine width to " + width + " and height to " + height + "."));
    }

}
