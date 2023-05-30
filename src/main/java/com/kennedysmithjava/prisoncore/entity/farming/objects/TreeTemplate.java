package com.kennedysmithjava.prisoncore.entity.farming.objects;

import com.kennedysmithjava.prisoncore.blockhandler.BlockWrapper;
import com.kennedysmithjava.prisoncore.entity.farming.TreesConf;
import com.kennedysmithjava.prisoncore.util.regions.Offset;
import com.massivecraft.massivecore.store.EntityInternal;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class TreeTemplate extends EntityInternal<TreeTemplate> {

    // Tree info
    private String name;
    private String schematicName;

    // Blocks in tree needed to be mined to reset
    private int timesNeededToReset;

    // Rarity
    private double rarity;

    // Block broken to increase times hit
    private BlockWrapper blockBroken;

    // What to replace the tree with when mined
    private BlockWrapper blockToReplaceWhenBroken;

    // The sapling type
    private BlockWrapper saplingBlock;

    // Center block from were the spawn position is
    private Offset offset;

}
