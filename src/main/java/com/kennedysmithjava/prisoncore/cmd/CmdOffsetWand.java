package com.kennedysmithjava.prisoncore.cmd;

import com.massivecraft.massivecore.MassiveException;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class CmdOffsetWand extends CoreCommand {

    public CmdOffsetWand() {

        // Aliases
        this.addAliases("wand");

    }

    @Override
    public void perform() throws MassiveException {
        if (!(sender instanceof Player)) return;
        Player player = (Player) sender;

        player.getInventory().addItem(new ItemStack(Material.STONE_AXE, 1));
    }



}
