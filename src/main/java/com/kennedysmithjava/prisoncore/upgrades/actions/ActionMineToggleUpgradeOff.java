package com.kennedysmithjava.prisoncore.upgrades.actions;

import com.kennedysmithjava.prisoncore.entity.mines.Mine;
import com.kennedysmithjava.prisoncore.entity.player.MPlayer;
import com.massivecraft.massivecore.util.MUtil;

import java.util.List;

public class ActionMineToggleUpgradeOff extends AbstractAction {

    List<String> upgrades;

    public ActionMineToggleUpgradeOff(List<String> upgrades){
        this.upgrades = upgrades;
    }

    public ActionMineToggleUpgradeOff(String upgrade){
        this.upgrades = MUtil.list(upgrade);
    }

    @Override
    public void apply(MPlayer player) {
        Mine mine = player.getMine();
        if(mine == null) return;
        upgrades.forEach(mine::deactivateUpgrade);
    }
}
