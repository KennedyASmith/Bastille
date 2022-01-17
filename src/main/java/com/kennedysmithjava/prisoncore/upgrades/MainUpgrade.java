package com.kennedysmithjava.prisoncore.upgrades;

import com.kennedysmithjava.prisoncore.eco.Cost;
import com.kennedysmithjava.prisoncore.upgrades.actions.AbstractAction;

import java.util.List;

public class MainUpgrade {


    List<String> lore;
    List<AbstractAction> abstractActions;
    List<Cost> costs;

    public MainUpgrade(List<String> lore, List<AbstractAction> abstractActions, List<Cost> costs) {
        this.lore = lore;
        this.abstractActions = abstractActions;
        this.costs = costs;
    }

    public List<String> getLore() {
        return lore;
    }

    public List<AbstractAction> getAbstractActions() {
        return abstractActions;
    }

    public List<Cost> getCosts() {
        return costs;
    }
}
