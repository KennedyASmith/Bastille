package com.kennedysmithjava.prisoncore.enchantment;

import com.kennedysmithjava.prisoncore.event.EventMineBlockBreak;

public abstract class BlockBreakEnchant<E extends BlockBreakEnchant<E>> extends Enchant<E>{

    public abstract void onBreak(EventMineBlockBreak e, int enchantLevel);

}
