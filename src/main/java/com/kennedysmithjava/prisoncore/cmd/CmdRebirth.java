package com.kennedysmithjava.prisoncore.cmd;

import com.kennedysmithjava.prisoncore.entity.player.MPlayer;
import com.kennedysmithjava.prisoncore.util.Color;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.requirement.RequirementIsPlayer;
import com.massivecraft.massivecore.command.type.primitive.TypeInteger;
import com.massivecraft.massivecore.command.type.sender.TypePlayer;
import com.massivecraft.massivecore.util.MUtil;
import org.bukkit.entity.Player;

import java.util.List;

public class CmdRebirth extends CoreCommand
{
    // -------------------------------------------- //
    // INSTANCE
    // -------------------------------------------- //

    private static CmdRebirth i = new CmdRebirth();

    public static CmdRebirth get()
    {
        return i;
    }


    public CmdRebirth() {
        this.addRequirements(RequirementIsPlayer.get());
        this.setSetupPermBaseClassName("REBIRTHCOMMAND");
        this.addParameter(TypePlayer.get(), "player");
        this.addParameter(-1, TypeInteger.get(), "level");
    }

    // -------------------------------------------- //
    // OVERRIDE
    // -------------------------------------------- //


    @Override
    public void perform() throws MassiveException {
        if(senderIsConsole) return;
        Player player = readArg();
        int level = readArg();
        MPlayer mPlayer = MPlayer.get(player);
        mPlayer.setLife(level, true);
        me.sendMessage(Color.get("&7[&bServer&7] You have given " + player.getName() + " &7life level &b" + level));
    }

    @Override
    public List<String> getAliases()
    {
        return MUtil.list("rebirth");
    }

}
