package com.kennedysmithjava.prisoncore.cmd.type;

import com.kennedysmithjava.prisoncore.entity.player.MPlayer;
import com.kennedysmithjava.prisoncore.entity.player.MPlayerColl;
import com.massivecraft.massivecore.command.type.Type;

public class TypeMPlayer {
    // -------------------------------------------- //
    // INSTANCE
    // -------------------------------------------- //

    public static Type<MPlayer> get() {
        return MPlayerColl.get().getTypeEntity();
    }

}