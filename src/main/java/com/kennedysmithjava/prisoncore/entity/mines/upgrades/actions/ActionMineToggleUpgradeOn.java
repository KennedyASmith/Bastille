package com.kennedysmithjava.prisoncore.entity.mines.upgrades.actions;

import com.kennedysmithjava.prisoncore.entity.mines.Mine;
import com.kennedysmithjava.prisoncore.entity.player.MPlayer;
import com.massivecraft.massivecore.util.MUtil;

import java.util.List;

public class ActionMineToggleUpgradeOn extends AbstractAction {

    List<String> upgrades;

    public ActionMineToggleUpgradeOn(List<String> upgrades){
        this.upgrades = upgrades;
    }

    public ActionMineToggleUpgradeOn(String upgrade){
        this.upgrades = MUtil.list(upgrade);
    }

    @Override
    public void apply(MPlayer player) {
        Mine mine = player.getMine();
        if(mine == null) return;
        upgrades.forEach(mine::activateUpgrade);
    }


}
