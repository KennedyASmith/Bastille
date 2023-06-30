package com.kennedysmithjava.prisoncore.npc;

import com.kennedysmithjava.prisoncore.eco.WorthUtil;
import com.kennedysmithjava.prisoncore.engine.EngineCooldown;
import com.kennedysmithjava.prisoncore.entity.mines.CoinCollectorConf;
import com.kennedysmithjava.prisoncore.entity.mines.CoinCollectorGuiConf;
import com.kennedysmithjava.prisoncore.entity.mines.Mine;
import com.kennedysmithjava.prisoncore.entity.mines.MineColl;
import com.kennedysmithjava.prisoncore.entity.mines.upgrades.UpgradeName;
import com.kennedysmithjava.prisoncore.entity.player.MPlayer;
import com.kennedysmithjava.prisoncore.entity.player.MPlayerColl;
import com.kennedysmithjava.prisoncore.pouch.DatalessPouchable;
import com.kennedysmithjava.prisoncore.pouch.Pouch;
import com.kennedysmithjava.prisoncore.pouch.PouchFullException;
import com.kennedysmithjava.prisoncore.pouch.PouchManager;
import com.kennedysmithjava.prisoncore.util.Color;
import com.kennedysmithjava.prisoncore.util.*;
import com.massivecraft.massivecore.chestgui.ChestGui;
import com.massivecraft.massivecore.util.MUtil;
import net.citizensnpcs.api.event.NPCRightClickEvent;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.trait.Trait;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

public class NPCCoinCollectorTrait extends Trait {

    /*
     Progression:
        - Default: You can put blocks into the inventory manually and confirm sell
        - Sell All: You can press a button to select all.
        - I: Slow auto selling, still has access to sell all.
        - II+: Faster auto selling.
     */

    private static final String
            DEFAULT = UpgradeName.COLLECTION_DEFAULT.get(),
            SELL_ALL = UpgradeName.COLLECTION_SELL_ALL.get(),
            SELECT_ALL = UpgradeName.COLLECTION_SELECT_ALL.get(),
            AUTO_SELL_I = UpgradeName.COLLECTION_AUTO_1.get(),
            AUTO_SELL_II = UpgradeName.COLLECTION_AUTO_2.get(),
            AUTO_SELL_III = UpgradeName.COLLECTION_AUTO_3.get(),
            AUTO_SELL_IV = UpgradeName.COLLECTION_AUTO_4.get();

    private static final ClickHandler onConfirm = (p, i) -> {
        UUID uuid = p.getUniqueId();
        if(EngineCooldown.inCooldown(uuid, CooldownReason.GUI_ACTION)){
            int time = 1 + EngineCooldown.getTime(uuid, CooldownReason.GUI_ACTION);
            p.sendMessage(Color.get("&7[&bCaleb&7] You cannot sell me your items for another &e" + time + " &7seconds!"));
            return true;
        }
        Inventory inv = i.getClickedInventory();
        sellAll(inv, p);
        EngineCooldown.add(uuid, 60, CooldownReason.GUI_ACTION);
        return true;
    };

    private static final ClickHandler onSellAll = (p, i) -> {
        UUID uuid = p.getUniqueId();
        if(EngineCooldown.inCooldown(uuid, CooldownReason.GUI_ACTION)){
            int time = 1 + EngineCooldown.getTime(uuid, CooldownReason.GUI_ACTION);
            p.sendMessage(Color.get("&7[&bCooldown&7] &bYou cannot sell me items for another &e" + time + "&b seconds!"));
            return true;
        }
        Inventory inv = p.getInventory();
        sellAll(inv, p);
        return true;
    };

    public static List<ItemStack> recursivePouchCollector(DatalessPouchable dItem, Integer quantity) {
        if(quantity > 64){
            List<ItemStack> list = MUtil.list(dItem.getProductItem(64));
            list.addAll(recursivePouchCollector(dItem, quantity - 64));
            return list;
        }else {
            return MUtil.list(dItem.getProductItem(quantity));
        }
    }

    private static final ClickHandler onSelect = (p, i) -> {
        ChestGui gui = ChestGui.getInventoryToGui().get(i.getClickedInventory());
        Inventory inv = p.getInventory();
        Map<Integer, ItemStack> sellable = WorthUtil.getSellable.apply(inv);
        Map<Integer, Map<DatalessPouchable, Integer>> pouches = WorthUtil.getPouched.apply(inv);
        sellable.forEach((slot, itemStack) -> {
            inv.remove(itemStack);
            RemovableItem removableItem = new RemovableItem(itemStack);
            int index = gui.getInventory().addItem(removableItem).keySet().stream().findFirst().orElse(-1);
            gui.setAction(index, ClickHandler.toChestAction(removableItem.getClickHandler()));
        });

        final PouchManager pouchManager = PouchManager.get();
        pouches.forEach((slot, pouchableMap) -> {
            ItemStack item = inv.getItem(slot);
            if (Pouch.isPouch(item)){
                Pouch pouch = pouchManager.getPouch(item);
                pouch.getPouched().forEach((datalessPouchable, integer) -> {
                    List<ItemStack> items = recursivePouchCollector(datalessPouchable, integer);
                    items.forEach(itemStack -> {
                        pouch.removePouched(item, datalessPouchable, itemStack.getAmount());
                        PouchedItem pouchedItem = new PouchedItem(itemStack, datalessPouchable, pouch);
                        int index = gui.getInventory().addItem(pouchedItem).keySet().stream().findFirst().orElse(-1);
                        gui.setAction(index, ClickHandler.toChestAction(pouchedItem.getClickHandler()));
                    });
                });

            }
        });

        p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1, 1);
        return true;
    };

    public static final class PouchedItem extends ClickItem {
        public PouchedItem(ItemStack stack, DatalessPouchable pouchable, Pouch pouch) throws IllegalArgumentException {
            super(stack, (player, clickEvent) -> {
                clickEvent.setCurrentItem(null);
                try {
                    pouch.pouch(pouchable, stack);
                } catch (PouchFullException e) {
                    player.getWorld().dropItem(player.getLocation(), stack);
                }
                player.playSound(player.getLocation(), Sound.BLOCK_LAVA_POP, 1, 1);
                return true;
            });
        }
    }

    public NPCCoinCollectorTrait() {
        super("coincollectortrait");
    }

    @EventHandler
    public void click(NPCRightClickEvent event) {
        //Handle a click on a NPC.
        //Be sure to check event.getNPC() == this.getNPC() so you only handle clicks on this NPC!
        if (event.getNPC() != this.getNPC()) return;
        if (!event.getNPC().hasTrait(NPCCoinCollectorTrait.class)) return;
        Player player = event.getClicker();
        MPlayer mPlayer = MPlayerColl.get().getByPlayer(player);
        if (mPlayer == null) return;

        player.sendMessage(Color.get(CoinCollectorConf.get().collectorLineWelcome));
        openGUI(mPlayer);
    }

    public void openGUI(MPlayer player) {
        if (setting.equals(DEFAULT)) {
            openDefaultGUI(player);
        } else if (setting.equals(SELECT_ALL)) {
            openSelectAllGUI(player);
        } else if (setting.equals(SELL_ALL)) {
            openSellAllGUI(player);
        } else if (setting.equals(AUTO_SELL_I)) {
            openAutoSellGUI(player, 1);
        } else if (setting.equals(AUTO_SELL_II)) {
            openAutoSellGUI(player, 2);
        } else if (setting.equals(AUTO_SELL_III)) {
            openAutoSellGUI(player, 3);
        } else if (setting.equals(AUTO_SELL_IV)) {
            openAutoSellGUI(player, 4);
        } else {
            openDefaultGUI(player);
        }
    }

    public void openDefaultGUI(MPlayer player) {
        CoinCollectorGuiConf conf = CoinCollectorGuiConf.get();
        ChestGui gui = getChestGUI();

        for (int i = 0; i < 9; i++) {
            populatePane(gui, i, conf.guiSize - i - 1);
        }

        populateItem(gui, conf.confirmSlot, buildItem(conf.confirmMaterial,  conf.confirmName, conf.confirmLore), onConfirm);
        gui.getInventory().setItem(conf.selectSlot, buildItem(conf.selectLockedMaterial, conf.selectLockedName, conf.selectLockedLore));

        player.getPlayer().openInventory(gui.getInventory());
    }

    public void openSelectAllGUI(MPlayer player) {
        CoinCollectorGuiConf conf = CoinCollectorGuiConf.get();
        ChestGui gui = getChestGUI();

        for (int i = 0; i < 9; i++) {
            populatePane(gui, i, conf.guiSize - i - 1);
        }

        populateItem(gui, conf.confirmSlot, buildItem(conf.confirmMaterial, conf.confirmName, conf.confirmLore), onConfirm);
        populateItem(gui, conf.selectSlot, buildItem(conf.selectMaterial, conf.selectName, conf.selectLore), onSelect);

        player.getPlayer().openInventory(gui.getInventory());
    }

    private void populatePane(ChestGui gui, int... index) {
        for (Integer i : index)
            this.populateItem(gui, i, new ItemStack(Material.LIME_STAINED_GLASS_PANE), (p, j) -> false);
    }

    private void populateItem(ChestGui gui, int index, ItemStack item, ClickHandler clickHandler) {
        gui.getInventory().setItem(index, new ClickItem(item, clickHandler));

        gui.setAction(index, ClickHandler.toChestAction(clickHandler));
    }

    private ChestGui getChestGUI() {
        CoinCollectorGuiConf coinCollectorGuiConf = CoinCollectorGuiConf.get();
        ChestGui gui = ChestGui.getCreative(
                Bukkit.createInventory(null,
                        coinCollectorGuiConf.guiSize,
                        Color.get(coinCollectorGuiConf.guiName))
        );
        gui.getMeta().put("coincollector", 1);

        gui.setBottomInventoryAllow(true);
        gui.setAutoclosing(false);

        return gui;
    }

    public void openSellAllGUI(MPlayer player) {
        // Just add a sell all button to the gui at the bottom

        CoinCollectorGuiConf conf = CoinCollectorGuiConf.get();
        ChestGui gui = getChestGUI();

        for (int i = 0; i < 9; i++) {
            populatePane(gui, i, conf.guiSize - i - 1);
        }

        populateItem(gui, conf.confirmSlot, buildItem(conf.confirmMaterial, conf.confirmName, conf.confirmLore), onConfirm);

        populateItem(gui, conf.selectSlot, buildItem(conf.selectMaterial, conf.selectName, conf.selectLore), onSelect);

        populateItem(gui, conf.sellAllSlot, buildItem(conf.sellAllMaterial, conf.sellAllName, conf.sellAllLore), onSellAll);
        player.getPlayer().openInventory(gui.getInventory());
    }

    public void openAutoSellGUI(MPlayer player, int tier) {
        CoinCollectorGuiConf conf = CoinCollectorGuiConf.get();
        ChestGui gui = getChestGUI();

        for (int i = 0; i < 9; i++) {
            populatePane(gui, i, conf.guiSize - i - 1);
        }

        populateItem(gui, conf.confirmSlot, buildItem(conf.confirmMaterial,  conf.confirmName, conf.confirmLore), onConfirm);

        populateItem(gui, conf.selectSlot, buildItem(conf.autoSellMaterial,  conf.autoSellName, getAutoSellLore(CoinCollectorConf.get().autoSellTimes.get(tier), conf.autoSellLore)), onSelect);

        populateItem(gui, conf.sellAllSlot, buildItem(conf.sellAllMaterial,  conf.sellAllName, conf.sellAllLore), onSellAll);
        player.getPlayer().openInventory(gui.getInventory());
    }

    public List<String> getAutoSellLore(double time, List<String> lore) {
        return lore.stream().map(s -> s.replaceAll("%t", String.valueOf(time))).collect(Collectors.toList());
    }

    private Mine mine;
    private long lastSell = -1L;
    private String setting = DEFAULT;
    private boolean isAutoSell = false;
    int tier;

    // Called every tick
    @Override
    public void run() {
        if (mine == null) {
            return;
        }
        if(!isAutoSell) return;

        double time = CoinCollectorConf.get().autoSellTimes.get(tier);
        int ms = (int) (time * 1000);

        if (System.currentTimeMillis() > ms + lastSell) {
            this.doAutoSell();
        }

    }

    private static final double MINE_SELL_DISTANCE = 300;

    private void doAutoSell() {
        Location mineCenter = mine.getMineCenter();
        World world = mineCenter.getWorld();
        if(world == null) return;
        List<Player> players = mineCenter.getWorld().getPlayers();
        players.stream()
                .filter(p -> p.getLocation().distanceSquared(mineCenter) < MINE_SELL_DISTANCE)
                .forEach(p -> {
                    Inventory inv = p.getInventory();
                    sellAll(inv, p);
                });

        lastSell = System.currentTimeMillis();
    }

    private static void sellAll(Inventory inv, Player p){
        Map<Integer, ItemStack> sellable = WorthUtil.getSellable.apply(inv);
        AtomicReference<BigDecimal> value = new AtomicReference<>(new BigDecimal(0));
        AtomicInteger count = new AtomicInteger();
        sellable.forEach((slot, it) -> {
            value.updateAndGet(v -> v.add(BigDecimal.valueOf(WorthUtil.sellFunc.apply(p, it))));
            count.addAndGet(it.getAmount());
            inv.clear(slot);
        });
        if(count.get() > 0){
            p.sendMessage(Color.get("&7[&bCaleb&7] You have sold me &e" + count.get() + " &7items for &a⛃" + value));
            p.playSound(p.getLocation(), Sound.BLOCK_COMPOSTER_FILL_SUCCESS, 1, 1);
        }
    }

    private Mine findMine() {
        Location location = npc.getStoredLocation();
        return MineColl.get().getByLocation(location);
    }

    public void setSetting() {
        if (mine.isUpgradeActive(DEFAULT)) {
            setting = DEFAULT;
            isAutoSell = false;
        } else if (mine.isUpgradeActive(SELECT_ALL)) {
            setting = SELECT_ALL;
            isAutoSell = false;
        } else if (mine.isUpgradeActive(SELL_ALL)) {
            setting = SELL_ALL;
            isAutoSell = false;
        } else if (mine.isUpgradeActive(AUTO_SELL_I)) {
            setting = AUTO_SELL_I;
            isAutoSell = true;
            tier = 1;
        } else if (mine.isUpgradeActive(AUTO_SELL_II)) {
            setting = AUTO_SELL_II;
            isAutoSell = true;
            tier = 2;
        } else if (mine.isUpgradeActive(AUTO_SELL_III)) {
            setting = AUTO_SELL_III;
            isAutoSell = true;
            tier = 3;
        } else if (mine.isUpgradeActive(AUTO_SELL_IV)) {
            setting = AUTO_SELL_IV;
            isAutoSell = true;
            tier = 4;
        } else {
            setting = DEFAULT;
        }
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
        this.mine = findMine();
        setSetting();
    }

    //run code when the NPC is removed. Use this to tear down any repeating tasks.
    @Override
    public void onRemove() {

    }


    @SuppressWarnings("DataFlowIssue")
    public ItemStack buildItem(Material material, String unformattedName, List<String> unformattedLore) {
        ItemStack itemStack = new ItemStack(material, 1);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', unformattedName));
        itemMeta.setLore(
                unformattedLore
                        .stream()
                        .map(s -> ChatColor.translateAlternateColorCodes('&', s))
                        .collect(Collectors.toList())
        );

        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

}