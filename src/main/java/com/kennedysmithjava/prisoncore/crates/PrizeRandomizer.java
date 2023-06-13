package com.kennedysmithjava.prisoncore.crates;

import com.kennedysmithjava.prisoncore.util.MiscUtil;
import org.apache.commons.math3.distribution.EnumeratedDistribution;
import org.apache.commons.math3.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PrizeRandomizer {


    /**
     * Picks a prize from the provided rarities map based on their probabilities.
     *
     * @param rarities  The map of rarity lists.
     * @return          The selected prize.
     */
    public static CratePrize pickPrize(Map<Double, List<CratePrize>> rarities) {
        List<Pair<List<CratePrize>, Double>> itemProbPairs = new ArrayList<>();
        for (Map.Entry<Double, List<CratePrize>> entry : rarities.entrySet()) {
            Double rarity = entry.getKey();
            List<CratePrize> prizes = entry.getValue();
            itemProbPairs.add(new Pair<>(prizes, rarity));
        }
        EnumeratedDistribution<List<CratePrize>> distribution = new EnumeratedDistribution<>(itemProbPairs);
        return MiscUtil.pickRandomElement(distribution.sample());
    }



    /**
     * Adjusts the rarity of prizes based on the provided luck factor.
     *
     * @param rarityMap  The map of rarity lists.
     * @param luck       The luck factor.
     * @return           The adjusted rarity map.
     */
    public static Map<Double, List<CratePrize>> adjustRarity(Map<Double, List<CratePrize>> rarityMap, double luck) {
        Map<Double, List<CratePrize>> newMap = new HashMap<>();
        rarityMap.forEach((rarity, prizes) -> newMap.put(Math.pow(rarity, luck), prizes));
        return newMap;
    }

    /**
     * Groups prizes with identical rarities together in a map.
     *
     * @param prizes  The list of prizes.
     * @return        The map of prizes grouped by rarity.
     */
    static Map<Double, List<CratePrize>> getSimilarRarities(List<CratePrize> prizes) {
        Map<Double, List<CratePrize>> similarRarityPrizes = new HashMap<>();
        for (CratePrize prize : prizes) {
            double rarity = prize.getRarity();
            similarRarityPrizes.computeIfAbsent(rarity, k -> new ArrayList<>()).add(prize);
        }
        return similarRarityPrizes;
    }


}
