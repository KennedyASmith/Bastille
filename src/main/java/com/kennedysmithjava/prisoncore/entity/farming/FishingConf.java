package com.kennedysmithjava.prisoncore.entity.farming;

import com.kennedysmithjava.prisoncore.entity.farming.objects.FishingArea;
import com.massivecraft.massivecore.command.editor.annotation.EditorName;
import com.massivecraft.massivecore.store.Entity;

import java.util.HashMap;
import java.util.Map;

@EditorName("config")
public class FishingConf extends Entity<FishingConf> {

    // -------------------------------------------- //
    // META
    // -------------------------------------------- //

    protected static FishingConf i;

    public static FishingConf get() {
        return i;
    }

    public Map<String, FishingArea> fishingAreaList = new HashMap<>();

    public Map<String, FishingArea> getFishingAreaList() {
        return fishingAreaList;
    }

    public void setFishingAreaList(Map<String, FishingArea> fishingAreaList) {
        this.fishingAreaList = fishingAreaList;
        this.changed();
    }

    public void addFishingArea(String name, FishingArea area) {
        fishingAreaList.put(name, area);
        this.changed();
    }

    @Override
    public FishingConf load(FishingConf that) {
        this.setFishingAreaList(that.fishingAreaList);
        return this;
    }



}
