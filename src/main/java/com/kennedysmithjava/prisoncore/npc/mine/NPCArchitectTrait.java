package com.kennedysmithjava.prisoncore.npc.mine;

import com.kennedysmithjava.prisoncore.CooldownReason;
import com.kennedysmithjava.prisoncore.engine.EngineCooldown;
import com.kennedysmithjava.prisoncore.engine.EngineLoadingScreen;
import com.kennedysmithjava.prisoncore.entity.mines.*;
import com.kennedysmithjava.prisoncore.entity.mines.objects.Floor;
import com.kennedysmithjava.prisoncore.entity.mines.objects.Wall;
import com.kennedysmithjava.prisoncore.entity.player.MPlayer;
import com.kennedysmithjava.prisoncore.entity.player.MPlayerColl;
import com.kennedysmithjava.prisoncore.upgrades.actions.ActionMineSize;
import com.kennedysmithjava.prisoncore.util.Color;
import com.kennedysmithjava.prisoncore.util.DistributionPage;
import com.kennedysmithjava.prisoncore.util.Glow;
import com.massivecraft.massivecore.chestgui.ChestAction;
import com.massivecraft.massivecore.chestgui.ChestGui;
import com.massivecraft.massivecore.util.MUtil;
import net.citizensnpcs.api.event.NPCRightClickEvent;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.trait.Trait;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class NPCArchitectTrait extends Trait {

    public NPCArchitectTrait() {
        super("architecttrait");
    }

    @EventHandler
    public void click(NPCRightClickEvent event) {
        //Handle a click on a NPC.
        //Be sure to check event.getNPC() == this.getNPC() so you only handle clicks on this NPC!
        if(event.getNPC() != this.getNPC()) return;
        if(!event.getNPC().hasTrait(NPCArchitectTrait.class)) return;
        NPC npc = event.getNPC();
        Player player = event.getClicker();
        MPlayer mPlayer = MPlayerColl.get().getByPlayer(player);
        if(mPlayer == null) return;

        /*String mineID = mPlayer.getMineID();

        //Checks if the player has permission to use this architect.
        if(!Mine.get(mineID).getArchitectUUID().equals(npc.getUniqueId().toString())){
            player.sendMessage(Color.get(WarrenConf.get().researcherLineNoPermission));
            return;
        }*/

        player.sendMessage(Color.get(WarrenConf.get().researcherLineWelcome));
        openMenuGUI(mPlayer);
    }

    public void openMenuGUI(MPlayer player){

        ChestGui gui = ChestGui.getCreative(Bukkit.createInventory(null, 9 * 3, "Architect"));
        Inventory inv = gui.getInventory();
        gui.setAutoclosing(false);
        gui.setBottomInventoryAllow(false);

        //Fill the rest of the slots with placeholder
        blockFill(inv, Material.WHITE_STAINED_GLASS_PANE);

        ItemStack upgradesSizeIcon = new ItemStack(Material.PISTON, 1);
        ItemMeta sizeIconMeta = upgradesSizeIcon.getItemMeta();
        sizeIconMeta.setDisplayName(Color.get("&6&lMine Size"));
        sizeIconMeta.setLore(Color.get(MUtil.list("", "&eCLICK &7to", "&7Change the size of your mine!")));
        upgradesSizeIcon.setItemMeta(sizeIconMeta);

        ItemStack distributionIcon = new ItemStack(Material.COAL_ORE, 1);
        ItemMeta distributionIconMeta = distributionIcon.getItemMeta();
        distributionIconMeta.setDisplayName(Color.get("&6&lMine Blocks"));
        distributionIconMeta.setLore(Color.get(MUtil.list("", "&eCLICK &7to", "&7Change the blocks that", "&7generate in your mine!")));
        distributionIcon.setItemMeta(distributionIconMeta);

        ItemStack decorIcon = new ItemStack(Material.PAINTING, 1);
        ItemMeta decorIconMeta = decorIcon.getItemMeta();
        decorIconMeta.setDisplayName(Color.get("&6&lMine Decoration"));
        decorIconMeta.setLore(Color.get(MUtil.list("", "&eCLICK &7to", "&7Change your mine's Wall", "&7and Path designs!")));
        decorIcon.setItemMeta(decorIconMeta);

        inv.setItem(11, distributionIcon);
        inv.setItem(13, upgradesSizeIcon);
        inv.setItem(15, decorIcon);

        gui.setAction(13, inventoryClickEvent -> {
            HumanEntity whoClicked = inventoryClickEvent.getWhoClicked();
            if(!(whoClicked instanceof Player)) return false;
            openSizesMenu(player);
            return false;
        });

        gui.setAction(11, inventoryClickEvent -> {
            HumanEntity whoClicked = inventoryClickEvent.getWhoClicked();
            if(!(whoClicked instanceof Player)) return false;
            openDistributionMenu(player, 1);
            return false;
        });

        gui.setAction(15, inventoryClickEvent -> {
            HumanEntity whoClicked = inventoryClickEvent.getWhoClicked();
            if(!(whoClicked instanceof Player)) return false;
            Mine mine = player.getMine();
            openDecorMenu(player, 1, mine.getPathID(), mine.getWallID());
            return false;
        });

        player.getPlayer().openInventory(gui.getInventory());
    }

    public void openSizesMenu(MPlayer player){

        Mine mine = player.getMine();

        int height = mine.getHeight();
        int nextHeight = height + 1;
        int lastHeight = height - 1;

        int width = mine.getWidth();
        int nextWidth = width + 1;
        int lastWidth = width - 1;

        ChestGui gui = ChestGui.getCreative(Bukkit.createInventory(null, 9 * 5, "Size Upgrades"));
        Inventory inv = gui.getInventory();
        gui.setAutoclosing(true);
        gui.setBottomInventoryAllow(false);

        //Fill the slots with placeholder item
        blockFill(inv, Material.WHITE_STAINED_GLASS_PANE);

        //Set up slots
        byte wI = 23;
        byte hI = 21;
        byte iW = 14;
        byte dW = 32;
        byte iH = 12;
        byte dH = 30;

        ItemStack widthIcon = itemBuild(Material.OAK_FENCE, "&6&lWidth", MUtil.list("&7Current Width: " + width));
        ItemStack heightIcon = itemBuild(Material.COAL_ORE, "&6&lHeight", MUtil.list("&7Current Height: " + height));

        inv.setItem(wI, widthIcon);
        inv.setItem(hI, heightIcon);

        //ENSURE THAT THE MINE'S HEIGHT CAN INCREASE
        if(nextHeight <= 49){
            ItemStack incHeight = itemBuild(Material.LIME_DYE, "&aIncrease Height", MUtil.list("", "&7(&f" + height + "&7) &m->&r&7 (&e" + nextHeight + "&7)", "", "&e&lCOST", "&a⛃1,000,000", "", "&7Increase the height of your mine!"));
            inv.setItem(iH, incHeight);
            gui.setAction(iH, sizeAction(player, nextHeight, width));
        }else{
            ItemStack incHeight = itemBuild(Material.LIME_DYE, "&7&lIncrease Height", MUtil.list("", "&7MAXIMUM REACHED"));
            inv.setItem(iH, incHeight);
        }

        //ENSURE THAT THE MINE'S HEIGHT CAN DECREASE
        if(lastHeight >= MinesConf.get().mineDefaultHeight){
            ItemStack decHeight = itemBuild(Material.RED_DYE, "&cDecrease Height", MUtil.list("", "&7(&f" + height + ") &m->&r&7 (&e" + lastHeight + "&7)", "", "&a⛃FREE", "", "&7Decrease the height of your mine!"));
            inv.setItem(dH, decHeight);
            gui.setAction(dH, sizeAction(player, lastHeight, width));
        }else{
            ItemStack decHeight = itemBuild(Material.RED_DYE, "&cDecrease Height", MUtil.list("", "&7MINIMUM REACHED"));
            inv.setItem(dH, decHeight);
        }

        //ENSURE THAT THE MINE'S WIDTH CAN INCREASE
        if(nextWidth <= 49){
            ItemStack incWidth = itemBuild(Material.LIME_DYE, "&aIncrease Width", MUtil.list("", "&7(&f" + width + ") &m->&r&7 (&e" + nextWidth + "&7)", "", "&e&lCOST", "&a⛃1,000,000", "", "&7Increase the width of your mine!"));
            inv.setItem(iW, incWidth);
            gui.setAction(iW, sizeAction(player, height, nextWidth));
        }else{
            ItemStack incWidth = itemBuild(Material.LIME_DYE, "&7&lIncrease Width", MUtil.list("", "&7MAXIMUM REACHED"));
            inv.setItem(iW, incWidth);
        }

        //ENSURE THAT THE MINE'S WIDTH CAN DECREASE
        if(lastWidth >= MinesConf.get().mineDefaultWidth){
            ItemStack decWidth = itemBuild(Material.RED_DYE,"&cDecrease Width", MUtil.list("", "&7(&f" + width + ") &m->&r&7 (&e" + lastWidth + "&7)", "",  "&e&lCOST", "&a⛃FREE", "", "&7Decrease the width of your mine!"));
            inv.setItem(dW, decWidth);
            gui.setAction(dW, sizeAction(player,height, lastWidth));
        }else{
            ItemStack decWidth = itemBuild(Material.RED_DYE,"&cDecrease Width", MUtil.list("", "&7MINIMUM REACHED"));
            inv.setItem(dW, decWidth);
        }


        player.getPlayer().openInventory(gui.getInventory());
    }

    public void openDecorMenu(MPlayer player, int pageNum, int selectedFloor, int selectedWall){
        ChestGui gui = ChestGui.getCreative(Bukkit.createInventory(null, 9 * 6, Color.get("&4&lMine Decoration &r&7| Page " + pageNum)));
        Inventory inv = gui.getInventory();
        gui.setAutoclosing(true);
        gui.setBottomInventoryAllow(false);

        Mine mine = player.getMine();
        LayoutConf layoutConf = LayoutConf.get();

        List<Integer> floorSlots = MUtil.list(10,11,12,13,19,20,21,22,28,29,30,31);
        List<Integer> wallSlots = MUtil.list(15, 16, 24,25,33,34);
        int floorBackSlot = 37;
        int floorNextSlot = 40;
        int wallBackSlot = 42;
        int wallNextSlot = 43;
        int confirmSlot = 23;
        int cancelSlot = 32;

        //Fill the slots with placeholder item
        blockFill(inv, Material.WHITE_STAINED_GLASS_PANE);


        Map<Integer, Floor> floors = layoutConf.getFloors();
        Iterator<Integer> floorIterator = floors.keySet().iterator();

        List<Integer> compatibleWalls = layoutConf.getPath(selectedFloor).getCompatibleWalls();
        Iterator<Integer> wallIterator = compatibleWalls.iterator();

        // Set up arrows

        ItemStack floorBackItem = new ItemStack(Material.ARROW, 1);
        ItemMeta backButton = floorBackItem.getItemMeta();
        backButton.setDisplayName(Color.get("&6LAST PAGE"));
        floorBackItem.setItemMeta(backButton);
        inv.setItem(floorBackSlot, floorBackItem);

        ItemStack floorNextItem = new ItemStack(Material.ARROW, 1);
        ItemMeta nextButton = floorBackItem.getItemMeta();
        nextButton.setDisplayName(Color.get("&6NEXT PAGE"));
        floorNextItem.setItemMeta(nextButton);
        inv.setItem(floorNextSlot, floorNextItem);

        ItemStack wallBackItem = new ItemStack(Material.ARROW, 1);
        floorBackItem.setItemMeta(backButton);
        inv.setItem(wallBackSlot, wallBackItem);

        ItemStack wallNextItem = new ItemStack(Material.ARROW, 1);
        floorNextItem.setItemMeta(nextButton);
        inv.setItem(wallNextSlot, wallNextItem);

        // Fill floor slots
        floorSlots.forEach(slot -> {
            if(floorIterator.hasNext()){
                int floorID = floorIterator.next();
                Floor floor = floors.get(floorID);
                ItemStack item = new ItemStack(floor.getIcon(), 1, (short) 0, (byte) floor.getMaterialData());
                ItemMeta meta = item.getItemMeta();
                meta.setDisplayName(Color.get(floor.getDisplayName()));
                meta.setLore(Color.get(floor.getLore()));

                if(floorID == selectedFloor){
                    meta.addEnchant(Glow.getGlow(), 1, true);
                    List<String> newLore = meta.getLore();
                    newLore.add(1, Color.get("&a[SELECTED]"));
                    meta.setLore(newLore);
                }else{
                    gui.setAction(slot, inventoryClickEvent -> {
                        Optional<Integer> optionalWall = floor.getCompatibleWalls().stream().findFirst();
                        if(optionalWall.isPresent()){
                            openDecorMenu(player, pageNum, floorID, optionalWall.get());
                        }else{ player.sendMessage(Color.get("&7[&f&lMCRivals&7] No walls exist for this floor! Please report to an administrator.")); }
                        return false;
                    });
                }

                item.setItemMeta(meta);
                inv.setItem(slot, item);

            }else{
                ItemStack item = new ItemStack(Material.AIR);
                inv.setItem(slot, item);
            }
        });

        // Fill wall slots for selected Floor
        wallSlots.forEach(slot -> {
            if(wallIterator.hasNext()){
                int wallID = wallIterator.next();
                Wall wall = layoutConf.getWall(wallID);
                ItemStack item = new ItemStack(wall.getIcon(), 1);
                ItemMeta meta = item.getItemMeta();
                meta.setDisplayName(Color.get(wall.getDisplayName()));
                meta.setLore(Color.get(wall.getLore()));

                if(wallID == selectedWall){
                    meta.addEnchant(Glow.getGlow(), 1, true);
                    List<String> newLore = meta.getLore();
                    newLore.add(1, Color.get("&a[SELECTED]"));
                    meta.setLore(newLore);
                }else{
                    gui.setAction(slot, inventoryClickEvent -> {
                        openDecorMenu(player, pageNum, selectedFloor, wallID);
                        return false;
                    });
                }

                item.setItemMeta(meta);
                inv.setItem(slot, item);

            }else{
                ItemStack item = new ItemStack(Material.AIR);
                inv.setItem(slot, item);
            }
        });

        if(selectedFloor != mine.getPathID() || selectedWall != mine.getWallID()){

            ItemStack confirmButton = new ItemStack(Material.LIME_WOOL);
            ItemStack cancelButton = new ItemStack(Material.RED_WOOL);

            ItemMeta confirmMeta = confirmButton.getItemMeta();
            confirmMeta.setDisplayName(Color.get("&a&lCONFIRM"));
            confirmMeta.setLore(Color.get((MUtil.list("", "&7Confirm layout changes."))));
            confirmButton.setItemMeta(confirmMeta);

            ItemMeta cancelMeta = cancelButton.getItemMeta();
            cancelMeta.setDisplayName(Color.get("&c&lCANCEL"));
            cancelMeta.setLore(Color.get((MUtil.list("", "&7Cancel layout changes."))));
            cancelButton.setItemMeta(cancelMeta);

            inv.setItem(confirmSlot, confirmButton);
            inv.setItem(cancelSlot, cancelButton);

            gui.setAction(confirmSlot, inventoryClickEvent -> {

                if(player.inCooldown(CooldownReason.REGEN)){
                    player.getPlayer().sendMessage(Color.get("&7[&fServer&7] &cYour mine is regenerating. Please wait to use this action."));
                    return true;
                }

                EngineCooldown.add(player.getPlayer(), 20 * 30, CooldownReason.DECOR);

                EngineLoadingScreen.addLoadingScreen(player.getPlayer(), mine.getSpawnPoint());
                mine.rebuildSchematics(selectedFloor, selectedWall, () -> {
                    if(player.getPlayer() != null){
                        EngineLoadingScreen.removeLoadingScreen(player.getPlayer(), 0);
                        player.getPlayer().teleport(mine.getSpawnPoint());
                    }
                });

                return false;
            });

        }

        player.getPlayer().openInventory(gui.getInventory());
    }

    public void openDistributionMenu(MPlayer player, int pageNum){
        Mine mine = player.getMine();
        List<Integer> unlockedDistributions = mine.getUnlockedDistributions();
        Map<Integer, DistributionPage> pages = DistributionConf.get().pages;
        DistributionPage page = pages.get(pageNum);
        ChestGui gui = ChestGui.getCreative(Bukkit.createInventory(null, 9 * 6, Color.get(page.getName())));
        Inventory inv = gui.getInventory();
        gui.setAutoclosing(true);
        gui.setBottomInventoryAllow(false);

        //Fill the slots with placeholder item
        blockFill(inv, Material.WHITE_STAINED_GLASS_PANE);

        List<Integer> fillableSlots = MUtil.list(10,11,12,13,14,15,16,19,20,21,22,23,24,25,28,29,30,31,32,33,34);
        Map<Integer, Distribution> distributions = DistributionConf.get().distribution;

        Iterator<Integer> iterator = page.getDistributionIDs().iterator();

        for (Integer slot : fillableSlots) {
            if(iterator.hasNext()){

                int next = iterator.next();
                ItemStack item = new ItemStack(Material.BEDROCK, 1);

                if(unlockedDistributions.contains(next)){
                    Distribution d = distributions.get(next);
                    item.setType(d.getIcon());
                    ItemMeta meta = item.getItemMeta();
                    meta.setDisplayName(Color.get(d.getName()));
                    List<String> lore = new ArrayList<>(d.getLore());
                    if(mine.getCurrentDistributionID() == next){
                        meta.addEnchant(Glow.getGlow(), 1, false);
                        lore.add(1, "&a[ACTIVE]");
                    }
                    meta.setLore(Color.get(lore));
                    item.setItemMeta(meta);
                    gui.setAction(slot, inventoryClickEvent -> {

                        HumanEntity whoClicked = inventoryClickEvent.getWhoClicked();
                        if(!(whoClicked instanceof Player)) return false;
                        Player player1 = (Player) whoClicked;

                        if(EngineCooldown.inCooldown(player1, CooldownReason.DECOR)){
                            player1.sendMessage(Color.get("&7[&fServer&7] &cThis action is on a cooldown for " + EngineCooldown.getTime(player1, CooldownReason.SIZE_UPGRADE) + " seconds."));
                            return false;
                        }

                        if(EngineCooldown.inCooldown(player1, CooldownReason.REGEN)){
                            player1.sendMessage(Color.get("&7[&fServer&7] &cYour mine is regenerating. Please wait to use this action."));
                            return false;
                        }

                        player.sendMessage(Color.get("&7[Server] &fYou changed your mine's blocks!"));
                        player.getMine().setBlockDistribution(next);

                        openDistributionMenu(player, pageNum);
                        EngineCooldown.add(player1, 20*60, CooldownReason.DECOR);
                        return false;
                    });
                }else{
                    ItemMeta meta = item.getItemMeta();
                    meta.setDisplayName(Color.get("&cLOCKED"));
                    meta.setLore(Color.get(MUtil.list("", "&7You haven't unlocked this yet.")));
                    item.setItemMeta(meta);
                    item.setType(Material.IRON_BARS);
                    gui.setAction(slot, inventoryClickEvent -> {

                        HumanEntity whoClicked = inventoryClickEvent.getWhoClicked();
                        if(!(whoClicked instanceof Player)) return false;
                        Player player1 = (Player) whoClicked;

                        if(EngineCooldown.inCooldown(player1, CooldownReason.DECOR)){
                            player1.sendMessage(Color.get("&7[&fServer&7] &cThis action is on a cooldown for " + EngineCooldown.getTime(player1, CooldownReason.DECOR) + " seconds."));
                            return false;
                        }

                        player.sendMessage(Color.get("&7[Server] &fYou bought a new block distribution!"));
                        MPlayer mPlayer = MPlayerColl.get().getByPlayer(player1);
                        mPlayer.getMine().addUnlockedDistribution(next);
                        openDistributionMenu(mPlayer, pageNum);
                        EngineCooldown.add(player1, 20*60, CooldownReason.DECOR);
                        return false;
                    });
                }

                inv.setItem(slot, item);
            }else{
                inv.clear(slot);
            }

        }


            ItemStack nextArrow = new ItemStack(Material.ARROW, 1);
            ItemMeta meta = nextArrow.getItemMeta();
            meta.setDisplayName(Color.get("&eNEXT PAGE"));
            nextArrow.setItemMeta(meta);

            int slot = 41;
            inv.setItem(slot, nextArrow);
            gui.setAction(slot, inventoryClickEvent -> {
                HumanEntity whoClicked = inventoryClickEvent.getWhoClicked();
                if(!(whoClicked instanceof Player)) return false;
                Player player12 = (Player) whoClicked;

                openDistributionMenu(MPlayerColl.get().getByPlayer(player12), pageNum + 1);
                return false;
            });

            ItemStack backArrow = new ItemStack(Material.ARROW, 1);
            ItemMeta meta2 = backArrow.getItemMeta();
            meta2.setDisplayName(Color.get("&eLAST PAGE"));
            backArrow.setItemMeta(meta2);
            int slot2 = 39;
            inv.setItem(slot2, backArrow);
            if(pages.containsKey(pageNum - 1)){

            }
            gui.setAction(slot2, inventoryClickEvent -> {
                HumanEntity whoClicked = inventoryClickEvent.getWhoClicked();
                if(!(whoClicked instanceof Player)) return false;
                Player player12 = (Player) whoClicked;

                openDistributionMenu(MPlayerColl.get().getByPlayer(player12), pageNum - 1);
                return false;
            });

        player.getPlayer().openInventory(gui.getInventory());

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

    public ChestAction sizeAction(MPlayer mPlayer, int height, int width){
        return inventoryClickEvent -> {
            HumanEntity whoClicked = inventoryClickEvent.getWhoClicked();
            if(!(whoClicked instanceof Player)) return false;
            Player player = (Player) whoClicked;

            if(EngineCooldown.inCooldown(player, CooldownReason.SIZE_UPGRADE)){
                player.sendMessage(Color.get("&7[&fServer&7] &cThis action is on a cooldown for " + EngineCooldown.getTime(player, CooldownReason.SIZE_UPGRADE) + " seconds."));
                return false;
            }else if(EngineCooldown.inCooldown(player, CooldownReason.REGEN)){
                player.sendMessage(Color.get("&7[&fServer&7] &cYour mine is regenerating. Please wait to use this action."));
                return false;
            }
            new ActionMineSize(width, height).apply(mPlayer);
            if(!player.isOp()){
                EngineCooldown.add(player, 20*60, CooldownReason.SIZE_UPGRADE);
            }

            return false;
        };
    }

    public ItemStack itemBuild(Material material, String name, List<String> lore){
        ItemStack item = new ItemStack(material, 1);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(Color.get(name));
        itemMeta.setLore(Color.get(lore));
        item.setItemMeta(itemMeta);
        return item;
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

        npc.data().set(NPC.NAMEPLATE_VISIBLE_METADATA, false);

    }

    //run code when the NPC is removed. Use this to tear down any repeating tasks.
    @Override
    public void onRemove() {

    }

}
