package com.kennedysmithjava.prisonmines.util;

import java.lang.ref.SoftReference;
import java.util.HashSet;
import java.util.stream.Stream;

public class SoftSet<A> extends HashSet<SoftReference<A>> {

    public Stream<A> getStream() {
        return this.stream().map(SoftReference::get);
    }

}
