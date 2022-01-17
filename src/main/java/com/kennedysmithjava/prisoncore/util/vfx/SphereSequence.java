package com.kennedysmithjava.prisoncore.util.vfx;

import org.bukkit.Location;
import org.bukkit.util.Vector;

public class SphereSequence extends ParticleSequence {

    public SphereSequence(double radius, int lats, int longs) {
        for(int i = 0; i <= lats; i++) {
            double lat0 = Math.PI * (-0.5 + (double) (i - 1) / (double) lats);
            double z0  = Math.sin(lat0);
            double zr0 =  Math.cos(lat0);

            double lat1 = Math.PI * (-0.5 + (double) i / (double) lats);
            double z1 = Math.sin(lat1);
            double zr1 = Math.cos(lat1);

            for(int j = 0; j <= longs; j++) {
                double lng = 2 * Math.PI * (double) (j - 1) / (double) longs;
                double x = Math.cos(lng);
                double y = Math.sin(lng);
                this.vectors.add(new Vector(radius * x * zr0, radius * y * zr0, radius * z0));
                this.vectors.add(new Vector(radius * x * zr1, radius * y * zr1, radius * z1));
            }

        }
    }

    @Override
    public void draw(Location location) {
        super.draw(location);
        rotateY(2 * Math.PI / 64d);
    }
}
