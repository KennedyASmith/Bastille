package com.kennedysmithjava.prisoncore.tools.enchantment;

import com.kennedysmithjava.prisoncore.event.EventMineBlockBreak;

public abstract class BlockBreakEnchant<E extends BlockBreakEnchant<E>> extends Enchant<E>{
    public abstract void onBreak(EventMineBlockBreak e, int enchantLevel);
}
