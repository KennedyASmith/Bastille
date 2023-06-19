package com.kennedysmithjava.prisoncore.engine;

import com.kennedysmithjava.prisoncore.mobs.MobType;
import com.kennedysmithjava.prisoncore.util.MiscUtil;
import com.massivecraft.massivecore.Engine;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class EngineMobs extends Engine {

    // -------------------------------------------- //
    // INSTANCE & CONSTRUCT
    // -------------------------------------------- //


    private static final EngineMobs i = new EngineMobs();

    public static EngineMobs get() {
        return i;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onEntityDeath(EntityDeathEvent event){
        LivingEntity killed = event.getEntity();
        EntityType type = killed.getType();
        Player killer = killed.getKiller();
        if (killer == null) return;

        MobType mType = MobType.get(type);
        if(mType == null) return;
        List<ItemStack> drops = mType.getDrops();
        for (ItemStack drop : drops) {
            MiscUtil.givePlayerItem(killer, drop, drop.getAmount());
        }
    }
}
