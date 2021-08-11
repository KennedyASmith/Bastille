package com.kennedysmithjava.prisonmines.entity;

import com.kennedysmithjava.prisonmines.util.BlockMaterial;
import org.bukkit.Bukkit;
import org.bukkit.Material;

import java.util.*;

public class Distribution {
    private transient Map<BlockMaterial, Integer> blockPrisonBlockMap = null;
    private transient Map<BlockMaterial, Double> rates = null;

    private final Map<Integer, Double> rateData;
    private final Material icon;
    private final String name;
    private final List<String> lore;

    public Distribution(String name, Material icon, Map<Integer, Double> rates, List<String> lore) {
        this.icon = icon;
        this.name = name;
        this.lore = lore;
        this.rateData = rates;
    }

    public Material getIcon() {
        return icon;
    }

    public PrisonBlock generatePrisonBlock(Material material, byte data) {
        if (this.blockPrisonBlockMap == null) {
            this.initRates();
        }

        final Map<Integer, PrisonBlock> blocks = BlocksConf.get().blocks;

        Integer prisonBlockId = this.blockPrisonBlockMap.get(new BlockMaterial(material, data));
        return blocks.getOrDefault(prisonBlockId, null);
    }

    private void initRates() {
        final Map<Integer, PrisonBlock> blocks = BlocksConf.get().blocks;

        this.blockPrisonBlockMap = new HashMap<>();
        this.rates = new HashMap<>();

        for (Map.Entry<Integer, Double> integerDoubleEntry : this.rateData.entrySet()) {

            Integer blockKey = integerDoubleEntry.getKey();
            BlockMaterial bm = blocks.get(blockKey).getBlock();

            this.blockPrisonBlockMap.put(bm, blockKey);

            this.rates.put(bm, integerDoubleEntry.getValue());
        }
    }

    public Map<BlockMaterial, Double> getRates() {
        if (this.rates == null) {
            this.initRates();
        }

        return this.rates;
    }

    public String getName() {
        return name;
    }

    public List<String> getLore() {
        return lore;
    }

    /*

     public PrisonBlock generatePrisonBlock(Material material, byte data) {

        BlockMaterial key = new BlockMaterial(material, data);
        double sum = this.sums.getOrDefault(key, -1D);

        if (sum == -1D) {
            return null;
        }

        double rand = sum * random.nextDouble();

        SortedMap<Double, Integer> partial = this.cumulativeRates.get(key).headMap(rand, true);

        int prisonBlockId = partial.get(partial.lastKey());
        return BlocksConf.get().blocks.get(prisonBlockId);
    }

     */

//
//        this.rates = new HashMap<>();
//        this.cumulativeRates = new HashMap<>();
//        this.sums = new HashMap<>();
    // Map<Integer, PrisonBlock> blocks = BlocksConf.get().blocks;
    //
    //        // Pre-processing to make the prisonblock computation quick
    //        for (Map.Entry<Integer, Double> integerDoubleEntry : rates.entrySet()) {
    //            Integer blockKey = integerDoubleEntry.getKey();
    //            BlockMaterial bm = blocks.get(blockKey).getBlock();
    //
    //            double sum = this.sums.getOrDefault(bm, 0D);
    //
    //            TreeMap<Double, Integer> map = this.cumulativeRates.getOrDefault(bm, new TreeMap<>());
    //            map.put(sum, blockKey);
    //
    //            this.cumulativeRates.put(bm, map);
    //
    //            Double value = integerDoubleEntry.getValue();
    //
    //            sum += value;
    //            this.sums.put(bm, sum);
    //
    //            this.rates.compute(bm, (b, v) -> v == null ? value : v + value);
    //        }

//
//    private final transient Map<BlockMaterial, TreeMap<Double, Integer>> cumulativeRates;
//    private final transient Map<BlockMaterial, Double> rates;
//    private final transient Map<BlockMaterial, Double> sums;

}
