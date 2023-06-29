package com.kennedysmithjava.prisoncore.quest.reward;

import com.kennedysmithjava.prisoncore.crafting.PrisonObject;
import com.kennedysmithjava.prisoncore.entity.player.MPlayer;
import com.massivecraft.massivecore.util.MUtil;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class QuestRewardPrisonObject extends QuestReward {

    private final PrisonObject prisonObject;
    private final int amt;

    public QuestRewardPrisonObject(PrisonObject prisonObject, int amt) {
        super(prisonObject.getName(), prisonObject.giveRawItem().getType(), prisonObject.give(1).getItemMeta().getLore());
        this.prisonObject = prisonObject;
        this.amt = amt;
    }


    @Override
    public void applyRewardAction(MPlayer player) {
        ItemStack item = prisonObject.give(amt);
        player.getPlayer().getInventory().addItem(item);
    }

    @Override
    public boolean canGive(MPlayer player) {
        return player.getPlayer().getInventory().firstEmpty() != -1;
    }

    @Override
    public String cantGiveReason(MPlayer player) {
        return "&7Inventory full";
    }

    @Override
    public List<String> getQuantityLore() {
        return MUtil.list("&e" + amt +"x " + prisonObject.getName());
    }
}
