package com.kennedysmithjava.prisoncore.regions;

import java.util.Objects;

public record RegionWrapper(Region region, RegionType type, String name) {


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RegionWrapper that = (RegionWrapper) o;
        return Objects.equals(region, that.region) && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(region, type);
    }
}
