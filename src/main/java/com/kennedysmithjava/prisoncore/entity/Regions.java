package com.kennedysmithjava.prisoncore.entity;

import com.kennedysmithjava.prisoncore.engine.EngineRegions;
import com.kennedysmithjava.prisoncore.regions.RegionCombined;
import com.kennedysmithjava.prisoncore.regions.RegionFlatSquare;
import com.kennedysmithjava.prisoncore.regions.RegionType;
import com.kennedysmithjava.prisoncore.regions.RegionWrapper;
import com.kennedysmithjava.prisoncore.util.MapMarker;
import com.massivecraft.massivecore.command.editor.annotation.EditorName;
import com.massivecraft.massivecore.store.Entity;

import java.util.*;

@EditorName("config")
public class Regions extends Entity<Regions>
{
    // -------------------------------------------- //
    // META
    // -------------------------------------------- //

    protected static transient Regions i;
    public static Regions get() { return i; }


    @Override
    public Regions load(Regions that)
    {
        this.setRegions(that.regions);
        this.setRegionTypes(that.regionTypes);
        this.setRegionMarkers(that.regionMarkers);
        this.regions.forEach((regionName, regionFlatSquares) -> {
            RegionType type = regionTypes.get(regionName); //A type should exist for every region
            RegionWrapper wrapper = getRegionWrapper(regionName, type, regionFlatSquares);
            EngineRegions.addActiveRegion(regionName, wrapper);
        });
        return this;
    }

    private Map<String, Set<RegionFlatSquare>> regions = new HashMap<>();
    private Map<String, RegionType> regionTypes = new HashMap<>();
    private Map<String, List<MapMarker>> regionMarkers = new HashMap<>();


    public void setRegions(Map<String, Set<RegionFlatSquare>> regions) {
        this.regions = regions;
    }

    public void setRegionTypes(Map<String, RegionType> regionTypes) {
        this.regionTypes = regionTypes;
    }

    public void setRegionMarkers(Map<String, List<MapMarker>> regionMarkers) {
        this.regionMarkers = regionMarkers;
    }

    public void addRegionMarker(String region, MapMarker marker){
        List<MapMarker> markers = regionMarkers.getOrDefault(region, new ArrayList<>());
        markers.add(marker);
        regionMarkers.put(region, markers);
        this.changed();
    }

    public List<MapMarker> getRegionMarkers(String name){
        return regionMarkers.get(name);
    }

    public void addRegion(String name, RegionFlatSquare region, RegionType type){
        Set<RegionFlatSquare> regionFlatSquares = regions.getOrDefault(name, new HashSet<>());
        regionFlatSquares.add(region);
        regions.put(name, regionFlatSquares);
        regionTypes.put(name, type);
        EngineRegions.addActiveRegion(name, getRegionWrapper(name, type, regionFlatSquares));
        this.changed();
    }

    public void removeRegion(String name, RegionFlatSquare region, RegionType type){
        Set<RegionFlatSquare> regionFlatSquares = regions.getOrDefault(name, new HashSet<>());
        if(regionFlatSquares.isEmpty()) return;
        regionFlatSquares.remove(region);
        if(regionFlatSquares.isEmpty()){
            regions.remove(name);
            EngineRegions.removeActiveRegion(name);
        }else {
            regions.put(name, regionFlatSquares);
            EngineRegions.addActiveRegion(name, getRegionWrapper(name, type, regionFlatSquares));
        }

        this.changed();
    }

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    private static RegionWrapper getRegionWrapper(String name, RegionType type, Set<RegionFlatSquare> regionFlatSquares) {
        RegionWrapper wrapper;
        if(regionFlatSquares.size() > 1){
            RegionCombined regionCombined = new RegionCombined(regionFlatSquares);
            wrapper = new RegionWrapper(regionCombined, type, name);
        }else {
            wrapper = new RegionWrapper(regionFlatSquares.stream().findFirst().get(), type, name);
        }
        return wrapper;
    }

    public Map<String, Set<RegionFlatSquare>> getRegions() {
        return regions;
    }

    public RegionType getRegionType(String name){
       return regionTypes.get(name);
    }
}

