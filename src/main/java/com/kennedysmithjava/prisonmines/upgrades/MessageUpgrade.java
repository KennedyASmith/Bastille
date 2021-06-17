package com.kennedysmithjava.prisonmines.upgrades;

import com.kennedysmithjava.prisonmines.util.Color;
import com.mcrivals.prisoncore.entity.MPlayer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MessageUpgrade extends AbstractUpgrade{

    List<String> messages;

    MessageUpgrade(String message){
        this.messages = new ArrayList<>(Collections.singletonList(message));
    }

    MessageUpgrade(List<String> messages){
        this.messages = messages;
    }

    @Override
    public void apply(MPlayer player) {
        messages.forEach(s -> {
            player.sendMessage(Color.get(s));
        });
    }

}
