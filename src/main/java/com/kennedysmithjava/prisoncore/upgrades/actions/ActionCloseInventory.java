package com.kennedysmithjava.prisoncore.upgrades.actions;

import com.kennedysmithjava.prisoncore.entity.player.MPlayer;
import org.bukkit.entity.Player;

public class ActionCloseInventory extends AbstractAction {


    @Override
    public void apply(MPlayer mPlayer) {
        Player player = mPlayer.getPlayer();
        if(player == null) return;
        
        player.closeInventory();
    }
}
