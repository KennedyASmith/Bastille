package com.kennedysmithjava.prisoncore.entity.mines.upgrades;

import com.kennedysmithjava.prisoncore.entity.mines.upgrades.buttons.GUIButton;
import com.kennedysmithjava.prisoncore.util.Color;

import java.util.List;

public record UpgradeGuiWrapper(String name, int size, List<GUIButton> buttons) {

    @Override
    public String name() {
        return Color.get(name);
    }
}
