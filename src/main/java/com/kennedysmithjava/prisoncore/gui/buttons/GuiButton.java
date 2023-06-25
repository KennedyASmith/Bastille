package com.kennedysmithjava.prisoncore.gui.buttons;

import com.kennedysmithjava.prisoncore.eco.Cost;
import com.kennedysmithjava.prisoncore.engine.EngineCooldown;
import com.kennedysmithjava.prisoncore.entity.mines.Mine;
import com.kennedysmithjava.prisoncore.entity.mines.upgrades.UpgradeName;
import com.kennedysmithjava.prisoncore.entity.player.MPlayer;
import com.kennedysmithjava.prisoncore.util.Color;
import com.kennedysmithjava.prisoncore.util.CooldownReason;
import com.kennedysmithjava.prisoncore.util.ItemBuilder;
import com.massivecraft.massivecore.util.MUtil;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class GuiButton {

    private final String displayName;
    private final int slot;
    private final List<String> lore;
    private final Material material;
    private final Runnable onClick;
    private final List<UpgradeName> requiredUnlockedUpgrades;
    private final UpgradeName thisUpgrade;
    private final String buttonTag;
    protected final List<Cost> additionalCosts;


    public GuiButton(String displayName, String buttonTag, int slot, List<String> lore, Material material, List<UpgradeName> requiredUpgradesToUnlock, UpgradeName thisUpgrade, Runnable onClick, List<Cost> additionalCosts) {
        this.displayName = displayName;
        this.buttonTag = buttonTag;
        this.slot = slot;
        this.lore = lore;
        this.material = material;
        this.thisUpgrade = thisUpgrade;
        this.onClick = onClick;
        this.requiredUnlockedUpgrades = requiredUpgradesToUnlock;
        this.additionalCosts = additionalCosts;

    }

    public void clicked(MPlayer player){
        boolean inCooldown = EngineCooldown.inCooldown(player.getPlayer(), CooldownReason.GUI_ACTION);
        if(inCooldown){
            int time = EngineCooldown.getTime(player.getPlayer(), CooldownReason.GUI_ACTION);
            player.message(Color.get("&7[&bCooldown&7] &cPlease wait another &e" + time + " &cseconds to perform this action again."));
            return;
        }
        Mine mine = player.getMine();
        boolean isPurchased = isPurchased(mine);
        boolean isAffordable = isAffordable(player);
        if(!isPurchased && isAffordable){
            payCosts(player);
            if(thisUpgrade != null) mine.unlockUpgrade(thisUpgrade.get(), true, true);
            getOnClick().run();
        }else if (isPurchased){
            getOnClick().run();
        }
    }

    public List<String> getLore(MPlayer player, boolean isUnlocked, boolean isAffordable, boolean isPurchased, boolean isActive) {
        List<String> lore = getBaseLore();
        String buttonTag = getButtonTag(isUnlocked, isAffordable, isPurchased, isActive);
        List<String> buyPrompt = getBuyPrompt(isUnlocked);
        if(!buttonTag.equals("")){
            lore.add(0, buttonTag);
            lore.add(1, " &r");
        }
        if(!isPurchased){
            lore.addAll(buyPrompt);
            if(isUnlocked){
                lore.addAll(getCostLore(player));
            }else {
                lore.addAll(getUnlockRequirements());
            }
        }
        return lore;
    }

    public List<String> getBaseLore() {
        return this.lore;
    }


    public Runnable getOnClick() {
        return onClick;
    }

    public List<String> getCostLore(MPlayer player){
        List<String> lore = new ArrayList<>();

        for (Cost cost : getAdditionalCosts()) {
            if(cost.hasCost(player)){
                lore.add("&7- " + cost.getPriceline());
            }else {
                lore.add("&7- " + cost.getInsufficientLine(player));
            }
        }
        return lore;
    }

    public String getButtonTag(boolean isUnlocked, boolean isAffordable, boolean isPurchased, boolean isActive){
        String buttonTag = this.buttonTag;
        if(!isPurchased){
            if(isUnlocked && isAffordable){
                buttonTag = "&7[&aPURCHASE&7]";
            } else if(!isAffordable && isUnlocked){
                buttonTag = "&7[&cCANT AFFORD&7]";
            } else if (isActive) {
                buttonTag = "&7[&7UNLOCKED&7]";
            }  else {
                buttonTag = "&7[&cLOCKED&7]";
            }
        }
        return buttonTag;
    }


    public List<String> getBuyPrompt(boolean isUnlocked){
        if(isUnlocked){
            return MUtil.list(" &r", "&eClick to purchase!", " &r", " &7&lREQUIREMENTS");
        } else {
            return MUtil.list(" &r", "&cUnlock previous upgrades first:");
        }
    }

    public ItemStack getItem(MPlayer player, Mine mine){
        boolean isPurchased = isPurchased(mine);
        boolean isAffordable = isAffordable(player);
        boolean isUnlocked = isUnlocked(mine, player);
        boolean isActive = isActive(mine);
        String name = getDisplayName();
        Material buttonMaterial = getMaterial();
        if(!isUnlocked) buttonMaterial = Material.IRON_BARS;
        List<String> lore = getLore(player, isUnlocked, isAffordable, isPurchased, isActive);
        ItemBuilder builder = new ItemBuilder(buttonMaterial).name(name).lore(lore);
        if(isActive) builder.addGlow();
        return builder.build();
    }

    public void payCosts(MPlayer player){
        for (Cost additionalCost : getAdditionalCosts()) {
            additionalCost.transaction(player);
        }
    }

    public boolean isAffordable(MPlayer player){
        for (Cost additionalCost : getAdditionalCosts()) {
            if(!additionalCost.hasCost(player)) return false;
        }
        return true;
    }

    public boolean isUnlocked(Mine mine, MPlayer player){
        for (UpgradeName upgrade : getRequiredUnlockedUpgrades()) {
            if(!mine.isUpgradeUnlocked(upgrade.get())) return false;
        }
        return true;
    }

    public boolean isPurchased(Mine mine){
        return mine.isUpgradePurchased(thisUpgrade.get());
    }

    public boolean isActive(Mine mine){
        return mine.isUpgradeActive(thisUpgrade.get());
    }

    public void addCost(Cost cost){
        additionalCosts.add(cost);
    }


    public List<String> getUnlockRequirements() {
        return new ArrayList<>();
    }

    public List<Cost> getAdditionalCosts() {
        return additionalCosts;
    }

    public String getDisplayName() {
        return displayName;
    }

    public Material getMaterial() {
        return material;
    }

    public int getSlot() {
        return slot;
    }

    public List<UpgradeName> getRequiredUnlockedUpgrades() {
        return requiredUnlockedUpgrades;
    }

    public UpgradeName getThisUpgrade() {
        return thisUpgrade;
    }
}
