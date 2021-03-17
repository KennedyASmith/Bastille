package com.kennedysmithjava.prisonmines.cmd.type;

import com.kennedysmithjava.prisonmines.entity.MConf;
import com.kennedysmithjava.prisonmines.entity.MineColl;
import com.kennedysmithjava.prisonmines.util.MiscUtil;
import com.massivecraft.massivecore.Named;
import com.massivecraft.massivecore.command.type.TypeNameAbstract;
import org.bukkit.command.CommandSender;

public class TypeMineNameAbstract extends TypeNameAbstract
{
    // -------------------------------------------- //
    // CONSTRUCT
    // -------------------------------------------- //

    public TypeMineNameAbstract(boolean strict)
    {
        super(strict);
    }

    // -------------------------------------------- //
    // OVERRIDE
    // -------------------------------------------- //


    @Override
    public boolean isNameTaken(String name)
    {
        return MineColl.get().isNameTaken(name);
    }

    @Override
    public boolean isCharacterAllowed(char character)
    {
        return MiscUtil.substanceChars.contains(String.valueOf(character));
    }

    @Override
    public Named getCurrent(CommandSender commandSender) {
        return null;
    }

    @Override
    public Integer getLengthMin()
    {
        return MConf.get().mineNameLengthMin;
    }

    @Override
    public Integer getLengthMax()
    {
        return MConf.get().mineNameLengthMax;
    }

}
