package com.kennedysmithjava.prisoncore.entity.player;

import com.massivecraft.massivecore.MassiveCore;
import com.massivecraft.massivecore.store.SenderColl;
import org.bukkit.entity.Player;

import java.util.UUID;

public class MPlayerColl extends SenderColl<MPlayer> {
    // -------------------------------------------- //
    // INSTANCE & CONSTRUCT
    // -------------------------------------------- //

    private static final MPlayerColl i = new MPlayerColl();

    public static MPlayerColl get() {
        return i;
    }

    // -------------------------------------------- //
    // STACK TRACEABILITY
    // -------------------------------------------- //

    @Override
    public void onTick() {
        super.onTick();
    }

    // -------------------------------------------- //
    // OVERRIDE: COLL
    // -------------------------------------------- //

    @Override
    public void setActive(boolean active) {
        super.setActive(active);
        if (!active) return;
    }

    public MPlayer getByUUID(UUID uuid) {
        for (MPlayer player : this.getAll()) {

            if (player.getUuid().equals(uuid)) {
                return player;
            }
        }
        return null;
    }

    public MPlayer getByUUID(String uuid) {
        for (MPlayer player : this.getAll()) {

            if (player.getUuid().toString().equals(uuid)) {
                return player;
            }
        }
        return null;
    }

    public MPlayer getByPlayer(Player player) {
        for (MPlayer mPlayer : this.getAll()) {
            if (mPlayer.getUuid().equals(player.getUniqueId())) {
                return mPlayer;
            }
        }
        return null;
    }

}
