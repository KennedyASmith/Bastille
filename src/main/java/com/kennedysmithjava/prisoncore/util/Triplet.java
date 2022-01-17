package com.kennedysmithjava.prisoncore.util;

import java.util.Objects;

public class Triplet<X, Y, Z> {

    public final X x;
    public final Y y;
    public final Z z;

    public Triplet(X x,Y y, Z z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ", " + z + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Triplet<?, ?, ?> triplet = (Triplet<?, ?, ?>) o;
        return Objects.equals(x, triplet.x) && Objects.equals(y, triplet.y) && Objects.equals(z, triplet.z);
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, z);
    }
}
