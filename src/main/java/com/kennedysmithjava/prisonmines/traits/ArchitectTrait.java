package com.kennedysmithjava.prisonmines.traits;

import com.kennedysmithjava.prisonmines.PrisonMines;
import com.kennedysmithjava.prisonmines.animation.UpgradeGUIAnimation;
import com.kennedysmithjava.prisonmines.entity.MConf;
import com.kennedysmithjava.prisonmines.entity.Mine;
import com.kennedysmithjava.prisonmines.entity.MineColl;
import com.kennedysmithjava.prisonmines.entity.UpgradesGUIConf;
import com.kennedysmithjava.prisonmines.upgrades.Upgrade;
import com.kennedysmithjava.prisonmines.util.Color;
import com.kennedysmithjava.prisonmines.util.Glow;
import com.massivecraft.massivecore.chestgui.ChestGui;
import com.massivecraft.massivecore.util.MUtil;
import com.mcrivals.prisoncore.PrisonCore;
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

public class ArchitectTrait extends Trait {
    boolean SomeSetting = false;

    public ArchitectTrait() {
        super("architecttrait");
    }

    @EventHandler
    public void click(NPCRightClickEvent event) {
        //Handle a click on a NPC.
        //Be sure to check event.getNPC() == this.getNPC() so you only handle clicks on this NPC!
        if(event.getNPC() != this.getNPC()) return;
        Player player = event.getClicker();
        MPlayer mPlayer = MPlayerColl.get().getByPlayer(player);
        if(mPlayer == null) return;

        openGUI(mPlayer);
    }

    public void openGUI(MPlayer player){

        String mineID = player.getMineID();
        Mine mine = Mine.get(mineID);

        ChestGui gui = ChestGui.getCreative(Bukkit.createInventory(null, 9 * 3, "Architect"));
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

        int sizeIconIndex = 11;
        int mineralIconIndex = 13;
        int customizeIconIndex = 15;

        ItemStack sizeIcon = new ItemStack(Material.PISTON_STICKY_BASE);
        ItemMeta m1 = sizeIcon.getItemMeta();
        m1.setLore(colorLore(MUtil.list("&7Change the &aWidth and", "&aHeight &7of your mine")));
        m1.setDisplayName("");

        ItemStack mineralIcon = new ItemStack(Material.PISTON_STICKY_BASE);
        ItemMeta m2 = sizeIcon.getItemMeta();

        ItemStack customizeIcon = new ItemStack(Material.PISTON_STICKY_BASE);
        ItemMeta m3 = sizeIcon.getItemMeta();

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
