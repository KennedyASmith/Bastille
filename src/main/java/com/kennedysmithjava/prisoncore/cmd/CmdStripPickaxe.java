package com.kennedysmithjava.prisoncore.cmd;

import com.kennedysmithjava.prisoncore.tools.AbilityItem;
import com.kennedysmithjava.prisoncore.tools.Pickaxe;
import com.kennedysmithjava.prisoncore.tools.Tool;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;

public class CmdStripPickaxe extends ToolsCommand {

    private static final CmdStripPickaxe INSTANCE = new CmdStripPickaxe();

    public static CmdStripPickaxe get() {
        return INSTANCE;
    }

    public CmdStripPickaxe() {
        setDesc("Strip an ability off of a pickaxe");
    }

    @Override
    public void perform() {
        Player player = (Player) sender;
        ItemStack hand = player.getItemInHand();

        if (!Tool.isTool(hand)) {
            player.sendMessage("This is not a tool");
            return;
        }
        Tool t = Tool.get(hand);
        //If the item is a pickaxe
        if (!(t instanceof Pickaxe)) {
            player.sendMessage("This is not a pickaxe");
            return;
        }
        Pickaxe pickaxe = (Pickaxe) t;
        AbilityItem abilityItem = pickaxe.stripLeveledAbility();
        player.getInventory().addItem(abilityItem.getItemStack());
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("strippick", "pickstrip");
    }
}
