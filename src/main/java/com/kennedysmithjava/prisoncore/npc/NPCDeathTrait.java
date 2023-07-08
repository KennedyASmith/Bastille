package com.kennedysmithjava.prisoncore.npc;

import com.kennedysmithjava.prisoncore.entity.player.MPlayer;
import com.kennedysmithjava.prisoncore.entity.player.MPlayerColl;
import com.kennedysmithjava.prisoncore.gui.RebirthGui;
import net.citizensnpcs.api.event.NPCLeftClickEvent;
import net.citizensnpcs.api.event.NPCRightClickEvent;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.trait.Trait;
import net.citizensnpcs.api.trait.TraitName;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;


@TraitName("deathtrait")
public class NPCDeathTrait extends Trait {

    public NPCDeathTrait() {
        super("deathtrait");
    }

    @EventHandler
    public void click(NPCRightClickEvent event) {
        //Handle a click on a NPC.
        //Be sure to check event.getNPC() == this.getNPC() so you only handle clicks on this NPC!
        if(event.getNPC() != this.getNPC()) return;
        if(!event.getNPC().hasTrait(NPCDeathTrait.class)) return;
        NPC npc = event.getNPC();
        Player player = event.getClicker();
        if (player == null) return;
        //Ensure that the clicker isn't already in the Limbo NPC's dialogue.

        MPlayer mPlayer = MPlayerColl.get().getByPlayer(player);
        if (mPlayer == null) return;
        if(mPlayer.getMine() == null) return;

        RebirthGui gui = new RebirthGui(player);
        gui.open();
    }

    @EventHandler
    public void click(NPCLeftClickEvent event) {

    }


    // Called every tick
    @Override
    public void run() {
    }

    //Run code when your trait is attached to a NPC.
    @Override
    public void onAttach() {

    }

    // Run code when the NPC is despawned. This is called before the entity actually despawns so npc.getEntity() is still valid.
    @Override
    public void onDespawn() {

    }

    //Run code when the NPC is spawned. Note that npc.getEntity() will be null until this method is called.
    //This is called AFTER onAttach and AFTER Load when the server is started.
    @Override
    public void onSpawn() {

    }

    //run code when the NPC is removed. Use this to tear down any repeating tasks.
    @Override
    public void onRemove() {

    }
}
