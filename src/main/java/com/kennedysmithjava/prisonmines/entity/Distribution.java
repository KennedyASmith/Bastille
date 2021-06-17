package com.kennedysmithjava.prisonmines.entity;

import org.bukkit.Material;

import java.util.Map;

public class Distribution{
    Map<String, Double> rates;
    Material icon;

    public Distribution(Material icon, Map<String, Double> rates){
        this.rates = rates;
        this.icon = icon;
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

    @Override
    public String toString() {
        return "Distribution{" +
                "rates=" + rates +
                ", icon=" + icon +
                '}';
    }
}
