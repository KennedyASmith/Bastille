package com.kennedysmithjava.prisonmines.cmd;

import com.massivecraft.massivecore.MassiveException;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class CmdOffsetWand extends MineCommand {

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
