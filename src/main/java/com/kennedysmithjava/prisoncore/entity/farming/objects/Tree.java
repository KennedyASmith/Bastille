package com.kennedysmithjava.prisoncore.entity.farming.objects;

import com.kennedysmithjava.prisoncore.entity.farming.TreesConf;
import com.massivecraft.massivecore.ps.PS;
import com.massivecraft.massivecore.store.EntityInternal;
import lombok.AllArgsConstructor;
import lombok.Data;

@SuppressWarnings("Lombok")
@AllArgsConstructor
@Data
public class Tree extends EntityInternal<TreesConf> {

    // Name of type of tree
    private String name;

    // Amount of Blocks Hit
    private int blocksHit;

    // Position where the tree will respond
    private PS spawnPoint;

    // Regeneration time
    private transient long regenerationTime;

    private boolean needsRegeneration;

}
