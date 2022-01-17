package com.kennedysmithjava.prisoncore.npc.spawn;

import com.kennedysmithjava.prisoncore.entity.mines.CoinCollectorGuiConf;
import com.kennedysmithjava.prisoncore.entity.npcs.FarmerGuiConf;
import com.kennedysmithjava.prisoncore.entity.player.MPlayer;
import com.kennedysmithjava.prisoncore.entity.player.MPlayerColl;
import com.kennedysmithjava.prisoncore.util.Color;
import com.massivecraft.massivecore.chestgui.ChestGui;
import com.massivecraft.massivecore.util.Txt;
import net.citizensnpcs.api.event.NPCLeftClickEvent;
import net.citizensnpcs.api.event.NPCRightClickEvent;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.trait.Trait;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.stream.Collectors;

public class NPCFarmerTrait extends Trait {

    public NPCFarmerTrait() {
        super("farmingtrait");
    }

    @EventHandler
    public void click(NPCRightClickEvent event) {
        //Handle a click on a NPC.
        //Be sure to check event.getNPC() == this.getNPC() so you only handle clicks on this NPC!
        if(event.getNPC() != this.getNPC()) return;
        if(!event.getNPC().hasTrait(NPCFarmerTrait.class)) return;
        NPC npc = event.getNPC();
        Player player = event.getClicker();
        if (player == null) return;
        //Ensure that the clicker isn't already in the Limbo NPC's dialogue.

        MPlayer mPlayer = MPlayerColl.get().getByPlayer(player);
        if (mPlayer == null) return;

        openDefaultGUI(mPlayer);
    }

    public void openDefaultGUI(MPlayer player) {
        CoinCollectorGuiConf conf = CoinCollectorGuiConf.get();
        ChestGui gui = getChestGUI();

        player.getPlayer().openInventory(gui.getInventory());
    }


    private ChestGui getChestGUI() {
        Inventory inventory = Bukkit.createInventory(null, FarmerGuiConf.get().guiSize, Txt.parse(FarmerGuiConf.get().guiName));

        ChestGui chestGui = ChestGui.getCreative(inventory);
        chestGui.setAutoclosing(false);
        chestGui.setAutoremoving(false);
        chestGui.setSoundOpen(null);
        chestGui.setSoundClose(null);

        blockFill(chestGui.getInventory(), Material.WHITE_STAINED_GLASS_PANE);

        return chestGui;
    }


    @EventHandler
    public void click(NPCLeftClickEvent event) {

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
    public ItemStack buildItem(Material material, String unformattedName, List<String> unformattedLore) {
        ItemStack itemStack = new ItemStack(material, 1);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(Color.get(unformattedName));
        itemMeta.setLore(unformattedLore.stream().map(Color::get).collect(Collectors.toList()));
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public void blockFill(Inventory inv, Material material){
        for (int b = 0; b < inv.getSize(); b++) {
            ItemStack p = new ItemStack(material, 1);
            ItemMeta itemMeta = p.getItemMeta();
            itemMeta.setDisplayName(" ");
            p.setItemMeta(itemMeta);
            inv.setItem(b, p);
        }
    }
}
