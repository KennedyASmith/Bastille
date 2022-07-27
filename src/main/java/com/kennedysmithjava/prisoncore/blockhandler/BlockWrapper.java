package com.kennedysmithjava.prisoncore.blockhandler;

import com.massivecraft.massivecore.store.EntityInternal;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Directional;

public class BlockWrapper extends EntityInternal<BlockWrapper>{

    Material material;
    String blockData;

    public BlockWrapper(Material material, BlockData blockData) {
        this.material = material;
        this.blockData = blockData.getAsString();
    }

    public BlockWrapper(Material material) {
        this.material = material;
        this.blockData = material.createBlockData().getAsString();
    }

    public BlockWrapper(Material material, BlockFace direction) {
        this.material = material;
        Directional data = (Directional) Bukkit.createBlockData(material);
        data.setFacing(direction);
        this.blockData = data.getAsString();
    }

    public Material getMaterial() {
        return material;
    }

    public String getBlockDataString() {
        return blockData;
    }

    public BlockData getBlockData() {
        return Bukkit.createBlockData(getBlockDataString());
    }

    public boolean hasBlockData(){
        return !blockData.equals("");
    }

    @Override
    public String toString() {
        return "BlockWrapper{" +
                "material=" + material +
                ", blockData='" + blockData + '\'' +
                '}';
    }

    @Override
    public int hashCode() {

        Bukkit.broadcastMessage("Material name: " + this.material.name());
        Bukkit.broadcastMessage("BlockData: " + this.blockData);

        Bukkit.broadcastMessage("Material hashCode: " + this.material.name().hashCode());
        Bukkit.broadcastMessage("BlockData hashCode: " + this.blockData.hashCode());

        int result = 17;
        result = 31 * result + this.material.name().hashCode();
        result = 31 * result + this.blockData.hashCode();

        Bukkit.broadcastMessage("Checking hash: " + result);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        Bukkit.broadcastMessage("Checking if blockwrapper is equal 1");
        if (obj == this) return true;
        Bukkit.broadcastMessage("Checking if blockwrapper is equal 2");
        if (!(obj instanceof BlockWrapper wrapper)) return false;
        Bukkit.broadcastMessage("Checking if blockwrapper is equal 3");
        Bukkit.broadcastMessage("Hashcode 1: " + obj.hashCode() + " Hashcode 2: " + obj.hashCode());
        boolean isTrue = (this.material.name().equals(wrapper.getMaterial().name()) && this.blockData.equals(wrapper.getBlockDataString()));
        if(!isTrue){
            Bukkit.broadcastMessage("Something's not true here!");
            Bukkit.broadcastMessage("1: " + this.material.name().equals(wrapper.getMaterial().name()));
            Bukkit.broadcastMessage("1: " + this.blockData.equals(wrapper.getBlockDataString()));
        }
        return isTrue;
    }
}

