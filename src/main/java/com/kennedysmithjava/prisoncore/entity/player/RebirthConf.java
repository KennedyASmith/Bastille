package com.kennedysmithjava.prisoncore.entity.player;

import com.kennedysmithjava.prisoncore.eco.CurrencyType;
import com.kennedysmithjava.prisoncore.enchantment.PickaxeExplosiveEnchant;
import com.kennedysmithjava.prisoncore.entity.player.objects.Award;
import com.kennedysmithjava.prisoncore.entity.player.objects.RebirthObjectAward;
import com.kennedysmithjava.prisoncore.quest.paths.PathIntroduction;
import com.kennedysmithjava.prisoncore.skill.SkillType;
import com.massivecraft.massivecore.command.editor.annotation.EditorName;
import com.massivecraft.massivecore.store.Entity;
import com.massivecraft.massivecore.util.MUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@EditorName("config")
public class RebirthConf extends Entity<RebirthConf> {

    // -------------------------------------------- //
    // META
    // -------------------------------------------- //

    protected static RebirthConf i;

    public static RebirthConf get() {
        return i;
    }

    public Map<Integer, Award> rebirthAwards = MUtil.map(-1,
            new Award(
                    MUtil.map(CurrencyType.CASH, 1.0, CurrencyType.GEMS, 1.0),
                    MUtil.map(PickaxeExplosiveEnchant.get().getID(), 1),
                    MUtil.map(1, 1),
                    MUtil.map(SkillType.ENCHANTING, 10),
                    MUtil.map(SkillType.PLAYER, 10),
                    MUtil.map(SkillType.MINING, 0.5),
                    MUtil.map(CurrencyType.CASH, 0.5),
                    MUtil.list("&7You've won an award!", "&7Here's a second line!"),
                    MUtil.list(RebirthObjectAward.TEST),
                    MUtil.list("essentials.fly"),
                    MUtil.list("/mines tp %player%"),
                    MUtil.list(PathIntroduction.get().getClass().getSimpleName()),
                    MUtil.list("coal_digger"),
                    MUtil.list("test_hoe"),
                    MUtil.list("test_axe"),
                    MUtil.list("fishing_pole_awards"),
                    MUtil.list(1)
            ));

    public transient Map<Integer, List<String>> awardLore = new HashMap<>();

    @Override
    public RebirthConf load(RebirthConf that) {
        this.setRebirthAwards(that.rebirthAwards);
        return this;
    }

    public void setRebirthAwards(Map<Integer, Award> rebirthAwards) {
        this.rebirthAwards = rebirthAwards;
    }

    public Map<Integer, Award> getRebirthAwards() {
        return rebirthAwards;
    }

    public Award getAward(int level){
        return rebirthAwards.get(level);
    }

    public Map<Integer, List<String>> getAwardLore() {
        if(awardLore.isEmpty()){
            this.getRebirthAwards().forEach((integer, award) -> awardLore.put(integer, award.getLore()));
        }
        return awardLore;
    }
}
