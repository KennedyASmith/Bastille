package com.kennedysmithjava.prisoncore.entity.tools;

import com.kennedysmithjava.prisoncore.tools.ability.Ability;
import com.kennedysmithjava.prisoncore.tools.ability.AbilityAsteroidStrike;
import com.massivecraft.massivecore.command.editor.annotation.EditorName;
import com.massivecraft.massivecore.store.Entity;
import com.massivecraft.massivecore.util.MUtil;
import org.bukkit.Material;

import java.util.List;
import java.util.Map;

@EditorName("config")
public class AbilityConf extends Entity<AbilityConf> {

    public String asteroidStrikeName = "Asteroid Strike";
    public String asteroidStrikeDisplayname = "&6&l%name%";
    public Material asteroidStrikeItem = Material.FIRE_CHARGE;
    public int asteroidStrikeMaxLevel = 3;
    //The %magnitude% of an asteroid strike is = its explosion radius
    public List<String> asteroidStrikeItemLore = MUtil.list("This item will blast", "tons of blocks.", "Radius: %magnitude%", "%buffers%", " ", "Mining Level Required: %mining_level_required%");
    //The item used for the asteroid strike animation
    public Material asteroidStrikeAsteroid = Material.NETHERRACK;
    //Key: Level of ability. Value: The base radius of the asteroid explosion
    public Map<Integer, Integer> asteroidStrikeLevelVSExplosionRadius = MUtil.map(1, 2, 2, 4, 3, 6);
    //Key: Level of ability. Value: The base multiplier of the blocks broken from the explosion
    public Map<Integer, Integer> asteroidStrikeLevelVSMultiplier = MUtil.map(1, 2, 2, 4, 3, 6);
    //Key: Level of ability. Value: The base chance that the blocks within the explosion radius are actually broken
    public Map<Integer, Double> asteroidStrikeLevelVSBreakChance = MUtil.map(1, 0.25, 2, 0.35, 3, 0.45);
    //Key: Level of ability. Value: The base cooldown of the ability in seconds
    public Map<Integer, Integer> asteroidStrikeLevelVSCooldownSeconds = MUtil.map(1, 340, 2, 320, 3, 300);
    //Key: Level of ability. Value: The number of buffers this ability can have
    public Map<Integer, Integer> asteroidStrikeLevelVSBufferSlots = MUtil.map(1, 340, 2, 320, 3, 300);

    protected static transient AbilityConf i;

    public static AbilityConf get() { return i; }

    public static transient Map<String, Ability<?>> activeAbilities = MUtil.map(
            "asteroid", new AbilityAsteroidStrike()
    );

    @Override
    public AbilityConf load(AbilityConf that)
    {
        super.load(that);
        return this;
    }


}
