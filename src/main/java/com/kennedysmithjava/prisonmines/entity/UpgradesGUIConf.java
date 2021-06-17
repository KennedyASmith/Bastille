package com.kennedysmithjava.prisonmines.entity;

import com.kennedysmithjava.prisonmines.upgrades.Upgrade;
import com.massivecraft.massivecore.command.editor.annotation.EditorName;
import com.massivecraft.massivecore.store.Entity;
import com.massivecraft.massivecore.util.MUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@EditorName("config")
public class UpgradesGUIConf extends Entity<UpgradesGUIConf>
{
    // -------------------------------------------- //
    // META
    // -------------------------------------------- //

    protected static transient UpgradesGUIConf i;

    public static UpgradesGUIConf get() { return i; }

    @Override
    public UpgradesGUIConf load(UpgradesGUIConf that)
    {
        super.load(that);
        return this;
    }

    public int guiBorderColor = 15;
    public int guiSize = 45;

    Map<Integer, List<Upgrade>> upgrades = MUtil.map(
               1,
                new ArrayList<>(Arrays.asList(
                        Upgrade.COBWEB_CLEAR_1,
                        Upgrade.HEIGHT_1,
                        Upgrade.RESOURCE_1
                )),
            2,
            new ArrayList<>(Arrays.asList(
                    Upgrade.COBWEB_CLEAR_1,
                    Upgrade.HEIGHT_1,
                    Upgrade.RESOURCE_1
            ))
    );

    Map<Integer, Upgrade> mainMineUpgrades = MUtil.map(
            1, Upgrade.MINE_2,
            2, Upgrade.MINE_2
    );

    public Map<Integer, List<Upgrade>> getUpgrades() {
        return upgrades;
    }

    public Map<Integer, Upgrade> getMainMineUpgrades() {
        return mainMineUpgrades;
    }
}

