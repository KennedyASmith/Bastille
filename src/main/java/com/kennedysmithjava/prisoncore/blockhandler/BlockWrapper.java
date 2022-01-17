package com.kennedysmithjava.prisoncore.blockhandler;

import com.massivecraft.massivecore.store.EntityInternal;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Directional;

public class BlockWrapper extends EntityInternal<BlockWrapper> {

    Material material;
    String blockData;

    public BlockWrapper(Material material, BlockData blockData) {
        this.material = material;
        this.blockData = blockData.getAsString();
    }

    public BlockWrapper(Material material) {
        this.material = material;
        this.blockData = "";
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
}

