package com.kennedysmithjava.prisoncore.upgrades.actions;

import com.kennedysmithjava.prisoncore.eco.Cost;
import com.kennedysmithjava.prisoncore.entity.mines.Mine;
import com.kennedysmithjava.prisoncore.entity.mines.UpgradesConf;
import com.kennedysmithjava.prisoncore.entity.player.MPlayer;
import com.kennedysmithjava.prisoncore.npc.mine.NPCWarrenTrait;
import com.kennedysmithjava.prisoncore.upgrades.UpgradeGUI;
import com.kennedysmithjava.prisoncore.upgrades.buttons.ButtonType;
import com.kennedysmithjava.prisoncore.upgrades.buttons.GUIButtonPurchasable;
import com.kennedysmithjava.prisoncore.upgrades.buttons.GUIButtonPurchasableToggleable;
import com.kennedysmithjava.prisoncore.upgrades.buttons.GUIButtonToggleable;
import com.kennedysmithjava.prisoncore.util.Color;
import com.kennedysmithjava.prisoncore.util.Glow;
import com.massivecraft.massivecore.chestgui.ChestGui;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class ActionOpenGUI extends AbstractAction {

    String menuID;

    public ActionOpenGUI(String menuID) {
        this.menuID = menuID;
    }

    @Override
    public void apply(MPlayer mPlayer) {
        Player player = mPlayer.getPlayer();
        if(player == null) return;

        if(!menuID.equals(UpgradesConf.mainMenu)){

            Mine mine = mPlayer.getMine();
            UpgradeGUI upgradeGUI = UpgradesConf.upgradeGUIs.get(menuID);

            ChestGui gui = ChestGui.getCreative(Bukkit.createInventory(null, upgradeGUI.getSize(), upgradeGUI.getName()));
            gui.setBottomInventoryAllow(false);
            gui.setAutoclosing(false);

            Inventory inv = gui.getInventory();
            blockFill(inv, Material.WHITE_STAINED_GLASS_PANE);

            upgradeGUI.getButtons().forEach(button -> {

                List<AbstractAction> actions = button.getOnClick();

                ItemStack item = new ItemStack(button.getMaterial());
                ItemMeta meta = item.getItemMeta();
                meta.setDisplayName(Color.get(button.getName()));
                meta.setLore(Color.get(button.getLore()));

                boolean unlocked = true;

                for (String requiredUnlockedUpgrade : button.getRequiredUnlockedUpgrades()) {
                    if (!mine.isUpgradeUnlocked(requiredUnlockedUpgrade)) {
                        unlocked = false;
                        break;
                    }
                }

                if(!unlocked){
                    item.setType(Material.IRON_BARS);
                    meta = item.getItemMeta();
                    meta.setLore(Color.get(button.getLockedLore()));
                    meta.setDisplayName(Color.get("&c" + ChatColor.stripColor(button.getName())));
                }else{
                    for (ButtonType buttonType : button.getButtonTypeList()) {
                        switch(buttonType){
                            case TOGGLEABLE:
                            {

                                String toggleableUpgrade = "";
                                boolean purchased = true;
                                List<AbstractAction> toggleOn;
                                List<AbstractAction> toggleOff;

                                if(button instanceof GUIButtonPurchasableToggleable){
                                    GUIButtonPurchasableToggleable buttonToggleable = (GUIButtonPurchasableToggleable) button;
                                    toggleableUpgrade = buttonToggleable.getAssociatedUpgrade();
                                    toggleOff = buttonToggleable.getOnToggleOff();
                                    toggleOn = buttonToggleable.getOnToggleOn();
                                    if(!mine.isUpgradePurchased(toggleableUpgrade)) purchased = false;
                                }else{
                                    if(!(button instanceof GUIButtonToggleable)) continue;
                                    GUIButtonToggleable buttonToggleable = (GUIButtonToggleable) button;
                                    toggleableUpgrade = buttonToggleable.getToggleableUpgrade();
                                    toggleOff = buttonToggleable.getOnToggleOff();
                                    toggleOn = buttonToggleable.getOnToggleOn();
                                }

                                if(mine.isUpgradeActive(toggleableUpgrade) && purchased){
                                    meta.addEnchant(Glow.getGlow(), 1, true);
                                    meta.setLore(Color.get(addLine(meta.getLore(), 0, "&7[&aENABLED&7]")));
                                    actions.addAll(toggleOff);
                                    actions.add(new ActionMineToggleUpgradeOff(toggleableUpgrade));
                                }else{
                                    meta.setLore(Color.get(addLine(meta.getLore(), 0, "&7[&cDISABLED&7]")));
                                    actions.addAll(toggleOn);
                                    actions.add(new ActionMineToggleUpgradeOn(toggleableUpgrade));
                                }

                                actions.add(new ActionOpenGUI(menuID));

                                continue;
                            }
                            case PURCHASABLE:
                            {
                                if(!(button instanceof GUIButtonPurchasable buttonPurchasable)) continue;
                                List<Cost> costs = ((GUIButtonPurchasable) button).getCosts();
                                if(!mine.isUpgradePurchased(buttonPurchasable.getAssociatedUpgrade())){
                                    List<AbstractAction> onPurchase = buttonPurchasable.getOnPurchase();
                                    onPurchase.add(new ActionUnlockUpgrade(buttonPurchasable.getAssociatedUpgrade(), true, true));

                                    actions.add(new AbstractAction() {
                                        @Override
                                        public void apply(MPlayer player) {
                                            for (Cost cost : costs) {
                                                if(!cost.hasCost(player)) return;
                                            }
                                            costs.forEach(cost -> cost.transaction(mPlayer));
                                            onPurchase.forEach(abstractAction -> abstractAction.apply(player));
                                        }
                                    });

                                    meta.setLore(Color.get(addLine(meta.getLore(), 0, "&7[&aUNLOCKED&7]")));
                                }else{
                                    if(!(button instanceof GUIButtonPurchasableToggleable)){
                                        item.setType(Material.RED_STAINED_GLASS_PANE);
                                    }
                                }
                            }
                        }
                    }
                }

                item.setItemMeta(meta);
                inv.setItem(button.getSlot(), item);
                gui.setAction(button.getSlot(), inventoryClickEvent -> {
                    List<AbstractAction> actionList = new ArrayList<>(actions);
                    actionList.forEach(abstractAction -> abstractAction.apply(mPlayer));
                    return false;
                });
            });

            player.openInventory(gui.getInventory());
        }else{
            NPCWarrenTrait.openMainMenu(mPlayer, UpgradesConf.upgradeGUIs.get(UpgradesConf.mainMenu));
        }

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

    public List<String> addLine(List<String> list, int index, String value){
        list.add(index, value);
        return list;
    }

}
