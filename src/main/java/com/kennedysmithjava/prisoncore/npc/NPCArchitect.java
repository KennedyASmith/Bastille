package com.kennedysmithjava.prisoncore.npc;

import com.kennedysmithjava.prisoncore.PrisonCore;
import com.kennedysmithjava.prisoncore.entity.mines.ArchitectConf;
import com.kennedysmithjava.prisoncore.util.Color;
import com.massivecraft.massivecore.util.MUtil;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.trait.Trait;
import net.citizensnpcs.trait.HologramTrait;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class NPCArchitect extends MineNPC {

    @Override
    public void onSpawn(NPC npc) {

        new BukkitRunnable() {
            @Override
            public void run() {
                if(npc.getEntity() == null) return;

                HologramTrait holoTrait = new HologramTrait();
                npc.addTrait(holoTrait);

                getHologramLines().forEach(s -> holoTrait.addLine(Color.get(s)));

                holoTrait.setDirection(HologramTrait.HologramDirection.TOP_DOWN);
                holoTrait.setLineHeight(0.3);
            }
        }.runTaskLater(PrisonCore.get(), 10);
    }

    @Override
    public Skin getSkin() {
        return ArchitectConf.get().architectSkin;
    }

    @Override
    public List<Trait> getTraits() {
        return MUtil.list(new NPCArchitectTrait());
    }

    public List<String> getHologramLines() {
        return ArchitectConf.get().architectHologram;
    }
}
