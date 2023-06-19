package com.kennedysmithjava.prisoncore.entity.mines.upgrades.buttons;

import com.kennedysmithjava.prisoncore.entity.mines.upgrades.actions.AbstractAction;
import com.massivecraft.massivecore.util.MUtil;
import org.bukkit.Material;

import java.util.List;

public class GUIButtonMainUpgrade extends GUIButton {
    public GUIButtonMainUpgrade(String name, int slot, Material material, List<AbstractAction> onClick) {
        super(name, slot, MUtil.list(), MUtil.list(), material, onClick, MUtil.list());
    }
}
