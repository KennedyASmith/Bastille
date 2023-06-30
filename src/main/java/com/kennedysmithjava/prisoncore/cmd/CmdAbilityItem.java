package com.kennedysmithjava.prisoncore.cmd;

import com.kennedysmithjava.prisoncore.entity.tools.BufferConf;
import com.kennedysmithjava.prisoncore.tools.AbilityItem;
import com.kennedysmithjava.prisoncore.ability.AbilityType;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.type.primitive.TypeInteger;
import com.massivecraft.massivecore.command.type.primitive.TypeString;
import com.massivecraft.massivecore.util.MUtil;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class CmdAbilityItem extends ToolsCommand {

    private final static CmdAbilityItem INSTANCE = new CmdAbilityItem();

    public static CmdAbilityItem get() {
        return INSTANCE;
    }

    public CmdAbilityItem() {
        setDesc("Get a ability item");
        addParameter(AbilityType.ASTEROID.getId(), new TypeString(), "ability", "set ability (astr, pulv...)");
        addParameter(1, new TypeInteger(), "level", "set ability's level");
        addParameter(1, new TypeInteger(), "power", "set power buffer");
        addParameter(1, new TypeInteger(), "speed", "set speed buffer");
        addParameter(1, new TypeInteger(), "efficiency", "set efficiency buffer");
    }

    @Override
    public void perform() throws MassiveException {
        String ability = this.readArg();
        int level = this.readArg();
        int power = this.readArg();
        int speed = this.readArg();
        int efficiency = this.readArg();

        Player player = (Player) sender;

        AbilityItem abilityItem = new AbilityItem(AbilityType.getFromId(ability),
                level,
                MUtil.map(BufferConf.SPEED, speed,
                        BufferConf.POWER, power,
                        BufferConf.EFFICIENCY, efficiency
                )
        );

        player.getInventory().addItem(abilityItem.getItemStack());
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("abilityitem", "abitem");
    }
}
