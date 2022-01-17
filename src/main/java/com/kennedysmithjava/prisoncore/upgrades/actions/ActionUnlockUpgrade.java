package com.kennedysmithjava.prisoncore.upgrades.actions;

import com.kennedysmithjava.prisoncore.entity.mines.Mine;
import com.kennedysmithjava.prisoncore.entity.player.MPlayer;

public class ActionUnlockUpgrade extends AbstractAction {

    String upgrade;
    boolean setPurchased;
    boolean setActive;

    public ActionUnlockUpgrade(String upgrade, boolean setPurchased, boolean setActive) {
        this.upgrade = upgrade;
        this.setPurchased = setPurchased;
        this.setActive = setActive;
    }
    public ActionUnlockUpgrade(String upgrade) {
        this.upgrade = upgrade;
        this.setPurchased = false;
        this.setActive = false;
    }

    @Override
    public void apply(MPlayer player) {
        Mine mine = player.getMine();
        mine.unlockUpgrade(upgrade, setPurchased, setActive);
    }
}
