package com.kennedysmithjava.prisoncore.util.vfx;

import com.kennedysmithjava.prisoncore.util.BlockArea;
import com.kennedysmithjava.prisoncore.util.XYPair;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class CuboidSequence extends ParticleSequence {

    private final Vector from;
    private final Vector to;
    private final Vector midPoint;
    private final List<XYPair<Vector, Vector>> lines = new ArrayList<>();

    public CuboidSequence(Vector from, Vector to) {
        this.from = Vector.getMinimum(from, to);
        this.to = Vector.getMaximum(from, to);
        this.midPoint = this.to.clone().subtract(this.from).divide(new Vector(2, 2, 2)).add(this.from);

        Vector start = this.from;
        Vector end = this.to;

        Vector bottom1 = start.clone().setX(this.to.getX());
        Vector bottom2 = bottom1.clone().setZ(this.to.getZ());
        Vector bottom3 = start.clone().setZ(this.to.getZ());

        Vector top1 = end.clone().setX(start.getX());
        Vector top2 = top1.clone().setZ(start.getZ());
        Vector top3 = end.clone().setZ(start.getZ());

        vectors.add(start);
        vectors.add(bottom1);
        vectors.add(bottom2);
        vectors.add(bottom3);
        vectors.add(top1);
        vectors.add(top2);
        vectors.add(top3);
        vectors.add(end);

        //Bottom lines
        lines.add(new XYPair<>(start, bottom1));
        lines.add(new XYPair<>(bottom1, bottom2));
        lines.add(new XYPair<>(bottom2, bottom3));
        lines.add(new XYPair<>(bottom3, start));

        //Top lines
        lines.add(new XYPair<>(end, top1));
        lines.add(new XYPair<>(top1, top2));
        lines.add(new XYPair<>(top2, top3));
        lines.add(new XYPair<>(top3, end));

        //Side lines
        lines.add(new XYPair<>(start, top2));
        lines.add(new XYPair<>(bottom1, top3));
        lines.add(new XYPair<>(bottom2, end));
        lines.add(new XYPair<>(bottom3, top1));


    }

    public CuboidSequence(double length, double width, double height) {
        this(new Vector(length / -2d, height / -2d, width / -2d), new Vector(length / 2d, height / 2d, width / 2d));
    }

    public CuboidSequence(BlockArea area) {
        this(area.getMin().toVector(), area.getMax().toVector().add(new Vector(1, 1, 1)));
    }

    @Override
    public void draw(Location location) {
        List<Player> playerList = getParticlePlayers(location);
        for (XYPair<Vector, Vector> line : lines) {
            Vector from = line.x;
            Vector to = line.y;
            for (double k = 0d; k < 1d; k += 1d / divisions) {
                Vector vec = to.clone().subtract(from).multiply(k).add(from);
                particleFn.drawParticle(location.clone().add(vec), playerList);
            }
        }
    }

    public void standOnCorner() {
        rotateX(Math.PI / 4);
        rotateZ(0.615479709d); //arctan(1/sqrt(2))
    }

    @Override
    public void rotateY(double angle) {
        super.rotateY(midPoint, angle);
    }

    @Override
    public void rotateX(double angle) {
        super.rotateX(midPoint, angle);
    }

    @Override
    public void rotateZ(double angle) {
        super.rotateZ(midPoint, angle);
    }

    public Vector getFrom() {
        return from.clone();
    }

    public Vector getTo() {
        return to.clone();
    }

    public Vector getMidPoint() {
        return midPoint.clone();
    }
}
