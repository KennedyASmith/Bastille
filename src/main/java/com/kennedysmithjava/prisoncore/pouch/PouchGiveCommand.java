package com.kennedysmithjava.prisoncore.pouch;

import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.MassiveCommand;
import com.massivecraft.massivecore.command.requirement.RequirementIsPlayer;
import com.massivecraft.massivecore.command.type.primitive.TypeInteger;
import org.bukkit.inventory.ItemStack;

public class PouchGiveCommand extends MassiveCommand {

    public PouchGiveCommand() {
        this.addAliases("g", "give");

        this.addParameter(TypeInteger.get(), "id", true);

        this.addRequirements(RequirementIsPlayer.get());
    }

    @Override
    public void perform() throws MassiveException {
        int id = this.readArg();

        ItemStack newPouchItem = PouchType.from(id).getNewPouchItem();

        me.getInventory().addItem(newPouchItem);
    }
}
