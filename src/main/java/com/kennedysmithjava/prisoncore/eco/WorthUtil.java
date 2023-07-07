package com.kennedysmithjava.prisoncore.eco;

import com.kennedysmithjava.prisoncore.entity.mines.BlocksConf;
import com.kennedysmithjava.prisoncore.entity.mines.objects.PrisonBlock;
import com.kennedysmithjava.prisoncore.entity.player.MPlayer;
import com.kennedysmithjava.prisoncore.entity.player.MPlayerColl;
import com.kennedysmithjava.prisoncore.pouch.DatalessPouchable;
import com.kennedysmithjava.prisoncore.pouch.Pouch;
import com.kennedysmithjava.prisoncore.pouch.PouchManager;
import com.kennedysmithjava.prisoncore.util.Color;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.function.*;

public class WorthUtil {

    public static Predicate<ItemStack> isSellable = i -> {
        if (i == null)
            return false;

        if (!i.hasItemMeta())
            return false;

        PrisonBlock prisonBlock = fromName(i.getItemMeta().getDisplayName());
        return prisonBlock != null;
    };

    public static Function<Inventory, Map<Integer, ItemStack>> getSellable = inv -> {
        Map<Integer, ItemStack> result = new HashMap<>();
        ItemStack[] contents = inv.getContents();

        for (int i = 0; i < contents.length; i++) {
            ItemStack item = contents[i];
            if (isSellable.test(item)) {
                result.put(i, item);
            }
        }

        return result;
    };


    public static Function<Inventory, Map<Integer, Map<DatalessPouchable, Integer>>> getPouched = inv -> {
        Map<Integer, Map<DatalessPouchable, Integer>> result = new HashMap<>();
        ItemStack[] contents = inv.getContents();
        final PouchManager pouchManager = PouchManager.get();

        for (int i = 0; i < contents.length; i++) {
            ItemStack item = contents[i];
            if (Pouch.isPouch(item)) {
                Pouch pouch = pouchManager.getPouch(item);
                result.put(i, pouch.getPouched());
            }
        }

        return result;
    };

    public static final BiFunction<Player, ItemStack, Double> sellFunc = (p, i) -> {
        if (i == null)
            return -1D;
        if (!i.hasItemMeta())
            return -1D;
        if (i.getAmount() < 0)
            return -1D;

        PrisonBlock prisonBlock = fromName(i.getItemMeta().getDisplayName());
        if (prisonBlock != null) {
            double value = prisonBlock.getValue() * i.getAmount();
            MPlayer byPlayer = MPlayerColl.get().getByPlayer(p);
            byPlayer.addBalance(prisonBlock.getCurrencyType(), value, true);
            return value;
        }
        return -1D;
    };

    public static PrisonBlock fromName(String name) {
        return BlocksConf.get().blocks.values().stream().filter(p -> Color.get(p.getDisplayName()).equals(name)).findAny().orElse(null);
    }
}
