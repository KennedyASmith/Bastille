package com.kennedysmithjava.prisoncore.entity.player.objects;

import com.kennedysmithjava.prisoncore.PrisonCore;
import com.kennedysmithjava.prisoncore.crafting.objects.PrisonEnchantBook;
import com.kennedysmithjava.prisoncore.eco.CurrencyType;
import com.kennedysmithjava.prisoncore.enchantment.Enchant;
import com.kennedysmithjava.prisoncore.entity.mines.Mine;
import com.kennedysmithjava.prisoncore.entity.player.MPlayer;
import com.kennedysmithjava.prisoncore.entity.player.QuestProfile;
import com.kennedysmithjava.prisoncore.entity.player.SkillProfile;
import com.kennedysmithjava.prisoncore.entity.tools.AxeType;
import com.kennedysmithjava.prisoncore.entity.tools.FishingPoleType;
import com.kennedysmithjava.prisoncore.entity.tools.HoeType;
import com.kennedysmithjava.prisoncore.entity.tools.PickaxeType;
import com.kennedysmithjava.prisoncore.pouch.PouchType;
import com.kennedysmithjava.prisoncore.quest.QuestPath;
import com.kennedysmithjava.prisoncore.quest.QuestPathRegistry;
import com.kennedysmithjava.prisoncore.skill.SkillType;
import com.kennedysmithjava.prisoncore.util.MiscUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;
import java.util.logging.Level;

public class Award {
    public Map<CurrencyType, Double> currencyAwards;
    public Map<String, Integer> enchantAwards;
    public Map<Integer, Integer> pouchAwards;
    public Map<SkillType, Integer> skillLevelUpgradesAwards;
    public Map<SkillType, Integer> skillXPUpgradesAwards;
    public Map<SkillType, Double> xpMultiplierAward;
    public Map<CurrencyType, Double> currencyMultiplierAward;
    public List<String> messageAwards;
    public List<RebirthObjectAward> objectAwards;
    public List<String> permissionAwards;
    public List<String> commandAwards;
    public List<String> questUnlockAwards;
    public List<String> pickaxeAwards;
    public List<String> hoeAwards;
    public List<String> axeAwards;
    public List<String> fishingPoleAwards;
    public List<Integer> mineDistributionAwards;

    public Award(){

    }

    public Award(Map<CurrencyType, Double> currencyAwards, Map<String, Integer> enchantAwards, Map<Integer, Integer> pouchAwards, Map<SkillType, Integer> skillLevelUpgradesAwards, Map<SkillType, Integer> skillXPUpgradesAwards, Map<SkillType, Double> xpMultiplierAward, Map<CurrencyType, Double> currencyMultiplierAward, List<String> messageAwards, List<RebirthObjectAward> objectAwards, List<String> permissionAwards, List<String> commandAwards, List<String> questUnlockAwards, List<String> pickaxeAwards, List<String> hoeAwards, List<String> axeAwards, List<String> fishingPoleAwards, List<Integer> mineDistributionAwards) {
        this.currencyAwards = currencyAwards;
        this.enchantAwards = enchantAwards;
        this.pouchAwards = pouchAwards;
        this.skillLevelUpgradesAwards = skillLevelUpgradesAwards;
        this.skillXPUpgradesAwards = skillXPUpgradesAwards;
        this.xpMultiplierAward = xpMultiplierAward;
        this.currencyMultiplierAward = currencyMultiplierAward;
        this.messageAwards = messageAwards;
        this.objectAwards = objectAwards;
        this.permissionAwards = permissionAwards;
        this.commandAwards = commandAwards;
        this.questUnlockAwards = questUnlockAwards;
        this.pickaxeAwards = pickaxeAwards;
        this.hoeAwards = hoeAwards;
        this.axeAwards = axeAwards;
        this.fishingPoleAwards = fishingPoleAwards;
        this.mineDistributionAwards = mineDistributionAwards;
    }

    public void giveAward(MPlayer player){
        Player p = player.getPlayer();

        if(currencyAwards != null){
            currencyAwards.forEach((currencyType, amount) -> player.addBalance(currencyType, amount, false));
        }

        if(enchantAwards != null){
            enchantAwards.forEach((enchantName, level) -> {
                try{
                    Enchant<?> enchant = Enchant.getByName(enchantName);
                    PrisonEnchantBook enchantBook = new PrisonEnchantBook(enchant, level);
                    MiscUtil.givePlayerItem(p, enchantBook.give(1), 1);
                } catch (NullPointerException e){
                    Bukkit.getLogger().log(Level.SEVERE, "Unknown/Inactive enchant name in Rebirth Award: " + enchantName);
                    e.printStackTrace();
                }
            });
        }

        if(pouchAwards != null){
            pouchAwards.forEach((id, count) -> {
                PouchType type = PouchType.from(id);
                if(type == null){
                    Bukkit.getLogger().log(Level.SEVERE, "Unknown/Inactive pouch id in Rebirth Award: " + id);
                }else{
                    MiscUtil.givePlayerItem(p, type.getNewPouchItem(), 1);
                }
            });
        }

        if(skillLevelUpgradesAwards != null){
            SkillProfile profile = player.getSkillProfile();
            skillLevelUpgradesAwards.forEach((skillType, integer) ->
                    profile.getSkill(skillType).addLevel(integer));
        }

        if(skillXPUpgradesAwards != null){
            SkillProfile profile = player.getSkillProfile();
            skillXPUpgradesAwards.forEach((skillType, integer) ->
                    profile.getSkill(skillType).addXP(integer));
        }

        if(xpMultiplierAward != null){
            SkillProfile profile = player.getSkillProfile();
            xpMultiplierAward.forEach((skillType, multiplier) ->
                    profile.getSkill(skillType).addXpMultiplier(multiplier));
        }

        if(currencyMultiplierAward != null){
            currencyMultiplierAward.forEach(player::addEconomyMultiplier);
        }

        if(messageAwards != null){
            messageAwards.forEach(player::msg);
        }

        if(objectAwards != null){
            objectAwards.forEach(rebirthObjectAward -> rebirthObjectAward.give(player));
        }

        if(permissionAwards != null){
            //TODO: Add permission awards
        }

        if(commandAwards != null){
            commandAwards.forEach(s -> {
                String cmd = s.replaceAll("%player%", p.getName());
                CommandSender sender = PrisonCore.get().getServer().getConsoleSender();
                try{
                    Bukkit.dispatchCommand(sender, cmd);
                }catch(CommandException e){
                    Bukkit.getLogger().log(Level.SEVERE, "Failure to run command in Rebirth Award: " + cmd);
                    e.printStackTrace();
                }
            });
        }

        if(questUnlockAwards != null){
            QuestPathRegistry registry = QuestPathRegistry.get();
            QuestProfile profile = player.getQuestProfile();
            questUnlockAwards.forEach(s -> {
                QuestPath path = registry.getQuest(s);
                if(path != null){
                    profile.addUnlockedQuestPath(path);
                }else{
                    Bukkit.getLogger().log(Level.SEVERE, "Unknown quest in Rebirth Award: " + s);
                }
            });
        }
        
        if(pickaxeAwards != null){
            pickaxeAwards.forEach(pickName -> {
                PickaxeType type = PickaxeType.get(pickName);
                if(type != null){
                    MiscUtil.givePlayerItem(p, type.getItemStack(), 1);
                }else {
                    Bukkit.getLogger().log(Level.SEVERE, "Unknown pickaxe type in Rebirth Award: " + pickName);
                }
            });
        }

        if(hoeAwards != null){
            hoeAwards.forEach(pickName -> {
                HoeType type = HoeType.get(pickName);
                if(type != null){
                    MiscUtil.givePlayerItem(p, type.getItemStack(), 1);
                }else {
                    Bukkit.getLogger().log(Level.SEVERE, "Unknown hoe type in Rebirth Award: " + pickName);
                }
            });
        }

        if(axeAwards != null){
            axeAwards.forEach(pickName -> {
                AxeType type = AxeType.get(pickName);
                if(type != null){
                    MiscUtil.givePlayerItem(p, type.getItemStack(), 1);
                }else {
                    Bukkit.getLogger().log(Level.SEVERE, "Unknown axe type in Rebirth Award: " + pickName);
                }
            });
        }

        if(fishingPoleAwards != null){
            fishingPoleAwards.forEach(pickName -> {
                FishingPoleType type = FishingPoleType.get(pickName);
                if(type != null){
                    MiscUtil.givePlayerItem(p, type.getItemStack(), 1);
                }else {
                    Bukkit.getLogger().log(Level.SEVERE, "Unknown fishing pole type in Rebirth Award: " + pickName);
                }
            });
        }

        if(mineDistributionAwards != null){
            mineDistributionAwards.forEach(id -> {
                Mine mine = player.getMine();
                if(mine != null){
                    mine.addUnlockedDistribution(id);
                }else{
                    Bukkit.getLogger().log(Level.SEVERE, "Mine is null for distribution award. ");
                }
            });
        }
    }

    public void setAxeAwards(List<String> axeAwards) {
        this.axeAwards = axeAwards;
    }

    public void setCommandAwards(List<String> commandAwards) {
        this.commandAwards = commandAwards;
    }

    public void setCurrencyAwards(Map<CurrencyType, Double> currencyAwards) {
        this.currencyAwards = currencyAwards;
    }

    public void setCurrencyMultiplierAward(Map<CurrencyType, Double> currencyMultiplierAward) {
        this.currencyMultiplierAward = currencyMultiplierAward;
    }

    public void setEnchantAwards(Map<String, Integer> enchantAwards) {
        this.enchantAwards = enchantAwards;
    }

    public void setFishingPoleAwards(List<String> fishingPoleAwards) {
        this.fishingPoleAwards = fishingPoleAwards;
    }

    public void setHoeAwards(List<String> hoeAwards) {
        this.hoeAwards = hoeAwards;
    }

    public void setMessageAwards(List<String> messageAwards) {
        this.messageAwards = messageAwards;
    }

    public void setMineDistributionAwards(List<Integer> mineDistributionAwards) {
        this.mineDistributionAwards = mineDistributionAwards;
    }

    public void setObjectAwards(List<RebirthObjectAward> objectAwards) {
        this.objectAwards = objectAwards;
    }

    public void setPermissionAwards(List<String> permissionAwards) {
        this.permissionAwards = permissionAwards;
    }

    public void setPickaxeAwards(List<String> pickaxeAwards) {
        this.pickaxeAwards = pickaxeAwards;
    }

    public void setPouchAwards(Map<Integer, Integer> pouchAwards) {
        this.pouchAwards = pouchAwards;
    }

    public void setQuestUnlockAwards(List<String> questUnlockAwards) {
        this.questUnlockAwards = questUnlockAwards;
    }

    public void setSkillLevelUpgradesAwards(Map<SkillType, Integer> skillLevelUpgradesAwards) {
        this.skillLevelUpgradesAwards = skillLevelUpgradesAwards;
    }

    public void setSkillXPUpgradesAwards(Map<SkillType, Integer> skillXPUpgradesAwards) {
        this.skillXPUpgradesAwards = skillXPUpgradesAwards;
    }

    public void setXpMultiplierAward(Map<SkillType, Double> xpMultiplierAward) {
        this.xpMultiplierAward = xpMultiplierAward;
    }

}
