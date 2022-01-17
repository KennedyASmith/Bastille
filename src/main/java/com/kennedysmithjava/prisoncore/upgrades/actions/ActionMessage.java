package com.kennedysmithjava.prisoncore.upgrades.actions;

import com.kennedysmithjava.prisoncore.entity.player.MPlayer;
import com.kennedysmithjava.prisoncore.util.Color;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ActionMessage extends AbstractAction {

    List<String> messages;

    public ActionMessage(String message){
        this.messages = new ArrayList<>(Collections.singletonList(message));
    }

    ActionMessage(List<String> messages){
        this.messages = messages;
    }

    @Override
    public void apply(MPlayer player) {
        messages.forEach(s -> {
            player.sendMessage(Color.get(s));
        });
    }

}
