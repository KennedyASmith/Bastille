package com.kennedysmithjava.prisoncore.upgrades;

import com.kennedysmithjava.prisoncore.upgrades.buttons.GUIButton;
import com.kennedysmithjava.prisoncore.util.Color;

import java.util.List;

public class UpgradeGUI {

    String name;
    int size;
    List<GUIButton> buttons;

    public UpgradeGUI(String name, int size, List<GUIButton> buttons) {
        this.name = name;
        this.size = size;
        this.buttons = buttons;
    }

    public String getName() {
        return Color.get(name);
    }

    public int getSize() {
        return size;
    }

    public List<GUIButton> getButtons() {
        return buttons;
    }
}
