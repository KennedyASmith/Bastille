package com.kennedysmithjava.prisoncore.util;

import de.tr7zw.nbtapi.NBTCompound;

import java.nio.ByteBuffer;
import java.util.UUID;

public class NBTUtil {

    public static void writeUUID(NBTCompound nbtCompound, String name, UUID uuid) {
        ByteBuffer bb = ByteBuffer.wrap(new byte[16]);
        bb.putLong(uuid.getMostSignificantBits());
        bb.putLong(uuid.getLeastSignificantBits());
        nbtCompound.setByteArray(name, bb.array());
    }

    public static UUID readUUID(NBTCompound nbtCompound, String name) {
        byte[] bytes = nbtCompound.getByteArray(name);
        ByteBuffer bb = ByteBuffer.wrap(bytes);
        long firstLong = bb.getLong();
        long secondLong = bb.getLong();
        return new UUID(firstLong, secondLong);
    }


}
