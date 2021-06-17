package com.kennedysmithjava.prisonmines.upgrades;

import com.mcrivals.prisoncore.entity.MPlayer;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CommandUpgrade extends AbstractUpgrade{

    List<String> commands;

    CommandUpgrade(List<String> commands) {
        this.commands = commands;
    }

    CommandUpgrade(String command) {
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
