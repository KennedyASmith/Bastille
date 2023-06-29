package com.kennedysmithjava.prisoncore.quest.reward;

import com.kennedysmithjava.prisoncore.entity.player.MPlayer;
import org.bukkit.Material;

import java.util.List;
import java.util.function.Consumer;

public class QuestRewardRunnable extends QuestReward {

    private final Consumer<MPlayer> runnable;
    private final List<String> quantityLore;

    QuestRewardRunnable(String name, Material icon, List<String> lore, Consumer<MPlayer> runnable, List<String> quantityLore) {
        super(name, icon, lore);
        this.runnable = runnable;
        this.quantityLore = quantityLore;
    }


    @Override
    public void applyRewardAction(MPlayer player) {
        runnable.accept(player);
    }

    @Override
    public boolean canGive(MPlayer player) {
        return true;
    }

    @Override
    public String cantGiveReason(MPlayer player) {
        return "Unknown";
    }

    @Override
    public List<String> getQuantityLore() {
        return quantityLore;
    }
}
