package com.kennedysmithjava.prisoncore.eco;

import com.kennedysmithjava.prisoncore.entity.player.MPlayer;
import com.kennedysmithjava.prisoncore.util.Color;

public class CostCurrency extends Cost{

    private final CurrencyType currencyType;
    private final double amount;

    public CostCurrency(CurrencyType currencyType, double amount){
        this.currencyType = currencyType;
        this.amount = amount;
    }

    @Override
    public boolean hasCost(MPlayer player) {
        return player.hasCurrency(currencyType, amount);
    }

    @Override
    public boolean transaction(MPlayer player) {
        return player.takeCurrency(currencyType, amount);
    }

    @Override
    public String getName() {
        return currencyType.getDisplayName();
    }

    @Override
    public String getPriceline() {
        return currencyType.getDisplayName() + " " + amount + " " + currencyType.getSymbol();
    }

    @Override
    public String getInsufficientLine(MPlayer player) {
        return "&cInsufficient "
                + Color.strip(currencyType.getDisplayName())
                + ": &e" + Color.strip(currencyType.getSymbol())
                + " " + player.getEconomy().get(currencyType)
                + "&7/&7&o" + getAmount();
    }

    @Override
    public Cost combine(Cost similarCost) {
        double amt = ((CostCurrency) similarCost).getAmount();
        return new CostCurrency(currencyType, amt + getAmount());
    }

    public double getAmount() {
        return amount;
    }
}
