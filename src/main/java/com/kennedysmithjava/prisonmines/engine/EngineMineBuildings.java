package com.kennedysmithjava.prisonmines.engine;

import com.kennedysmithjava.prisonmines.entity.Mine;
import com.kennedysmithjava.prisonmines.entity.MineColl;
import com.kennedysmithjava.prisonmines.util.Color;
import com.kennedysmithjava.prisontools.enchantment.Enchant;
import com.kennedysmithjava.prisontools.entity.Pickaxe;
import com.kennedysmithjava.prisontools.entity.Tool;
import com.kennedysmithjava.prisontools.util.Glow;
import com.massivecraft.massivecore.Engine;
import com.massivecraft.massivecore.chestgui.ChestGui;
import com.massivecraft.massivecore.util.MUtil;
import com.mcrivals.prisoncore.engine.Cooldown;
import com.mcrivals.prisoncore.engine.CooldownReason;
import com.mcrivals.prisoncore.entity.MPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class EngineMineBuildings extends Engine {

    private static final EngineMineBuildings i = new EngineMineBuildings();

    public static EngineMineBuildings get() {
        return i;
    }


    public static List<Material> blockList = MUtil.list(Material.ENCHANTMENT_TABLE, Material.BEACON, Material.CHEST, Material.ANVIL, Material.HOPPER);



    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerBlockInteract(PlayerInteractEvent event) {
        Action action = event.getAction();
        if(action == Action.LEFT_CLICK_BLOCK || action == Action.RIGHT_CLICK_BLOCK) {
            Block block = event.getClickedBlock();
            if(!blockList.contains(block.getType())) return;

            switch(block.getType()){
                default:
                    return;
                case ENCHANTMENT_TABLE:
                    enchantMenu(event.getPlayer());
                    event.setCancelled(true);
                    break;
                case CHEST:
                    chestMenu(event.getPlayer());
                    event.setCancelled(true);
                    break;
                case BEACON:
                    beaconMenu(event.getPlayer());
                    event.setCancelled(true);
                    break;
                case ANVIL:
                    anvilMenu(event.getPlayer());
                    event.setCancelled(true);
                    break;
                case HOPPER:
                    hopperMenu(event.getPlayer());
                    event.setCancelled(true);
                    break;
            }


        }
    }

    public void enchantMenu(Player player){

        ItemStack item = player.getItemInHand();

        if(!Tool.isTool(item)){ player.sendMessage(Color.get("You are not holding an enchantable item.")); return; }

        Tool tool = Tool.get(item);
        Pickaxe pickaxe = (Pickaxe) tool;

        Inventory enchantInv= Bukkit.createInventory(null, 5*9, Color.get("&8&lPickaxe Enchantments"));
        ChestGui gui = ChestGui.getCreative(enchantInv);
        gui.setAutoclosing(false);

        blockFill(enchantInv, Material.STAINED_GLASS_PANE, (short) 1);

        Map<String, Integer> enchants = pickaxe.getEnchantsRaw();

        ItemStack pickaxeItem = pickaxe.getItem();

        Enchant.getActiveEnchants().forEach((id, enchant) -> {

            if(enchants.containsKey(id)){
                enchantInv.setItem(enchant.getEnchantGUISlot(), getItem(enchant.getDisplayName().replaceAll("%name%", enchant.getName()), enchant.getIcon(),  enchant.getGUILore(enchants.get(id)), true));
                gui.setAction(enchant.getEnchantGUISlot(), inventoryClickEvent -> {
                            pickaxe.addEnchant(enchant, enchant.incrementLevel(enchants.get(id)));
                            player.sendMessage(Color.get("&aYou have upgraded your " + enchant.getDisplayName().replaceAll("%name%", enchant.getName()) + " &aenchant!"));
                            ItemStack newItem = pickaxe.getItem();
                            ItemMeta meta = pickaxeItem.getItemMeta();
                            meta.setLore(pickaxe.getType().getLore(pickaxe.getEnchants()));
                            newItem.setItemMeta(meta);
                            player.setItemInHand(newItem);
                            player.closeInventory();
                            return false;
                        });
            }else{
                enchantInv.setItem(enchant.getEnchantGUISlot(), getItem(enchant.getDisplayName().replaceAll("%name%", enchant.getName()), enchant.getIcon(),  MUtil.list("You have not unlocked this."), false));

                gui.setAction(enchant.getEnchantGUISlot(), inventoryClickEvent -> {
                    pickaxe.addEnchant(enchant, 1);
                    player.sendMessage(Color.get("&aYou have upgraded your " + enchant.getDisplayName().replaceAll("%name%", enchant.getName()) + " &aenchant!"));
                    ItemStack newItem = pickaxe.getItem();
                    ItemMeta meta = pickaxeItem.getItemMeta();
                    meta.setLore(pickaxe.getType().getLore(pickaxe.getEnchants()));
                    newItem.setItemMeta(meta);
                    player.setItemInHand(newItem);
                    player.closeInventory();
                    return false;
                });
            }

        });

        player.openInventory(gui.getInventory());

    }

    public void chestMenu(Player player){

    }

    public void beaconMenu(Player player){

    }

    public void anvilMenu(Player player){

    }

    public void hopperMenu(Player player){

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

    public void blockFill(Inventory inv, Material material, short data){
        for (int b = 0; b < inv.getSize(); b++) {
            ItemStack p = new ItemStack(material, 1, data);
            ItemMeta itemMeta = p.getItemMeta();
            itemMeta.setDisplayName(" ");
            p.setItemMeta(itemMeta);
            inv.setItem(b, p);
        }
    }

}
