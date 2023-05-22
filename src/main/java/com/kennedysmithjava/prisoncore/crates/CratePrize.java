package com.kennedysmithjava.prisoncore.crates;

import com.kennedysmithjava.prisoncore.util.MiscUtil;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.function.Consumer;
import java.util.function.Function;

public class CratePrize {

    private final Consumer<Player> prizeFunction;
    private final ItemStack prizeIcon;
    private final Material glassColor;
    private final double rarity;
    private final String prizeName;

    public CratePrize(ItemStack prize, ItemStack prizeIcon, Material glassColor, double rarity) {
        this(player -> MiscUtil.givePlayerItem(player, prize, prize.getAmount()), prize.getItemMeta().getDisplayName(), prizeIcon, glassColor, rarity);
    }

    public CratePrize(Consumer<Player> prizeFunction, String prizeName, ItemStack prizeIcon, Material glassColor, double rarity) {
        this.prizeFunction = prizeFunction;
        this.prizeIcon = prizeIcon;
        this.glassColor = glassColor;
        this.rarity = rarity;
        this.prizeName = prizeName;
    }

    public ItemStack getPrizeIcon() {
        return prizeIcon;
    }

    public void givePrize(Player player) {
        prizeFunction.accept(player);
    }

    public double getRarity() {
        return rarity;
    }

    public Material getGlassColor() {
        return glassColor;
    }

    public String getPrizeName() {
        return prizeName;
    }
}

