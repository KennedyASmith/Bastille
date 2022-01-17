package com.kennedysmithjava.prisoncore.util.vfx;


import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import xyz.xenondevs.particle.ParticleEffect;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public abstract class ParticleSequence {

    //Vectors are mutable!!!
    //Use ArrayList for fast indexing
    protected List<Vector> vectors = new ArrayList<>();

    protected double divisions = 10d;

    protected static final int PARTICLE_RANGE = 50*50;

    protected ParticleFn particleFn = (loc, list) -> {
        list.forEach(player -> ParticleEffect.REDSTONE.display(loc, Color.BLACK, player));
    };

    public List<Player> getParticlePlayers(Location location) {
        return Bukkit.getOnlinePlayers()
                .stream()
                .filter(player -> player.getLocation().getWorld().getName().equals(location.getWorld().getName())
                        && player.getLocation().distanceSquared(location) <= PARTICLE_RANGE)
                .collect(Collectors.toList());
    }

    public void draw(Location location) {
        List<Player> playerList = getParticlePlayers(location);
        for(int i = 0; i < vectors.size(); i++) {
            Vector curr = vectors.get(i);

            if (i == vectors.size() - 1) {
                particleFn.drawParticle(location.clone().add(curr), playerList);
                continue;
            }

            Vector next = vectors.get(i + 1);

            for(double k = 0d; k < 1d; k += 1d / divisions) {
                Vector vec = next.clone().subtract(curr).multiply(k).add(curr);
                particleFn.drawParticle(location.clone().add(vec), playerList);
            }
        }
    }

    public void setParticleFn(ParticleFn particleFn) {
        this.particleFn = particleFn;
    }

    public void setDivisions(double divisions) {
        this.divisions = divisions;
    }

    public void rotateY(double angle) {
        rotateY(new Vector(0, 0, 0), angle);
    }

    public void rotateY(Vector origin, double angle) {
        for(Vector vector : this.vectors) {
            rotateVectorY(vector, origin, angle);
        }
    }

    public void rotateX(double angle) {
        rotateX(new Vector(0, 0, 0), angle);
    }

    public void rotateX(Vector origin, double angle) {
        for(Vector vector : this.vectors) {
            rotateVectorX(vector, origin, angle);
        }
    }

    public void rotateZ(double angle) {
        rotateZ(new Vector(0, 0, 0), angle);
    }

    public void rotateZ(Vector origin, double angle) {
        for(Vector vector : this.vectors) {
            rotateVectorZ(vector, origin, angle);
        }
    }

    public static void rotateVectorY(Vector vector, Vector origin, double angle) {
        double s = Math.sin(angle);
        double c = Math.cos(angle);

        // translate point back to origin:
        vector.subtract(origin);

        // rotate point
        double xnew = vector.getX() * c - vector.getZ() * s;
        double znew = vector.getX() * s + vector.getZ() * c;
        vector.setX(xnew);
        vector.setZ(znew);
        // translate point back:
        vector.add(origin);
    }

    public static void rotateVectorX(Vector vector, Vector origin, double angle) {
        double s = Math.sin(angle);
        double c = Math.cos(angle);

        // translate point back to origin:
        vector.subtract(origin);

        // rotate point
        double ynew = vector.getY() * c - vector.getZ() * s;
        double znew = vector.getY() * s + vector.getZ() * c;
        vector.setY(ynew);
        vector.setZ(znew);
        // translate point back:
        vector.add(origin);
    }

    public static void rotateVectorZ(Vector vector, Vector origin, double angle) {
        double s = Math.sin(angle);
        double c = Math.cos(angle);

        // translate point back to origin:
        vector.subtract(origin);

        // rotate point
        double ynew = vector.getY() * c - vector.getX() * s;
        double xnew = vector.getY() * s + vector.getX() * c;
        vector.setY(ynew);
        vector.setX(xnew);
        // translate point back:
        vector.add(origin);
    }
}
