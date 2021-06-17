package com.kennedysmithjava.prisonmines.traits;

import com.kennedysmithjava.prisonmines.PrisonMines;
import com.kennedysmithjava.prisonmines.animation.UpgradeGUIAnimation;
import com.kennedysmithjava.prisonmines.entity.MConf;
import com.kennedysmithjava.prisonmines.entity.Mine;
import com.kennedysmithjava.prisonmines.entity.UpgradesGUIConf;
import com.kennedysmithjava.prisonmines.upgrades.Upgrade;
import com.kennedysmithjava.prisonmines.util.Color;
import com.kennedysmithjava.prisonmines.util.Glow;
import com.massivecraft.massivecore.chestgui.ChestGui;
import com.mcrivals.prisoncore.entity.MPlayer;
import com.mcrivals.prisoncore.entity.MPlayerColl;
import net.citizensnpcs.api.event.NPCRightClickEvent;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.trait.Trait;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ResearcherTrait extends Trait {
    boolean SomeSetting = false;

    public ResearcherTrait() {
        super("researchertrait");
    }

    @EventHandler
    public void click(NPCRightClickEvent event) {
        //Handle a click on a NPC.
        //Be sure to check event.getNPC() == this.getNPC() so you only handle clicks on this NPC!
        if(event.getNPC() != this.getNPC()) return;
        NPC npc = event.getNPC();
        Player player = event.getClicker();
        MPlayer mPlayer = MPlayerColl.get().getByPlayer(player);
        if(mPlayer == null) return;

        String mineID = mPlayer.getMineID();

        //Checks if the player has permission to use this architect.
        if(!Mine.get(mineID).getArchitectUUID().equals(npc.getUniqueId().toString())){
            player.sendMessage(Color.get(MConf.get().architectLineNoPermission));
            return;
        }

        player.sendMessage(Color.get(MConf.get().architectLineWelcome));
        openGUI(mPlayer);
    }

    public void openGUI(MPlayer player){

        String mineID = player.getMineID();
        Mine mine = Mine.get(mineID);

        ChestGui gui = ChestGui.getCreative(Bukkit.createInventory(null, 9 * 5, "Warren"));
        Inventory inv = gui.getInventory();


        //Fill the rest of the slots with placeholder
        for (int b = 0; b < inv.getSize(); b++) {
            ItemStack p = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) UpgradesGUIConf.get().guiBorderColor);
            ItemMeta itemMeta = p.getItemMeta();
            itemMeta.setDisplayName(" ");
            p.setItemMeta(itemMeta);
            inv.setItem(b, p);
        }

        gui.setAutoclosing(false);
        gui.setBottomInventoryAllow(false);

        List<Upgrade> upgrades = UpgradesGUIConf.get().getUpgrades().get(mine.getLevel());
        upgrades.forEach(upgrade -> {

            Material material;
            List<String> lore = new ArrayList<>();

            boolean locked = true;
            boolean purchased = false;
            boolean glow = false;

            if(mine.getUpgrades().containsAll(upgrade.getRequiredUpgrades())) locked = false;
            if(mine.getUpgrades().contains(upgrade.getName())) purchased = true;

            if(!locked && !purchased){

                material = upgrade.getIcon();
                gui.setAction(upgrade.getSlot(), e -> {
                    MPlayer p = MPlayerColl.get().getByPlayer((Player) e.getWhoClicked());
                    Mine.get(p.getMineID()).addUpgrade(upgrade);
                    upgrade.apply(p);
                    openGUI(p);
                    return true;
                });
                glow = true;
                lore.add(Color.get("&7Purchase for &a$500"));
            }else if(locked && !purchased){
                material = Material.IRON_FENCE;
                lore.add(Color.get("Unlock previous upgrades first!"));
            }else{
                material = Material.STAINED_GLASS_PANE;
                lore.add(Color.get("&aPurchased ✔"));
            }

            ItemStack item = new ItemStack(material, 1);
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(Color.get(upgrade.getDisplayName()));
            meta.setLore(lore);
            item.setItemMeta(meta);
            if(glow) meta.addEnchant(Glow.getGlow(), 1, false);

            inv.setItem(upgrade.getSlot(), item);
        });

        Upgrade mainUpgrade = UpgradesGUIConf.get().getMainMineUpgrades().get(mine.getLevel());
        ItemStack mineLevelItem = new ItemStack(Material.MINECART, 1);
        ItemMeta meta = mineLevelItem.getItemMeta();
        meta.setDisplayName(Color.get("&c&l" + ChatColor.stripColor(Color.get(mine.getName()))));
        List<String> lore;

        if(mine.getUpgrades().containsAll(mainUpgrade.getRequiredUpgrades())){
            lore = colorLore(Arrays.asList(" ", "&aLeft Click &7to upgrade mine!", " ", "&7LVL &a" + mine.getLevel() + "&7 ⊳ " + "LVL &a" + (Math.addExact(mine.getLevel(), 1))));
            meta.addEnchant(Glow.getGlow(), 1, false);
        }else{
            lore = colorLore(Arrays.asList(" ", "&7Purchase all upgrades before", "", "unlocking the next level!"));
        }

        meta.setLore(lore);

        mineLevelItem.setItemMeta(meta);
        inv.setItem(mainUpgrade.getSlot(), mineLevelItem);

        gui.setAction(mainUpgrade.getSlot(), e -> {
            MPlayer p = MPlayerColl.get().getByPlayer((Player) e.getWhoClicked());
            mainUpgrade.apply(p);
            new UpgradeGUIAnimation(e.getClickedInventory(), player).runTaskTimer(PrisonMines.get(), 5, 1);
            return true;
        });

        player.getPlayer().openInventory(gui.getInventory());
    }

    // Called every tick
    @Override
    public void run() {
    }

    //Run code when your trait is attached to a NPC.
    @Override
    public void onAttach() {

    }

    // Run code when the NPC is despawned. This is called before the entity actually despawns so npc.getEntity() is still valid.
    @Override
    public void onDespawn() {

    }

    //Run code when the NPC is spawned. Note that npc.getEntity() will be null until this method is called.
    //This is called AFTER onAttach and AFTER Load when the server is started.
    @Override
    public void onSpawn() {

    }

    //run code when the NPC is removed. Use this to tear down any repeating tasks.
    @Override
    public void onRemove() {

    }


    public List<String> colorLore(List<String> lore){
        List<String> formatted = new ArrayList<>();
        lore.forEach(s -> formatted.add(Color.get(s)));
        return formatted;
    }
}
