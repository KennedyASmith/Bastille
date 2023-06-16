package com.kennedysmithjava.prisoncore.cmd.type;

import com.kennedysmithjava.prisoncore.skill.SkillType;
import com.massivecraft.massivecore.command.type.enumeration.TypeEnum;

public class TypeSkill extends TypeEnum<SkillType> {

    // -------------------------------------------- //
    // INSTANCE & CONSTRUCT
    // -------------------------------------------- //

    private static final TypeSkill i = new TypeSkill();


    public TypeSkill() { super(SkillType.class); }

    public static TypeSkill get() {
        return i;
    }

    // -------------------------------------------- //
    // OVERRIDE
    // -------------------------------------------- //

    @Override
    public String getName() {
        return "skills";
    }


    @Override
    public String getNameInner(SkillType value)
    {
        return value.name();
    }

}
