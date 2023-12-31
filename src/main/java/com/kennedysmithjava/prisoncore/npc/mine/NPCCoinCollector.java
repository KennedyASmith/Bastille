package com.kennedysmithjava.prisoncore.npc.mine;

import com.kennedysmithjava.prisoncore.PrisonCore;
import com.kennedysmithjava.prisoncore.entity.mines.CoinCollectorConf;
import com.kennedysmithjava.prisoncore.npc.NPCCoinCollectorTrait;
import com.kennedysmithjava.prisoncore.npc.Skin;
import com.kennedysmithjava.prisoncore.npc.mine.MineNPC;
import com.kennedysmithjava.prisoncore.util.Color;
import com.massivecraft.massivecore.util.MUtil;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.trait.Trait;
import net.citizensnpcs.trait.HologramTrait;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class NPCCoinCollector extends MineNPC {

    @Override
    public void onSpawn(NPC npc) {

        new BukkitRunnable() {
            @Override
            public void run() {

                if(npc.getEntity() == null) return;

                HologramTrait holoTrait = new HologramTrait();
                npc.addTrait(holoTrait);

                getHologramLines().forEach(s -> {
                    holoTrait.addLine(Color.get(s));
                });

                holoTrait.setDirection(HologramTrait.HologramDirection.TOP_DOWN);
                holoTrait.setLineHeight(0.3);
            }
        }.runTaskLater(PrisonCore.get(), 10);
    }

    @Override
    public Skin getSkin() {
        return CoinCollectorConf.get().collectorSkin;
    }

    @Override
    public List<Trait> getTraits() {
        return MUtil.list(new NPCCoinCollectorTrait());
    }

    public List<String> getHologramLines(){
        return CoinCollectorConf.get().collectorHologram;
    }




}
