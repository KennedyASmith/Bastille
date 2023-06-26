package com.kennedysmithjava.prisoncore.gui;

import com.kennedysmithjava.prisoncore.entity.player.MPlayer;
import com.kennedysmithjava.prisoncore.entity.player.objects.StorageWrapper;
import com.kennedysmithjava.prisoncore.storage.StorageType;
import com.kennedysmithjava.prisoncore.util.ItemBuilder;
import com.massivecraft.massivecore.chestgui.ChestGui;
import com.massivecraft.massivecore.util.MUtil;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.List;

public class StorageGui extends BaseGui {
    public StorageGui(Player player) {
        super(player, "&4&lSelect a storage tab", 5, false, true);
    }

    @Override
    public void onBuild(Player player, ChestGui gui, Inventory inventory) {
        blockFill(Material.BLUE_STAINED_GLASS_PANE, 0, 8);
        blockFill(Material.LIGHT_BLUE_STAINED_GLASS_PANE, 9, 35);
        blockFill(Material.BLUE_STAINED_GLASS_PANE, 36, 44);

        MPlayer mPlayer = MPlayer.get(player);
        for (StorageType type : StorageType.values()) {
            int slot = type.getSlot();
            StorageWrapper wrapper = mPlayer.getStorageWrapper(type);
            ItemBuilder storageButton = new ItemBuilder(wrapper.getIcon());
            if(wrapper.isUnlocked() || player.isOp()){
                List<String> lore = MUtil.list("&r", "&eLeft Click &7to open.");
                boolean isEditable = (type != StorageType.PRESTIGE && type != StorageType.QUEST);
                if(isEditable) lore.add("&eRight Click&7 to edit settings.");
                storageButton.lore(lore);
                storageButton.name(wrapper.getName());
                setAction(slot, inventoryClickEvent -> {
                    if(inventoryClickEvent.isLeftClick()){
                        if(type == StorageType.QUEST){
                            //TODO
                        }else {
                            close();
                            mPlayer.openStorage(type, player);
                        }
                    }else {
                        if(isEditable){
                            close();
                            MineMenuStorageTabGui tabGui = new MineMenuStorageTabGui(player, type, this);
                            tabGui.open();
                        }
                    }
                    return false;
                });
            }else{
                storageButton.name("&c&lLocked");
                storageButton.lore(MUtil.list("&r", "&7This storage tab is locked!"));
            }
            setItem(slot, storageButton.build());
        }
    }
}
