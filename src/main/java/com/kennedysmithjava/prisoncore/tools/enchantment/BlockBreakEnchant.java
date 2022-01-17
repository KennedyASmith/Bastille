package com.kennedysmithjava.prisoncore.tools.enchantment;

import com.kennedysmithjava.prisoncore.event.MineBlockBreakEvent;

public abstract class BlockBreakEnchant<E extends BlockBreakEnchant<E>> extends Enchant<E>{
    public abstract void onBreak(MineBlockBreakEvent e, int enchantLevel);
}
