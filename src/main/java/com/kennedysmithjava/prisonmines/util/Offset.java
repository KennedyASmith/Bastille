package com.kennedysmithjava.prisonmines.util;

import org.bukkit.Location;

public class Offset {

    int x; int y; int z;
    float pitch; float yaw;

    public Offset(int x, int y, int z, float pitch, float yaw) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.pitch = pitch;
        this.yaw = yaw;
    }

    public Offset(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.pitch = 0F;
        this.yaw = 0F;
    }

    public int getY() {
        return y;
    }

    public int getX() {
        return x;
    }

    public int getZ() {
        return z;
    }

    public float getPitch() {
        return pitch;
    }

    public float getYaw() {
        return yaw;
    }

    public Location get(Location origin){
        if(yaw != 0F && pitch != 0F){
            return origin.clone().add(getX(), getY(), getZ());
        }else{
            Location location = origin.clone().add(getX(), getY(), getZ());
            location.setPitch(pitch);
            location.setYaw(yaw);
            return location;
        }
    }

    @Override
    public String toString() {
        return "Offset{" +
                "x=" + x +
                ", y=" + y +
                ", z=" + z +
                ", pitch=" + pitch +
                ", yaw=" + yaw +
                '}';
    }
}
