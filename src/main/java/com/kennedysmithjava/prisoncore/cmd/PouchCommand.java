package com.kennedysmithjava.prisoncore.cmd;

import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.MassiveCommand;
import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class PouchCommand extends MassiveCommand {

    private static final PouchCommand i = new PouchCommand();
    public static PouchCommand get() {
        return i;
    }

    public PouchGiveCommand pouchGiveCommand = new PouchGiveCommand();

    public PouchCommand() {
        this.setAliases("pouch");
        this.addChild(pouchGiveCommand);
        this.setSetupEnabled(true);
    }

    @Override
    public void perform() throws MassiveException {
        ItemStack itemStack = new ItemStack(Material.APPLE);

        ItemStack itemInHand = me.getItemInHand();
        NBTItem nbtItem = new NBTItem(itemInHand);
        me.sendMessage(nbtItem.toString());
    }


}
