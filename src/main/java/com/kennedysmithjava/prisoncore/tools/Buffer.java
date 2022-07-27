package com.kennedysmithjava.prisoncore.tools;

import com.kennedysmithjava.prisoncore.entity.tools.BufferConf;
import com.massivecraft.massivecore.store.EntityInternal;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

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

    public static Map<String, Integer> serialize(Map<Buffer, Integer> entry){
        return entry.entrySet().stream().collect(Collectors.toMap(k -> k.getKey().getName(), Map.Entry::getValue));
    }

    public static Map<Buffer, Integer> deserialize(Map<String, Integer> entry){
        return entry.entrySet().stream().collect(Collectors.toMap(k -> Buffer.get(k.getKey()), Map.Entry::getValue));
    }
}
