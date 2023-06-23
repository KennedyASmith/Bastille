package com.kennedysmithjava.prisoncore.gui.buttons;

import com.kennedysmithjava.prisoncore.eco.CostCurrency;
import com.kennedysmithjava.prisoncore.eco.CurrencyType;
import com.kennedysmithjava.prisoncore.engine.EngineXP;
import com.kennedysmithjava.prisoncore.entity.player.MPlayer;
import com.kennedysmithjava.prisoncore.entity.player.Skill;
import com.kennedysmithjava.prisoncore.entity.player.SkillProfile;
import com.kennedysmithjava.prisoncore.skill.SkillType;
import com.massivecraft.massivecore.util.MUtil;

import java.util.ArrayList;
import java.util.List;

public class GuiSkillUpgradeButton extends GuiButton {

    private final Skill skill;
    private final MPlayer player;
    private final int xpRequired;

    public GuiSkillUpgradeButton(MPlayer player, SkillType skillType, int slot, List<String> lore, Runnable onClick) {
        super(skillType.getDisplayName() + " &7Level", "", slot, lore, skillType.getIcon(), new ArrayList<>(), null, onClick, new ArrayList<>());
        SkillProfile profile = player.getSkillProfile();
        this.skill = profile.getSkill(skillType);
        this.player = player;
        this.xpRequired = skill.getXPRequired();
        this.addCost(new CostCurrency(CurrencyType.CASH, xpRequired));
    }


    @Override
    public List<String> getBaseLore() {
        List<String> baseLore = super.getBaseLore();
        baseLore.add(" &r");
        baseLore.add("&7Level &e" + skill.getCurrentLevel());
        baseLore.add(skill.getXPBar(xpRequired));
        if(!skill.maxLevelReached()){
            baseLore.add("&7XP: (&e" + skill.getCurrentXP() + "&7/&e" + xpRequired + "&7)");
        }else {
            baseLore.add("&7XP: (~~/~~)");
        }
        return baseLore;
    }

    @Override
    public Runnable getOnClick() {
        return () -> {
            if(!skill.maxLevelReached()){
                EngineXP.forceGive(player, player.getSkillProfile(), skill.getType(), xpRequired);
                GuiSkillUpgradeButton.super.getOnClick().run();
            }
        };
    }

    public List<String> getBuyPrompt(){
        if(skill.maxLevelReached()){
            MUtil.list(" &r", "&7Maximum level reached!", " &r");
        }
        return MUtil.list(" &r", "&eClick to purchase next level!", " &r", "&e&lCOST");
    }

    @Override
    public String getButtonTag(boolean isUnlocked, boolean isPurchased, boolean isAffordable) {
        if(isAffordable && !skill.maxLevelReached()){
            return "&7[&aAVAILABLE&7]";
        }
        return "";
    }

}
