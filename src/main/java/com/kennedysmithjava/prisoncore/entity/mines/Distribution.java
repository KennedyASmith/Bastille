package com.kennedysmithjava.prisoncore.entity.mines;

import com.kennedysmithjava.prisoncore.blockhandler.BlockWrapper;
import com.kennedysmithjava.prisoncore.entity.mines.objects.PrisonBlock;
import com.massivecraft.massivecore.util.MUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;

import java.util.*;

public class Distribution {
    private final transient Map<Integer, Material> blocks = new HashMap<>();
    private final transient Map<Integer, PrisonBlock> blocksPB = new HashMap<>();
    private final transient Collection<Material> materials;

    private final transient Map<Material, List<Integer>> matchingMaterialRates = new HashMap<>(); //Used for determining product between multiple blocks of the same material
    private final transient Map<Material, PrisonBlock> uniqueOccurences = new HashMap<>();
    private final Map<BlockWrapper, Double> rawRates = new HashMap<>();
    private final transient List<Integer> rateData = new ArrayList<>(); //A list of IDs generated from number of entries each block gets for this distribution
    private final Map<Integer, Integer> storedRates = new HashMap<>();
    private final Material icon;
    private final String name;
    private final Integer totalEntries;
    private final List<String> lore;
    private final transient Random random = new Random();


    public Distribution(String name, Material icon, Map<Integer, Integer> rates, List<String> lore) {
        this.icon = icon;
        this.name = name;
        this.lore = lore;
        this.storedRates.putAll(rates);
        this.totalEntries = sum(storedRates.values());
        storedRates.forEach((id, entries) -> {
            PrisonBlock pb = BlocksConf.get().blocks.get(id);
            Material mat = pb.getBlock().getMaterial();
            blocksPB.put(id, pb);
            rateData.addAll(MUtil.repeat(id, entries));
            if (blocks.containsValue(mat)){
                blocks.forEach((otherID, material) -> {
                    if (material.equals(mat)){
                        int occurrences = Collections.frequency(rateData, otherID);
                        List<Integer> combinedRates = new ArrayList<>();
                        combinedRates.addAll(MUtil.repeat(otherID, occurrences));
                        combinedRates.addAll(MUtil.repeat(id, entries));
                        matchingMaterialRates.put(mat, combinedRates);
                    }
                });
            }else {
                uniqueOccurences.put(mat, pb);
            }
            if (rawRates.containsKey(pb.getBlock())){
                rawRates.put(pb.getBlock(), rawRates.get(pb.getBlock()) + (100 * ((double) entries / (double) totalEntries)));
            }else {
                rawRates.put(pb.getBlock(), (100 * ((double) entries / (double) totalEntries)));
            }
            blocks.put(id, mat);
        });

        this.materials = blocks.values();
    }

    public Material getIcon() {
        return icon;
    }

    //TODO: Include blockdata
    public PrisonBlock generatePrisonBlock(Material material, BlockData blockData) {

        if (Collections.frequency(materials, material) > 1){
            List<Integer> rts = matchingMaterialRates.get(material);
            int id = rts.get(random.nextInt(rts.size() - 1));
            return blocksPB.get(id);
        }else {
            return uniqueOccurences.get(material);
        }
    }

    public Map<BlockWrapper, Double> getRates() {
        return this.rawRates;
    }

    public Map<Integer, Integer> getStoredRates() {
        return storedRates;
    }

    public String getName() {
        return name;
    }

    public List<String> getLore() {
        return lore;
    }

    private int sum(Collection<Integer> list) {
        int sum = 0;

        for (int i : list)
            sum = sum + i;

        return sum;
    }

}
