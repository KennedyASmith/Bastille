package com.kennedysmithjava.prisoncore.entity.player;

import com.kennedysmithjava.prisoncore.skill.SkillType;
import com.massivecraft.massivecore.command.editor.annotation.EditorName;
import com.massivecraft.massivecore.store.Entity;
import com.massivecraft.massivecore.util.MUtil;
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;
import org.bukkit.Bukkit;

import java.util.HashMap;
import java.util.Map;

@EditorName("config")
public class SkillsConf extends Entity<SkillsConf> {

    // -------------------------------------------- //
    // META
    // -------------------------------------------- //

    protected static SkillsConf i;

    public static SkillsConf get() {
        return i;
    }

    private transient static final String LEVEL_VARIABLE = "level";

    public Map<SkillType, String> rawXPFormulas = MUtil.map(
            SkillType.PLAYER, "10 * (level^0.2) * (level^2) + ((level - 1)^2)",
            SkillType.ACROBATICS, "10 * (level^0.2) * (level^2) + ((level - 1)^2)",
            SkillType.DEFENSE, "10 * (level^0.2) * (level^2) + ((level - 1)^2)",
            SkillType.CRAFTING, "10 * (level^0.2) * (level^2) + ((level - 1)^2)",
            SkillType.ALCHEMY, "10 * (level^0.2) * (level^2) + ((level - 1)^2)",
            SkillType.FISHING, "10 * (level^0.2) * (level^2) + ((level - 1)^2)",
            SkillType.ENCHANTING, "10 * (level^0.2) * (level^2) + ((level - 1)^2)",
            SkillType.MELEE, "10 * (level^0.2) * (level^2) + ((level - 1)^2)",
            SkillType.EXPLORATION, "10 * (level^0.2) * (level^2) + ((level - 1)^2)",
            SkillType.GEMCUTTING, "10 * (level^0.2) * (level^2) + ((level - 1)^2)",
            SkillType.LEADERSHIP, "10 * (level^0.2) * (level^2) + ((level - 1)^2)",
            SkillType.METALWORKING, "10 * (level^0.2) * (level^2) + ((level - 1)^2)",
            SkillType.MINING, "10 * (level^0.2) * (level^2) + ((level - 1)^2)",
            SkillType.QUESTING, "10 * (level^0.2) * (level^2) + ((level - 1)^2)",
            SkillType.RANGED, "10 * (level^0.2) * (level^2) + ((level - 1)^2)",
            SkillType.REPUTATION, "10 * (level^0.2) * (level^2) + ((level - 1)^2)",
            SkillType.TAMING, "10 * (level^0.2) * (level^2) + ((level - 1)^2)",
            SkillType.WOODCUTTING, "10 * (level^0.2) * (level^2) + ((level - 1)^2)"
            );
    public transient static Map<SkillType, Map<Integer, Integer>> xpNeeded = new HashMap<>();

    public Map<SkillType, Integer> maxLevels = MUtil.map(
            SkillType.PLAYER, 100,
            SkillType.ACROBATICS, 50,
            SkillType.DEFENSE, 50,
            SkillType.CRAFTING, 50,
            SkillType.ALCHEMY, 50,
            SkillType.FISHING, 50,
            SkillType.ENCHANTING, 50,
            SkillType.MELEE, 50,
            SkillType.EXPLORATION, 50,
            SkillType.GEMCUTTING, 50,
            SkillType.LEADERSHIP, 5,
            SkillType.METALWORKING, 50,
            SkillType.MINING, 50,
            SkillType.QUESTING, 50,
            SkillType.RANGED, 50,
            SkillType.REPUTATION, 50,
            SkillType.TAMING, 50,
            SkillType.WOODCUTTING, 50
            );

    @Override
    public SkillsConf load(SkillsConf that) {
        this.setRawXPFormulas(that.rawXPFormulas);
        this.setMaxLevels(that.maxLevels);
        return this;
    }

    public void setRawXPFormulas(Map<SkillType, String> rawXPFormulas) {
        this.rawXPFormulas = rawXPFormulas;
        this.changed();
    }

    public static int getXpRequired(SkillType type, int level){
        if(xpNeeded.isEmpty()) SkillsConf.get().buildFormulas();
        Map<Integer, Integer> xpMap = xpNeeded.get(type);
        if(xpMap == null){
            Bukkit.broadcastMessage("No XP formula exists for skill type: " + type);
            return 42069;
        }else {
            return xpMap.get(level);
        }
    }

    public void buildFormulas(){
        xpNeeded = new HashMap<>();
        rawXPFormulas.forEach((skillType, s) -> {
            Expression e = new ExpressionBuilder(s)
                    .variables(LEVEL_VARIABLE)
                    .build();
            Map<Integer, Integer> xpNeed = new HashMap<>();
            for (int level = 0; level <= maxLevels.get(skillType); level++) {
                e.setVariable("level", level);
                int xp = (int) e.evaluate();
                xpNeed.put(level, xp);
            }
            xpNeeded.put(skillType, xpNeed);
        });
    }

    public void setMaxLevels(Map<SkillType, Integer> maxLevels) {
        this.maxLevels = maxLevels;
    }

    public Map<SkillType, Integer> getMaxLevels() {
        return maxLevels;
    }
}
