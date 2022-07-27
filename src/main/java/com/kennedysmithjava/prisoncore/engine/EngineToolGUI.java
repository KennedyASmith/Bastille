package com.kennedysmithjava.prisoncore.engine;

import com.kennedysmithjava.prisoncore.tools.Pickaxe;
import com.kennedysmithjava.prisoncore.tools.enchantment.Enchant;
import com.kennedysmithjava.prisoncore.util.Color;
import com.kennedysmithjava.prisoncore.util.Glow;
import com.massivecraft.massivecore.Engine;
import com.massivecraft.massivecore.chestgui.ChestAction;
import com.massivecraft.massivecore.chestgui.ChestGui;
import com.massivecraft.massivecore.util.MUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class EngineToolGUI extends Engine {

    private static EngineToolGUI i = new EngineToolGUI();

    public static EngineToolGUI get()
    {
        return i;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerUse(PlayerInteractEvent event){
        if(!event.getAction().equals(Action.RIGHT_CLICK_AIR)) return;
        Player p = event.getPlayer();
        if(!p.isSneaking()) return;
        ItemStack item = p.getInventory().getItemInMainHand();
        if(!Pickaxe.isPickaxe(item)) return;
        Pickaxe tool = Pickaxe.get(item);
        openPickaxeGUI(p, tool, item);
    }

    public void openPickaxeGUI(Player p, Pickaxe pickaxeLegacy, ItemStack item){

        Inventory pickaxeInv= Bukkit.createInventory(null, InventoryType.HOPPER, color("&8&lPickaxe Upgrades"));

        ItemStack enchantsItem = new ItemStack(Material.BOOK, 1);
        ItemMeta enchantsMeta = enchantsItem.getItemMeta();
        enchantsMeta.addEnchant(Glow.getGlow(), 1, true);
        enchantsMeta.setDisplayName(color("&d&lEnchantments"));
        enchantsMeta.setLore(MUtil.list(" ", color("&7Buff your pickaxe with enchants!")));
        enchantsItem.setItemMeta(enchantsMeta);

        ItemStack powerupsItem = new ItemStack(Material.PRISMARINE_SHARD, 1);
        ItemMeta powerupsMeta = powerupsItem.getItemMeta();
        powerupsMeta.addEnchant(Glow.getGlow(), 1, true);
        powerupsMeta.setDisplayName(color("&6&lPowerups"));
        powerupsMeta.setLore(MUtil.list(" ", color("&7Buff your pickaxe with powerups!")));
        powerupsItem.setItemMeta(powerupsMeta);

        pickaxeInv.setItem(0, enchantsItem);
        pickaxeInv.setItem(1, new ItemStack(Material.WHITE_STAINED_GLASS_PANE, 1));
        pickaxeInv.setItem(2, item.clone());
        pickaxeInv.setItem(3, new ItemStack(Material.WHITE_STAINED_GLASS_PANE, 1));
        pickaxeInv.setItem(4, powerupsItem);


        ChestGui gui = ChestGui.getCreative(pickaxeInv);
        gui.setAction(0, inventoryClickEvent -> {
            enchantMenu(p, item);
                return true;
        });

        gui.setBottomInventoryAllow(false);
        p.openInventory(gui.getInventory());
    }

    public static void enchantMenu(Player player, ItemStack item){
        if(!Pickaxe.isPickaxe(item)){ player.sendMessage(Color.get("You are not holding a pickaxe.")); return; }

        Pickaxe pickaxe = Pickaxe.get(item);

        Inventory enchantInv= Bukkit.createInventory(null, 6*9, Color.get("&8&lPickaxe Enchantments"));
        ChestGui gui = ChestGui.getCreative(enchantInv);
        gui.setAutoclosing(false);

        for (int b = 0; b < enchantInv.getSize(); b++) {
            ItemStack p = new ItemStack(Material.WHITE_STAINED_GLASS_PANE, 1);
            ItemMeta itemMeta = p.getItemMeta();
            itemMeta.setDisplayName(" ");
            p.setItemMeta(itemMeta);
            enchantInv.setItem(b, p);
        }

        Map<String, Integer> enchants = pickaxe.getEnchantsRaw();

        Enchant.getActiveEnchants().forEach((id, enchant) -> {

            if(enchants.containsKey(id)){

                enchantInv.setItem(enchant.getEnchantGUISlot(), getItem(enchant.getDisplayName().replaceAll("%name%", enchant.getName()), enchant.getIcon(),  enchant.getGUILore(enchants.get(id)), true));
                gui.setAction(enchant.getEnchantGUISlot(), upgradeChestAction(enchant, enchants.get(id), pickaxe, player));

            }else{
                enchantInv.setItem(enchant.getEnchantGUISlot(), getItem(enchant.getDisplayName().replaceAll("%name%", enchant.getName()), enchant.getIcon(),  MUtil.list("You have not unlocked this."), false));
                gui.setAction(enchant.getEnchantGUISlot(), buyChestAction(enchant, pickaxe, player, gui));
            }

        });

        player.openInventory(gui.getInventory());
    }


    private static ChestAction upgradeChestAction(Enchant<?> enchant, int currentLevel, Pickaxe pickaxeLegacy, Player player){
        return event -> {
            pickaxeLegacy.addEnchant(enchant, enchant.incrementLevel(currentLevel));
            player.sendMessage(Color.get("&aYou have upgraded your " + enchant.getDisplayName().replaceAll("%name%", enchant.getName()) + " &aenchant!"));
            ItemStack item = pickaxeLegacy.getItem();
            ItemMeta meta = item.getItemMeta();
            meta.setLore(pickaxeLegacy.getType().getLore(pickaxeLegacy.getEnchants(), pickaxeLegacy.getLeveledAbility(), pickaxeLegacy.getDurability(), pickaxeLegacy.getMaxDurability()));
            item.setItemMeta(meta);
            player.setItemInHand(item);
            player.closeInventory();
            return false;
        };
    }

    private static ChestAction buyChestAction(Enchant<?> enchant, Pickaxe pickaxeLegacy, Player player, ChestGui gui){
        return inventoryClickEvent -> {
            pickaxeLegacy.addEnchant(enchant, 1);

            ItemStack item = pickaxeLegacy.getItem();
            ItemMeta meta = item.getItemMeta();
            meta.setLore(pickaxeLegacy.getType().getLore(pickaxeLegacy.getEnchants(), pickaxeLegacy.getLeveledAbility(), pickaxeLegacy.getDurability(), pickaxeLegacy.getMaxDurability()));
            item.setItemMeta(meta);

            player.setItemInHand(item);
            player.sendMessage(Color.get("&aYou purchased the " + enchant.getDisplayName().replaceAll("%name%", enchant.getName()) + " &aenchant!"));

            player.closeInventory();
            return false;
        };
    }

    private static ItemStack getItem(String name, Material material, List<String> lore, boolean glow){
        ItemStack item = new ItemStack(material, 1);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(Color.get((name)));
        List<String> newLore = new ArrayList<>();
        lore.forEach(s -> newLore.add(Color.get(s)));
        meta.setLore(newLore);
        if(glow){ meta.addEnchant(Glow.getGlow(), 1, true); }
        item.setItemMeta(meta);
        return item;
    }

    private String color(String color) {
        return ChatColor.translateAlternateColorCodes('&', color);
    }

}
