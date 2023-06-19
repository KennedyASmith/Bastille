package com.kennedysmithjava.prisoncore.entity.mines.upgrades.actions;

import com.kennedysmithjava.prisoncore.entity.player.MPlayer;
import org.bukkit.enchantments.Enchantment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ActionEnchantUnlock extends AbstractAction {

    List<Enchantment> enchantments;

    ActionEnchantUnlock(Enchantment enchantment){
        this.enchantments = new ArrayList<>(Collections.singletonList(enchantment));
    }

    ActionEnchantUnlock(List<Enchantment> enchantments){
        this.enchantments = enchantments;
    }

    @Override
    public void apply(MPlayer player) {

    }
}
