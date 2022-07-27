package com.kennedysmithjava.prisoncore.entity.mines;

import com.kennedysmithjava.prisoncore.blockhandler.BlockWrapper;
import com.kennedysmithjava.prisoncore.entity.mines.objects.PrisonBlock;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;

import java.util.*;

public class Distribution {
    private transient Map<BlockWrapper, Integer> blockPrisonBlockMap = null;
    private transient Map<BlockWrapper, Double> rates = null;

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

    public PrisonBlock generatePrisonBlock(Material material, BlockData blockData) {
        if (this.blockPrisonBlockMap == null) {
            this.initRates();
        }

        Bukkit.broadcastMessage("PBM: " + blockPrisonBlockMap);

        Integer prisonBlockId = this.blockPrisonBlockMap.get(new BlockWrapper(material, blockData));


        return BlocksConf.get().blocks.getOrDefault(prisonBlockId, null);
    }

    private void initRates() {
        final Map<Integer, PrisonBlock> blocks = BlocksConf.get().blocks;

        this.blockPrisonBlockMap = new HashMap<>();
        this.rates = new HashMap<>();

        for (Map.Entry<Integer, Double> integerDoubleEntry : this.rateData.entrySet()) {

            Integer blockKey = integerDoubleEntry.getKey();
            BlockWrapper bm = blocks.get(blockKey).getBlock();

            this.blockPrisonBlockMap.put(bm, blockKey);

            this.rates.put(bm, integerDoubleEntry.getValue());
        }
    }

    public Map<BlockWrapper, Double> getRates() {
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

}
