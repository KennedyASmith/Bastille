package com.kennedysmithjava.prisonmines.upgrades;

import com.mcrivals.prisoncore.entity.MPlayer;

import java.util.List;

public class BuildingUnlock extends AbstractUpgrade{

    String schemPath;
    int pasteOffsetX;
    int pasteOffsetY;
    int pasteOffsetZ;

    BuildingUnlock(String schemPath, int pasteOffsetX, int pasteOffsetY, int pasteOffsetZ) {
        this.schemPath = schemPath;
        this.pasteOffsetX = pasteOffsetX;
        this.pasteOffsetY = pasteOffsetY;
        this.pasteOffsetZ = pasteOffsetZ;
    }

    @Override
    public void apply(MPlayer player) {

    }
}
