package com.kennedysmithjava.prisoncore.engine;

import com.kennedysmithjava.prisoncore.PrisonCore;
import com.kennedysmithjava.prisoncore.entity.MConf;
import com.kennedysmithjava.prisoncore.entity.mines.Mine;
import com.kennedysmithjava.prisoncore.entity.mines.MineColl;
import com.kennedysmithjava.prisoncore.gui.EnchantMenuGui;
import com.kennedysmithjava.prisoncore.util.Color;
import com.kennedysmithjava.prisoncore.util.Glow;
import com.kennedysmithjava.prisoncore.util.regions.LazyRegion;
import com.massivecraft.massivecore.Engine;
import com.massivecraft.massivecore.chestgui.ChestGui;
import com.massivecraft.massivecore.util.MUtil;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class EngineMineBuildings extends Engine {

    private static final EngineMineBuildings i = new EngineMineBuildings();
    public static EngineMineBuildings get() {
        return i;
    }

    private static final MineColl mineColl = MineColl.get();
    private static final double VELOCITY = Math.sqrt(4 * 4 * 0.08);
    private static final List<Material> BUILDINGS = MUtil.list(Material.ENCHANTING_TABLE, Material.BEACON, Material.CHEST, Material.ANVIL, Material.HOPPER);

    @EventHandler
    public void onJumpPadPress(PlayerInteractEvent ev) {
        if (ev.getAction().equals(Action.PHYSICAL)) {
            if (ev.getClickedBlock().getType() == Material.HEAVY_WEIGHTED_PRESSURE_PLATE) {
                Player player = ev.getPlayer();
                Mine mine = mineColl.getByLocation(ev.getClickedBlock());
                if(mine == null) return;
                int targetY = (player.getLocation().getBlockY() + mine.getHeight());

                BukkitRunnable jumpAnimation = new BukkitRunnable() {
                    @Override
                    public void run() {
                        if(player == null || player.getLocation().getY() > targetY) {
                            end();
                            return;
                        }
                        player.setVelocity(new Vector(player.getVelocity().getX(), VELOCITY, player.getVelocity().getZ()));
                        player.spawnParticle(Particle.CAMPFIRE_SIGNAL_SMOKE, player.getLocation().clone().add(0,-1,0), 1);
                        player.playSound(player.getLocation(), Sound.BLOCK_FIRE_EXTINGUISH, 0.5F, 0.0F);
                    }

                    public void end(){
                        this.cancel();
                    }
                };
                jumpAnimation.runTaskTimerAsynchronously(PrisonCore.get(), 0, 10);
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerPortalUse(PlayerMoveEvent event){
        Player player = event.getPlayer();
        Material m = player.getLocation().getBlock().getType();
        Material a = player.getLocation().clone().add(0,1,0).getBlock().getType();
        if ((m != Material.WATER) || (a != Material.WATER)) return;

        Mine mine = mineColl.getByLocation(event.getTo());
        if(mine == null) return;

        LazyRegion region = new LazyRegion(mine.getPortalMaxLocation(), mine.getPortalMinLocation());
        if(!region.contains(player.getLocation())) return;

        Inventory inventory = Bukkit.createInventory(null, InventoryType.HOPPER, Color.get("&8&lSelect a Destination"));
        ChestGui gui = ChestGui.getCreative(inventory);
        gui.setAutoclosing(false);
        blockFill(inventory, Material.WHITE_STAINED_GLASS_PANE, (short) 1);

        ItemStack spawnItem = getItem("&a&lTown", Material.COMPASS, MUtil.list("", "&7Teleport to the spawn Town!"), false);
        inventory.setItem(2, spawnItem);


        gui.setAction(2, clickEvent -> {
            Location spawn = MConf.get().getSpawnLocation().add(-0.5, 0, -0.5);
            player.teleport(spawn);
            return true;
        });

        player.openInventory(gui.getInventory());
    }

    public boolean inRegion(Player p, Location loc1, Location loc2) {
        double x1 = loc1.getX();
        double y1 = loc1.getY();
        double z1 = loc1.getZ();
        double x2 = loc2.getX();
        double y2 = loc2.getY();
        double z2 = loc2.getZ();
        return (p.getLocation().getX() > x1) && (p.getLocation().getY() > y1) && (p.getLocation().getZ() > z1) && (p.getLocation().getX() < x2) && (p.getLocation().getY() < y2) && (p.getLocation().getZ() < z2);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerBlockInteract(PlayerInteractEvent event) {
        Action action = event.getAction();
        if(action == Action.LEFT_CLICK_BLOCK || action == Action.RIGHT_CLICK_BLOCK) {
            Block block = event.getClickedBlock();
            if(!BUILDINGS.contains(block.getType())) return;

            switch(block.getType()){
                default:
                    return;
                case ENCHANTING_TABLE:
                    enchantMenu(event.getPlayer());
                    event.setCancelled(true);
                    break;
                case CHEST:
                    chestMenu(event.getPlayer());
                    break;
                case BEACON:
                    beaconMenu(event.getPlayer());
                    break;
                case ANVIL:
                    anvilMenu(event.getPlayer());
                    break;
                case HOPPER:
                    hopperMenu(event.getPlayer());
                    break;
            }


        }
    }

    public void enchantMenu(Player player){
        EnchantMenuGui menu = new EnchantMenuGui(player);
        menu.open();
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
