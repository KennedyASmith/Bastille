package com.kennedysmithjava.prisonmines.entity;

import org.bukkit.Material;

import java.util.List;
import java.util.Map;

public class Distribution{
    Map<String, Double> rates;
    Material icon;
    String name;
    List<String> lore;

    public Distribution(String name, Material icon, Map<String, Double> rates, List<String> lore){
        this.rates = rates;
        this.icon = icon;
        this.name = name;
        this.lore = lore;
    }

    public Distribution(Map<String, Double> rates){
        this.rates = rates;
    }

    public Material getIcon() {
        return icon;
    }

    public Map<String, Double> getRates() {
        return rates;
    }

    public String getName() {
        return name;
    }

    public List<String> getLore() {
        return lore;
    }
}
