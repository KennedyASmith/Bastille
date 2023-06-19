package com.kennedysmithjava.prisoncore.entity.mines.upgrades.actions;

import com.kennedysmithjava.prisoncore.entity.player.MPlayer;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ActionCommand extends AbstractAction {

    List<String> commands;

    ActionCommand(List<String> commands) {
        this.commands = commands;
    }

    ActionCommand(String command) {
        this.commands = new ArrayList<>(Collections.singletonList(command));
    }

    @Override
    public void apply(MPlayer player) {
        commands.forEach(
                s -> {
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), s.replaceAll("%player%", player.getPlayer().getDisplayName()));
                }
        );
    }
}
