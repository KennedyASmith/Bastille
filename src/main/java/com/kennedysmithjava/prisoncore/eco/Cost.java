package com.kennedysmithjava.prisoncore.eco;

import com.kennedysmithjava.prisoncore.entity.player.MPlayer;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class Cost {

    /**
     * Returns true if the player meets the requirements of this cost.
     */
    public abstract boolean hasCost(MPlayer player);

    /**
     * Attempts to create a transaction with this cost on the player.
     * Returns true if successful.
     */
    public abstract boolean transaction(MPlayer player);
    public abstract String getName();
    public abstract String getPriceline();
    public abstract String getInsufficientLine(MPlayer player);

    public abstract Cost combine(Cost similarCost);

    public static Collection<Cost> combineList(List<Cost> costsWithDuplicates){
        Map<String, Cost> costsWithoutDuplicatesMap = new HashMap<>();
        for (Cost c : costsWithDuplicates) {
            String cName = c.getName();
            if(costsWithoutDuplicatesMap.containsKey(cName)){
                Cost existingCost = costsWithoutDuplicatesMap.get(cName);
                Cost combined = existingCost.combine(c);
                costsWithoutDuplicatesMap.put(cName, combined);
            }else {
                costsWithoutDuplicatesMap.put(cName, c);
            }
        }

        return costsWithoutDuplicatesMap.values();
    }

}
