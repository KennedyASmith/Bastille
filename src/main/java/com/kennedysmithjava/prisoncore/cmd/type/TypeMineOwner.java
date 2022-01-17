package com.kennedysmithjava.prisoncore.cmd.type;

import com.kennedysmithjava.prisoncore.entity.player.MPlayer;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.type.TypeAbstract;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.*;

public class TypeMineOwner extends TypeAbstract<MPlayer>
{
    // -------------------------------------------- //
    // INSTANCE & CONSTRUCT
    // -------------------------------------------- //

    private static TypeMineOwner i = new TypeMineOwner();
    public static TypeMineOwner get() { return i; }
    public TypeMineOwner() {
        super(MPlayer.class);
    }

    // -------------------------------------------- //
    // OVERRIDE
    // -------------------------------------------- //

    @Override
    public MPlayer read(String str, CommandSender sender) throws MassiveException
    {
        MPlayer ret;
        Player player = Bukkit.getPlayer(str);

        if (player != null)
        {
            ret = MPlayer.get(player);
            if (ret != null){
                if(ret.hasMine()) return ret;
            }
        }

        throw new MassiveException().addMsg("<b>No mine owner matching \"<p>%s<b>\".", str);
    }

    @Override
    public Collection<String> getTabList(CommandSender sender, String arg)
    {
        // Create
        Set<String> ret = new HashSet<>();

        // Fill
        for (Player player : Bukkit.getOnlinePlayers())
        {
            MPlayer mPlayer = MPlayer.get(player);
            if(mPlayer == null) continue;
            if(mPlayer.hasMine()) ret.add(player.getName());
        }

        // Return
        return ret;
    }

}
