package com.kennedysmithjava.prisoncore.engine;

import com.kennedysmithjava.prisoncore.PrisonCore;
import com.kennedysmithjava.prisoncore.entity.player.MPlayer;
import com.kennedysmithjava.prisoncore.entity.player.Skill;
import com.kennedysmithjava.prisoncore.entity.player.SkillProfile;
import com.kennedysmithjava.prisoncore.entity.player.SkillsConf;
import com.kennedysmithjava.prisoncore.skill.SkillType;
import com.kennedysmithjava.prisoncore.util.Color;
import com.massivecraft.massivecore.Engine;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class EngineXP extends Engine {

    // -------------------------------------------- //
    // INSTANCE & CONSTRUCT
    // -------------------------------------------- //


    private static final EngineXP i = new EngineXP();

    public static EngineXP get() {
        return i;
    }

    private static final Map<MPlayer, Map<SkillType, Integer>> oweList = new HashMap<>();
    private static final long delay = 3*20L;
    public static Random random = new Random();

    public EngineXP() {
        BukkitScheduler scheduler = PrisonCore.get().getServer().getScheduler();
        scheduler.scheduleSyncRepeatingTask(PrisonCore.get(), () -> {
            oweList.forEach((player, skillMap) -> {
                SkillProfile profile = player.getSkillProfile();
                skillMap.forEach((type, xp) -> {
                    Skill skill = profile.getSkill(type);
                    boolean leveledUp = skill.addXP(xp);
                    int neededXP = SkillsConf.getXpRequired(type, skill.getCurrentLevel());
                    if(leveledUp){
                        String colorCoded = Color.getGradient(
                                " ※ You have reached " +
                                        Color.strip(type.getDisplayName()) +
                                        " level " +
                                        skill.getCurrentLevel() + "! ※"
                                , "43EF20", "24FDF7");
                        player.msg("&7[&fSkills&7]"+ colorCoded);
                    } else {
                        player.msg("&7[&fSkills&7] &aYou have gained &e" + xp + " " + type.getDisplayName() + " &axp!");
                        player.msg("&7XP Remaining for next level: "
                                + skill.getXPBar(neededXP)
                                + " &7(&e" + skill.getCurrentXP() + "&7/&e" + neededXP + "&7)");
                    }
                });
            });
            oweList.clear();
        }, 20*10L, delay);
    }


    public static void giveXP(SkillType type, MPlayer player, int xpAmount){
        Map<SkillType, Integer> skillXpMap = oweList.get(player);
        if(skillXpMap == null) skillXpMap = new HashMap<>();

        Integer xpAlreadyOwed = skillXpMap.get(type);
        if(xpAlreadyOwed == null) xpAlreadyOwed = 0;
        skillXpMap.put(type, xpAmount + xpAlreadyOwed);
        oweList.put(player, skillXpMap);
    }
}
