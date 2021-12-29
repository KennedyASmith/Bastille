package com.kennedysmithjava.prisonmines.cmd.requirement;

import com.massivecraft.massivecore.command.MassiveCommand;
import com.massivecraft.massivecore.command.requirement.RequirementAbstract;
import com.massivecraft.massivecore.util.Txt;
import com.mcrivals.prisoncore.entity.MPlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RequirementHasMine extends RequirementAbstract {
    private static final long serialVersionUID = 1L;
    private static RequirementHasMine i = new RequirementHasMine();

    public RequirementHasMine() {
    }

    public static RequirementHasMine get() {
        return i;
    }

    public boolean apply(CommandSender sender, MassiveCommand command) {
        MPlayer player = MPlayer.get(sender);
        return player.hasMine();
    }

    public String createErrorMessage(CommandSender sender, MassiveCommand command) {
        return Txt.parse("<b>This command can only be run if you have a personal mine.");
    }
}