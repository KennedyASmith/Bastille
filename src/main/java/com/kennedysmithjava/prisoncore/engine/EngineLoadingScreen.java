package com.kennedysmithjava.prisoncore.engine;

import com.kennedysmithjava.prisoncore.PrisonCore;
import com.kennedysmithjava.prisoncore.entity.MConf;
import com.kennedysmithjava.prisoncore.util.TitleUtility;
import com.massivecraft.massivecore.Engine;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EngineLoadingScreen extends Engine {

    private static final EngineLoadingScreen i = new EngineLoadingScreen();

    public static EngineLoadingScreen get() {
        return i;
    }

    public static Map<Player, Location> lPlayers = new HashMap<>();

    private static boolean active;

    private static void open() {

        if (active) return;

        active = true;
        List<String> loadingList = MConf.get().loadingFrames;

        new BukkitRunnable() {
            int frame = 0;

            @Override
            public void run() {
                if (!active) this.cancel();
                if (frame == loadingList.size()) frame = 0;
                String title = loadingList.get(frame);
                frame++;

                lPlayers.forEach((player, location) ->
                {
                    if (player == null) return;
                    //Give player blindness effect for 5 seconds
                    if (!player.hasPotionEffect(PotionEffectType.BLINDNESS))
                        player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 9999, 1));
                    TitleUtility.sendTitle(player, 0, 20, 10, title, "Please wait");
                });

            }
        }.runTaskTimer(PrisonCore.get(), 0L, 5L);

    }

    public static void addLoadingScreen(Player player, Location location) {
        lPlayers.put(player, location);
        open();
    }

    public static void removeLoadingScreen(Player player, int delay) {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (player == null) {
                    this.cancel();
                    return;
                }
                lPlayers.remove(player);
                //Remove blindness from the player, if applicable.
                if (player.hasPotionEffect(PotionEffectType.BLINDNESS))
                    player.removePotionEffect(PotionEffectType.BLINDNESS);
                //Cancel the limbo runnable.
                if (lPlayers.isEmpty()) active = false;
            }
        }.runTaskLater(PrisonCore.get(), delay);
    }

    public static void removeLoadingScreen(List<Player> players) {
        players.forEach(player -> lPlayers.remove(player));
    }

    //Removes residue blindness
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (event.getPlayer().hasPotionEffect(PotionEffectType.BLINDNESS)) ;
        player.removePotionEffect(PotionEffectType.BLINDNESS);
    }

}
