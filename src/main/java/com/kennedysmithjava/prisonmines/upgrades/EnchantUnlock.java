package com.kennedysmithjava.prisonmines.upgrades;

import com.mcrivals.prisoncore.entity.MPlayer;
import org.bukkit.enchantments.Enchantment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class EnchantUnlock extends AbstractUpgrade{

    List<Enchantment> enchantments;

    EnchantUnlock(Enchantment enchantment){
        this.enchantments = new ArrayList<>(Collections.singletonList(enchantment));
    }

    EnchantUnlock(List<Enchantment> enchantments){
        this.enchantments = enchantments;
    }

    @Override
    public void apply(MPlayer player) {

    }
}
