package com.kennedysmithjava.prisonmines.pouch;

import com.kennedysmithjava.prisonmines.PrisonMines;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.MassiveCommand;
import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

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
