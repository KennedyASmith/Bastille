package com.kennedysmithjava.prisoncore.engine;

import com.kennedysmithjava.prisoncore.event.PlayerEnterRegionEvent;
import com.kennedysmithjava.prisoncore.maps.MapUtil;
import com.kennedysmithjava.prisoncore.maps.PrisonMapRenderer;
import com.massivecraft.massivecore.Engine;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;

public class EngineMaps extends Engine {

    // -------------------------------------------- //
    // INSTANCE & CONSTRUCT
    // -------------------------------------------- //
    private static final EngineMaps i = new EngineMaps();
    public static EngineMaps get() {
        return i;
    }

    @EventHandler
    public void onItemHeldChange(PlayerItemHeldEvent event) {
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItem(event.getNewSlot());
        if(item == null){
            PrisonMapRenderer.shouldRenderPlayers.remove(player.getUniqueId());
            return;
        }

        if(item.getType() == Material.FILLED_MAP){
            PrisonMapRenderer.shouldRenderPlayers.add(player.getUniqueId());
        }
    }

    @EventHandler
    public void onRegionChange(PlayerEnterRegionEvent event){

    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event){
        Player player = event.getPlayer();
        MapUtil.removeMaps(player);
    }


}
