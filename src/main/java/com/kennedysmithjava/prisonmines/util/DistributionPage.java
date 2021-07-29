package com.kennedysmithjava.prisonmines.util;

import java.util.List;

public class DistributionPage {

    String name;
    List<Integer> distributionIDs;

    public DistributionPage(String name, List<Integer> distributionIDs){
        this.name = name;
        this.distributionIDs = distributionIDs;
    }

    public List<Integer> getDistributionIDs() {
        return distributionIDs;
    }

    public String getName() {
        return name;
    }
}
