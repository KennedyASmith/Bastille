package com.kennedysmithjava.prisoncore.util.vfx;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.List;

@FunctionalInterface
public interface ParticleFn {
    void drawParticle(Location location, List<Player> playerList);

    default void drawParticle(Location location) {
        drawParticle(location, null);
    }
}
