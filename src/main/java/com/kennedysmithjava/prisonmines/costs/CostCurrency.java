package com.kennedysmithjava.prisonmines.costs;

import com.mcrivals.prisoncore.CurrencyType;
import com.mcrivals.prisoncore.entity.MPlayer;

public class CostCurrency extends Cost {

    CurrencyType type;
    double amount;

    CostCurrency(CurrencyType type, double amount){
        this.type = type;
        this.amount = amount;
    }

    @Override
    public boolean hasCost(MPlayer player) {
        return player.getEconomy().get(type) >= amount;
    }

    @Override
    public boolean consumeCost(MPlayer player) {
        if(hasCost(player)){
            player.removeBalance(type, amount);
            return true;
        }else{
            return false;
        }
    }
}
