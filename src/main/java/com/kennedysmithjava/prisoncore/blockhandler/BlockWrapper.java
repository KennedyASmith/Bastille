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
        int result = 17;
        result = 31 * result + this.material.name().hashCode();
        result = 31 * result + this.blockData.hashCode();

        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (!(obj instanceof BlockWrapper wrapper)) return false;
        boolean isTrue = (this.material.name().equals(wrapper.getMaterial().name()) && this.blockData.equals(wrapper.getBlockDataString()));
        return isTrue;
    }
}

