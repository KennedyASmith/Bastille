package com.kennedysmithjava.prisonmines.entity.blocks;

import com.kennedysmithjava.prisonmines.util.BlockMaterial;
import org.bukkit.Material;

import java.util.*;

public class Distribution {
    private final Map<BlockMaterial, SortedMap<Double, Integer>> cumulativeRates;
    private final Map<BlockMaterial, Double> rates;

    private final Material icon;
    private final String name;
    private final List<String> lore;
    private final Map<BlockMaterial, Double> sums;
    private final Random random = new Random();

    public Distribution(String name, Material icon, Map<Integer, Double> rates, List<String> lore) {
        this.icon = icon;
        this.name = name;
        this.lore = lore;

        this.rates = new HashMap<>();
        this.cumulativeRates = new HashMap<>();
        this.sums = new HashMap<>();

        Map<Integer, PrisonBlock> blocks = BlocksConf.get().blocks;

        // Pre-processing to make the prisonblock computation quick
        for (Map.Entry<Integer, Double> integerDoubleEntry : rates.entrySet()) {
            Integer blockKey = integerDoubleEntry.getKey();
            BlockMaterial bm = blocks.get(blockKey).getBlock();

            double sum = this.sums.get(bm);
            this.cumulativeRates
                    .getOrDefault(bm, this.cumulativeRates.put(bm, new TreeMap<>(Double::compare)))
                    .put(sum, blockKey);

            Double value = integerDoubleEntry.getValue();

            sum += value;
            this.sums.put(bm, sum);

            this.rates.compute(bm, (b, v) -> v == null ? value : v + value);
        }
    }

    public Material getIcon() {
        return icon;
    }

    public PrisonBlock generatePrisonBlock(Material material, byte data) {

        BlockMaterial key = new BlockMaterial(material, data);
        double sum = this.sums.getOrDefault(key, -1D);

        if (sum == -1D) {
            return null;
        }

        double rand = sum * random.nextDouble() + Double.MIN_NORMAL;

        SortedMap<Double, Integer> partial = this.cumulativeRates.get(key).headMap(rand);

        int prisonBlockId = partial.get(partial.lastKey());
        return BlocksConf.get().blocks.get(prisonBlockId);
    }




    public Map<BlockMaterial, Double> getRates() {
        return this.rates;
    }

    public String getName() {
        return name;
    }

    public List<String> getLore() {
        return lore;
    }
}
