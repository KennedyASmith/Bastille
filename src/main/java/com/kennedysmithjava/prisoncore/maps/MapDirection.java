package com.kennedysmithjava.prisoncore.maps;

enum MapDirection {
    NORTH((byte) 8),
    NORTHEASTNORTH((byte) 9),
    NORTHEAST((byte) 10),
    NORTHEASTEAST((byte) 11),
    EAST((byte) 12),
    SOUTHEASTEAST((byte) 13),
    SOUTHEAST((byte) 14),
    SOUTHEASTSOUTH((byte) 15),
    SOUTH((byte) 0),
    SOUTHWESTSOUTH((byte) 1),
    SOUTHWEST((byte) 2),
    SOUTHWESTWEST((byte) 3),
    WEST((byte) 4),
    NORTHWESTWEST((byte) 5),
    NORTHWEST((byte) 6),
    NORTHWESTNORTH((byte) 7);

    final byte d;

    MapDirection(byte d) {
        this.d = d;
    }
}
