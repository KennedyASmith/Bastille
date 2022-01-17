package com.kennedysmithjava.prisoncore.tools;

import com.kennedysmithjava.prisoncore.entity.tools.BufferConf;
import com.massivecraft.massivecore.store.EntityInternal;

import java.util.List;
import java.util.Objects;

public class Buffer extends EntityInternal<Buffer> {

    public String name;
    public String displayName;
    /*public BlockMaterial material;*/
    public List<String> lore;
    public String infoLore;

    public Buffer(String name, String displayName, /*BlockMaterial itemMaterial,*/ List<String> lore, String infoLore){
        this.name = name;
        this.displayName = displayName;
        /*this.material = itemMaterial;*/
        this.lore = lore;
        this.infoLore = infoLore;
    }

    public List<String> getLore() {
        return lore;
    }

    public String getInfoLore() {
        return infoLore;
    }
    /*public BlockMaterial getMaterial() {
        return material;
    }*/

    public String getName() {
        return name;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static boolean exists(String name){
        return BufferConf.activeBuffers.containsKey(name);
    }

    public static Buffer get(String name){
        return BufferConf.activeBuffers.get(name);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Buffer buffer = (Buffer) o;
        return name.equals(buffer.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return "Buffer{" +
                "name='" + name + '\'' +
                ", displayName='" + displayName + '\'' +
                ", lore=" + lore +
                ", infoLore='" + infoLore + '\'' +
                '}';
    }
}
