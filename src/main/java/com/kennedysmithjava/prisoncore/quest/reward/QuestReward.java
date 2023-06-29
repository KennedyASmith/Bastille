package com.kennedysmithjava.prisoncore.quest.reward;

import com.kennedysmithjava.prisoncore.engine.EngineCooldown;
import com.kennedysmithjava.prisoncore.entity.player.MPlayer;
import com.kennedysmithjava.prisoncore.entity.player.QuestProfile;
import com.kennedysmithjava.prisoncore.gui.BaseGui;
import com.kennedysmithjava.prisoncore.gui.QuestRewardsGui;
import com.kennedysmithjava.prisoncore.quest.QuestPath;
import com.kennedysmithjava.prisoncore.util.Color;
import com.kennedysmithjava.prisoncore.util.CooldownReason;
import com.kennedysmithjava.prisoncore.util.ItemBuilder;
import com.massivecraft.massivecore.chestgui.ChestAction;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class QuestReward {

    private Material icon;
    private String name;
    private List<String> lore;

    QuestReward(String name, Material icon, List<String> lore){
        this.icon = icon;
        this.name = name;
        this.lore = lore;
    }

    public abstract void applyRewardAction(MPlayer player);
    public abstract boolean canGive(MPlayer player);

    public void give(MPlayer player, String pathName, int rewardIndex){
        applyRewardAction(player);
        QuestProfile profile = player.getQuestProfile();
        profile.removeUnclaimedReward(pathName, rewardIndex);
    }

    public abstract String cantGiveReason(MPlayer player);

    public abstract List<String> getQuantityLore();

    public ChestAction getAction(MPlayer player, BaseGui gui, String pathName, int rewardIndex){
        return clickEvent -> {
            if(canGive(player)){
                int slot = clickEvent.getSlot();
                gui.setItem(slot, Material.AIR);
                gui.close();
                give(player, pathName, rewardIndex);
                QuestProfile profile = player.getQuestProfile();
                Map<QuestPath, List<QuestReward>> rewards = profile.getUnclaimedQuestRewards();
                QuestRewardsGui newGui = new QuestRewardsGui(player.getPlayer(), rewards);
                newGui.open();
            }else{
                if(!player.inCooldown(CooldownReason.GUI_ACTION)){
                    player.msg("&7[&bQuests&7] Cannot give this quest reward: " + cantGiveReason(player));
                    EngineCooldown.add(player.getUuid(), 10, CooldownReason.GUI_ACTION);
                    gui.close();
                }
            }
            return false;
        };
    }
    public ItemStack getItem(String pathDisplayName){
        List<String> loreModified = new ArrayList<>(lore);
        loreModified.add(0, "&7&oFrom: " + Color.strip(pathDisplayName));
        loreModified.add(" &r");
        loreModified.addAll(getQuantityLore());
        loreModified.add(" &r");
        loreModified.add(" &eClick to claim!");
        ItemBuilder questItem = new ItemBuilder(icon).name(name).lore(loreModified);
        return questItem.build();
    }

}
