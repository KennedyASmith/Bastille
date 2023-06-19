package com.kennedysmithjava.prisoncore.entity.mines.upgrades;

import com.kennedysmithjava.prisoncore.PrisonCore;
import com.kennedysmithjava.prisoncore.entity.mines.Mine;
import com.kennedysmithjava.prisoncore.entity.player.MPlayer;
import com.kennedysmithjava.prisoncore.npc.NPCCoinCollectorTrait;
import com.kennedysmithjava.prisoncore.entity.mines.upgrades.actions.AbstractAction;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.npc.NPCRegistry;

public class ActionCollectionChangeSetting extends AbstractAction {

    public static NPCRegistry npcRegistry = PrisonCore.getNonPersistNPCRegistry();

    @Override
    public void apply(MPlayer player) {
        Mine mine = player.getMine();
        NPC collector = npcRegistry.getById(mine.getCollectorID());
        NPCCoinCollectorTrait trait = collector.getTrait(NPCCoinCollectorTrait.class);
        trait.setSetting();
    }
}
