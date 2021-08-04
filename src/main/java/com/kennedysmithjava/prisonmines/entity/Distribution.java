package com.kennedysmithjava.prisonmines.entity;

import com.kennedysmithjava.prisonmines.util.BlockMaterial;
import org.bukkit.Material;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Distribution{
    Map<Integer, Double> rates;
    Material icon;
    String name;
    List<String> lore;

    public Distribution(String name, Material icon, Map<Integer, Double> rates, List<String> lore){
        this.rates = rates;
        this.icon = icon;
        this.name = name;
        this.lore = lore;
    }

    public Material getIcon() {
        return icon;
    }

    public Map<BlockMaterial, Double> getRates() {
        Map<BlockMaterial, Double> newRates = new HashMap<>();
        Map<Integer, PrisonBlock> blocks = BlocksConf.get().blocks;
        rates.forEach((prisonBlockID, rate) -> newRates.put(blocks.get(prisonBlockID).getBlock(), rate));
        return newRates;
    }

    public String getName() {
        return name;
    }

    public List<String> getLore() {
        return lore;
    }
}
