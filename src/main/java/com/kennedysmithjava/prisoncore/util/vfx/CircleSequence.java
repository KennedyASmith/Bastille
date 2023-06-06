package com.kennedysmithjava.prisoncore.util.vfx;

import org.bukkit.util.Vector;
import org.bukkit.Location;

public class CircleSequence extends ParticleSequence {

    public CircleSequence(double radius, int points) {
        for (int i = 0; i < points; i++) {
            double angle = 2 * Math.PI * (double) i / (double) points;
            double x = Math.cos(angle);
            double z = Math.sin(angle);
            this.vectors.add(new Vector(radius * x, 0, radius * z));
        }
    }

    @Override
    public void draw(Location location) {
        super.draw(location);
        // You can add rotation logic here if desired
    }
}
