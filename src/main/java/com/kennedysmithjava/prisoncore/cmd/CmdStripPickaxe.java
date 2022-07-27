package com.kennedysmithjava.prisoncore.cmd;

import com.kennedysmithjava.prisoncore.tools.AbilityItem;
import com.kennedysmithjava.prisoncore.tools.Pickaxe;
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
        ItemStack hand = player.getInventory().getItemInMainHand();

        if (!Pickaxe.isPickaxe(hand)) {
            player.sendMessage("This is not a pickaxe.");
            return;
        }
        Pickaxe pickaxe = Pickaxe.get(hand);
        AbilityItem abilityItem = pickaxe.stripLeveledAbility();
        player.getInventory().addItem(abilityItem.getItemStack());
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("strippick", "pickstrip");
    }
}
